package com.example.ClinicaDefinitiva.domain.administration.contable.model;

import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.OpeningBalanceId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.Money;

import java.time.LocalDate;

public class OpeningBalance { // saldo inicial
    private final OpeningBalanceId openingBalanceId;
    private final ThirdPartiesId companyId;
    private final LedgerAccountId cuentaId;
    private final ThirdPartiesId thirdPartiesId; // opcional
    private final Money amount;
    private final LocalDate date;


    public OpeningBalance(OpeningBalanceId openingBalanceId, ThirdPartiesId companyId, LedgerAccountId cuentaId, ThirdPartiesId thirdPartiesId, Money amount, LocalDate date) {

        validateMandatoryFields(amount, date);
        this.openingBalanceId = openingBalanceId;
        this.companyId = companyId;
        this.cuentaId = cuentaId;
        this.thirdPartiesId = thirdPartiesId;
        this.amount = amount;
        this.date = date;
    }

    public static OpeningBalance registerOpeningBalance(
            OpeningBalanceId openingBalanceId,
            ThirdPartiesId companyId,
            LedgerAccountId cuentaId,
            ThirdPartiesId thirdPartiesId,
            Money amount
            ) {

        return new OpeningBalance(
                openingBalanceId,
                companyId,
                cuentaId,
                thirdPartiesId,
                amount,
                LocalDate.now()
                );



    }

  private void validateMandatoryFields(Money amount, LocalDate date) {
    if (amount.isNegativeOrZero()) {
        throw new InvalidJournalEntryException("El monto debe ser mayor que cero");
    }
    if (amount == null) {
        throw new InvalidJournalEntryException("El monto es obligatorio");
    }
      if (date == null) {
          throw new InvalidJournalEntryException("La fecha es obligatoria");
      }
  }

    public ThirdPartiesId getCompanyId() {
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

    public Money getValor() {
        return amount;
    }
}
