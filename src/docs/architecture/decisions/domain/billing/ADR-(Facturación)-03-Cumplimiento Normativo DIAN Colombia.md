

# ADR-03 (Facturación): Cumplimiento Normativo DIAN Colombia


- Estado: Aprobado
- Fecha: 2026-01-8
- Autor: David Stiven Sanclemente

## Contexto
En Colombia, la facturación electrónica es **obligatoria** para prestadores de servicios de salud según el **Decreto 358 de 2020** y resoluciones de la DIAN (Dirección de Impuestos y Aduanas Nacionales). El sistema debe cumplir con:

### Normativa Aplicable
1. **Decreto 358 de 2020**: Facturación electrónica obligatoria
2. **Resolución 000042 de 2020**: Especificaciones técnicas factura electrónica
3. **Ley 1438 de 2011**: Facturación en salud (RIPS, CUPS)
4. **Circular 006 de 2019**: Glosas y devoluciones en salud

### Requisitos Legales Mínimos

Una factura electrónica válida en Colombia debe incluir:
✅ Numeración consecutiva autorizada por DIAN
✅ NIT del prestador (clínica/profesional)
✅ Identificación del paciente (CC, TI, CE, pasaporte)
✅ Código CUPS del servicio prestado
✅ Descripción detallada del servicio
✅ Valor unitario y total
✅ Discriminación de impuestos (IVA si aplica)
✅ Fecha de emisión y vencimiento
✅ Forma de pago
✅ CUFE (Código Único de Factura Electrónica)
✅ QR con enlace a validación DIAN

## Problemática Identificada

Numeración consecutiva: Debe ser correlativa y sin saltos
Inmutabilidad: Factura emitida no puede modificarse (solo anularse con nota crédito)
Transmisión a DIAN: Envío en tiempo real (< 24 horas)
Conservación: Facturas deben conservarse por 5 años
Código CUPS: Servicios deben codificarse según nomenclatura oficial

## Decisión
Implementamos cumplimiento normativo DIAN mediante:
### 1. Numeración Consecutiva Autorizada
```java 
  javapublic class InvoiceNumberGenerator {
   private final String resolutionNumber;  // Ej: "18764050242673"
   private final String prefix;            // Ej: "FECO" (Factura Electrónica Clínica Odonto)
   private final LocalDate validFrom;      // Inicio vigencia resolución
   private final LocalDate validTo;        // Fin vigencia resolución
   private final long rangeStart;          // Ej: 1
   private final long rangeEnd;            // Ej: 5000
   private AtomicLong currentNumber;       // Contador atómico

   /**
    * Genera próximo número consecutivo.
    * Thread-safe mediante AtomicLong.
      */
      public String generateNext() {
      long nextNumber = currentNumber.incrementAndGet();

      // Validación: no exceder rango autorizado
      if (nextNumber > rangeEnd) {
      throw new InvoiceNumberRangeExhaustedException(
      resolutionNumber,
      rangeEnd
      );
      }

      // Formato: FECO-0001, FECO-0002, ...
      return String.format("%s-%04d", prefix, nextNumber);
      }

   /**
    * Valida que resolución DIAN esté vigente.
      */
      public void ensureResolutionIsValid() {
      LocalDate today = LocalDate.now();

      if (today.isBefore(validFrom) || today.isAfter(validTo)) {
      throw new InvoiceResolutionExpiredException(
      resolutionNumber,
      validFrom,
      validTo
      );
      }
      }
      }
```      
### 2. Modelo de Factura con Campos DIAN
```java 
  javapublic class Invoice {
   // Campos básicos
   private InvoiceId id;
   private String invoiceNumber;          // FECO-0001
   private InvoiceStatus status;

   // Información DIAN
   private String resolutionNumber;       // Resolución autorizada
   private String cufe;                   // Código Único Factura Electrónica
   private String qrCode;                 // QR para validación DIAN
   private LocalDateTime transmittedAt;   // Fecha envío a DIAN
   private String dianTrackingId;         // ID seguimiento DIAN

   // Identificación prestador
   private String providerNIT;            // NIT clínica
   private String providerName;
   private String providerAddress;

   // Identificación paciente
   private String patientDocumentType;    // CC, TI, CE, PAS
   private String patientDocumentNumber;
   private String patientName;
   private String patientAddress;

   // Información financiera
   private BigDecimal subtotal;
   private BigDecimal taxAmount;          // IVA (19% si aplica)
   private BigDecimal total;
   private String currency;               // COP

   // Tipo de factura
   private InvoiceType type;              // HEALTH_SERVICE, COSMETIC
   private PayerType payerType;           // EPS, PARTICULAR, PREPAGADA

   // Fechas
   private LocalDateTime issuedAt;
   private LocalDateTime dueDate;
   private LocalDate servicePeriodFrom;   // Inicio período facturado
   private LocalDate servicePeriodTo;     // Fin período facturado

   /**
    * Genera CUFE según especificación DIAN.
    * CUFE = SHA-384(NumFac + FecFac + HorFac + ValFac + CodImp1 + ...)
      */
      public String generateCUFE() {
      String dataToHash = String.format(
      "%s%s%s%s%s%s%s%s",
      this.invoiceNumber,
      this.issuedAt.format(DateTimeFormatter.ISO_LOCAL_DATE),
      this.issuedAt.format(DateTimeFormatter.ISO_LOCAL_TIME),
      this.total.setScale(2, RoundingMode.HALF_UP),
      this.taxAmount.setScale(2, RoundingMode.HALF_UP),
      this.subtotal.setScale(2, RoundingMode.HALF_UP),
      this.providerNIT,
      this.patientDocumentNumber
      );

      return DigestUtils.sha384Hex(dataToHash).toUpperCase();
      }

   /**
    * Genera QR con enlace a validación DIAN.Continuar17:08 */
      public String generateQRCode() {
      String validationURL = String.format(
      "https://catalogo-vpfe.dian.gov.co/document/searchqr?documentkey=%s",
      this.cufe
      );

   return QRCodeGenerator.generate(validationURL, 200, 200);
   }
   }
```
### 3. Ítems con Código CUPS
```java
public class InvoiceItem {
    private InvoiceItemId id;
    
    // Código CUPS (obligatorio para salud)
    private String cupsCode;               // Ej: "890101" (Consulta odontológica)
    private String cupsDescription;
    
    // Clasificación servicio
    private ServiceCategory category;      // PREVENTIVE, RESTORATIVE, SURGICAL
    private boolean isHealthService;       // true = exento IVA, false = IVA 19%
    
    // Información fiscal
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal lineTotal;
    private BigDecimal lineTax;            // 0 para salud, 19% para estética
    private TaxType taxType;               // EXEMPT, IVA_19
    
    /**
     * Valida código CUPS según nomenclatura oficial.
     */
    public void validateCUPSCode() {
        if (!CUPSValidator.isValid(this.cupsCode)) {
            throw new InvalidCUPSCodeException(this.cupsCode);
        }
    }
    
    /**
     * Determina si aplica IVA según tipo de servicio.
     */
    public TaxType determineTaxType() {
        // Servicios de salud: exentos de IVA
        if (this.isHealthService) {
            return TaxType.EXEMPT;
        }
        
        // Procedimientos estéticos: IVA 19%
        if (this.category == ServiceCategory.COSMETIC) {
            return TaxType.IVA_19;
        }
        
        return TaxType.EXEMPT;
    }
}
```

### 4. Transmisión a DIAN
```java
@Service
public class DIANIntegrationService {
    private final DIANAPIClient dianClient;
    
    /**
     * Transmite factura a DIAN.
     * Debe ejecutarse < 24 horas tras emisión.
     */
    public void transmitInvoice(Invoice invoice) {
        // Validar resolución vigente
        invoice.ensureResolutionIsValid();
        
        // Generar CUFE
        String cufe = invoice.generateCUFE();
        invoice.setCufe(cufe);
        
        // Generar QR
        String qrCode = invoice.generateQRCode();
        invoice.setQrCode(qrCode);
        
        // Construir XML según especificación DIAN
        String xmlInvoice = buildDIANXML(invoice);
        
        // Firmar digitalmente (certificado digital)
        String signedXML = digitalSignatureService.sign(xmlInvoice);
        
        // Transmitir a DIAN
        DIANResponse response = dianClient.sendInvoice(signedXML);
        
        if (response.isSuccess()) {
            invoice.setTransmittedAt(LocalDateTime.now());
            invoice.setDianTrackingId(response.getTrackingId());
            invoiceRepository.save(invoice);
        } else {
            throw new DIANTransmissionException(
                response.getErrorCode(),
                response.getErrorMessage()
            );
        }
    }
    
    /**
     * Construye XML según esquema DIAN.
     */
    private String buildDIANXML(Invoice invoice) {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <Invoice xmlns="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2">
                <cbc:ID>%s</cbc:ID>
                <cbc:UUID>%s</cbc:UUID>
                <cbc:IssueDate>%s</cbc:IssueDate>
                <cbc:InvoiceTypeCode>01</cbc:InvoiceTypeCode>
                <cac:AccountingSupplierParty>
                    <cac:Party>
                        <cac:PartyIdentification>
                            <cbc:ID>%s</cbc:ID>
                        </cac:PartyIdentification>
                    </cac:Party>
                </cac:AccountingSupplierParty>
                <cac:LegalMonetaryTotal>
                    <cbc:TaxExclusiveAmount>%s</cbc:TaxExclusiveAmount>
                    <cbc:TaxInclusiveAmount>%s</cbc:TaxInclusiveAmount>
                    <cbc:PayableAmount>%s</cbc:PayableAmount>
                </cac:LegalMonetaryTotal>
            </Invoice>
            """.formatted(
                invoice.getInvoiceNumber(),
                invoice.getCufe(),
                invoice.getIssuedAt().toLocalDate(),
                invoice.getProviderNIT(),
                invoice.getSubtotal(),
                invoice.getTotal(),
                invoice.getTotal()
            );
    }
}
```

### 5. Anulación con Nota Crédito
```java
public class Invoice {
    /**
     * Cancelación con nota crédito (cumplimiento DIAN).
     */
    public CreditNote cancel(String reason, String responsibleUser) {
        // Validación: solo PENDING puede cancelarse
        if (this.status != InvoiceStatus.PENDING) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_CANNOT_CANCEL,
                "Invoice",
                "Only PENDING invoices can be cancelled"
            );
        }
        
        // Crear nota crédito
        CreditNote creditNote = CreditNote.create(
            this.id,
            this.invoiceNumber,
            this.total,
            reason,
            responsibleUser,
            LocalDateTime.now()
        );
        
        // Generar número consecutivo de nota crédito
        creditNote.setNumber(creditNoteNumberGenerator.generateNext());
        
        // Transmitir a DIAN
        dianService.transmitCreditNote(creditNote);
        
        // Marcar factura como cancelada
        this.status = InvoiceStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.cancellationReason = reason;
        
        this.addDomainEvent(new InvoiceCancelled(
            this.id,
            creditNote.getNumber(),
            reason
        ));
        
        return creditNote;
    }
}
```

### 6. Conservación Legal (5 años)
```java
@Service
public class InvoiceArchivalService {
    /**
     * Archiva factura en formato inmutable (PDF/A).
     * Cumplimiento: conservación 5 años.
     */
    public void archiveInvoice(InvoiceId invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId);
        
        // Generar PDF/A (formato archivo)
        byte[] pdfBytes = pdfGenerator.generateInvoicePDF(invoice);
        
        // Almacenar en sistema de archivos inmutable
        String archivePath = documentArchiveService.store(
            pdfBytes,
            String.format("invoice_%s.pdf", invoice.getInvoiceNumber()),
            LocalDate.now().plusYears(5) // Retención 5 años
        );
        
        invoice.setArchivePath(archivePath);
        invoiceRepository.save(invoice);
    }
}
```

## Consecuencias

### Positivas
1. **✅ Cumplimiento legal**: Facturación conforme a DIAN
2. **✅ Trazabilidad**: CUFE permite validación en portal DIAN
3. **✅ Auditoría**: Conservación legal por 5 años
4. **✅ Integridad**: Factura inmutable tras emisión
5. **✅ Interoperabilidad**: XML estándar UBL 2.1

### Negativas
1. **⚠️ Complejidad**: Integración con DIAN requiere certificado digital
2. **⚠️ Dependencia externa**: DIAN debe estar disponible
3. **⚠️ Costo**: Certificado digital (~ $300,000 COP/año)
4. **⚠️ Latencia**: Transmisión puede demorar 5-10 segundos

### Mitigaciones
```java
// Cola asíncrona para transmisión DIAN
@Async
public void transmitInvoiceAsync(InvoiceId invoiceId) {
    try {
        Invoice invoice = invoiceRepository.findById(invoiceId);
        dianService.transmitInvoice(invoice);
    } catch (DIANTransmissionException e) {
        // Reintentar 3 veces
        retryService.scheduleRetry(invoiceId, 3);
    }
}
```

## Implementación

### Fase 1: Numeración Consecutiva ✅
```java
InvoiceNumberGenerator → genera FECO-0001, FECO-0002, ...
```

### Fase 2: Generación CUFE ✅
```java
Invoice.generateCUFE() → SHA-384 según DIAN
```

### Fase 3: Transmisión DIAN 🔄
```java
DIANIntegrationService.transmitInvoice()
```

### Fase 4: Archivo PDF/A 📋
```java
InvoiceArchivalService.archiveInvoice()
```

## Notas
- Requiere **certificado digital** emitido por proveedor autorizado DIAN
- Relacionado con **[ADR-(Facturación)-01-Validación de Tarifas Vigentes al Momento de Facturar.md](ADR-(Facturación)-01-Validación de Tarifas Vigentes al Momento de Facturar.md)** (validación tarifas)
- Relacionado con **[ADR-(Facturación)-02-Snapshot Inmutable de Precios en Facturación.md](ADR-(Facturación)-02-Snapshot Inmutable de Precios en Facturación.md)** (snapshot inmutable)
- Proveedor DIAN recomendado: Certicámara, GSE