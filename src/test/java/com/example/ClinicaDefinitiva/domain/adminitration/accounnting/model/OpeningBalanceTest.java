
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Currency;
import static org.junit.jupiter.api.Assertions.*;

class OpeningBalanceTest {

    @Test
    void shouldRegisterOpeningBalanceSuccessfully() {
        OpeningBalance balance = OpeningBalance.registerOpeningBalance(
                CompanyId.of(1L),
                LedgerAccountId.of(1105L),
                ThirdPartiesId.of(99L),
                Price.of(1000, Currency.getInstance("COP"))
        );

        assertEquals(CompanyId.of(1L), balance.getCompanyId());
        assertEquals(LedgerAccountId.of(1105L), balance.getCuentaId());
        assertEquals(ThirdPartiesId.of(99L), balance.getThirdPartiesId());
        assertEquals(1000, balance.getValor().asBigDecimal().intValue());
        assertEquals(LocalDate.now(), balance.getFecha());
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        assertThrows(BusinessRuleViolationException.class, () ->
            OpeningBalance.registerOpeningBalance(
                    CompanyId.of(1L),
                    LedgerAccountId.of(1105L),
                    null,
                    Price.of(0, Currency.getInstance("COP"))
            )
        );
    }

    @Test
    void shouldThrowExceptionForMissingAmount() {
        assertThrows(DomainAggregateException.class, () ->
             OpeningBalance.registerOpeningBalance(
                     
                    CompanyId.of(1L),
                    LedgerAccountId.of(1105L),
                    null,
                    null
                    
                     
             )
                            
            
        );
    }

  
}

