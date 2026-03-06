package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.OrthodonticError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Set;
import java.util.Objects;

/**
 * Detalles de un servicio de ortodoncia.
 *
 * Representa tratamientos de ortodoncia para la alineación dental y corrección de la mordida.
 * Incluye información sobre el tipo de aparato, la duración del tratamiento y los requisitos de seguimiento.
 *
 * Reglas de negocio:
 * - RN-ORTHODONTIC-001: El tipo de aparato es obligatorio.
 * - RN-ORTHODONTIC-002: La duración del tratamiento debe estar entre 6 y 48 meses.
 * - RN-ORTHODONTIC-003: Solo se permiten tipos de aparatos válidos.
 * - RN-ORTHODONTIC-004: La duración no puede ser negativa ni cero.
 */
public final class OrthodonticDetails implements ServiceDetails {

    private static final Set<String> VALID_APPLIANCE_TYPES = Set.of(
            "METAL_BRACKETS",        // Brackets metálicos tradicionales
            "CERAMIC_BRACKETS",      // Brackets cerámicos del color del diente
            "LINGUAL_BRACKETS",      // Brackets colocados en la parte posterior de los dientes
            "CLEAR_ALIGNERS",        // Alineadores transparentes removibles (ej. Invisalign)
            "REMOVABLE_APPLIANCES",  // Aparatos ortodónticos removibles
            "FUNCTIONAL_APPLIANCES"  // Dispositivos para corregir la relación mandibular
    );

    private static final int MIN_DURATION_MONTHS = 6;
    private static final int MAX_DURATION_MONTHS = 48;

    private final String applianceType;
    private final Integer treatmentDurationMonths;
    private final boolean requiresFollowup;

    /**
     * Constructor controlado con validaciones centralizadas.
     *
     * @param applianceType Tipo de aparato ortodóntico.
     * @param treatmentDurationMonths Duración del tratamiento en meses.
     * @param requiresFollowup Indica si se requiere seguimiento posterior.
     */
    public OrthodonticDetails(String applianceType, Integer treatmentDurationMonths,
                              Boolean requiresFollowup) {
        // RN-ORTHODONTIC-001: Tipo de aparato obligatorio
        if (applianceType == null || applianceType.isBlank()) {
            throw new ValueObjectValidationException(
                    OrthodonticError.ERR_ORTHODONTIC_MISSING_APPLIANCE,
                    VOContext.DENTAL_SERVICES
            );
        }

        // RN-ORTHODONTIC-003: Normalización y validación contra catálogo permitido
        this.applianceType = ValidationHelper.normalizeAndValidate(
                applianceType,
                VALID_APPLIANCE_TYPES,
                OrthodonticError.ERR_ORTHODONTIC_INVALID_APPLIANCE,
                VOContext.DENTAL_SERVICES
        );

        // RN-ORTHODONTIC-004 & RN-ORTHODONTIC-002: Validaciones de duración
        if (treatmentDurationMonths != null) {
            if (treatmentDurationMonths <= 0) {
                throw new ValueObjectValidationException(
                        OrthodonticError.ERR_ORTHODONTIC_NEGATIVE_DURATION,
                        VOContext.DENTAL_SERVICES
                );
            }

            ValidationHelper.validateRange(
                    treatmentDurationMonths,
                    MIN_DURATION_MONTHS,
                    MAX_DURATION_MONTHS,
                    OrthodonticError.ERR_ORTHODONTIC_INVALID_DURATION,
                    VOContext.DENTAL_SERVICES
            );
        }

        this.treatmentDurationMonths = treatmentDurationMonths;
        this.requiresFollowup = Boolean.TRUE.equals(requiresFollowup);
    }

    @Override
    public ServiceType serviceType() {
        return ServiceType.ORTHODONTIC;
    }

    public String getApplianceType() {
        return applianceType;
    }

    public Integer getTreatmentDurationMonths() {
        return treatmentDurationMonths;
    }

    public Boolean getRequiresFollowup() {
        return requiresFollowup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrthodonticDetails)) return false;
        OrthodonticDetails that = (OrthodonticDetails) o;
        return requiresFollowup == that.requiresFollowup &&
                applianceType.equals(that.applianceType) &&
                Objects.equals(treatmentDurationMonths, that.treatmentDurationMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applianceType, treatmentDurationMonths, requiresFollowup);
    }

    @Override
    public String toString() {
        return "OrthodonticDetails{" +
                "aparato=" + applianceType +
                ", duración=" + treatmentDurationMonths + " meses" +
                ", seguimiento=" + requiresFollowup +
                '}';
    }
}
