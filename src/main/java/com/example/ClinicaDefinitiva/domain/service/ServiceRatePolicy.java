package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Price;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.math.BigDecimal;

public final class ServiceRatePolicy {

    private static final BigDecimal MIN_RATE_PERCENTAGE = new BigDecimal("0.80"); // 80%
    private static final BigDecimal MAX_RATE_PERCENTAGE = new BigDecimal("1.20"); // 120%

    private ServiceRatePolicy() {
        
    }

    public static void validateRateChange(Price oldRate, Price newRate) {
        BigDecimal oldAmount = oldRate.asBigDecimal();
        BigDecimal newAmount = newRate.asBigDecimal();

        BigDecimal minAllowed = oldAmount.multiply(MIN_RATE_PERCENTAGE);
        BigDecimal maxAllowed = oldAmount.multiply(MAX_RATE_PERCENTAGE);

        if (newAmount.compareTo(minAllowed) < 0 || newAmount.compareTo(maxAllowed) > 0) {
            throw new BusinessRuleViolationException(
                    ServiceError.ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE, EntityContext.DENTAL_SERVICE
            );
        }
    }
}