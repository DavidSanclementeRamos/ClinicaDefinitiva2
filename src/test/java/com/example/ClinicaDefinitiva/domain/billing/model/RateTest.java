
package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.enu.PayerType;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Currency;


import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.enu.PayerType;
import com.example.ClinicaDefinitiva.domain.billing.enu.RateStatus;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.RateError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Tests del Agregado Rate")
class RateTest {

    private ServiceId serviceId;
    private Price amount;
    private PayerType payerType;
    private ContractId contractId;
    private Currency cop;

    @BeforeEach
    void setUp() {
        serviceId = ServiceId.of(1L);
        cop = Currency.getInstance("COP");
        amount = Price.of(150000, cop);
        payerType = PayerType.EPS;
        contractId = ContractId.of(100L);
    }

    @Nested
    @DisplayName("Tests de creación de Rate")
    class RateCreationTests {

        @Test
        @DisplayName("Debe crear una tarifa exitosamente")
        void shouldCreateRateSuccessfully() {
            // Act
            Rate rate = Rate.create(serviceId, amount, payerType, contractId);

            // Assert
            assertNotNull(rate);
            assertNull(rate.getId());
            assertEquals(serviceId, rate.getServiceId());
            assertEquals(amount, rate.getAmount());
            assertEquals(payerType, rate.getPayerType());
            assertEquals(contractId, rate.getContractId());
            assertNotNull(rate.getValidFrom());
            assertNull(rate.getValidTo());
            assertEquals(RateStatus.ACTIVE, rate.getStatus());
            assertTrue(rate.isActive());
            assertTrue(rate.isCurrentlyValid());
            assertTrue(rate.isIndefinite());
            assertTrue(rate.isForEPS());
        }

        @Test
        @DisplayName("Debe crear una tarifa sin contrato para particulares")
        void shouldCreateRateWithoutContractForParticular() {
            // Arrange
            PayerType particular = PayerType.EPS;

            // Act
            Rate rate = Rate.create(serviceId, amount, particular, null);

            // Assert
            assertNotNull(rate);
            assertEquals(particular, rate.getPayerType());
            assertNull(rate.getContractId());
            assertFalse(rate.isForEPS());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la fecha de vigencia final es anterior a la inicial")
        void shouldThrowExceptionWhenValidToBeforeValidFrom() {
            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Rate.builder()
                    .serviceId(serviceId)
                    .amount(amount)
                    .payerType(payerType)
                    .contractId(contractId)
                    .validFrom(LocalDateTime.now())
                    .validTo(LocalDateTime.now().minusDays(1))
                    .build()
            );
            
            assertEquals(RateError.ERR_RATE_INVALID_VALIDITY_RANGE, exception.getCatalogo());
        }
    }

    @Nested
    @DisplayName("Tests de validación de vigencia")
    class ValidityTests {

        private Rate rate;

        @BeforeEach
        void setUp() {
            rate = Rate.create(serviceId, amount, payerType, contractId);
        }

        @Test
        @DisplayName("Debe retornar true si la tarifa es válida en una fecha específica")
        void shouldReturnTrueIfValidAtSpecificDate() {
            // Arrange
            LocalDateTime validFrom = LocalDateTime.now().minusDays(10);
            LocalDateTime validTo = LocalDateTime.now().plusDays(10);
            
            Rate datedRate = Rate.builder()
                .serviceId(serviceId)
                .amount(amount)
                .payerType(payerType)
                .contractId(contractId)
                .validFrom(validFrom)
                .validTo(validTo)
                .build();

            // Act & Assert
            assertTrue(datedRate.isValidAt(validFrom));
            assertTrue(datedRate.isValidAt(validFrom.plusDays(5)));
            assertTrue(datedRate.isValidAt(validTo));
        }

        @Test
        @DisplayName("Debe retornar false si la tarifa no es válida en una fecha específica")
        void shouldReturnFalseIfNotValidAtSpecificDate() {
            // Arrange
            LocalDateTime validFrom = LocalDateTime.now().minusDays(10);
            LocalDateTime validTo = LocalDateTime.now().minusDays(5);
            
            Rate expiredRate = Rate.builder()
                .serviceId(serviceId)
                .amount(amount)
                .payerType(payerType)
                .contractId(contractId)
                .validFrom(validFrom)
                .validTo(validTo)
                .build();

            // Act & Assert
            assertFalse(expiredRate.isValidAt(LocalDateTime.now()));
            assertFalse(expiredRate.isValidAt(validFrom.minusDays(1)));
            assertFalse(expiredRate.isValidAt(validTo.plusDays(1)));
        }

        @Test
        @DisplayName("Debe retornar true para tarifa indefinida en cualquier fecha futura")
        void shouldReturnTrueForIndefiniteRateAnyFutureDate() {
            // Act & Assert
            assertTrue(rate.isValidAt(LocalDateTime.now().plusYears(1)));
            assertTrue(rate.isValidAt(LocalDateTime.now().plusMonths(6)));
        }

        @Test
        @DisplayName("Debe lanzar excepción al verificar validez con ensureValidAt")
        void shouldThrowExceptionWhenEnsuringValidityFails() {
            // Arrange
            LocalDateTime validFrom = LocalDateTime.now().minusDays(10);
            LocalDateTime validTo = LocalDateTime.now().minusDays(5);
            
            Rate expiredRate = Rate.builder()
                .serviceId(serviceId)
                .amount(amount)
                .payerType(payerType)
                .contractId(contractId)
                .validFrom(validFrom)
                .validTo(validTo)
                .build();

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> expiredRate.ensureValidAt(LocalDateTime.now())
            );
            
            assertEquals(RateError.ERR_RATE_NOT_VALID_AT_DATE, exception.getCatalogo());
        }
    }

    @Nested
    @DisplayName("Tests de gestión de estado")
    class RateStatusTests {

        private Rate rate;

        @BeforeEach
        void setUp() {
            rate = Rate.create(serviceId, amount, payerType, contractId);
        }

        @Test
        @DisplayName("Debe finalizar la vigencia correctamente")
        void shouldEndValiditySuccessfully() {
            // Arrange
            LocalDateTime endDate = LocalDateTime.now().plusDays(30);

            // Act
            rate.endValidityAt(endDate);

            // Assert
            assertEquals(endDate, rate.getValidTo());
            assertEquals(RateStatus.EXPIRED, rate.getStatus());
            assertFalse(rate.isActive());
            assertTrue(rate.isExpired());
        }

        @Test
        @DisplayName("Debe lanzar excepción al finalizar vigencia con fecha anterior al inicio")
        void shouldThrowExceptionWhenEndingValidityWithDateBeforeStart() {
            // Arrange
            LocalDateTime invalidDate = rate.getValidFrom().minusDays(1);

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> rate.endValidityAt(invalidDate)
            );
            
            assertEquals(RateError.ERR_RATE_INVALID_VALIDITY_RANGE, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe marcar como reemplazada correctamente")
        void shouldMarkAsReplaced() {
            // Act
            rate.markAsReplaced();

            // Assert
            assertEquals(RateStatus.REPLACED, rate.getStatus());
            assertTrue(rate.isReplaced());
            assertFalse(rate.isActive());
        }

        @Test
        @DisplayName("Debe desactivar la tarifa correctamente")
        void shouldDeactivateRate() {
            // Act
            rate.deactivate();

            // Assert
            assertEquals(RateStatus.INACTIVE, rate.getStatus());
            assertTrue(rate.isInactive());
            assertFalse(rate.isActive());
        }
    }
}