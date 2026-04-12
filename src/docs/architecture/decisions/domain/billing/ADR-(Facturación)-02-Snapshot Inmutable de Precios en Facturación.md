
# ADR- 02 (Facturación): Snapshot Inmutable de Precios en Facturación

- Estado: Aprobado
- Fecha: 2026-01-8
- Autor: David Stiven Sanclemente

## Contexto
En facturación de servicios de salud, existe una tensión fundamental entre dos necesidades:

1. **Tarifas deben evolucionar**: Contratos se renuevan, precios se ajustan por inflación, convenios cambian
2. **Facturas emitidas son inmutables**: Una vez emitida, la factura es un documento legal que no puede modificarse

### Problemática Identificada

### Escenario Problemático:
1. Paciente atendido el 15/12/2025 (Tarifa: $55,000)
2. Factura emitida el 20/12/2025 (Usa Rate.amount actual: $55,000)
3. Tarifa actualizada el 01/01/2026 (Nueva tarifa: $60,000)
4. Sistema consulta factura el 15/01/2026
5. ¿Qué monto debe mostrar la factura? ¿$55,000 o $60,000?


Problema Real: Si InvoiceItem almacena solo rateId y consulta Rate.amount dinámicamente, el monto facturado cambia retroactivamente cuando la tarifa se actualiza.
### Consecuencias de No Tener Snapshot

Inconsistencia financiera: Facturas históricas muestran montos diferentes cada vez que se consultan
Auditorías inválidas: No se puede demostrar qué tarifa se aplicó al momento de facturar
Reclamaciones de pacientes: "Me cobraron $55,000 pero ahora la factura dice $60,000"
Incumplimiento normativo DIAN: Factura electrónica debe ser inmutable

## Decisión
 Implementamos snapshot inmutable de precios mediante:
### 1. Desnormalización Controlada en InvoiceItem
```java
public class InvoiceItem {
private InvoiceItemId id;
private InvoiceId invoiceId;

    // Información del servicio (snapshot)
    private String serviceCode;        // Copia del código CUPS
    private String serviceDescription; // Copia del nombre
    
    // Snapshot de tarifa (INMUTABLE tras emisión)
    private BigDecimal unitPrice;      // ⚠️ Copia del Rate.amount
    private String currency;           // ⚠️ Copia del Rate.currency
    private int quantity;
    
    // Referencia a tarifa original (trazabilidad)
    private RateId rateId;             // Para auditoría, NO para precio
    
    // Metadatos
    private LocalDateTime performedAt; // Fecha de prestación del servicio
    private DentistId providerId;
    
    // Totales calculados (inmutables)
    private BigDecimal lineTotal;      // unitPrice * quantity
    private BigDecimal lineTax;        // lineTotal * taxRate
    
    /**
     * Constructor para nuevo ítem (DRAFT).
     * Copia unitPrice desde Rate vigente.
     */
    public static InvoiceItem create(
        InvoiceId invoiceId,
        ProvidedService service,
        Rate rate,
        int quantity,
        LocalDateTime performedAt,
        DentistId providerId
    ) {
        // Validación: tarifa debe ser vigente
        rate.ensureValidAt(performedAt);
        
        // Snapshot: copiar precio de Rate
        BigDecimal unitPrice = rate.getAmount().getValue();
        String currency = rate.getCurrency();
        
        InvoiceItem item = new InvoiceItem(
            InvoiceItemId.generate(),
            invoiceId,
            service.getCode(),
            service.getName(),
            unitPrice,  // ⚠️ COPIA, no referencia
            currency,
            quantity,
            rate.getId(), // Solo para trazabilidad
            performedAt,
            providerId
        );
        
        item.recalculateTotals();
        return item;
    }
    
    /**
     * ⚠️ CRÍTICO: unitPrice NO se consulta desde Rate.
     * Es una copia inmutable tomada al momento de crear el ítem.
     */
    public BigDecimal getUnitPrice() {
        return this.unitPrice; // Valor copiado, NO rate.getAmount()
    }
    
    /**
     * Para auditoría: consultar tarifa original.
     * NO afecta el precio facturado.
     */
    public Rate getOriginalRate(RateRepository rateRepo) {
        return rateRepo.findById(this.rateId);
    }
}
```
### 2. Inmutabilidad Tras Emisión
```java
public class Invoice {
private InvoiceStatus status;
private List<InvoiceItem> items;

    /**
     * Transición DRAFT → PENDING.
     * Congela snapshot de precios.
     */
    public void markPending() {
        this.validateBeforeEmit();
        
        // ⚠️ Snapshot se congela aquí
        this.items = Collections.unmodifiableList(this.items);
        
        this.status = InvoiceStatus.PENDING;
        this.emittedAt = LocalDateTime.now();
        
        this.addDomainEvent(new InvoiceEmitted(
            this.id,
            this.emittedAt,
            this.createPriceSnapshot() // Snapshot para auditoría
        ));
    }
    
    private PriceSnapshot createPriceSnapshot() {
        return new PriceSnapshot(
            this.items.stream()
                .map(item -> new ItemSnapshot(
                    item.getServiceCode(),
                    item.getUnitPrice(),
                    item.getQuantity(),
                    item.getRateId() // Referencia histórica
                ))
                .toList()
        );
    }
    
    /**
     * ⚠️ No puede modificar ítems tras emisión.
     */
    public void addItem(InvoiceItem item) {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_CANNOT_ADD_ITEM,
                "Invoice",
                "Cannot add items after emission"
            );
        }
        
        this.items.add(item);
        this.recalculateTotals();
    }
}
```
### 3. Modelo de Base de Datos
```sql
CREATE TABLE invoice_item (
id UUID PRIMARY KEY,
invoice_id UUID NOT NULL,

    -- Snapshot de servicio
    service_code VARCHAR(20) NOT NULL,
    service_description VARCHAR(255) NOT NULL,
    
    -- Snapshot de precio (INMUTABLE)
    unit_price DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'COP',
    quantity INT NOT NULL,
    
    -- Referencia histórica (NO usada para cálculos)
    rate_id UUID NOT NULL,
    
    -- Totales pre-calculados
    line_total DECIMAL(15,2) NOT NULL,
    line_tax DECIMAL(15,2) NOT NULL DEFAULT 0,
    
    -- Metadatos
    performed_at TIMESTAMP NOT NULL,
    provider_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (rate_id) REFERENCES rate(id) -- Solo trazabilidad
);
```
-- ⚠️ CRÍTICO: unit_price NO tiene FK a rate.amount
-- Es una copia estática tomada al momento de crear el ítem

### 4. Auditoría: Comparación Histórica
```java
public class InvoiceAuditService {
/**
* Compara precio facturado vs tarifa actual.
* Útil para detectar cambios retroactivos.
*/
public AuditReport compareInvoiceWithCurrentRates(InvoiceId invoiceId) {
Invoice invoice = invoiceRepository.findById(invoiceId);
List<PriceDiscrepancy> discrepancies = new ArrayList<>();

        for (InvoiceItem item : invoice.getItems()) {
            Rate currentRate = rateRepository.findById(item.getRateId());
            
            BigDecimal invoicedPrice = item.getUnitPrice();
            BigDecimal currentPrice = currentRate.getAmount().getValue();
            
            if (!invoicedPrice.equals(currentPrice)) {
                discrepancies.add(new PriceDiscrepancy(
                    item.getServiceCode(),
                    invoicedPrice,
                    currentPrice,
                    invoice.getIssuedAt(),
                    currentRate.getValidFrom()
                ));
            }
        }
        
        return new AuditReport(invoice.getId(), discrepancies);
    }
}
```
### 5. Evento de Dominio con Snapshot
```java
public record InvoiceEmitted(
InvoiceId invoiceId,
LocalDateTime emittedAt,
PriceSnapshot priceSnapshot // Snapshot completo para auditoría
) implements DomainEvent {

    public record PriceSnapshot(
        List<ItemSnapshot> items
    ) {
        public BigDecimal getTotalAmount() {
            return items.stream()
                .map(item -> item.unitPrice().multiply(
                    BigDecimal.valueOf(item.quantity())
                ))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
    
    public record ItemSnapshot(
        String serviceCode,
        BigDecimal unitPrice,
        int quantity,
        RateId rateId // Referencia histórica
    ) {}
}
```
### Positivas

✅ Inmutabilidad garantizada: Facturas emitidas nunca cambian de monto

✅ Auditoría precisa: Se puede demostrar qué tarifa se aplicó al facturar

✅ Cumplimiento DIAN: Factura electrónica es inmutable tras emisión

✅ Trazabilidad completa: Relación entre precio facturado y tarifa original

✅ Performance: No requiere JOIN con rate para mostrar facturas

### Negativas

⚠️ Desnormalización: unit_price duplicado entre invoice_item y rate

⚠️ Espacio en disco: Mayor almacenamiento por redundancia

⚠️ Complejidad: Lógica de snapshot debe implementarse correctamente

## Mitigaciones
```java
// Test: Verificar inmutabilidad
@Test
void invoicePriceShouldNotChangeWhenRateIsUpdated() {
// Crear factura con tarifa $55,000
Rate originalRate = createRate(serviceId, Money.of(55000, "COP"));
Invoice invoice = createInvoice(originalRate);
invoice.markPending();

    BigDecimal originalPrice = invoice.getTotal();
    
    // Actualizar tarifa a $60,000
    originalRate.adjustAmount(Money.of(60000, "COP"), "Ajuste 2026");
    rateRepository.save(originalRate);
    
    // Consultar factura nuevamente
    Invoice reloadedInvoice = invoiceRepository.findById(invoice.getId());
    
    // ⚠️ Precio NO debe cambiar
    assertThat(reloadedInvoice.getTotal()).isEqualTo(originalPrice);
}
```
## Alternativas Consideradas
### Alternativa 1: Consulta Dinámica de Rate (Descartada)

```
// ❌ NO HACER ESTO
public BigDecimal getUnitPrice() {
Rate rate = rateRepository.findById(this.rateId);
return rate.getAmount().getValue(); // ⚠️ Cambia retroactivamente
}
```

Rechazo: Violación de inmutabilidad, auditorías inválidas
### Alternativa 2: Tabla de Historial de Tarifas (Complementaria)
```sql
CREATE TABLE rate_history (
id UUID PRIMARY KEY,
rate_id UUID NOT NULL,
amount DECIMAL(15,2) NOT NULL,
valid_from TIMESTAMP NOT NULL,
valid_to TIMESTAMP,
changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
changed_by VARCHAR(255),
reason TEXT
);
```

Decisión: Implementar como complemento para auditoría avanzada
### Alternativa 3: Snapshot en Tabla Separada (Descartada)
```java

CREATE TABLE invoice_item_snapshot (
invoice_item_id UUID PRIMARY KEY,
snapshot_data JSON NOT NULL
);
```

Rechazo: Complejidad innecesaria, dificulta consultas

## Notas
- Esta decisión complementa **[ADR-(Facturación)-01-Validación de Tarifas Vigentes al Momento de Facturar.md](ADR-%28Facturaci%C3%B3n%29-01-Validaci%C3%B3n%20de%20Tarifas%20Vigentes%20al%20Momento%20de%20Facturar.md)** (validación de vigencia)
- Relacionado con **[ADR-(Facturación)-03-Cumplimiento Normativo DIAN Colombia.md](ADR-%28Facturaci%C3%B3n%29-03-Cumplimiento%20Normativo%20DIAN%20Colombia.md)** (cumplimiento DIAN)
- Patrón: **Desnormalización controlada** para inmutabilidad

---