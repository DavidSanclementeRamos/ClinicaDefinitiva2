package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ProvidedServiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceRatePolicy;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceCatalog;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.*;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import java.util.Map;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
 * Reglas de negocio:
 * - RN-SERVICE-001: El servicio debe estar activo para ser editado
 * - RN-SERVICE-002: La categoría debe coincidir con el tipo de detalles
 * - RN-SERVICE-003: El tipo de detalles no puede cambiar después de la creación
 * - RN-SERVICE-004: Los cambios de tarifa requieren justificación
 * - RN-SERVICE-005: Los cambios de tarifa deben estar dentro de un rango razonable
 * - RN-SERVICE-006: Validación de política de tarifas
 * - RN-SERVICE-007: La desactivación requiere un motivo detallado
 *
 * @see ServiceDetails para detalles específicos por especialidad
 * @see ProvidedServiceError para catálogo de errores
 */
public class ProvidedService {

    private static final int MIN_DEACTIVATION_REASON_LENGTH = 10;


    private final ServiceId id;
    private ServiceName name;
    private ServiceCatalog category;
    private final ServiceCode code;
    private Price baseRate;
    private ServiceDuration duration;
    private boolean requiresAuthorization;
    private ServiceDescription description;
    private ServiceStatus status;


    private ServiceDetails details;

    private ProvidedService(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.category = builder.category;
        this.code = builder.code;
        this.baseRate = builder.baseRate;
        this.duration = builder.duration;
        this.description = builder.description;
        this.status = builder.status;
        this.requiresAuthorization = builder.requiresAuthorization;
        this.details = builder.details;


        validateCategoryMatch(this.category, this.details);
    }



    public static Builder builder() {
        return new Builder();
    }

    /**
     * Crea un nuevo servicio activo - método de conveniencia.
     * Emite ServiceCreatedEvent (cuando se agregue soporte de eventos).
     *
     * Usar builder() para mayor control sobre la construcción.
     */
    public static ProvidedService create(
            ServiceName name,
            ServiceCatalog category,
            ServiceCode code,
            Price baseRate,
            ServiceDuration duration,
            ServiceDescription description,
            ServiceDetails details,
            boolean requiresAuthorization) {

        return builder()
                .name(name)
                .category(category)
                .code(code)
                .baseRate(baseRate)
                .duration(duration)
                .description(description)
                .details(details)
                .requiresAuthorization(requiresAuthorization)
                .status(ServiceStatus.of(ServiceStatus.State.ACTIVE))
                .build();
    }


    /**
     * Actualiza información común del servicio.
     *
     * Reglas de negocio:
     * - RN-SERVICE-003: El servicio debe estar activo para ser editable
     * - RN-SERVICE-004: La categoría debe coincidir con los detalles si ambos están presentes
     * - RN-SERVICE-009: Validaciones de nombre y descripción en los VO
     */
    public void updateInformation(
            ServiceName name,
            ServiceCatalog category,
            ServiceDuration duration,
            Boolean requiresAuthorization,
            ServiceDescription description) {
        ensureEditable(); // RN-SERVICE-003

        if (name != null) this.name = name;
        if (description != null) this.description = description;

        if (category != null) {
            if (this.details != null) {
                validateCategoryMatch(category, this.details);
            }
            this.category = category;
        }

        if (duration != null) this.duration = duration;
        if (requiresAuthorization != null) this.requiresAuthorization = requiresAuthorization;
    }

    /**
     * Actualiza la tarifa del servicio con justificación obligatoria.
     *
     * Reglas de negocio:
     * - RN-SERVICE-003: El servicio debe estar activo
     * - RN-SERVICE-008: La justificación es obligatoria
     * - RN-SERVICE-011: El cambio debe estar dentro de un rango razonable
     */
    public void updateRate(Price newRate, String justification) {
        ensureEditable();

        if (justification == null || justification.isBlank()) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION,
                    EntityContext.DENTAL_SERVICE
            );
        }

        Price oldRate = this.baseRate;
        ServiceRatePolicy.validateRateChange(oldRate, newRate);
        this.baseRate = newRate;
    }

    /**
     * Actualiza los detalles específicos del servicio.
     *
     * Reglas de negocio:
     * - RN-SERVICE-003: El servicio debe estar activo
     * - RN-SERVICE-006: No se puede cambiar el tipo de servicio
     * - RN-SERVICE-004: Los detalles deben coincidir con la categoría
     */
    public void updateDetails(ServiceDetails newDetails) {
        ensureEditable();
        Objects.requireNonNull(newDetails, "Los detalles no pueden ser nulos");

        if (this.details != null &&
                !this.details.serviceType().equals(newDetails.serviceType())) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_TYPE_IMMUTABLE,
                    EntityContext.DENTAL_SERVICE
            );
        }

        validateCategoryMatch(this.category, newDetails);
        this.details = newDetails;
    }

    /**
     * Desactiva el servicio con motivo obligatorio.
     *
     * Reglas de negocio:
     * - RN-SERVICE-003: El servicio debe estar activo
     * - RN-SERVICE-015: La desactivación requiere un motivo detallado (mínimo 10 caracteres)
     */
    public void deactivate(String reason) {
        ensureEditable();

        if (reason == null || reason.length() < MIN_DEACTIVATION_REASON_LENGTH) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_DEACTIVATION_REASON_REQUIRED,
                    EntityContext.DENTAL_SERVICE
            );
        }

        this.status = ServiceStatus.of(ServiceStatus.State.INACTIVE);
    }

    /**
     * Reactiva un servicio previamente desactivado.
     */
    public void reactivate() {
        this.status = ServiceStatus.of(ServiceStatus.State.ACTIVE);
    }


    public boolean canBeScheduled() {
        return status.isActive() && details != null;
    }

    public ServiceDuration estimatedDuration() {
        return duration;
    }

    public boolean requiresPreAuthorization() {
        return requiresAuthorization;
    }

    public boolean isActive() {
        return status.isActive();
    }


    private void ensureEditable() {
        if (!status.isActive()) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_INACTIVE,
                    EntityContext.DENTAL_SERVICE
            );
        }
    }

   private void validateCategoryMatch(ServiceCatalog category, ServiceDetails details) {
    if (category == null || details == null) return;

    String categoryName = normalizeCategory(category.getCategory());
    ServiceType type = details.serviceType();

    // Mapeo explícito entre ServiceType y categorías aceptadas
    Map<ServiceType, Set<String>> allowed = Map.of(
        ServiceType.SURGERY, Set.of("SURGERY", "Surgery"),
        ServiceType.IMPLANTOLOGY, Set.of("IMPLANT", "Implantology"),
        ServiceType.GENERAL, Set.of("GENERAL", "General"),
        ServiceType.ORTHODONTIC, Set.of("ORTHODONTIC", "Orthodontics"),
        ServiceType.PEDIATRICS, Set.of("PEDIATRICS", "Pediatrics"),
        ServiceType.AESTHETICS, Set.of("AESTHETICS", "Aesthetics"),
        ServiceType.PROSTHETICS, Set.of("PROSTHETICS", "Prosthetics")
    );

    boolean matches = allowed.getOrDefault(type, Set.of())
                             .contains(categoryName);

    if (!matches) {
        throw new BusinessRuleViolationException(
            ProvidedServiceError.ERR_SERVICE_CATEGORY_MISMATCH,
            EntityContext.DENTAL_SERVICE
        );
    }
}

private String normalizeCategory(String raw) {
    return raw == null ? "" : raw.trim().toUpperCase();
}
   


    public ServiceId getId() {
        return id;
    }

    public ServiceName getName() {
        return name;
    }

    public ServiceCatalog getCategory() {
        return category;
    }

    public ServiceCode getCode() {
        return code;
    }

    public Price getBaseRate() {
        return baseRate;
    }

    public ServiceDuration getDuration() {
        return duration;
    }

    public boolean isRequiresAuthorization() {
        return requiresAuthorization;
    }

    public ServiceDescription getDescription() {
        return description;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public Optional<ServiceDetails> getDetails() {
        return Optional.ofNullable(details);
    }



    public static class Builder {
        private ServiceId id;
        private ServiceName name;
        private ServiceCatalog category;
        private ServiceCode code;
        private Price baseRate;
        private ServiceDuration duration;
        private boolean requiresAuthorization = false;
        private ServiceDescription description;
        private ServiceStatus status = ServiceStatus.of(ServiceStatus.State.ACTIVE);
        private ServiceDetails details;

        private Builder() {}

        public Builder id(ServiceId id) {
            this.id = id;
            return this;
        }

        public Builder name(ServiceName name) {
            this.name = name;
            return this;
        }

        public Builder category(ServiceCatalog category) {
            this.category = category;
            return this;
        }

        public Builder code(ServiceCode code) {
            this.code = code;
            return this;
        }

        public Builder baseRate(Price baseRate) {
            this.baseRate = baseRate;
            return this;
        }

        public Builder duration(ServiceDuration duration) {
            this.duration = duration;
            return this;
        }

        public Builder requiresAuthorization(boolean requiresAuthorization) {
            this.requiresAuthorization = requiresAuthorization;
            return this;
        }

        public Builder description(ServiceDescription description) {
            this.description = description;
            return this;
        }

        public Builder status(ServiceStatus status) {
            this.status = status;
            return this;
        }

        public Builder details(ServiceDetails details) {
            this.details = details;
            return this;
        }

        public ProvidedService build() {
            return new ProvidedService(this);
        }
    }
}