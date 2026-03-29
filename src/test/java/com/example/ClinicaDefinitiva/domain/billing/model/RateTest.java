package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.enu.PayerType;
import com.example.ClinicaDefinitiva.domain.billing.enu.RateStatus;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.*;

class RateTest {

    private static final Currency COP = Currency.getInstance("COP");

    @Test
    @DisplayName("INV-UNIT-006: Crear tarifa activa")
    void create_shouldBeActive() {
        Rate rate = Rate.create(
                ServiceId.of(1L),
                Price.of(120_000, COP),
                PayerType.EPS,
                ContractId.of(1L)
        );

        assertThat(rate.isActive()).isTrue();
        assertThat(rate.getStatus()).isEqualTo(RateStatus.ACTIVE);
        assertThat(rate.getValidFrom()).isNotNull();
        assertThat(rate.getValidTo()).isNull();
    }

    @Test
    @DisplayName("INV-UNIT-007: Finalizar vigencia")
    void endValidityAt_shouldExpire() {
        Rate rate = createActiveRate();
        LocalDateTime end = LocalDateTime.now().plusMonths(6);

        rate.endValidityAt(end);

        assertThat(rate.getStatus()).isEqualTo(RateStatus.EXPIRED);
        assertThat(rate.getValidTo()).isEqualTo(end);
    }

    @Test
    @DisplayName("RN-RATE-002: Tarifa no válida en fecha anterior al inicio")
    void isValidAt_beforeValidFrom_shouldReturnFalse() {
        Rate rate = createActiveRate();
        LocalDateTime beforeStart = rate.getValidFrom().minusDays(1);

        assertThat(rate.isValidAt(beforeStart)).isFalse();
    }

    @Test
    @DisplayName("RN-RATE-002: Tarifa no válida después de fin si tiene fin")
    void isValidAt_afterValidTo_shouldReturnFalse() {
        Rate rate = createActiveRate();
        LocalDateTime end = rate.getValidFrom().plusMonths(12);
        rate.endValidityAt(end);
        LocalDateTime afterEnd = end.plusDays(1);

        assertThat(rate.isValidAt(afterEnd)).isFalse();
    }

    private Rate createActiveRate() {
        return Rate.create(
                ServiceId.of(1L),
                Price.of(100_000, COP),
                PayerType.INSURANCE,
                null
        );
    }
}
