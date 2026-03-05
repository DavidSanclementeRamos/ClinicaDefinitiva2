package com.example.ClinicaDefinitiva.domain.dentalService.model;


import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.AgeRange;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.PediatricError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;

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
 * - RN-PEDIATRIC-003: La descripción de materiales pediátricos debe tener una longitud mínima.
 */
public final class PediatricDetails implements ServiceDetails {

    private static final int MIN_MATERIALS_LENGTH = 5;

    private final AgeRange ageRange;
    private final String behaviorManagement;
    private final String pediatricMaterials;

    /**
     * Constructor controlado con validaciones centralizadas.
     *
     * @param ageRange Rango de edad del paciente pediátrico.
     * @param behaviorManagement Técnicas de manejo de comportamiento.
     * @param pediatricMaterials Materiales especializados para odontología pediátrica.
     */
    public PediatricDetails(AgeRange ageRange, String behaviorManagement, String pediatricMaterials) {
        

        // RN-PEDIATRIC-006: Validación de longitud mínima de materiales
        ValidationHelper.validateMinLength(
                pediatricMaterials,
                MIN_MATERIALS_LENGTH,
                PediatricError.ERR_PEDIATRIC_MATERIALS_TOO_SHORT,
                VOContext.AUTHORIZATION
        );

        this.ageRange = ageRange;
        this.behaviorManagement = behaviorManagement;
        this.pediatricMaterials = pediatricMaterials;
    }

    


    @Override
    public ServiceType serviceType() {
        return ServiceType.PEDIATRICS;
    }

    public AgeRange getAgeRange() {
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
