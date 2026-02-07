package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ProstheticError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Set;

public final class ProstheticDetails implements ServiceDetails {
    private final String fixedOrRemovable;
    private final String material;
    private final String prostheticType;
    private final Integer units;


    private static final Set VALID_TYPES = Set.of("FIXED", "REMOVABLE");
    private static final int MAX_UNITS_PER_ARCH = 14;



    public ProstheticDetails(String fixedOrRemovable, String material,
                             String prostheticType, Integer units) {
        // RN-PROSTHETIC-001
        if (fixedOrRemovable == null || fixedOrRemovable.isBlank()) {
            throw new ValueObjectValidationException(ProstheticError.ERR_PROSTHETIC_MISSING_TYPE, VOContext.PROSTHETIC
            );
        }

        String normalizedType = fixedOrRemovable.toUpperCase();

        // RN-PROSTHETIC-004
        if (!VALID_TYPES.contains(normalizedType)) {
            throw new ValueObjectValidationException(
                    ProstheticError.ERR_PROSTHETIC_INVALID_TYPE_VALUE,VOContext.PROSTHETIC
            );
        }

        int unitsValue = units == null ? 0 : units;

        // RN-PROSTHETIC-002
        if (unitsValue < 0) {
            throw new ValueObjectValidationException(
                    ProstheticError.ERR_PROSTHETIC_INVALID_UNITS,VOContext.PROSTHETIC
            );
        }

        // RN-PROSTHETIC-003
        if ("REMOVABLE".equals(normalizedType) && unitsValue > MAX_UNITS_PER_ARCH) {
            throw new ValueObjectValidationException(
                    ProstheticError.ERR_PROSTHETIC_EXCESSIVE_UNITS,VOContext.PROSTHETIC
            );
        }

        this.fixedOrRemovable = normalizedType;
        this.material = material;
        this.prostheticType = prostheticType;
        this.units = unitsValue;
    }


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
}
