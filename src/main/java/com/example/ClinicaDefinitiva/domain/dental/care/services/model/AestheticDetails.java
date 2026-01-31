package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.Enum.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.AestheticError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Set;

public final class AestheticDetails implements ServiceDetails {

    private static final Set VALID_AESTHETIC_TYPES = Set.of(
            "WHITENING",
            "VENEER",
            "BONDING",
            "CONTOURING",
            "GUM_RESHAPING",
            "SMILE_DESIGN",
            "COMPOSITE_RESTORATION",
            "INLAY_ONLAY"
    );

    private static final int MIN_TYPE_LENGTH = 3;
    private static final int MIN_RESULT_LENGTH = 10;

    private final String aestheticType;
    private final String materialUsed;
    private final String expectedResult;

    public AestheticDetails(String aestheticType, String materialUsed, String expectedResult) {
        // RN-AESTHETIC-001
        if (aestheticType == null || aestheticType.isBlank()) {
            throw new ValueObjectValidationException(
                    AestheticError.ERR_AESTHETIC_MISSING_TYPE, VOContext.AESTHETIC
            );
        }

        // RN-AESTHETIC-003
        if (aestheticType.length() < MIN_TYPE_LENGTH) {
            throw new ValueObjectValidationException(
                    AestheticError.ERR_AESTHETIC_TYPE_TOO_SHORT,VOContext.AESTHETIC
            );
        }

        String normalizedType = aestheticType.toUpperCase();

        // RN-AESTHETIC-002
        if (!VALID_AESTHETIC_TYPES.contains(normalizedType)) {
            throw new ValueObjectValidationException(
                    AestheticError.ERR_AESTHETIC_INVALID_TYPE,VOContext.AESTHETIC
            );
        }

        // RN-AESTHETIC-004
        if (expectedResult != null && expectedResult.length() < MIN_RESULT_LENGTH) {
            throw new ValueObjectValidationException(
                    AestheticError.ERR_AESTHETIC_RESULT_TOO_SHORT,VOContext.AESTHETIC
            );
        }

        this.aestheticType = normalizedType;
        this.materialUsed = materialUsed;
        this.expectedResult = expectedResult;
    }

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

}
