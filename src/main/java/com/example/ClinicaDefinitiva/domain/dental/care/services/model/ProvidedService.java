package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Price;
import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Agregado raíz: Servicio odontológico ofrecido por la clínica.
 *
 * Responsabilidades:
 * - Gestionar información común a todos los servicios
 * - Coordinar con detalles específicos por tipo
 * - Proteger coherencia entre categoría y detalles
 * - Asegurar trazabilidad de cambios de tarifa
 * - Validar estado operativo antes de operaciones
 *
 * @see ServiceDetails para detalles específicos por especialidad
 * @see ServiceError para catálogo de errores
 */
public class ProvidedService {

    private static final int MIN_DEACTIVATION_REASON_LENGTH = 10;


    private final ServiceId id;                             // Identificador único del servicio
    private ServiceName  name;                         // Nombre del servicio
    private ServiceCatalog category;      //Categoría (ej. "Orthodontics", "Surgery", "Pediatrics")
    private ServiceCode code;               // Código estandarizado (ej. CUPS en Colombia)
    private Price baseRate;                // Tarifa base del servicio
    private ServiceDuration duration;           // Duración estimada en minutos
    private boolean requiresAuthorization;      // Indica si requiere autorización (EPS/aseguradora)
    private ServiceDescription description;                // Descripción general del servicio
    private ServiceStatus status;        // Estado del servicio (activo/inactivo)
    private ServiceDetails details; // NUEVO: composición



    public ProvidedService(Price baseRate,
                           ServiceCatalog category,
                           ServiceCode code,
                           ServiceDescription description,
                           ServiceDetails details,
                           ServiceDuration duration,
                           ServiceId id,
                           ServiceName name,
                           boolean requiresAuthorization,
                           ServiceStatus status) {

        validateCategoryMatch(category, details);

        this.baseRate = baseRate;
        this.category = category;
        this.code = code;
        this.description = description;
        this.details = details;
        this.duration = duration;
        this.id = id;
        this.name = name;
        this.requiresAuthorization = requiresAuthorization;
        this.status = status;
    }

    // OPERACIONES DE DOMINIO

    public static ProvidedService  registerService(ServiceId id,
                                                   Price baseRate,
                                                   ServiceCatalog category,
                                                   ServiceCode code,
                                                   ServiceDescription description,
                                                   ServiceDetails details,
                                                   ServiceDuration duration,
                                                   ServiceName name,
                                                   boolean requiresAuthorization,
                                                   ServiceStatus status){

        return new ProvidedService(
                baseRate,
                category,
                code,
                description,
                details,
                duration,
                id,
                name,
                requiresAuthorization,
                status
                );
    }



    /**
     * RN-SERVICE-003, RN-SERVICE-009, RN-SERVICE-014: Actualizar datos comunes
     */
    public void updateInformation(ServiceName name, ServiceCatalog category,
                             ServiceDuration duration, Boolean requiresAuthorization,
                             ServiceDescription description) {
        ensureEditable(); // RN-SERVICE-003


            this.name = name;
            this.description = description;

        if (category != null) {
            // No validamos match aquí porque details puede cambiar después
            this.category = category;
        }
            // RN-SERVICE-008: Validación de justificación delegada a updateRate()
           // this.baseRate = baseRate;
            this.duration = duration;
        if (requiresAuthorization != null) {
            this.requiresAuthorization = requiresAuthorization;
        }

    }

    /**
     * RN-SERVICE-008: Actualizar tarifa con justificación si hay citas programadas
     * RN-SERVICE-011: Validar que cambio esté en rango razonable
     */
    public void updateRate(Price newRate, String justification) {
        ensureEditable(); // RN-SERVICE-003


        // RN-SERVICE-008: Justificación obligatoria si hay citas
        // NOTA: Validación de citas delegada a Domain Service en v2.0
        if (justification == null || justification.isBlank()) {
            throw new BusinessRuleViolationException(
                    ServiceError.ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION, EntityContext.DENTAL_SERVICE
            );
        }

        Price oldRate = this.baseRate;
        this.baseRate = newRate;

    }



    /**
     * RN-SERVICE-006: Actualizar detalles (debe mantener mismo tipo)
     * RN-SERVICE-004: Validar coherencia con categoría
     */
    public void updateDetails(ServiceDetails newDetails) {
        ensureEditable(); // RN-SERVICE-003

        if (newDetails == null) {
            throw new IllegalArgumentException("Los detalles no pueden ser null");
        }

        // RN-SERVICE-006: No puede cambiar tipo
        if (this.details != null &&
                !this.details.serviceType().equals(newDetails.serviceType())) {
            throw new BusinessRuleViolationException(
                    ServiceError.ERR_SERVICE_TYPE_IMMUTABLE,EntityContext.DENTAL_SERVICE
            );
        }

        // RN-SERVICE-004: Validar coherencia con categoría
        validateCategoryMatch(this.category, newDetails);

        ServiceType oldType = this.details != null ? this.details.serviceType() : null;
        this.details = newDetails;

    }

    /**
     * RN-SERVICE-015: Desactivar servicio con motivo obligatorio
     * RN-SERVICE-005: Valida que no tenga citas en próximas 48h (delegado a Domain Service)
     * RN-SERVICE-012: Valida que no tenga facturas pendientes (delegado a Domain Service)
     */

    public void deactivate(String reason) {
        ensureEditable(); // RN-SERVICE-003

        // RN-SERVICE-015
        if (reason == null || reason.length() < MIN_DEACTIVATION_REASON_LENGTH) {
            throw new BusinessRuleViolationException(
                    ServiceError.ERR_SERVICE_DEACTIVATION_REASON_REQUIRED,EntityContext.DENTAL_SERVICE
            );
        }

        // NOTA: Validaciones RN-SERVICE-005 y RN-SERVICE-012 se delegan a
        // ProvidedServiceDomainService.deactivateService() en v2.0
        // que consultará AppointmentRepository e InvoiceRepository
        this.status = ServiceStatus.of(ServiceStatus.State.INACTIVE);

    }



    /**
     * Verificar si puede ser usado en agendamiento
     */
    public boolean canBeScheduled() {
        return status.isActive() && details != null;
    }

    /**
     * Obtener duración estimada para cálculo de disponibilidad
     */
    public ServiceDuration estimatedDuration() {
        return duration;
    }

    /**
     * Verificar si requiere autorización previa
     */
    public boolean requiresPreAuthorization() {
        return requiresAuthorization;
    }

    // VALIDACIONES PRIVADAS

    /**
     * RN-SERVICE-003: Validar que esté activo antes de editar
     */
    private void ensureEditable() {
        if (!status.isActive()) {
            throw new BusinessRuleViolationException(
                    ServiceError.ERR_SERVICE_INACTIVE,EntityContext.DENTAL_SERVICE
            );
        }
    }

    /**
     * RN-SERVICE-004: Validar coherencia entre categoría y detalles
     */
    private void validateCategoryMatch(ServiceCatalog category, ServiceDetails details) {
        if (category == null || details == null) {
            return; // Permitir null temporal
        }

        String categoryName = category.getCategory().toUpperCase();
        String detailsType = details.serviceType().name().toUpperCase();

        // Mapeo flexible: ORTHODONTICS ↔ ORTHODONTIC, PROSTHETICS ↔ PROSTHETICS, etc.
        boolean matches = categoryName.contains(detailsType) ||
                detailsType.contains(categoryName) ||
                normalizeCategory(categoryName).equals(normalizeCategory(detailsType));

        if (!matches) {
            throw new BusinessRuleViolationException(
                    ServiceError.ERR_SERVICE_CATEGORY_MISMATCH,EntityContext.DENTAL_SERVICE
            );
        }
    }

    private String normalizeCategory(String category) {
        // Normalizar para comparación: ORTHODONTICS → ORTHODONTIC
        return category.replaceAll("S$", ""); // Quitar 'S' final si existe
    }

    public ServiceId getId() { return id; }
    public ServiceName getName() { return name; }
    public ServiceCatalog getCategory() { return category; }
    public ServiceCode getCode() { return code; }
    public Price getBaseRate() { return baseRate; }
    public ServiceDuration getDuration() { return duration; }
    public boolean isRequiresAuthorization() { return requiresAuthorization; }
    public ServiceDescription getDescription() { return description; }
    public ServiceStatus getStatus() { return status; }
    public Optional getDetails() { return Optional.ofNullable(details); }

}
