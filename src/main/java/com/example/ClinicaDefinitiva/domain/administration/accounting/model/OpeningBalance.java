package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.OpeningBalanceId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting.OpeningBalanceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
//import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;

import java.time.LocalDate;

public class OpeningBalance {
    private final OpeningBalanceId openingBalanceId;
    private final CompanyId companyId;
    private final LedgerAccountId cuentaId;
    private final ThirdPartiesId thirdPartiesId; // opcional
    private final Price amount;
    private final LocalDate date;


    private  OpeningBalance(OpeningBalanceId openingBalanceId, CompanyId companyId, LedgerAccountId cuentaId, ThirdPartiesId thirdPartiesId, Price amount, LocalDate date) {

        validateMandatoryFields(amount, date);
        this.openingBalanceId = openingBalanceId;
        this.companyId = companyId;
        this.cuentaId = cuentaId;
        this.thirdPartiesId = thirdPartiesId;
        this.amount = amount;
        this.date = date;
    }

    public static OpeningBalance registerOpeningBalance(
            CompanyId companyId,
            LedgerAccountId cuentaId,
            ThirdPartiesId thirdPartiesId,
            Price amount

            ) {
    
            

        return new OpeningBalance(
                null,
                companyId,
                cuentaId,
                thirdPartiesId,
                amount,
                LocalDate.now()
                );



    }

  private void validateMandatoryFields(Price amount, LocalDate date) {
    
    
      if (date == null) {
          throw new DomainAggregateException(OpeningBalanceError.ERR_OPENING_BALANCE_MISSING_DATE, EntityContext.OPENINGBALANCE);
      }
  }

    public CompanyId getCompanyId() {
        return companyId;
    }

    public LedgerAccountId getCuentaId() {
        return cuentaId;
    }

    public LocalDate getFecha() {
        return date;
    }

    public OpeningBalanceId getOpeningBalanceId() {
        return openingBalanceId;
    }

    public ThirdPartiesId getThirdPartiesId() {
        return thirdPartiesId;
    }

    public Price getValor() {
        return amount;
    }
}
