package com.example.ClinicaDefinitiva.domain.dentalService.model;

import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ProstheticError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;
import java.util.Set;

/**
 * Detalles de un servicio protésico.
 *
 * Representa prótesis dentales como coronas, puentes y dentaduras.
 * Diferencia entre prótesis fijas y removibles, con limitaciones en el número de unidades.
 *
 * Reglas de negocio:
 * - RN-PROSTHETIC-001: El tipo de prótesis (fija/removible) es obligatorio.
 * - RN-PROSTHETIC-002: El número de unidades no puede ser negativo.
 * - RN-PROSTHETIC-003: Las prótesis removibles están limitadas a 14 unidades por arco.
 * - RN-PROSTHETIC-004: Solo se permiten tipos de prótesis válidos.
 */
public final class ProstheticDetails implements ServiceDetails {

    private static final Set<String> VALID_TYPES = Set.of("FIXED", "REMOVABLE");
    private static final int MAX_UNITS_PER_ARCH = 14;

    private final String fixedOrRemovable;
    private final String material;
    private final String prostheticType;
    private final Integer units;

    /**
     * Constructor controlado con validaciones centralizadas.
     *
     * @param fixedOrRemovable Indica si la prótesis es fija o removible.
     * @param material Material utilizado en la prótesis.
     * @param prostheticType Tipo específico de prótesis (ej. corona, puente).
     * @param units Número de unidades de prótesis.
     */
    public ProstheticDetails(String fixedOrRemovable, String material,
                             String prostheticType, Integer units) {

        // RN-PROSTHETIC-001: El tipo de prótesis es obligatorio
        if (fixedOrRemovable == null || fixedOrRemovable.isBlank()) {
            throw new ValueObjectValidationException(
                    ProstheticError.ERR_PROSTHETIC_MISSING_TYPE,
                    VOContext.DENTAL_SERVICES
            );
        }

        // RN-PROSTHETIC-004: Validación del tipo de prótesis
        String normalizedType = ValidationHelper.normalizeAndValidate(
                fixedOrRemovable,
                VALID_TYPES,
                ProstheticError.ERR_PROSTHETIC_INVALID_TYPE_VALUE,
                VOContext.DENTAL_SERVICES
        );

        int unitsValue = units == null ? 0 : units;

        // RN-PROSTHETIC-002: Las unidades no pueden ser negativas
        if (unitsValue < 0) {
            throw new ValueObjectValidationException(
                    ProstheticError.ERR_PROSTHETIC_INVALID_UNITS,
                    VOContext.DENTAL_SERVICES
            );
        }

        // RN-PROSTHETIC-003: Limitación de unidades para prótesis removibles
        if ("REMOVABLE".equals(normalizedType) && unitsValue > MAX_UNITS_PER_ARCH) {
            throw new ValueObjectValidationException(
                    ProstheticError.ERR_PROSTHETIC_EXCESSIVE_UNITS,
                    VOContext.DENTAL_SERVICES
            );
        }

        this.fixedOrRemovable = normalizedType;
        this.material = material;
        this.prostheticType = prostheticType;
        this.units = unitsValue;
    }

    @Override
    public ServiceType serviceType() {
        return ServiceType.PROSTHETICS;
    }

    public String getFixedOrRemovable() {
        return fixedOrRemovable;
    }

    public String getMaterial() {
        return material;
    }

    public String getProstheticType() {
        return prostheticType;
    }

    public Integer getUnits() {
        return units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProstheticDetails)) return false;
        ProstheticDetails that = (ProstheticDetails) o;
        return fixedOrRemovable.equals(that.fixedOrRemovable) &&
                Objects.equals(material, that.material) &&
                Objects.equals(prostheticType, that.prostheticType) &&
                Objects.equals(units, that.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fixedOrRemovable, material, prostheticType, units);
    }

    @Override
    public String toString() {
        return "ProstheticDetails{" +
                "tipo=" + fixedOrRemovable +
                ", material=" + material +
                ", tipoPrótesis=" + prostheticType +
                ", unidades=" + units +
                '}';
    }
}
