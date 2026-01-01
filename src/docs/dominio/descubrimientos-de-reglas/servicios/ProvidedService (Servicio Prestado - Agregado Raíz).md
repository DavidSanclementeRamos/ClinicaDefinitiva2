# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: ProvidedService (Servicio Prestado)

## Propósito
Representar el catálogo maestro de servicios odontológicos que ofrece la clínica. Este agregado raíz gestiona información común a todos los servicios (tarifa, duración, código), coordina con detalles específicos por especialidad, y protege la integridad del sistema de gestión clínica asegurando que solo servicios completos, validados y con tarifas correctas puedan ser utilizados en agendamiento y facturación.

---

## CREACIÓN
- Debe tener nombre descriptivo único (no puede estar en blanco).
- Debe tener código de servicio único y válido (ej. CUPS en Colombia).
- El código de servicio debe tener entre 4 y 15 caracteres alfanuméricos.
- Debe tener categoría reconocida (ServiceCatalog) coherente con el tipo de servicio.
- La tarifa base (baseRate) debe ser mayor a 0.
- La moneda de la tarifa debe ser válida (COP, USD, EUR).
- La duración estimada (duration) debe estar entre 15 minutos y 4 horas (240 minutos).
- Debe indicar explícitamente si requiere autorización previa (requiresAuthorization).
- Debe tener descripción clara del procedimiento (no puede estar en blanco).
- La descripción debe tener al menos 20 caracteres.
- Estado inicial por defecto: ACTIVE.
- Debe tener detalles específicos (ServiceDetails) coherentes con la categoría.
- El tipo de detalles debe corresponder a la categoría del servicio (ej. categoría "Orthodontics" → OrthodonticDetails).

---

## EDICIÓN / ACTUALIZACIÓN

### Actualización de Datos Comunes
- No puede editarse si está inactivo.
- El nombre no puede quedar en blanco tras la actualización.
- El nombre debe tener al menos 5 caracteres.
- La descripción no puede quedar en blanco tras la actualización.
- La descripción debe mantener mínimo 20 caracteres.
- El código de servicio es inmutable (no puede modificarse después de creación).
- Cambios en tarifa base requieren justificación y auditoría.
- No puede modificarse la tarifa si tiene citas programadas en próximas 48 horas sin renegociación previa.
- La nueva tarifa debe ser al menos 50% de la tarifa anterior (protección contra errores).
- La nueva tarifa no puede exceder 300% de la tarifa anterior sin aprobación gerencial.
- La duración puede ajustarse solo dentro del rango válido (15min - 4h).
- Cambios de duración que afecten citas existentes requieren notificación.
- La categoría no puede cambiar si el servicio tiene historial de uso (citas o facturas).
- Cambios sensibles (tarifa, categoría) deben registrar fecha, responsable y motivo.

### Actualización de Detalles Específicos
- No puede cambiar el tipo de detalles (ej. OrthodonticDetails → SurgicalDetails).
- Los detalles deben seguir siendo coherentes con la categoría del servicio.
- Cambios en detalles críticos (ej. requiresAnesthesia en cirugía) requieren validación adicional.
- Detalles actualizados deben pasar validaciones específicas del tipo.
- No puede actualizar detalles si el servicio está inactivo.

---

## DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene citas programadas en las próximas 48 horas.
- No puede desactivarse si hay tratamientos en curso que lo utilizan.
- No puede desactivarse si hay facturas pendientes de cobro asociadas.
- La desactivación se realiza mediante cambio de estado (ServiceStatus).
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo obligatorio de desactivación (mínimo 10 caracteres).
- Debe notificar a sistemas dependientes (agenda, facturación) antes de desactivar.
- Servicios desactivados no pueden ser seleccionados en nuevas citas.
- Servicios desactivados mantienen su información histórica inmutable.

---

## OPERACIONES DE DOMINIO
- canBeScheduled() → Verifica si está activo y cumple requisitos para agendamiento.
- requiresPreAuthorization() → Indica si necesita autorización de EPS/aseguradora.
- estimatedDuration() → Retorna duración estimada para cálculo de disponibilidad.
- getCurrentRate() → Obtiene tarifa vigente (puede tener histórico de cambios).
- hasAppointmentsInNext48Hours() → Verifica si tiene citas que bloquean desactivación.
- hasPendingInvoices() → Verifica si tiene facturas pendientes.
- isCompatibleWith(ServiceCategory category) → Valida coherencia categoría vs detalles.
- updateDetails(ServiceDetails newDetails) → Reemplaza detalles validando tipo y coherencia.
- snapshotForBilling() → Crea instantánea inmutable de tarifa y datos para facturación.
- updateRate(Money newRate, String justification) → Actualiza tarifa con auditoría.
- validateRateChange(Money oldRate, Money newRate) → Valida que cambio de tarifa sea razonable.

---

## INVARIANTES GLOBALES
- Un servicio activo siempre debe tener tarifa base > 0.
- Un servicio siempre tiene exactamente un tipo de detalles específicos.
- La categoría del servicio debe ser coherente con el tipo de detalles.
- El código de servicio debe ser único en el sistema.
- La duración debe estar dentro del rango operativo válido (15min - 4h).
- Un servicio inactivo no puede ser usado en nuevas operaciones (citas, facturación).
- El nombre y descripción no pueden estar vacíos simultáneamente.
- La moneda de la tarifa debe ser consistente en todo el sistema.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de tarifa con fecha, responsable y justificación.
- Se registra cada cambio de estado con motivo obligatorio.
- Se crea snapshot inmutable de tarifa al momento de facturar para auditoría histórica.
- Se registra intento de desactivación rechazado por citas activas.
- Sistema emite alertas al intentar operaciones con servicio inactivo.
- Se audita cada cambio de detalles específicos con tipo anterior y nuevo.
- Se mantiene histórico de cambios de tarifa con timestamp.
- Cada snapshot de facturación es inmutable y trazable al servicio original.

---

## Justificación Semántica
Estas reglas protegen la coherencia del sistema de gestión clínica, evitan facturación incorrecta por cambios de tarifa no auditados, aseguran que solo servicios completos y validados sean utilizables, garantizan trazabilidad para cumplimiento regulatorio (ej. CUPS en Colombia), protegen la integridad referencial con citas y facturas existentes, y permiten auditorías confiables del histórico de precios.

---

## Reglas Descubiertas (formato estandarizado)

**RN-SERVICE-001**
- Descripción: La tarifa base debe ser mayor a 0.
- Condición: ProvidedService.baseRate.amount <= 0 al invocar creación o actualización.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_SERVICE_INVALID_RATE

**RN-SERVICE-002**
- Descripción: La duración debe estar entre 15 minutos y 4 horas.
- Condición: ProvidedService.duration < 15min || duration > 240min al invocar creación o actualización.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_SERVICE_INVALID_DURATION

**RN-SERVICE-003**
- Descripción: No puede editarse si está inactivo.
- Condición: ProvidedService.status != ACTIVE al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SERVICE_INACTIVE

**RN-SERVICE-004**
- Descripción: La categoría debe ser coherente con el tipo de detalles.
- Condición: ProvidedService.category.getCategory() != details.serviceType().name() al invocar creación o updateDetails().
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_SERVICE_CATEGORY_MISMATCH

**RN-SERVICE-005**
- Descripción: No puede desactivarse si tiene citas programadas en próximas 48 horas.
- Condición: ProvidedService.hasAppointmentsInNext48Hours() == true al invocar deactivate().
- Consecuencia: Se rechaza operación y se notifican citas afectadas.
- Error asociado: ERR_SERVICE_HAS_APPOINTMENTS

**RN-SERVICE-006**
- Descripción: No puede cambiar el tipo de detalles una vez establecido.
- Condición: newDetails.serviceType() != currentDetails.serviceType() al invocar updateDetails().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SERVICE_TYPE_IMMUTABLE

**RN-SERVICE-007**
- Descripción: El código de servicio debe ser único en el sistema.
- Condición: Existe otro ProvidedService con mismo serviceCode al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SERVICE_CODE_DUPLICATE

**RN-SERVICE-008**
- Descripción: Cambios en tarifa requieren justificación si hay citas programadas.
- Condición: newRate != currentRate && hasScheduledAppointments() == true al invocar updateRate() sin justificación.
- Consecuencia: Se rechaza operación si justificación es null o blank.
- Error asociado: ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION

**RN-SERVICE-009**
- Descripción: El nombre y descripción no pueden estar en blanco.
- Condición: name.isBlank() || description.isBlank() al invocar creación o updateCommon().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SERVICE_MISSING_REQUIRED_FIELDS

**RN-SERVICE-010**
- Descripción: Servicios inactivos no pueden ser utilizados en facturación.
- Condición: ProvidedService.status != ACTIVE al intentar snapshotForBilling().
- Consecuencia: Se rechaza operación de facturación.
- Error asociado: ERR_SERVICE_NOT_BILLABLE

**RN-SERVICE-011**
- Descripción: El cambio de tarifa debe estar dentro de rango razonable.
- Condición: newRate < (oldRate * 0.5) || newRate > (oldRate * 3.0) al invocar updateRate().
- Consecuencia: Se rechaza operación o requiere aprobación gerencial.
- Error asociado: ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE

**RN-SERVICE-012**
- Descripción: No puede desactivarse si hay facturas pendientes.
- Condición: ProvidedService.hasPendingInvoices() == true al invocar deactivate().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SERVICE_HAS_PENDING_INVOICES

**RN-SERVICE-013**
- Descripción: El código de servicio debe tener formato válido.
- Condición: serviceCode.length() < 4 || serviceCode.length() > 15 al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SERVICE_INVALID_CODE_FORMAT

**RN-SERVICE-014**
- Descripción: La descripción debe tener al menos 20 caracteres.
- Condición: description.length() < 20 al invocar creación o updateCommon().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SERVICE_DESCRIPTION_TOO_SHORT

**RN-SERVICE-015**
- Descripción: Debe registrar motivo de desactivación con mínimo 10 caracteres.
- Condición: reason == null || reason.length() < 10 al invocar deactivate().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SERVICE_DEACTIVATION_REASON_REQUIRED

---

## Relación con ADRs
- ADR-02 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-05 (Arquitectura): Creación de un módulo independiente para Servicios.
- ADR-10 (Arquitectura): Estrategia de modelado y persistencia con campos específicos consultables.
- ADR-13 (Arquitectura): Separar DTOs por operación y DTOs de Update por tipo de datos.
- ADR-18 (Arquitectura): Simplificación general de jerarquía de excepciones en el dominio.
- ADR-24 (Servicio): ServiceRendered como input de dominio para facturación.

---

## Eventos de Dominio
- ServiceCreated: Al crear nuevo servicio en catálogo.
- ServiceActivated: Al activar servicio inactivo.
- ServiceDeactivated: Al desactivar servicio con motivo.
- ServiceRateUpdated: Al cambiar tarifa base (crítico para auditoría).
- ServiceDetailsChanged: Al actualizar detalles específicos por tipo.
- ServiceCategoryChanged: Al cambiar categoría (raro, requiere validaciones).
- ServiceDurationAdjusted: Al modificar duración estimada.
- ServiceSnapshotCreated: Al crear snapshot inmutable para facturación.

---

## Catálogo de Servicios Predefinidos

**Servicios Base del Sistema:**
```java
ServiceCatalog.Defaults:
- GENERAL_CONSULTATION → "General Consultation" (General)
- PROPHYLAXIS_CLEANING → "Prophylaxis & Cleaning" (General)
- ORTHO_METAL_BRACKETS → "Metal Brackets" (Orthodontics)
- ORTHO_CLEAR_ALIGNERS → "Clear Aligners" (Orthodontics)
- SURG_WISDOM_EXTRACTION → "Wisdom Tooth Extraction" (Surgery)
- SURG_SOFT_TISSUE_GRAFT → "Soft Tissue Graft" (Surgery)
- PED_SEALANTS_FLUORIDE → "Sealants & Fluoride" (Pediatrics)
- PED_RESIN_RESTORATION → "Resin Restoration" (Pediatrics)
- AES_IN_OFFICE_WHITENING → "In-Office Whitening" (Aesthetics)
- AES_PORCELAIN_VENEER → "Porcelain Veneer" (Aesthetics)
- IMP_SINGLE_IMPLANT → "Single Implant" (Implantology)
- PRO_PORCELAIN_CROWN → "Porcelain Crown" (Prosthetics)
```

---

## Value Objects Involucrados

**ServiceId (Identificador de Servicio)**
- UUID único por servicio.
- Inmutable.
- Permite trazabilidad entre sistemas.
- Formato: UUID v4 estándar.

**ServiceCode (Código Estandarizado)**
- Código CUPS (Colombia) o similar.
- Único en el sistema.
- Inmutable.
- Longitud: 4-15 caracteres alfanuméricos.
- Ejemplos: "890201" (Ortodoncia), "890301" (Cirugía).

**ServiceCatalog (Catálogo de Servicios)**
- Contiene ServiceId, name, category.
- Enum de servicios predefinidos comunes.
- Extensible mediante configuración.
- Inmutable tras creación.

**Money (Tarifa)**
- Representa tarifa base del servicio.
- Incluye amount (BigDecimal) y currency (String).
- Validaciones: amount > 0, currency en [COP, USD, EUR].
- Operaciones: add, subtract, multiply, compare.

**ServiceDuration (Duración Estimada)**
- Representa tiempo estimado del procedimiento.
- Rango válido: 15 minutos - 4 horas (240 minutos).
- Usado para cálculo de disponibilidad en agenda.
- Inmutable.

**ServiceStatus (Estado del Servicio)**
- Posibles estados: ACTIVE, INACTIVE.
- Controla disponibilidad operativa.
- Afecta agendamiento y facturación.
- Transiciones válidas: ACTIVE ↔ INACTIVE.

**ServiceDetails (Detalles Específicos - Interfaz)**
- Interfaz implementada por cada tipo de detalle.
- Método obligatorio: `ServiceType serviceType()`.
- Implementaciones:
    - OrthodonticDetails
    - SurgicalDetails
    - ProstheticDetails
    - ImplantologyDetails
    - AestheticDetails
    - PediatricDetails

---

## Ejemplo de Uso
```java
// Crear servicio de ortodoncia con brackets metálicos
ServiceId serviceId = ServiceId.generate();
ServiceCode code = new ServiceCode("890201"); // CUPS Colombia
Money baseRate = Money.of(new BigDecimal("150000"), "COP");
ServiceDuration duration = ServiceDuration.ofMinutes(45);

OrthodonticDetails orthodonticDetails = new OrthodonticDetails(
    "METAL_BRACKETS",
    24, // meses
    true // requiere seguimiento
);

ProvidedService orthodonticService = new ProvidedService(
    serviceId,
    "Ortodoncia con Brackets Metálicos",
    ServiceCatalog.Defaults.ORTHO_METAL_BRACKETS.get(),
    code,
    baseRate,
    duration,
    true, // requiere autorización
    "Tratamiento ortodóntico con brackets metálicos convencionales para corrección de malposiciones dentales",
    ServiceStatus.active(),
    orthodonticDetails
);

// Validar antes de usar en cita
if (orthodonticService.canBeScheduled()) {
    // Proceder con agendamiento
    System.out.println("Servicio disponible para agendar");
}

// Actualizar tarifa con auditoría
Money newRate = Money.of(new BigDecimal("180000"), "COP");
orthodonticService.updateRate(newRate, "Ajuste por inflación anual 2025");
// Valida que cambio esté en rango razonable (50%-300%)

// Snapshot inmutable para facturación
ServiceSnapshot snapshot = orthodonticService.snapshotForBilling();
// Snapshot preserva tarifa actual, aunque servicio cambie después

// Intentar desactivar (valida citas futuras)
try {
    orthodonticService.deactivate("Servicio descontinuado por baja demanda");
} catch (BusinessRuleViolationException e) {
    // Lanza ERR_SERVICE_HAS_APPOINTMENTS si hay citas en 48h
    System.err.println("No se puede desactivar: " + e.getMessage());
}

// Cambio de detalles (debe ser mismo tipo)
OrthodonticDetails updatedDetails = new OrthodonticDetails(
    "CERAMIC_BRACKETS", // cambio de tipo de aparato
    24,
    true
);
orthodonticService.updateDetails(updatedDetails);
// Valida que sigue siendo OrthodonticDetails
```

---

## Métricas de Gestión

**Servicios Más Solicitados**
```sql
SELECT s.name, COUNT(a.id) as total_appointments
FROM provided_service s
JOIN appointment a ON a.service_id = s.id
WHERE a.status = 'COMPLETED'
GROUP BY s.id
ORDER BY total_appointments DESC
LIMIT 10;
```

**Rentabilidad por Servicio**
```sql
SELECT 
    s.name,
    SUM(ii.amount) as total_revenue,
    COUNT(ii.id) as times_billed,
    AVG(ii.amount) as avg_revenue
FROM provided_service s
JOIN invoice_item ii ON ii.service_id = s.id
WHERE ii.invoice_status = 'PAID'
GROUP BY s.id
ORDER BY total_revenue DESC;
```

**Duración Real vs Estimada**
```sql
SELECT 
    s.name,
    s.duration as estimated_minutes,
    AVG(a.actual_duration) as avg_actual_minutes,
    AVG(a.actual_duration) - s.duration as deviation_minutes
FROM provided_service s
JOIN appointment a ON a.service_id = s.id
WHERE a.status = 'COMPLETED'
GROUP BY s.id
HAVING ABS(AVG(a.actual_duration) - s.duration) > 10;
```

**Tasa de Uso de Servicios Activos**
```sql
SELECT 
    (SELECT COUNT(DISTINCT service_id) FROM appointment) * 100.0 / 
    (SELECT COUNT(*) FROM provided_service WHERE status = 'ACTIVE') as usage_percentage;
```

**Servicios que Requieren Autorización**
```sql
SELECT 
    category,
    COUNT(*) as total,
    SUM(CASE WHEN requires_authorization THEN 1 ELSE 0 END) as requires_auth,
    (SUM(CASE WHEN requires_authorization THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as auth_percentage
FROM provided_service
WHERE status = 'ACTIVE'
GROUP BY category;
```

**Histórico de Cambios de Tarifa**
```sql
SELECT 
    s.name,
    arc.old_rate,
    arc.new_rate,
    arc.change_percentage,
    arc.justification,
    arc.changed_at,
    arc.changed_by
FROM provided_service s
JOIN audit_rate_change arc ON arc.service_id = s.id
WHERE arc.changed_at >= NOW() - INTERVAL '6 months'
ORDER BY arc.changed_at DESC;
```