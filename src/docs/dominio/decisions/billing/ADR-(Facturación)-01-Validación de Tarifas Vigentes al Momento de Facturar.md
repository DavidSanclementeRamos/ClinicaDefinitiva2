# ADR-01 (Facturación) : Validación de Tarifas Vigentes al Momento de Facturar

- **Estado:** Aprobado
- **Fecha:** 2026-01-8
- **Autor:** David

## Contexto
El sistema de facturación de la clínica odontológica debe garantizar que todas las facturas emitidas utilicen tarifas válidas y vigentes al momento de la emisión. En Colombia, las tarifas están sujetas a:

Contratos con EPSs: Vigencias específicas (típicamente 1-2 años) que deben respetarse
Manual Tarifario ISS 2001: Base legal para cálculo de tarifas en salud
Acuerdos comerciales: Contratos con medicina prepagada, aseguradoras y ARLs
Tarifas particulares: Ajustes periódicos por inflación o políticas comerciales

## Problemática Identificada

Riesgo de glosas: Facturar con tarifas vencidas genera reclamaciones y rechazos por parte de EPSs
Inconsistencias financieras: Usar tarifas incorrectas afecta proyecciones de ingresos
Incumplimiento contractual: Aplicar tarifas no vigentes viola convenios con pagadores
Auditorías fallidas: Trazabilidad deficiente entre tarifas aplicadas y contratos vigentes

## Escenarios Críticos
### Escenario 1: Facturación con Tarifa Vencida
- Contrato EPS Sura vigente: 01/01/2025 - 31/12/2025
- Fecha de facturación: 15/01/2026
- Resultado actual: Sistema permite facturar con tarifa vencida
- Consecuencia: EPS rechaza pago (glosa)

### Escenario 2: Cambio de Tarifa Durante Atención
- Paciente atendido: 28/12/2025 (tarifa vigente: $55,000)
- Factura emitida: 03/01/2026 (nueva tarifa: $60,000)
- Resultado actual: Sistema usa tarifa nueva para servicio prestado con tarifa anterior
- Consecuencia: Inconsistencia entre servicio prestado y monto facturado

### Escenario 3: Múltiples Tarifas Activas
- Servicio: Limpieza dental
- Tarifa A: válida hasta 31/12/2025
- Tarifa B: válida desde 01/01/2026
- Fecha de facturación: 15/01/2026
- Resultado actual: Ambigüedad sobre qué tarifa aplicar
  Decisión
  Implementamos validación estricta de vigencia temporal en todo el ciclo de facturación mediante:
### 1. Validación Obligatoria Pre-Emisión


```java
  // En Invoice.validateBeforeEmit()
for (InvoiceItem item : items) {
    Rate rate = rateRepository.findById(item.getRateId());
    
    if (!rate.isValidAt(this.issuedAt)) {
        throw new BusinessRuleViolationException(
            RateError.ERR_RATE_EXPIRED,
            "Invoice",
            String.format(
                "Rate %s for service %s expired at %s. Invoice date: %s",
                rate.getId(),
                item.getServiceCode(),
                rate.getValidTo(),
                this.issuedAt
            )
        );
    }
}

```
### 2. Lógica de Validación en Rate

```java
   javapublic class Rate {
   public boolean isValidAt(LocalDateTime when) {
   if (!this.isActive) {
   return false;
   }

        if (when.isBefore(this.validFrom)) {
            return false;
        }
        
        if (this.validTo != null && when.isAfter(this.validTo)) {
            return false;
        }
        
        return true;
   }

   public void ensureValidAt(LocalDateTime when) {
   if (!isValidAt(when)) {
   throw new RateExpiredException(this.id, when);
   }
   }
   }
   
```
### 3. Consulta de Tarifa Vigente
```java
   javapublic interface RateRepository {
   /**
    * Encuentra la tarifa vigente para un servicio, pagador y fecha específica.
    * Lanza RateNotFoundException si no existe tarifa vigente.
      */
      Rate findActiveRateFor(
      ServiceId serviceId,
      PayerType payerType,
      ContractId contractId, // nullable
      LocalDateTime effectiveDate
      ) throws RateNotFoundException;

   /**
    * Valida que no existan conflictos de vigencia al crear nueva tarifa.
      */
      void ensureNoOverlappingRates(
      ServiceId serviceId,
      PayerType payerType,
      ContractId contractId,
      ValidityPeriod newPeriod
      ) throws RateValidityConflictException;
      }
      
```      
              
### 4. Transición de Estados con Validación
```java 
  javapublic class Invoice {
   public void markPending() {
   // Validación previa obligatoria
   this.validateBeforeEmit();

        // Verificación adicional de tarifas
        if (this.hasExpiredRates()) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_EXPIRED_RATE,
                "Invoice",
                "Cannot emit invoice with expired rates"
            );
        }
        
        // Transición de estado
        this.status = InvoiceStatus.PENDING;
        this.emittedAt = LocalDateTime.now();
        
        // Evento de dominio
        this.addDomainEvent(new InvoiceEmitted(this.id, this.emittedAt));
   }

   private boolean hasExpiredRates() {
   return this.items.stream()
   .anyMatch(item -> !item.getRate().isValidAt(this.issuedAt));
   }
   }
```

### 5. Protección en Capa de Aplicación

```java
   java@Service
   public class InvoiceApplicationService {
   public InvoiceId emitInvoice(EmitInvoiceCommand command) {
   Invoice invoice = invoiceRepository.findById(command.invoiceId());

        // Validación adicional antes de transición
        validateAllRatesAreActive(invoice);
        
        // Transición con validaciones internas
        invoice.markPending();
        
        invoiceRepository.save(invoice);
        eventPublisher.publish(invoice.getDomainEvents());
        
        return invoice.getId();
   }

   private void validateAllRatesAreActive(Invoice invoice) {
   for (InvoiceItem item : invoice.getItems()) {
   Rate rate = rateRepository.findById(item.getRateId());
   rate.ensureValidAt(invoice.getIssuedAt());
   }
   }
   }
```

## Consecuencias
### Positivas

✅ Cumplimiento contractual garantizado: Imposible facturar con tarifas no vigentes
✅ Reducción de glosas: EPSs no pueden rechazar facturas por tarifas incorrectas
✅ Trazabilidad completa: Cada factura vinculada a tarifa válida al momento de emisión
✅ Auditoría simplificada: Validación automática de vigencias
✅ Detección temprana: Errores bloqueados antes de emisión (no después)

### Negativas

⚠️ Bloqueo de facturación: Si tarifa vence, facturación se detiene hasta actualizar
⚠️ Complejidad de gestión: Requiere renovación proactiva de tarifas
⚠️ Impacto en flujo: Personal debe validar vigencias antes de agendar

## Mitigaciones
```java
java// Sistema de alertas preventivas
@Scheduled(cron = "0 0 8 * * MON") // Lunes 8 AM
public void alertExpiringRates() {
LocalDateTime threshold = LocalDateTime.now().plusDays(30);

    List<Rate> expiringRates = rateRepository.findExpiringBefore(threshold);
    
    for (Rate rate : expiringRates) {
        notificationService.send(
            NotificationChannel.EMAIL,
            "admin@clinica.com",
            String.format(
                "ALERTA: Tarifa %s para %s vence en %d días",
                rate.getId(),
                rate.getService().getName(),
                ChronoUnit.DAYS.between(LocalDateTime.now(), rate.getValidTo())
            )
        );
    }
}
```

## Alternativas Consideradas
### Alternativa 1: Validación Opcional (Descartada)

Descripción: Permitir facturar con tarifas vencidas emitiendo solo advertencia
Rechazo: Alto riesgo de glosas y reclamaciones de EPSs

### Alternativa 2: Corrección Automática (Descartada)

Descripción: Sistema selecciona automáticamente tarifa vigente más cercana
Rechazo: Puede aplicar tarifa incorrecta sin conocimiento del usuario

### Alternativa 3: Validación en Base de Datos (Descartada)

Descripción: CHECK constraints en BD para validar vigencias
Rechazo: Lógica de negocio debe estar en dominio, no en infraestructura

## Implementación
### Fase 1: Validación en Dominio ✅

```java
java// Rate.java
public boolean isValidAt(LocalDateTime when) { ... }

// Invoice.java
public void validateBeforeEmit() { ... }
Fase 2: Consultas Especializadas ✅
java// RateRepository.java
Rate findActiveRateFor(ServiceId, PayerType, ContractId, LocalDateTime);
Fase 3: Alertas Preventivas 🔄
java// RateExpirationAlertService.java
@Scheduled alertExpiringRates()
Fase 4: Auditoría y Métricas 📋
sql-- Reporte de tarifas próximas a vencer
SELECT * FROM rate
WHERE valid_to BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 30 DAY);
```

## Notas
- Esta decisión se complementa con **[ADR-(Facturación)-02-Snapshot Inmutable de Precios en Facturación.md](ADR-%28Facturaci%C3%B3n%29-02-Snapshot%20Inmutable%20de%20Precios%20en%20Facturaci%C3%B3n.md)** (snapshot inmutable)
- Relacionado con **[ADR-(Dominio)-02-Implementación de reglas de negocio.md](../ADR-%28Dominio%29-02-Implementaci%C3%B3n%20de%20reglas%20de%20negocio.md)** (reglas de negocio por agregado)
- Impacta módulos: Billing, dental.care.services, Schedule

---