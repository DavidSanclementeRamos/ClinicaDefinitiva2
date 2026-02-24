
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.vo;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContractIdTest {

    
    @Test
    void shouldCreateValidContractId() {
    ContractId id = ContractId.of(10L);
    assertEquals(10L, id.getValue());
    assertEquals("ContractId[getValue=10]", id.toString()); 
}



   

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> ContractId.of(null));

        assertEquals(VoAccountingError.ERR_CONTRACT_ID_NULL, ex.getCatalogo());
        assertEquals(VOContext.ACCOUNTING, ex.getContexto());
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        ContractId id1 = ContractId.of(5L);
        ContractId id2 = ContractId.of(5L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        ContractId id1 = ContractId.of(5L);
        ContractId id2 = ContractId.of(6L);

        assertNotEquals(id1, id2);
    }
}

