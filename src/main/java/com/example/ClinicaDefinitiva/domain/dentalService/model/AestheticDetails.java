package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.AestheticError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.Objects;
import java.util.Set;

/**
 * Detalles de un servicio dental estético.
 *
 * Representa procedimientos de odontología estética orientados a mejorar la apariencia.
 * Incluye validaciones para el tipo de tratamiento estético, materiales utilizados
 * y el resultado esperado.
 *
 * Reglas de negocio:
 * - RN-AESTHETIC-001: El tipo de tratamiento estético es obligatorio.
 * - RN-AESTHETIC-002: Solo se permiten tipos de tratamiento válidos.
 * - RN-AESTHETIC-003: El tipo de tratamiento debe tener una longitud mínima.
 * - RN-AESTHETIC-004: El resultado esperado debe tener una longitud mínima si se proporciona.
 */
public final class AestheticDetails implements ServiceDetails {

    private static final Set<String> VALID_AESTHETIC_TYPES = Set.of(
            "WHITENING",              // Blanqueamiento dental
            "VENEER",                 // Carillas de porcelana o composite
            "BONDING",                // Adhesión dental
            "CONTOURING",             // Recontorneado dental
            "GUM_RESHAPING",          // Contorneado de encías
            "SMILE_DESIGN",           // Diseño de sonrisa completo
            "COMPOSITE_RESTORATION",  // Restauraciones estéticas en composite
            "INLAY_ONLAY"             // Incrustaciones estéticas
    );

    private static final int MIN_TYPE_LENGTH = 3;
    private static final int MIN_RESULT_LENGTH = 10;

    private final String aestheticType;
    private final String materialUsed;
    private final String expectedResult;

    /**
     * Constructor controlado con validaciones centralizadas.
     *
     * @param aestheticType Tipo de tratamiento estético.
     * @param materialUsed  Material utilizado en el procedimiento.
     * @param expectedResult Resultado esperado del tratamiento.
     */
    public AestheticDetails(String aestheticType, String materialUsed, String expectedResult) {
        // RN-AESTHETIC-001: Tipo obligatorio
        if (aestheticType == null || aestheticType.isBlank()) {
            throw new ValueObjectValidationException(
                    AestheticError.ERR_AESTHETIC_MISSING_TYPE,
                    VOContext.DENTAL_SERVICES
            );
        }

        // RN-AESTHETIC-003: Longitud mínima del tipo
        ValidationHelper.validateMinLength(
                aestheticType,
                MIN_TYPE_LENGTH,
                AestheticError.ERR_AESTHETIC_TYPE_TOO_SHORT,
                VOContext.DENTAL_SERVICES
        );

        // RN-AESTHETIC-002: Normalización y validación contra catálogo permitido
        this.aestheticType = ValidationHelper.normalizeAndValidate(
                aestheticType,
                VALID_AESTHETIC_TYPES,
                AestheticError.ERR_AESTHETIC_INVALID_TYPE,
                VOContext.DENTAL_SERVICES
        );

        // RN-AESTHETIC-004: Validación de longitud mínima del resultado esperado
        ValidationHelper.validateMinLength(
                expectedResult,
                MIN_RESULT_LENGTH,
                AestheticError.ERR_AESTHETIC_RESULT_TOO_SHORT,
                VOContext.DENTAL_SERVICES
        );

        this.materialUsed = materialUsed;
        this.expectedResult = expectedResult;
    }

    @Override
    public ServiceType serviceType() {
        return ServiceType.AESTHETICS;
    }

    public String getAestheticType() {
        return aestheticType;
    }

    public String getMaterialUsed() {
        return materialUsed;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AestheticDetails)) return false;
        AestheticDetails that = (AestheticDetails) o;
        return aestheticType.equals(that.aestheticType) &&
                Objects.equals(materialUsed, that.materialUsed) &&
                Objects.equals(expectedResult, that.expectedResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aestheticType, materialUsed, expectedResult);
    }

    @Override
    public String toString() {
        return "AestheticDetails{" +
                "tipo=" + aestheticType +
                ", material=" + materialUsed +
                ", resultadoEsperado=" + expectedResult +
                '}';
    }
}
