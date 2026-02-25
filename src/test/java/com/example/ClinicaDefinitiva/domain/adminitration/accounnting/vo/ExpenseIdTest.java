
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ExpenseId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExpenseIdTest {

    @Test
    void shouldCreateValidExpenseId() {
        ExpenseId id = ExpenseId.of(10L);
        assertEquals(10L, id.getValue()); // compara Long con Long
        assertEquals("ExpenseId[getValue=10]", id.toString()); // formato por defecto de record
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> ExpenseId.of(null));

        assertEquals(VoAccountingError.ERR_EXPENSE_ID_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        ExpenseId id1 = ExpenseId.of(5L);
        ExpenseId id2 = ExpenseId.of(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        ExpenseId id1 = ExpenseId.of(5L);
        ExpenseId id2 = ExpenseId.of(6L);

        assertNotEquals(id1, id2);
    }
}

