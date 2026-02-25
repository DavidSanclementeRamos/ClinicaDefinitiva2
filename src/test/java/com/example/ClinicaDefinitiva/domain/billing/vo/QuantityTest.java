
package com.example.ClinicaDefinitiva.domain.billing.vo;

import com.example.ClinicaDefinitiva.domain.billing.valueObject.Quantity;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void shouldCreateValidQuantity() {
        Quantity q = Quantity.of(10);
        assertEquals(10, q.getValue());
        assertTrue(q.isMultiple());
        assertFalse(q.isSingle());
    }

    @Test
    void shouldCreateSingleQuantityWithFactoryMethod() {
        Quantity q = Quantity.one();
        assertEquals(1, q.getValue());
        assertTrue(q.isSingle());
        assertFalse(q.isMultiple());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsLessThanMinimum() {
        assertThrows(ValueObjectValidationException.class, () -> Quantity.of(0));
    }

    @Test
    void shouldThrowExceptionWhenQuantityExceedsMaximum() {
        assertThrows(ValueObjectValidationException.class, () -> Quantity.of(2000));
    }

    @Test
    void shouldAddQuantitiesCorrectly() {
        Quantity q1 = Quantity.of(5);
        Quantity q2 = Quantity.of(3);
        Quantity result = q1.add(q2);
        assertEquals(8, result.getValue());
    }

    @Test
    void shouldMultiplyQuantityCorrectly() {
        Quantity q = Quantity.of(4);
        Quantity result = q.multiply(3);
        assertEquals(12, result.getValue());
    }

    @Test
    void shouldThrowExceptionWhenMultiplyByZeroOrNegative() {
        Quantity q = Quantity.of(5);
        assertThrows(IllegalArgumentException.class, () -> q.multiply(0));
        assertThrows(IllegalArgumentException.class, () -> q.multiply(-2));
    }

    @Test
    void shouldRespectEquality() {
        Quantity q1 = Quantity.of(10);
        Quantity q2 = Quantity.of(10);
        Quantity q3 = Quantity.of(20);

        assertEquals(q1, q2);
        assertNotEquals(q1, q3);
    }

    @Test
    void toStringShouldReturnValue() {
        Quantity q = Quantity.of(15);
        assertEquals("15", q.toString());
    }
}

