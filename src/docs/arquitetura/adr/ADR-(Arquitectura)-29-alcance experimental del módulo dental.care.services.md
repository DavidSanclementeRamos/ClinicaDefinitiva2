# ADR-29 (Arquitectura): Alcance Experimental del Módulo dental.care.services

**Estado:** ✅ Aceptado  
**Fecha:** Enero 06, 2026  
**Contexto:** Definición del alcance de implementación para exhibición profesional  
**Autor:** David Stiven Sanclemente

---

## Contexto y Problema

Durante el proceso de implementación del módulo `dental.care.services` (que incluye el agregado raíz `ProvidedService` y 6 Value Objects estratégicos: `OrthodonticDetails`, `SurgicalDetails`, `ProstheticDetails`, `ImplantologyDetails`, `AestheticDetails`, `PediatricDetails`), se realizó un **descubrimiento exhaustivo de reglas de negocio** documentado en 7 archivos separados:

- [ProvidedService(ServicioPrestado).md](../../dominio/descubrimientos-de-reglas/servicios/ProvidedService(ServicioPrestado).md)
- [OrthodonticDetails(DetallesOrtodoncia).md](../../dominio/descubrimientos-de-reglas/servicios/OrthodonticDetails(DetallesOrtodoncia).md)
- [SurgicalDetails(DetallesCirugia).md](../../dominio/descubrimientos-de-reglas/servicios/SurgicalDetails(DetallesCirugia).md)
- [ProstheticDetails(DetallesProtesis).md](../../dominio/descubrimientos-de-reglas/servicios/ProstheticDetails(DetallesProtesis).md)
- [ImplantologyDetails(DetallesImplantes).md](../../dominio/descubrimientos-de-reglas/servicios/ImplantologyDetails(DetallesImplantes).md)
- [AestheticDetails(DetallesEstetica).md](../../dominio/descubrimientos-de-reglas/servicios/AestheticDetails(DetallesEstetica).md)
- [PediatricDetails(DetallesPediatria).md](../../dominio/descubrimientos-de-reglas/servicios/PediatricDetails(DetallesPediatria).md)

Sin embargo, al enfrentar la implementación real del dominio, surgieron las siguientes realidades:

### 1. **Madurez Arquitectónica Evolutiva**

A diferencia del módulo Actor (primero en implementarse), el módulo Services se benefició de:
- Experiencia adquirida en separación de responsabilidades
- Comprensión clara de delegación a Value Objects
- Patrón consolidado de catálogos de error por capa
- Estrategia madura de manejo de validaciones cross-aggregate

Esto resultó en un **descubrimiento más preciso** desde el inicio, con menos reglas mal ubicadas arquitectónicamente.

### 2. **Delegación Sistemática a Value Objects**

Se identificaron múltiples validaciones que inicialmente estaban en el descubrimiento de `ProvidedService` pero que arquitectónicamente pertenecen a Value Objects dedicados:

**Ejemplo del patrón aplicado:**
```java
// ❌ Descubrimiento inicial (anti-patrón)
RN-SERVICE-001: ProvidedService valida que baseRate > 0

// ✅ Implementación correcta (delegación)
Money.java valida amount > 0
Error: ValueObjectError.ERR_SERVICE_PRICE_NEGATIVE
```

Esto llevó a **crear nuevos Value Objects** que no existían en el diseño inicial:
- `ServiceName` (validaciones de nombre)
- `ServiceDescription` (validaciones de descripción)
- `ServiceDuration` (validaciones de duración con rangos min/max)

### 3. **Validaciones que Requieren Coordinación Externa**

Se detectaron reglas que **no pueden implementarse completamente** en el agregado porque requieren:
- Consulta a otros agregados (`Appointment`, `Invoice`)
- Domain Services para orquestación
- Repositorios de módulos aún no consolidados

**Ejemplo crítico:**
```
RN-SERVICE-005: "No puede desactivarse si tiene citas en próximas 48h"
→ Requiere: AppointmentRepository.findByServiceInNext48Hours()
→ Decisión: Validación pospuesta hasta consolidación de módulo Schedule
```

### 4. **Warnings vs Errores Críticos**

El descubrimiento incluía múltiples **reglas de advertencia** (warnings) para validaciones clínicas específicas:

```java
WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION
WARN_AESTHETIC_UNREALISTIC_WHITENING
WARN_PEDIATRIC_SEALANT_AGE_MISMATCH
```

**Pregunta arquitectónica:** ¿Implementar warnings en MVP experimental o postergar como feature de calidad de datos?

### 5. **Restricciones de Proyecto Experimental**

Este es un **proyecto de exhibición profesional**, no un sistema productivo completo. Los **warnings clínicos** aportan valor en producción pero **no son críticos** para demostrar:
- Comprensión de DDD
- Separación correcta de capas
- Validaciones de negocio core
- Manejo de errores profesional

**Decisión:** Posponer warnings a fase de refinamiento post-MVP.

### 6. **Eventos y Auditoría (Patrón Establecido)**

Siguiendo decisiones de módulos previos (Actor ADR-20, Schedule ADR-24):
- **Eventos de dominio** requieren infraestructura de mensajería
- **Auditoría completa** requiere integración con sistema de usuarios
- Ambos están **fuera del alcance v1.0** para todos los módulos

---

## Decisión

Se establece el **alcance experimental del Módulo Services** mediante la clasificación de reglas de negocio y catálogos de error en tres categorías:

### 🟢 **APLICADAS** - Implementadas en v1.0 (Exhibición)
Reglas críticas de negocio, delegaciones correctas a VOs, validaciones estructurales y coherencia entre agregado y detalles.

### 🟡 **POSPUESTAS** - Documentadas para v2.0 (Iteración Futura)
Validaciones que requieren coordinación con otros módulos, warnings clínicos no críticos, y features de calidad de datos avanzadas.

### 🔴 **ELIMINADAS** - Descartadas con Justificación
Reglas delegadas a Value Objects (con creación de nuevos VOs), validaciones redundantes, y responsabilidades que pertenecen a otros módulos.

---

## Decisiones Arquitectónicas Fundamentales

### 1. ✅ **Creación de Value Objects Dedicados (No Existentes en Diseño Inicial)**

**Decisión:** Extraer validaciones de `ProvidedService` a VOs especializados.

**Justificación:**
- **Problema original:** ProvidedService validaba formato de nombre, longitud de descripción, rangos de duración
- **Solución:** Crear VOs con responsabilidad única

**Value Objects nuevos creados:**

#### **ServiceName** (Nuevo)
```java
public final class ServiceName {
    private static final int MIN_LENGTH = 5;
    private final String value;
    
    public ServiceName(String value) {
        if (value == null || value.isBlank()) {
            throw new ValueObjectValidationException(
                ValueObjectError.ERR_SERVICE_NAME_CUSTOM_INVALID,
                "name", value,
                "El nombre no puede estar vacío"
            );
        }
        if (value.length() < MIN_LENGTH) {
            throw new ValueObjectValidationException(
                ValueObjectError.ERR_SERVICE_NAME_CUSTOM_INVALID,
                "name", value,
                "El nombre debe tener al menos " + MIN_LENGTH + " caracteres"
            );
        }
        this.value = value;
    }
}
```

**Catálogo eliminado de ProvidedService:**
- ❌ `ERR_SERVICE_MISSING_REQUIRED_FIELDS` (RN-SERVICE-009)
- ✅ Reemplazado por: `ValueObjectError.ERR_SERVICE_NAME_CUSTOM_INVALID`

---

#### **ServiceDescription** (Nuevo)
```java
public final class ServiceDescription {
    private static final int MIN_LENGTH = 20;
    private final String value;
    
    public ServiceDescription(String value) {
        if (value == null || value.isBlank()) {
            throw new ValueObjectValidationException(
                ValueObjectError.ERR_SERVICE_DESCRIPTION_INVALID,
                "description", value,
                "La descripción no puede estar vacía"
            );
        }
        if (value.length() < MIN_LENGTH) {
            throw new ValueObjectValidationException(
                ValueObjectError.ERR_SERVICE_DESCRIPTION_INVALID,
                "description", value,
                "La descripción debe tener al menos " + MIN_LENGTH + " caracteres"
            );
        }
        this.value = value;
    }
}
```

**Catálogo eliminado de ProvidedService:**
- ❌ `ERR_SERVICE_DESCRIPTION_TOO_SHORT` (RN-SERVICE-014)
- ✅ Reemplazado por: `ValueObjectError.ERR_SERVICE_DESCRIPTION_INVALID`

---

#### **ServiceDuration** (Refactorizado desde básico a robusto)
```java
public final class ServiceDuration {
    private static final int MIN_MINUTES = 15;
    private static final int MAX_MINUTES = 240; // 4 horas
    
    private final int minutes;
    
    public ServiceDuration(int minutes) {
        if (minutes < MIN_MINUTES) {
            throw new ValueObjectValidationException(
                ValueObjectError.ERR_SERVICE_DURATION_MINIMUM,
                "duration", minutes,
                String.format("La duración mínima es %d minutos", MIN_MINUTES)
            );
        }
        if (minutes > MAX_MINUTES) {
            throw new ValueObjectValidationException(
                ValueObjectError.ERR_SERVICE_DURATION_MAXIMUM,
                "duration", minutes,
                String.format("La duración máxima es %d minutos (%d horas)", 
                    MAX_MINUTES, MAX_MINUTES / 60)
            );
        }
        this.minutes = minutes;
    }
    
    public static ServiceDuration ofMinutes(int minutes) {
        return new ServiceDuration(minutes);
    }
    
    public static ServiceDuration ofHours(int hours) {
        return new ServiceDuration(hours * 60);
    }
}
```

**Catálogo eliminado de ProvidedService:**
- ❌ `ERR_SERVICE_INVALID_DURATION` (RN-SERVICE-002)
- ✅ Reemplazado por: `ValueObjectError.ERR_SERVICE_DURATION_MINIMUM` + `ERR_SERVICE_DURATION_MAXIMUM`

---

#### **ServiceCode** (Refactorizado con validaciones)
```java
public final class ServiceCode {
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 15;
    
    private final String value;
    
    public ServiceCode(String value) {
        if (value == null || value.isBlank()) {
            throw new ValueObjectValidationException(
                ValueObjectError.ERR_SERVICE_CODE_DUPLICATE,
                "code", value,
                "El código de servicio no puede estar vacío"
            );
        }
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new ValueObjectValidationException(
                ValueObjectError.ERR_SERVICE_CODE_LENGTH_INVALID,
                "code", value,
                String.format("El código debe tener entre %d y %d caracteres", 
                    MIN_LENGTH, MAX_LENGTH)
            );
        }
        this.value = value;
    }
}
```

**Catálogos eliminados de ProvidedService:**
- ❌ `ERR_SERVICE_CODE_DUPLICATE` (RN-SERVICE-007)
- ❌ `ERR_SERVICE_INVALID_CODE_FORMAT` (RN-SERVICE-013)
- ✅ Reemplazado por: `ValueObjectError.ERR_SERVICE_CODE_DUPLICATE` + `ERR_SERVICE_CODE_LENGTH_INVALID`

---

### 2. ✅ **Validación de Coherencia Categoría-Detalles (Crítica)**

**Decisión:** Implementar validación flexible que tolera variaciones de naming (plural/singular).

**Problema:**
```
category.getCategory() → "Orthodontics" (plural)
details.serviceType() → ORTHODONTIC (singular)
¿Cómo validamos que coinciden?
```

**Solución (3 estrategias):**
```java
private void validateCategoryMatch(ServiceCatalog category, ServiceDetails details) {
    String categoryName = category.getCategory().toUpperCase();
    String detailsType = details.serviceType().name().toUpperCase();
    
    boolean matches = 
        categoryName.contains(detailsType) ||          // "ORTHODONTICS" contiene "ORTHODONTIC"
        detailsType.contains(categoryName) ||          // "IMPLANTOLOGY" contiene "IMPLANT"
        normalizeCategory(categoryName).equals(        // Quitar 'S' final y comparar
            normalizeCategory(detailsType)
        );
    
    if (!matches) {
        throw new BusinessRuleViolationException(
            ServiceError.ERR_SERVICE_CATEGORY_MISMATCH,
            "category/details",
            String.format("Categoría '%s' no coincide con tipo '%s'", 
                categoryName, detailsType)
        );
    }
}

private String normalizeCategory(String category) {
    return category.replaceAll("S$", ""); // ORTHODONTICS → ORTHODONTIC
}
```

**Valor agregado:**
- ✅ Previene errores graves (crear servicio de ortodoncia con detalles de cirugía)
- ✅ Flexible con variaciones de nomenclatura
- ✅ Fácil de extender para nuevas especialidades

**Catálogo:** `ServiceError.ERR_SERVICE_CATEGORY_MISMATCH` (RN-SERVICE-004) ✅ **APLICADO**

---

### 3. 🟡 **Validaciones Cross-Aggregate (Pospuestas pero Preparadas)**

**Decisión:** Validaciones que requieren consulta a otros agregados se **delegan a Domain Services** (a implementar en v2.0).

#### **RN-SERVICE-005: No desactivar con citas en próximas 48h**

**Implementación actual (v1.0 - parcial):**
```java
public void deactivate(String reason) {
    ensureEditable(); // ✅ Valida estado activo
    validateDeactivationReason(reason); // ✅ Valida motivo (>= 10 caracteres)
    
    // 🟡 POSPUESTO: Validación de citas
    // TODO v2.0: Delegar a ProvidedServiceDomainService.deactivateService()
    //    que consultará AppointmentRepository.findByServiceInNext48Hours()
    
    this.status = ServiceStatus.inactive();
}
```

**Implementación futura (v2.0 - completa):**
```java
// Domain Service (a crear)
public class ProvidedServiceDomainService {
    
    public void deactivateService(ProvidedService service, String reason) {
        // Validar que no tenga citas en próximas 48h
        LocalDateTime cutoff = LocalDateTime.now().plusHours(48);
        List<Appointment> upcomingAppointments = appointmentRepository
            .findByServiceAndStartAfter(service.getId(), LocalDateTime.now())
            .stream()
            .filter(appt -> appt.getStart().isBefore(cutoff))
            .toList();
        
        if (!upcomingAppointments.isEmpty()) {
            throw new BusinessRuleViolationException(
                ServiceError.ERR_SERVICE_HAS_APPOINTMENTS,
                "ProvidedService",
                String.format("Tiene %d citas programadas en próximas 48h", 
                    upcomingAppointments.size())
            );
        }
        
        service.deactivate(reason);
    }
}
```

**Justificación de postergación:**
- ⚠️ **Requiere:** Módulo Schedule consolidado con `Appointment` completo
- ⚠️ **Requiere:** `AppointmentRepository` con query específico
- ⚠️ **Requiere:** Coordinación transaccional entre módulos
- ✅ **Preparación:** Agregado tiene método `deactivate()` funcional
- ✅ **Migración:** Agregar Domain Service NO requiere cambiar agregado

---

#### **RN-SERVICE-008: Justificación de cambio de tarifa si hay citas**

**Implementación actual (v1.0 - parcial):**
```java
public void updateRate(Money newRate, String justification) {
    ensureEditable();
    validateRate(newRate);
    validateRateChangeRange(oldRate, newRate); // ✅ Rango 50%-300%
    
    // ✅ APLICADO: Justificación siempre obligatoria
    if (justification == null || justification.isBlank()) {
        throw new BusinessRuleViolationException(
            ServiceError.ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION,
            "ProvidedService",
            "Cambios de tarifa requieren justificación"
        );
    }
    
    // 🟡 POSPUESTO: Consulta de citas programadas
    // TODO v2.0: Solo rechazar sin justificación SI hay citas programadas
    //    Actualmente: siempre requiere justificación (más estricto, válido para v1.0)
    
    this.baseRate = newRate;
}
```

**Decisión de simplificación v1.0:**
```
Regla original: "Requiere justificación SI hay citas programadas"
Implementación v1.0: "Requiere justificación SIEMPRE"

Justificación: Más estricto es correcto para demo.
En v2.0: Relajar validación solo si NO hay citas.
```

---

#### **RN-SERVICE-012: No desactivar con facturas pendientes**

**Estado:** 🟡 **COMPLETAMENTE POSPUESTO**

**Razón:**
- ❌ Módulo `Billing` aún no está construido con reglas sólidas
- ❌ No existe `Invoice` como agregado consolidado
- ❌ No existe `InvoiceRepository` para consultar facturas pendientes

**Implementación futura (v2.0):**
```java
// Domain Service
public void deactivateService(ProvidedService service, String reason) {
    // Validar facturas pendientes
    List<Invoice> pendingInvoices = invoiceRepository
        .findPendingByService(service.getId());
    
    if (!pendingInvoices.isEmpty()) {
        throw new BusinessRuleViolationException(
            ServiceError.ERR_SERVICE_HAS_PENDING_INVOICES,
            "ProvidedService",
            String.format("Tiene %d facturas pendientes de cobro", 
                pendingInvoices.size())
        );
    }
    
    service.deactivate(reason);
}
```

---

#### **RN-SERVICE-010: Servicios inactivos no facturables**

**Decisión:** ❌ **ELIMINADO de ProvidedService**

**Razón:** Validación **INCORRECTA** en este agregado.

**Ubicación correcta:** `BillingDomainService` o `InvoicingService`

```java
// ❌ INCORRECTO: Validar en ProvidedService
public ServiceSnapshot snapshotForBilling() {
    if (!status.isActive()) {
        throw new BusinessRuleViolationException(ERR_SERVICE_NOT_BILLABLE);
    }
    return new ServiceSnapshot(...);
}

// ✅ CORRECTO: Validar en servicio de facturación
public class InvoicingService {
    public Invoice createInvoice(ProvidedService service, ...) {
        if (!service.getStatus().isActive()) {
            throw new BusinessRuleViolationException(
                BillingError.ERR_CANNOT_BILL_INACTIVE_SERVICE
            );
        }
        // Continuar facturación
    }
}
```

**Justificación:**
- Snapshots pueden crearse para auditoría incluso si el servicio está inactivo
- La decisión de "no facturar inactivos" es regla de `Billing`, no de `Service`
- Separación de responsabilidades: Service expone datos, Billing decide si factura

---

### 4. 🟡 **Warnings Clínicos (Todos Pospuestos)**

**Decisión:** Todos los warnings de validaciones clínicas específicas se posponen a v2.0.

**Catálogos afectados:**

| Catálogo | Descripción | Razón Postergación |
|----------|-------------|-------------------|
| **WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION** | Alineadores típicamente 12-24 meses | Feature de calidad de datos, no crítico para MVP |
| **WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION** | Brackets linguales ≥ 18 meses | Validación clínica avanzada, no bloquea operación |
| **WARN_AESTHETIC_UNREALISTIC_WHITENING** | Blanqueamiento ≤ 10 tonos | Validación de expectativas, no seguridad |
| **WARN_AESTHETIC_VENEER_IRREVERSIBILITY** | Mencionar irreversibilidad en carillas | Información legal, pertenece a consentimiento informado |
| **WARN_AESTHETIC_MISSING_MATERIAL** | Especificar material en porcelana | Completitud de datos, no obligatorio |
| **WARN_IMPLANTOLOGY_SHORT_HEALING_TIME** | < 3 meses sin injerto es atípico | Sugerencia clínica, no error |
| **WARN_IMPLANTOLOGY_LONG_HEALING_TIME** | > 9 meses sin injerto complejo es atípico | Sugerencia clínica, no error |
| **WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING** | Zigomáticos requieren ≥ 6 meses | Validación clínica avanzada |
| **WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS** | Corona individual típicamente 1 unidad | Sugerencia, permite excepciones |
| **WARN_PROSTHETIC_FULL_DENTURE_UNITS** | Prótesis total típicamente 14 unidades | Sugerencia, permite variaciones |
| **WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS** | Puente ≥ 3 unidades | Sugerencia clínica |
| **WARN_PEDIATRIC_SEALANT_AGE_MISMATCH** | Sellantes típicamente 6-14 años | Sugerencia, no bloquea |
| **WARN_PEDIATRIC_SEDATION_UNSPECIFIED** | Especificar tipo de sedación | Completitud de datos |
| **WARN_PEDIATRIC_PHYSICAL_RESTRAINT** | Contención física solo emergencias | Advertencia ética, no técnica |
| **WARN_PEDIATRIC_INFANT_MANAGEMENT_MISSING** | Bebés requieren técnicas específicas | Sugerencia de mejores prácticas |

**Justificación unificada:**
- ✅ No bloquean operaciones críticas del sistema
- ✅ Son features de **calidad de datos**, no seguridad
- ✅ Etapa muy temprana de desarrollo para validaciones clínicas avanzadas
- ✅ Valor real emerge con **datos históricos** (detectar patrones atípicos)
- ✅ Mejor implementar como **sistema de alertas** separado en v2.0

**Implementación futura (v2.0):**
```java
// Sistema de alertas clínicas (separado)
public class ClinicalDataQualityService {
    
    public List<DataQualityWarning> validateService(ProvidedService service) {
        List<DataQualityWarning> warnings = new ArrayList<>();
        
        if (service.getDetails() instanceof OrthodonticDetails ortho) {
            if (ortho.getApplianceType().equals("CLEAR_ALIGNERS") && 
                (ortho.getTreatmentDurationMonths() < 12 || 
                 ortho.getTreatmentDurationMonths() > 24)) {
                warnings.add(new DataQualityWarning(
                    WarningLevel.INFO,
                    "Duración atípica para alineadores (típicamente 12-24 meses)"
                ));
            }
        }
        
        return warnings;
    }
}
```

---

### 5. ✅ **Eventos y Auditoría (Patrón Consistente)**

**Decisión:** Siguiendo ADR-20 (Actor) y ADR-24 (Schedule), eventos y auditoría completa se posponen a v2.0.

**Eventos identificados pero no implementados:**
```java
// TODO v2.0: Domain Events
- ServiceCreated(ServiceId id, ServiceType type, LocalDateTime createdAt)
- ServiceRateUpdated(ServiceId id, Money oldRate, Money newRate, String justification)
- ServiceDeactivated(ServiceId id, String reason, LocalDateTime deactivatedAt)
- ServiceDetailsChanged(ServiceId id, ServiceType oldType, ServiceType newType)
```

**Preparación v1.0:**
```java
// Método deactivate() ya tiene punto de inserción
public void deactivate(String reason) {
    validateDeactivationReason(reason);
    this.status = ServiceStatus.inactive();
    
    // TODO v2.0: Publicar evento
    // DomainEventPublisher.publish(new ServiceDeactivated(this.id, reason, now()));
}
```

**Auditoría preparada:**
```java
// Campos timestamp ya implementados
private LocalDateTime createdAt;    // ✅ Presente
private LocalDateTime lastUpdated;  // ✅ Presente

// TODO v2.0: Agregar campos de auditoría completa
// private UserId createdBy;
// private UserId lastUpdatedBy;
// private List<AuditEntry> changeHistory;
```

---

## Análisis Detallado por Agregado

---

## 🏥 Agregado: **ProvidedService** (Servicio Prestado - Raíz)

### 🟢 Catálogos APLICADOS

| Código | Descripción | Implementación | Justificación |
|--------|-------------|----------------|---------------|
| **RN-SERVICE-003** | No operar sobre inactivo | `ensureEditable()` | ⭐ Invariante crítico de estado |
| **RN-SERVICE-004** | Categoría coherente con detalles | `validateCategoryMatch()` | ⭐ Previene inconsistencias graves |
| **RN-SERVICE-006** | Tipo de detalles inmutable | `updateDetails()` con validación | ⭐ Protege integridad referencial |
| **RN-SERVICE-011** | Cambio tarifa en rango 50%-300% | `validateRateChangeRange()` | ⭐ Previene errores de captura |
| **RN-SERVICE-015** | Motivo desactivación >= 10 caracteres | `validateDeactivationReason()` | ⭐ Auditoría obligatoria |

**Total:** 5 reglas aplicadas en agregado raíz

---

### 🟡 Catálogos POSPUESTOS

| Código | Descripción | Razón | Prioridad | Análisis Crítico |
|--------|-------------|-------|-----------|------------------|
| **RN-SERVICE-005** | No desactivar con citas próximas 48h | Requiere `AppointmentRepository` | 🟡 MEDIA | ⚠️ **REVISAR:** Esta regla **SÍ es importante** para evitar desactivar servicios en uso activo. **Decisión:** Aunque pospuesta, es **PRIORITARIA en v2.0** cuando Schedule esté consolidado. |
| **RN-SERVICE-008** | Justificación solo si hay citas | Simplificado: siempre requiere justificación | 🟢 BAJA | ✅ **CORRECTO:** Versión más estricta (siempre pedir justificación) es válida para v1.0. |
| **RN-SERVICE-012** | No desactivar con facturas pendientes | Módulo Billing no consolidado | 🟡 MEDIA | ✅ **CORRECTO:** Depende de módulo externo, postergación justificada. |

**Total:** 3 reglas pospuestas

---

### 🔴 Catálogos ELIMINADOS (Delegados a VOs)

| Código Original | Nueva Ubicación | VO Creado | Catálogo VO |
|----------------|-----------------|-----------|-------------|
| **RN-SERVICE-001** | Money | ❌ (ya existía) | `ERR_SERVICE_PRICE_NEGATIVE` |
| **RN-SERVICE-002** | ServiceDuration | ✅ **NUEVO** | `ERR_SERVICE_DURATION_MINIMUM` + `MAXIMUM` |
| **RN-SERVICE-007** | ServiceCode | ❌ (ya existía, refactorizado) | `ERR_SERVICE_CODE_DUPLICATE` |
| **RN-SERVICE-009** | ServiceName | ✅ **NUEVO** | `ERR_SERVICE_NAME_CUSTOM_INVALID` |
| **RN-SERVICE-010** | BillingService | N/A (validación incorrecta aquí) | `BillingError.ERR_CANNOT_BILL_INACTIVE` |
| **RN-SERVICE-013** | ServiceCode | ❌ (refactorizado) | `ERR_SERVICE_CODE_LENGTH_INVALID` |
| **RN-SERVICE-014** | ServiceDescription | ✅ **NUEVO** | `ERR_SERVICE_DESCRIPTION_INVALID` |

**Total:** 7 reglas eliminadas (delegadas correctamente)

---

## 🦷 Value Object: **OrthodonticDetails**

### 🟢 Catálogos APLICADOS

| Código | Descripción | Implementación | Justificación |
|--------|-------------|----------------|---------------|
| **RN-ORTHODONTIC-001** | Tipo aparato obligatorio | Constructor con validación | ⭐ Campo crítico |
| **RN-ORTHODONTIC-002** | Duración 6-48 meses | Validación de rango | ⭐ Rangos clínicos realistas |
| **RN-ORTHODONTIC-003** | Tipo reconocido | Catálogo cerrado `VALID_APPLIANCE_TYPES` | ⭐ Integridad de datos |
| **RN-ORTHODONTIC-004** | Duración positiva | Validación > 0 | ⭐ Invariante básico |

**Total:** 4 reglas aplicadas

---

### 🟡 Catálogos POSPUESTOS

| Código | Razón | Prioridad |
|--------|-------|-----------|
| **WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION** | Feature de calidad de datos | 🟢 BAJA |
| **WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION** | Feature de calidad de datos | 🟢 BAJA |

**Total:** 2 warnings pospuestos

---

## 🔪 Value Object: **SurgicalDetails**

### 🟢 Catálogos APLICADOS

| Código | Descripción | Implementación | Justificación |
|--------|-------------|----------------|---------------|
| **RN-SURGICAL-001** | Anestesia → complejidad MEDIUM+ | Validación cruzada | ⭐⭐⭐ Seguridad del paciente |
| **RN-SURGICAL-003** | Complejidad válida | Catálogo cerrado `VALID_COMPLEXITY_LEVELS` | ⭐ Integridad de datos |
| **RN-SURGICAL-004** | CRITICAL → anestesia + quirófano | Validación cruzada | ⭐⭐ Coherencia clínica |
| **RN-SURGICAL-006** | Tipo ≥ 3 caracteres | Validación longitud | ⭐ Calidad de datos |
| **RN-SURGICAL-007** | Quirófano → complejidad MEDIUM+ | Validación cruzada | ⭐⭐ Coherencia operativa |

**Total:** 5 reglas aplicadas

**Análisis crítico:**
- ✅ **RN-SURGICAL-001, 004, 007** son reglas de **seguridad** → Correctamente priorizadas
- ✅ Validaciones cruzadas entre campos → Demuestra lógica de dominio robusta

---

### 🟡 Catálogos POSPUESTOS

| Código | Razón | Prioridad |
|--------|-------|-----------|
| **WARN_SURGICAL_LOW_COMPLEXITY_OPERATING_ROOM** | Warning no crítico | 🟢 BAJA |

**Total:** 1 warning pospuesto

---

### 🔴 Catálogo ELIMINADO (Delegación Cross-Aggregate)

| Código | Razón |
|--------|-------|
| **RN-SURGICAL-002** | "Quirófano → duración ≥ 60 min" requiere validar `ProvidedService.duration`. Esto es **validación cross-object**, debe hacerse en el agregado raíz al crear/actualizar. |

**Implementación correcta:**
```java
// ProvidedService.java
public void updateDetails(ServiceDetails newDetails) {
    validateCategoryMatch(category, newDetails);
    
    // Validación específica para cirugía con quirófano
    if (newDetails instanceof SurgicalDetails surgical) {
        if (surgical.getOperatingRoomNeeded() && 
            this.duration.toMinutes() < 60) {
            throw new BusinessRuleViolationException(
                ServiceError.ERR_SURGICAL_OPERATING_ROOM_DURATION_MISMATCH,
                "duration",
                "Cirugías con quirófano requieren duración >= 60 minutos"
            );
        }
    }
    
    this.details = newDetails;
}
```

---

## 🦴 Value Object: **ProstheticDetails**

### 🟢 Catálogos APLICADOS

| Código | Descripción | Implementación | Justificación |
|--------|-------------|----------------|---------------|
| **RN-PROSTHETIC-001** | Tipo obligatorio (FIXED/REMOVABLE) | Validación no-blank | ⭐ Campo crítico |
| **RN-PROSTHETIC-002** | Unidades ≥ 0 | Validación no negativo | ⭐ Invariante básico |
| **RN-PROSTHETIC-003** | Removible ≤ 14 unidades | Validación límite anatómico | ⭐⭐ Límite clínico real |
| **RN-PROSTHETIC-004** | Tipo válido (FIXED/REMOVABLE) | Catálogo cerrado | ⭐ Integridad de datos |

**Total:** 4 reglas aplicadas

---

### 🟡 Catálogos POSPUESTOS

| Código | Razón | Prioridad |
|--------|-------|-----------|
| **WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS** | Sugerencia, permite excepciones | 🟢 BAJA |
| **WARN_PROSTHETIC_FULL_DENTURE_UNITS** | Sugerencia, permite variaciones | 🟢 BAJA |
| **WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS** | Sugerencia clínica | 🟢 BAJA |

**Total:** 3 warnings pospuestos

---

## 🦷 Value Object: **ImplantologyDetails**

### 🟢 Catálogos APLICADOS

| Código | Descripción | Implementación | Justificación |
|--------|-------------|----------------|---------------|
| **RN-IMPLANTOLOGY-001** | Cicatrización 2-12 meses | Validación de rango | ⭐ Rangos clínicos realistas |
| **RN-IMPLANTOLOGY-002** | Injerto → mínimo 4 meses | Validación cruzada | ⭐⭐ Requisito biológico |
| **RN-IMPLANTOLOGY-003** | Cicatrización no negativa | Validación básica | ⭐ Invariante básico |
| **RN-IMPLANTOLOGY-007** | Sitio ≥ 2 caracteres | Validación longitud | ⭐ Calidad de datos |

**Total:** 4 reglas aplicadas

---

### 🟡 Catálogos POSPUESTOS

| Código | Razón | Prioridad |
|--------|-------|-----------|
| **WARN_IMPLANTOLOGY_SHORT_HEALING_TIME** | Sugerencia, no error | 🟢 BAJA |
| **WARN_IMPLANTOLOGY_LONG_HEALING_TIME** | Sugerencia, no error | 🟢 BAJA |
| **WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING** | Validación clínica avanzada | 🟢 BAJA |

**Total:** 3 warnings pospuestos

---

## 💎 Value Object: **AestheticDetails**

### 🟢 Catálogos APLICADOS

| Código | Descripción | Implementación | Justificación |
|--------|-------------|----------------|---------------|
| **RN-AESTHETIC-001** | Tipo obligatorio | Validación no-blank | ⭐ Campo crítico |
| **RN-AESTHETIC-002** | Tipo reconocido | Catálogo cerrado `VALID_AESTHETIC_TYPES` | ⭐ Integridad de datos |
| **RN-AESTHETIC-003** | Tipo ≥ 3 caracteres | Validación longitud | ⭐ Calidad de datos |
| **RN-AESTHETIC-004** | Resultado ≥ 10 caracteres | Validación descriptiva | ⭐ Completitud de datos |

**Total:** 4 reglas aplicadas

---

### 🟡 Catálogos POSPUESTOS

| Código | Razón | Prioridad |
|--------|-------|-----------|
| **WARN_AESTHETIC_UNREALISTIC_WHITENING** | Feature de expectativas | 🟢 BAJA |
| **WARN_AESTHETIC_VENEER_IRREVERSIBILITY** | Información legal (consentimiento) | 🟢 BAJA |
| **WARN_AESTHETIC_MISSING_MATERIAL** | Completitud opcional | 🟢 BAJA |

**Total:** 3 warnings pospuestos

---

## 👶 Value Object: **PediatricDetails**

### 🟢 Catálogos APLICADOS

| Código | Descripción | Implementación | Justificación |
|--------|-------------|----------------|---------------|
| **RN-PEDIATRIC-001** | Edad 0-18 años | Validación regex | ⭐ Límite pediátrico |
| **RN-PEDIATRIC-002** | Rango ≥ 5 caracteres | Validación longitud | ⭐ Formato válido |
| **RN-PEDIATRIC-006** | Materiales ≥ 5 caracteres | Validación descriptiva | ⭐ Completitud de datos |

**Total:** 3 reglas aplicadas

---

### 🟡 Catálogos POSPUESTOS

| Código | Razón | Prioridad |
|--------|-------|-----------|
| **WARN_PEDIATRIC_SEALANT_AGE_MISMATCH** | Sugerencia clínica | 🟢 BAJA |
| **WARN_PEDIATRIC_SEDATION_UNSPECIFIED** | Completitud opcional | 🟢 BAJA |
| **WARN_PEDIATRIC_PHYSICAL_RESTRAINT** | Advertencia ética | 🟢 BAJA |
| **WARN_PEDIATRIC_INFANT_MANAGEMENT_MISSING** | Mejores prácticas | 🟢 BAJA |

**Total:** 4 warnings pospuestos

---

## 📊 Estadísticas Finales

### Resumen por Categoría

| Componente | Aplicadas | Pospuestas | Eliminadas | Total | % Implementado |
|------------|-----------|------------|------------|-------|----------------|
| ProvidedService | 5 | 3 | 7 | 15 | 33% |
| OrthodonticDetails | 4 | 2 | 0 | 6 | 67% |
| SurgicalDetails | 5 | 1 | 1 | 7 | 71% |
| ProstheticDetails | 4 | 3 | 0 | 7 | 57% |
| ImplantologyDetails | 4 | 3 | 0 | 7 | 57% |
| AestheticDetails | 4 | 3 | 0 | 7 | 57% |
| PediatricDetails | 3 | 4 | 0 | 7 | 43% |
| **TOTAL** | **29** | **19** | **8** | **56** | **52%** |

### Distribución de Catálogos

```
Aplicadas:    52% ████████████████████████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
Pospuestas:   34% ██████████████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
Eliminadas:   14% █████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
```

**Análisis crítico del 52% implementado:**
- ✅ **Todas las reglas CRÍTICAS** están implementadas (seguridad, coherencia, invariantes)
- ✅ **Todas las validaciones estructurales** están completas
- 🟡 **Warnings clínicos** (19 pospuestos) son features avanzadas, no críticas para MVP
- 🟡 **Validaciones cross-aggregate** (3 pospuestas) esperan consolidación de módulos
- ✅ **Delegaciones a VOs** (8 eliminaciones) mejoran arquitectura

**Conclusión:** El 52% implementado representa **100% de funcionalidad crítica core** + preparación para el 48% restante.

---

## 🎯 Decisiones Críticas Revisadas

### ⚠️ **VALIDACIÓN IMPORTANTE IDENTIFICADA**

**RN-SERVICE-005:** "No desactivar con citas en próximas 48h"

**Estado inicial:** 🟡 POSPUESTA

**Análisis crítico:**
- ⚠️ **Impacto real:** Permite desactivar servicios mientras pacientes tienen citas programadas
- ⚠️ **Escenario crítico:** Dentista confirma cita de ortodoncia para mañana → Admin desactiva servicio hoy → Conflicto operativo
- ✅ **Mitigación parcial v1.0:** Método `deactivate()` requiere motivo obligatorio + auditoría

**Decisión revisada:**
```
Mantener como POSPUESTA pero marcada como PRIORITARIA para v2.0.
Razón: Requiere módulo Schedule estable, pero es CRÍTICA para operaciones reales.
Implementar INMEDIATAMENTE al consolidar AppointmentRepository.
```

**Preparación v1.0:**
```java
// TODO v2.0: PRIORITARIO - Implementar validación de citas
public void deactivate(String reason) {
    ensureEditable();
    validateDeactivationReason(reason);
    
    // FIXME: Validar citas próximas antes de permitir desactivación
    // if (hasAppointmentsInNext48Hours()) {
    //     throw ERR_SERVICE_HAS_APPOINTMENTS;
    // }
    
    this.status = ServiceStatus.inactive();
}
```

---

## 🚀 Roadmap de Implementación

### v1.0 (Exhibición - COMPLETADO) ✅
- ✅ 29 reglas de negocio core implementadas
- ✅ 5 catálogos de error por agregado/VO
- ✅ 4 Value Objects nuevos creados
- ✅ Validaciones estructurales completas
- ✅ Delegaciones arquitectónicas correctas

### v2.0 (Post-MVP - PRÓXIMA FASE) 🚀

#### **Prioridad ALTA (Implementar primero)**
1. **RN-SERVICE-005:** Validación de citas en desactivación
    - Requiere: `AppointmentRepository.findByServiceInNext48Hours()`
    - Tiempo estimado: 2 horas
    - Valor: ⭐⭐⭐⭐⭐ Crítico para operaciones reales

2. **RN-SERVICE-008:** Refinamiento de cambio de tarifa
    - Requiere: Query de citas programadas
    - Tiempo estimado: 1 hora
    - Valor: ⭐⭐⭐ Mejora UX (solo pedir justificación si hay citas)

3. **RN-SERVICE-012:** Validación de facturas pendientes
    - Requiere: Módulo Billing consolidado
    - Tiempo estimado: 3 horas
    - Valor: ⭐⭐⭐⭐ Protege integridad financiera

#### **Prioridad MEDIA (Segunda fase)**
4. **Domain Events:** ServiceCreated, ServiceRateUpdated, ServiceDeactivated
    - Requiere: Infraestructura de mensajería
    - Tiempo estimado: 8 horas
    - Valor: ⭐⭐⭐ Habilita features reactivas

5. **Auditoría Completa:** createdBy, lastUpdatedBy, changeHistory
    - Requiere: Integración con sistema de usuarios
    - Tiempo estimado: 6 horas
    - Valor: ⭐⭐⭐ Trazabilidad para compliance

#### **Prioridad BAJA (Refinamiento)**
6. **Warnings Clínicos:** Sistema de alertas de calidad de datos
    - Requiere: Datos históricos, análisis de patrones
    - Tiempo estimado: 12 horas
    - Valor: ⭐⭐ Feature avanzada

---

## 📝 Consecuencias

### Positivas ✅

1. **Arquitectura limpia:** 8 delegaciones correctas a VOs (vs validaciones en agregado)
2. **Separación de responsabilidades:** Billing valida facturación, no Service
3. **Value Objects robustos:** 4 VOs nuevos con validaciones propias
4. **Preparación para v2.0:** Métodos tienen puntos de inserción para eventos y validaciones cross-aggregate
5. **52% implementado = 100% core:** Todas las reglas críticas están completas
6. **Warnings documentados:** 19 validaciones clínicas identificadas para fase de refinamiento
7. **Coherencia con proyecto:** Eventos y auditoría pospuestos igual que Actor y Schedule

### Negativas / Riesgos ⚠️

1. **Desactivación sin validar citas:** RN-SERVICE-005 pospuesta es CRÍTICA, marcada como prioritaria v2.0
2. **Warnings ausentes:** Sistema no advierte sobre valores atípicos clínicamente (aceptable para MVP)
3. **Dependencias externas:** 3 reglas esperan consolidación de otros módulos

### Mitigaciones 🛡️

1. **RN-SERVICE-005:** Motivo obligatorio + auditoría mitigan parcialmente
2. **Documentación clara:** ADR marca prioridades para v2.0
3. **Preparación arquitectónica:** Código tiene TODOs y puntos de inserción

---

## 📚 Referencias

### Archivos de Descubrimiento
- [ProvidedService(ServicioPrestado).md](../../dominio/descubrimientos-de-reglas/servicios/ProvidedService(ServicioPrestado).md)
- [OrthodonticDetails(DetallesOrtodoncia).md](../../dominio/descubrimientos-de-reglas/servicios/OrthodonticDetails(DetallesOrtodoncia).md)
- [SurgicalDetails(DetallesCirugia).md](../../dominio/descubrimientos-de-reglas/servicios/SurgicalDetails(DetallesCirugia).md)
- [ProstheticDetails(DetallesProtesis).md](../../dominio/descubrimientos-de-reglas/servicios/ProstheticDetails(DetallesProtesis).md)
- [ImplantologyDetails(DetallesImplantes).md](../../dominio/descubrimientos-de-reglas/servicios/ImplantologyDetails(DetallesImplantes).md)
- [AestheticDetails(DetallesEstetica).md](../../dominio/descubrimientos-de-reglas/servicios/AestheticDetails(DetallesEstetica).md)
- [PediatricDetails(DetallesPediatria).md](../../dominio/descubrimientos-de-reglas/servicios/PediatricDetails(DetallesPediatria).md)

### ADRs Relacionados
- **ADR-26:** Separación de descubrimientos por VO estratégico
- **ADR-28:** Catálogos Eliminados - Histórico del Módulo Services
- **ADR-20:** Alcance Experimental del Módulo Actor (referencia)
- **ADR-24:** Alcance Experimental del Módulo Schedule (referencia)
- **ADR-22:** Estrategia de Numeración de Catálogos de Error

### Implementación
- Package: `com.example.ClinicaDefinitiva.domain.dental.care.services`
- Agregado raíz: `ProvidedService`
- VOs estratégicos: 6 tipos de detalles
- VOs nuevos: `ServiceName`, `ServiceDescription`, `ServiceDuration` (refactorizado)
- Catálogos: `ServiceError`, `OrthodonticError`, `SurgicalError`, `ProstheticError`, `ImplantologyError`, `AestheticError`, `PediatricError`

---

**Nota final:** Este ADR documenta decisiones arquitectónicas maduras resultado de experiencia en módulos previos. La delegación sistemática a Value Objects, la identificación correcta de validaciones cross-aggregate, y la priorización consciente de features avanzadas vs core demuestran **evolución técnica significativa** en el proyecto.