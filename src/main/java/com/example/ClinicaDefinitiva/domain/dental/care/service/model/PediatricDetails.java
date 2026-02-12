package com.example.ClinicaDefinitiva.domain.dental.care.service.model;


import com.example.ClinicaDefinitiva.domain.dental.care.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.PediatricError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Detalles de un servicio dental pediátrico.
 *
 * Representa servicios dentales diseñados específicamente para niños.
 * Incluye rango de edad, técnicas de manejo de comportamiento y materiales especializados.
 *
 * Reglas de negocio:
 * - RN-PEDIATRIC-001: El rango de edad debe ser válido para pacientes pediátricos (0-18 años).
 * - RN-PEDIATRIC-002: El rango de edad debe tener una longitud mínima.
 * - RN-PEDIATRIC-006: La descripción de materiales pediátricos debe tener una longitud mínima.
 */
public final class PediatricDetails implements ServiceDetails {

    private static final int MIN_AGE_RANGE_LENGTH = 5;
    private static final int MIN_MATERIALS_LENGTH = 5;
    // Patrón para asegurar que el rango de edad contenga al menos un número
    private static final Pattern AGE_PATTERN = Pattern.compile(".*\\d+.*");

    private final String ageRange;
    private final String behaviorManagement;
    private final String pediatricMaterials;

    /**
     * Constructor controlado con validaciones centralizadas.
     *
     * @param ageRange Rango de edad del paciente pediátrico.
     * @param behaviorManagement Técnicas de manejo de comportamiento.
     * @param pediatricMaterials Materiales especializados para odontología pediátrica.
     */
    public PediatricDetails(String ageRange, String behaviorManagement, String pediatricMaterials) {

        // RN-PEDIATRIC-002: Validación de longitud mínima del rango de edad
        ValidationHelper.validateMinLength(
                ageRange,
                MIN_AGE_RANGE_LENGTH,
                PediatricError.ERR_PEDIATRIC_AGE_RANGE_TOO_SHORT,
                VOContext.PEDIATRIC
        );

        // RN-PEDIATRIC-001: Validación de rango de edad pediátrico (0-18 años)
        if (ageRange != null && !isValidPediatricAge(ageRange)) {
            throw new ValueObjectValidationException(
                    PediatricError.ERR_PEDIATRIC_INVALID_AGE_RANGE,
                    VOContext.PEDIATRIC
            );
        }

        // RN-PEDIATRIC-006: Validación de longitud mínima de materiales
        ValidationHelper.validateMinLength(
                pediatricMaterials,
                MIN_MATERIALS_LENGTH,
                PediatricError.ERR_PEDIATRIC_MATERIALS_TOO_SHORT,
                VOContext.PEDIATRIC
        );

        this.ageRange = ageRange;
        this.behaviorManagement = behaviorManagement;
        this.pediatricMaterials = pediatricMaterials;
    }

    /**
     * Valida que el rango de edad sea apropiado para pacientes pediátricos.
     *
     * Reglas:
     * - Debe contener al menos un número.
     * - No puede mencionar edades >= 19.
     * - Rechaza rangos con referencias explícitas a adultos.
     */
    private boolean isValidPediatricAge(String ageRange) {
        // Debe contener números
        if (!AGE_PATTERN.matcher(ageRange).matches()) {
            return false;
        }

        String lower = ageRange.toLowerCase();
        // Rechazar referencias explícitas a edades >= 19
        return !lower.contains("19") &&
                !lower.contains("20") &&
                !lower.matches(".*[2-9]\\d+.*"); // Rechaza 20+, 30+, etc.
    }

    @Override
    public ServiceType serviceType() {
        return ServiceType.PEDIATRICS;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public String getBehaviorManagement() {
        return behaviorManagement;
    }

    public String getPediatricMaterials() {
        return pediatricMaterials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PediatricDetails)) return false;
        PediatricDetails that = (PediatricDetails) o;
        return Objects.equals(ageRange, that.ageRange) &&
                Objects.equals(behaviorManagement, that.behaviorManagement) &&
                Objects.equals(pediatricMaterials, that.pediatricMaterials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ageRange, behaviorManagement, pediatricMaterials);
    }

    @Override
    public String toString() {
        return "PediatricDetails{" +
                "rangoEdad=" + ageRange +
                ", manejoComportamiento=" + behaviorManagement +
                ", materiales=" + pediatricMaterials +
                '}';
    }
}
