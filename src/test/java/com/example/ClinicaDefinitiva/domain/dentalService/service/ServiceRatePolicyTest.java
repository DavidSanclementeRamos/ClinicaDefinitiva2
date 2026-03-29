package com.example.ClinicaDefinitiva.domain.dentalService.service;

import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.assertj.core.api.Assertions.*;

class ServiceRatePolicyTest {

    private static final Currency COP = Currency.getInstance("COP");

    @Test
    @DisplayName("Validar cambio dentro del rango permitido (80% - 120%)")
    void validateRateChange_withinRange_shouldNotThrow() {
        Price oldRate = Price.of(100_000, COP);
        Price newRate = Price.of(110_000, COP); // +10%
        assertThatCode(() -> ServiceRatePolicy.validateRateChange(oldRate, newRate))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Validar cambio menor al 80% lanza excepción")
    void validateRateChange_belowMinimum_shouldThrow() {
        Price oldRate = Price.of(100_000, COP);
        Price newRate = Price.of(79_000, COP); // -21%
        assertThatThrownBy(() -> ServiceRatePolicy.validateRateChange(oldRate, newRate))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("Validar cambio mayor al 120% lanza excepción")
    void validateRateChange_aboveMaximum_shouldThrow() {
        Price oldRate = Price.of(100_000, COP);
        Price newRate = Price.of(121_000, COP); // +21%
        assertThatThrownBy(() -> ServiceRatePolicy.validateRateChange(oldRate, newRate))
                .isInstanceOf(BusinessRuleViolationException.class);
    }
}
