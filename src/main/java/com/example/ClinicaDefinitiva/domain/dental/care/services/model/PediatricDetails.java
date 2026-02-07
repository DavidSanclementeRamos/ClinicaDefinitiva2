package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.PediatricError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.regex.Pattern;

public final class PediatricDetails implements ServiceDetails {

    private static final int MIN_AGE_RANGE_LENGTH = 5;
    private static final int MIN_MATERIALS_LENGTH = 5;
    private static final Pattern AGE_PATTERN = Pattern.compile(".*\\d+.*"); // Debe contener al menos un número

    private final String ageRange;
    private final String behaviorManagement;
    private final String pediatricMaterials;

    public PediatricDetails(String ageRange, String behaviorManagement, String pediatricMaterials) {
        // RN-PEDIATRIC-002
        if (ageRange != null && ageRange.length() < MIN_AGE_RANGE_LENGTH) {
            throw new ValueObjectValidationException(
                    PediatricError.ERR_PEDIATRIC_AGE_RANGE_TOO_SHORT, VOContext.PEDIATRIC
            );
        }

        // RN-PEDIATRIC-001 - Validación básica de edad pediátrica
        if (ageRange != null && !isValidPediatricAge(ageRange)) {
            throw new ValueObjectValidationException(
                    PediatricError.ERR_PEDIATRIC_INVALID_AGE_RANGE,VOContext.PEDIATRIC
            );
        }

        // RN-PEDIATRIC-006
        if (pediatricMaterials != null && pediatricMaterials.length() < MIN_MATERIALS_LENGTH) {
            throw new ValueObjectValidationException(
                    PediatricError.ERR_PEDIATRIC_MATERIALS_TOO_SHORT,VOContext.PEDIATRIC
            );
        }

        this.ageRange = ageRange;
        this.behaviorManagement = behaviorManagement;
        this.pediatricMaterials = pediatricMaterials;
    }

    private boolean isValidPediatricAge(String ageRange) {
        // Validación simple: debe contener números y no mencionar edades > 18
        if (!AGE_PATTERN.matcher(ageRange).matches()) {
            return false;
        }

        String lower = ageRange.toLowerCase();
        // Rechaza explícitamente rangos con adultos
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
}
