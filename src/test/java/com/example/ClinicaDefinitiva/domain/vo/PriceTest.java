
package com.example.ClinicaDefinitiva.domain.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.*;

class PriceTest {

    private final Currency usd = Currency.getInstance("USD");
    private final Currency eur = Currency.getInstance("EUR");

    @Nested
    @DisplayName("Creación válida")
    class CreationTests {

        @Test
        @DisplayName("crear precio válido con BigDecimal")
        void create_validBigDecimal() {
            Price price = Price.of(new BigDecimal("10.456"), usd);

            assertThat(price.asBigDecimal()).isEqualByComparingTo("10.46"); // redondeado a 2 decimales
            assertThat(price.getCurrency()).isEqualTo(usd);
        }

        @Test
        @DisplayName("crear precio válido con double")
        void create_validDouble() {
            Price price = Price.of(20.5, usd);

            assertThat(price.asBigDecimal()).isEqualByComparingTo("20.50");
            assertThat(price.getCurrency()).isEqualTo(usd);
        }

        @Test
        @DisplayName("crear precio cero")
        void create_zero() {
            Price price = Price.zero(usd);

            assertThat(price.isZero()).isTrue();
            assertThat(price.asBigDecimal()).isEqualByComparingTo("0.00");
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("lanza excepción si amount es nulo")
        void nullAmount_throws() {
            assertThatThrownBy(() -> Price.of(null, usd))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_PRICE_AMOUNT_REQUIRED));
        }

        @Test
        @DisplayName("lanza excepción si currency es nulo")
        void nullCurrency_throws() {
            assertThatThrownBy(() -> Price.of(BigDecimal.TEN, null))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_PRICE_CURRENCY_REQUIRED));
        }

        @Test
        @DisplayName("lanza excepción si amount es negativo")
        void negativeAmount_throws() {
            assertThatThrownBy(() -> Price.of(new BigDecimal("-5"), usd))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_SERVICE_PRICE_NEGATIVE));
        }
    }

    @Nested
    @DisplayName("Operaciones aritméticas")
    class ArithmeticTests {

        @Test
        @DisplayName("sumar dos precios con misma moneda")
        void add_valid() {
            Price p1 = Price.of(10, usd);
            Price p2 = Price.of(5, usd);

            Price result = p1.add(p2);

            assertThat(result.asBigDecimal()).isEqualByComparingTo("15.00");
        }

        @Test
        @DisplayName("restar dos precios con misma moneda")
        void subtract_valid() {
            Price p1 = Price.of(10, usd);
            Price p2 = Price.of(4, usd);

            Price result = p1.subtract(p2);

            assertThat(result.asBigDecimal()).isEqualByComparingTo("6.00");
        }

        @Test
        @DisplayName("multiplicar precio por factor BigDecimal")
        void multiply_bigDecimal() {
            Price p = Price.of(10, usd);

            Price result = p.multiply(new BigDecimal("1.5"));

            assertThat(result.asBigDecimal()).isEqualByComparingTo("15.00");
        }

        @Test
        @DisplayName("multiplicar precio por factor double")
        void multiply_double() {
            Price p = Price.of(10, usd);

            Price result = p.multiply(2.5);

            assertThat(result.asBigDecimal()).isEqualByComparingTo("25.00");
        }

        @Test
        @DisplayName("lanza excepción si se suman precios con distinta moneda")
        void add_currencyMismatch_throws() {
            Price p1 = Price.of(10, usd);
            Price p2 = Price.of(5, eur);

            assertThatThrownBy(() -> p1.add(p2))
                    .isInstanceOf(ValueObjectValidationException.class)
                    .satisfies(ex -> assertThat(((ValueObjectValidationException) ex).getCatalogo())
                            .isEqualTo(ServiceVOError.ERR_PRICE_CURRENCY_MISMATCH));
        }
    }

    @Nested
    @DisplayName("Comparaciones semánticas")
    class ComparisonTests {

        @Test
        @DisplayName("isNegativeOrZero devuelve true si es cero")
        void isNegativeOrZero_zero() {
            Price p = Price.zero(usd);
            assertThat(p.isNegativeOrZero()).isTrue();
        }

        @Test
        @DisplayName("isZero devuelve true si amount es cero")
        void isZero_valid() {
            Price p = Price.zero(usd);
            assertThat(p.isZero()).isTrue();
        }

        @Test
        @DisplayName("compareTo devuelve resultado correcto")
        void compareTo_valid() {
            Price p1 = Price.of(10, usd);
            Price p2 = Price.of(20, usd);

            assertThat(p1.compareTo(p2)).isLessThan(0);
            assertThat(p2.compareTo(p1)).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Igualdad y utilitarios")
    class EqualityTests {

        @Test
        @DisplayName("dos precios iguales son equals y tienen mismo hashCode")
        void equals_sameValues() {
            Price p1 = Price.of(10, usd);
            Price p2 = Price.of(10, usd);

            assertThat(p1).isEqualTo(p2);
            assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        }

        @Test
        @DisplayName("precios distintos no son equals")
        void equals_differentValues() {
            Price p1 = Price.of(10, usd);
            Price p2 = Price.of(20, usd);

            assertThat(p1).isNotEqualTo(p2);
        }

        @Test
        @DisplayName("toString devuelve amount + código de moneda")
        void toString_valid() {
            Price p = Price.of(10, usd);
            assertThat(p.toString()).isEqualTo("10.00 USD");
        }
    }
}
