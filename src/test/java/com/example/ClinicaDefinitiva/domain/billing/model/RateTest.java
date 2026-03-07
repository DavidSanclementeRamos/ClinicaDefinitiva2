
package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Currency;

class RateTest {

    private Rate buildValidRate() {
        return Rate.builder()
                .id(RateId.of(1L))
                .serviceId(ServiceId.of(10L))
                .amount(Price.of(500, Currency.getInstance("COP")))
                .payerType(Rate.PayerType.PRIVATE)
                .contractId(ContractId.of(100L))
                .validFrom(LocalDateTime.now().minusDays(1))
                .validTo(LocalDateTime.now().plusDays(10))
                .active(true)
                .build();
    }

    @Test
    void shouldCreateRateSuccessfullyWithBuilder() {
        Rate rate = buildValidRate();

        assertNotNull(rate.getId());
        assertEquals(Rate.PayerType.PRIVATE, rate.getPayerType());
        assertTrue(rate.isActive());
        assertTrue(rate.isCurrentlyValid());
    }

    @Test
    void shouldCreateRateSuccessfullyWithFactoryMethod() {
        Rate rate = Rate.create(
                ServiceId.of(20L),
                Price.of(300, Currency.getInstance("COP")),
                Rate.PayerType.EPS,
                ContractId.of(200L)
        );

        assertEquals(Rate.PayerType.EPS, rate.getPayerType());
        assertTrue(rate.isActive());
        assertTrue(rate.isIndefinite()); // porque no se pasó validTo
    }

    @Test
    void shouldThrowExceptionWhenValidityRangeIsInvalid() {
        assertThrows(BusinessRuleViolationException.class, () ->
                Rate.builder()
                        .id(RateId.of(2L))
                        .serviceId(ServiceId.of(30L))
                        .amount(Price.of(400, Currency.getInstance("COP")))
                        .payerType(Rate.PayerType.PRIVATE)
                        .validFrom(LocalDateTime.now())
                        .validTo(LocalDateTime.now().minusDays(1)) // inválido
                        .build()
        );
    }

    @Test
    void shouldBeValidAtDateWithinRange() {
        Rate rate = buildValidRate();
        LocalDateTime testDate = LocalDateTime.now().plusDays(5);

        assertTrue(rate.isValidAt(testDate));
    }

    @Test
    void shouldNotBeValidBeforeValidFrom() {
        Rate rate = buildValidRate();
        LocalDateTime testDate = rate.getValidFrom().minusDays(1);

        assertFalse(rate.isValidAt(testDate));
    }

    @Test
    void shouldNotBeValidAfterValidTo() {
        Rate rate = buildValidRate();
        LocalDateTime testDate = rate.getValidTo().plusDays(1);

        assertFalse(rate.isValidAt(testDate));
    }

    @Test
    void shouldThrowExceptionWhenEnsureValidAtInvalidDate() {
        Rate rate = buildValidRate();
        LocalDateTime testDate = rate.getValidFrom().minusDays(2);

        assertThrows(BusinessRuleViolationException.class, () -> rate.ensureValidAt(testDate));
    }

    @Test
    void shouldDeactivateRateSuccessfully() {
        Rate rate = buildValidRate();
        rate.deactivate();

        assertFalse(rate.isActive());
        assertFalse(rate.isCurrentlyValid());
    }

    @Test
    void shouldThrowExceptionWhenEndValidityBeforeValidFrom() {
        Rate rate = buildValidRate();
        LocalDateTime invalidEndDate = rate.getValidFrom().minusDays(1);

        assertThrows(BusinessRuleViolationException.class, () -> rate.endValidityAt(invalidEndDate));
    }

    @Test
    void shouldIdentifyRateAsForEPS() {
        Rate rate = Rate.create(
                ServiceId.of(40L),
                Price.of(600, Currency.getInstance("COP")),
                Rate.PayerType.EPS,
                ContractId.of(300L)
        );

        assertTrue(rate.isForEPS());
    }
}

