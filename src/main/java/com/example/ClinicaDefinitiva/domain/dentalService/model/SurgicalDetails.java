package com.example.ClinicaDefinitiva.domain.dentalService.model;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.SurgicalError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.Set;

/**
 * Detalles de un servicio quirúrgico.
 *
 * Representa procedimientos de cirugía oral y maxilofacial.
 * Incluye nivel de complejidad, requisitos de anestesia y necesidad de quirófano.
 *
 * Reglas de negocio:
 * - RN-SURGICAL-001: Las cirugías de baja complejidad no deben requerir anestesia.
 * - RN-SURGICAL-003: Solo se permiten niveles de complejidad válidos.
 * - RN-SURGICAL-004: Las cirugías críticas deben requerir tanto anestesia como quirófano.
 * - RN-SURGICAL-006: El tipo de cirugía debe tener una longitud mínima.
 * - RN-SURGICAL-007: Asignar quirófano a cirugías de baja complejidad es inconsistente.
 */
public final class SurgicalDetails implements ServiceDetails {

    private static final Set<String> VALID_COMPLEXITY_LEVELS = Set.of(
            "LOW",      // Procedimientos menores, extracciones simples
            "MEDIUM",   // Procedimientos quirúrgicos estándar
            "HIGH",     // Casos quirúrgicos complejos
            "CRITICAL"  // Procedimientos altamente complejos y de alto riesgo
    );

    private static final int MIN_TYPE_LENGTH = 3;

    private final String surgeryType;
    private final String complexityLevel;
    private final boolean requiresAnesthesia;
    private final boolean operatingRoomNeeded;

    /**
     * Constructor controlado con validaciones centralizadas.
     *
     * @param surgeryType Tipo de cirugía.
     * @param complexityLevel Nivel de complejidad.
     * @param requiresAnesthesia Indica si requiere anestesia.
     * @param operatingRoomNeeded Indica si requiere quirófano.
     */
    public SurgicalDetails(String surgeryType, String complexityLevel,
                           Boolean requiresAnesthesia, Boolean operatingRoomNeeded) {

        // RN-SURGICAL-006: Validación de longitud mínima del tipo de cirugía
        ValidationHelper.validateMinLength(
                surgeryType,
                MIN_TYPE_LENGTH,
                SurgicalError.ERR_SURGICAL_TYPE_TOO_SHORT,
                VOContext.DENTAL_SERVICES
        );

        // RN-SURGICAL-003: Validación del nivel de complejidad
        String normalizedComplexity = null;
        if (complexityLevel != null) {
            normalizedComplexity = ValidationHelper.normalizeAndValidate(
                    complexityLevel,
                    VALID_COMPLEXITY_LEVELS,
                    SurgicalError.ERR_SURGICAL_INVALID_COMPLEXITY,
                    VOContext.DENTAL_SERVICES
            );
        }

        boolean needsAnesthesia = Boolean.TRUE.equals(requiresAnesthesia);
        boolean needsOperatingRoom = Boolean.TRUE.equals(operatingRoomNeeded);

        // RN-SURGICAL-001: Baja complejidad no debe requerir anestesia
        if (needsAnesthesia && "LOW".equals(normalizedComplexity)) {
            throw new ValueObjectValidationException(
                    SurgicalError.ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH,
                    VOContext.DENTAL_SERVICES
            );
        }

        // RN-SURGICAL-004: Cirugías críticas deben tener anestesia y quirófano
        if ("CRITICAL".equals(normalizedComplexity) &&
                (!needsAnesthesia || !needsOperatingRoom)) {
            throw new ValueObjectValidationException(
                    SurgicalError.ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS,
                    VOContext.DENTAL_SERVICES
            );
        }

        // RN-SURGICAL-007: Quirófano en baja complejidad es inconsistente
        if (needsOperatingRoom && "LOW".equals(normalizedComplexity)) {
            throw new ValueObjectValidationException(
                    SurgicalError.ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH,
                    VOContext.DENTAL_SERVICES
            );
        }

        this.surgeryType = surgeryType;
        this.complexityLevel = normalizedComplexity;
        this.requiresAnesthesia = needsAnesthesia;
        this.operatingRoomNeeded = needsOperatingRoom;
    }

    @Override
    public ServiceType serviceType() {
        return ServiceType.SURGERY;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public String getComplexityLevel() {
        return complexityLevel;
    }

    public Boolean getRequiresAnesthesia() {
        return requiresAnesthesia;
    }

    public Boolean getOperatingRoomNeeded() {
        return operatingRoomNeeded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurgicalDetails)) return false;
        SurgicalDetails that = (SurgicalDetails) o;
        return requiresAnesthesia == that.requiresAnesthesia &&
                operatingRoomNeeded == that.operatingRoomNeeded &&
                Objects.equals(surgeryType, that.surgeryType) &&
                Objects.equals(complexityLevel, that.complexityLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surgeryType, complexityLevel, requiresAnesthesia, operatingRoomNeeded);
    }

    @Override
    public String toString() {
        return "SurgicalDetails{" +
                "tipo=" + surgeryType +
                ", complejidad=" + complexityLevel +
                ", anestesia=" + requiresAnesthesia +
                ", quirófano=" + operatingRoomNeeded +
                '}';
    }
}

