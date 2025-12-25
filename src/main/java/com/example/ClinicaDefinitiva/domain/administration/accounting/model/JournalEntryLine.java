package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.Money;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;


/**
 * Representa una línea individual dentro de un asiento accounting.
 * Cada línea afecta a una cuenta específica con un débito o crédito.
 */
public final class JournalEntryLine {

    private LedgerAccountId ledgerAccountId;
    private ThirdPartiesId thirdPartiesId;
    private String description;
    private Money amount;
    private boolean isDebit;
    private String documentReference;

    private JournalEntryLine(
            LedgerAccountId ledgerAccountId,
            ThirdPartiesId thirdPartiesId,
            String description,
            Money amount,
            boolean isDebit,
            String documentReference) {

        validateMandatoryFields(description, amount);
        validateAmount(amount);

        this.ledgerAccountId = ledgerAccountId;
        this.thirdPartiesId = thirdPartiesId;
        this.description = description.trim();
        this.amount = amount;
        this.isDebit = isDebit;
        this.documentReference = documentReference != null ? documentReference.trim() : null;
    }

    /**
     * Factory method para crear una línea de débito.
     */
    public static JournalEntryLine debit(
            LedgerAccountId ledgerAccountId,
            String description,
            Money amount) {

        return new JournalEntryLine(
                ledgerAccountId,
                null,
                description,
                amount,
                true,
                null
        );
    }

    /**
     * Factory method para crear una línea de débito con tercero.
     */
    public static JournalEntryLine debitWithThirdParty(
            LedgerAccountId ledgerAccountId,
            ThirdPartiesId thirdPartiesId,
            String description,
            Money amount,
            String documentReference) {

        return new JournalEntryLine(
                ledgerAccountId,
                thirdPartiesId,
                description,
                amount,
                true,
                documentReference
        );
    }

    /**
     * Factory method para crear una línea de crédito.
     */
    public static JournalEntryLine credit(
            LedgerAccountId ledgerAccountId,
            String description,
            Money amount) {

        return new JournalEntryLine(
                ledgerAccountId,
                null,
                description,
                amount,
                false,
                null
        );
    }

    /**
     * Factory method para crear una línea de crédito con tercero.
     */
    public static JournalEntryLine creditWithThirdParty(
            LedgerAccountId ledgerAccountId,
            ThirdPartiesId thirdPartiesId,
            String description,
            Money amount,
            String documentReference) {

        return new JournalEntryLine(
                ledgerAccountId,
                thirdPartiesId,
                description,
                amount,
                false,
                documentReference
        );
    }

    /**
     * Crea una línea reversa (invierte débito/crédito).
     */
    public JournalEntryLine reverse() {
        return new JournalEntryLine(
                this.ledgerAccountId,
                this.thirdPartiesId,
                "REVERSA: " + this.description,
                this.amount,
                !this.isDebit,
                this.documentReference
        );
    }

    /**
     * Verifica si es un débito.
     */
    public boolean isDebit() {
        return this.isDebit;
    }

    /**
     * Verifica si es un crédito.
     */
    public boolean isCredit() {
        return !this.isDebit;
    }

    /**
     * Verifica si tiene un tercero asociado.
     */
    public boolean hasThirdParty() {
        return this.thirdPartiesId != null;
    }

    /**
     * Verifica si tiene una referencia de documento.
     */
    public boolean hasDocumentReference() {
        return this.documentReference != null && !this.documentReference.isBlank();
    }

    /**
     * Obtiene el tipo de movimiento como texto.
     */
    public String getMovementType() {
        return this.isDebit ? "DÉBITO" : "CRÉDITO";
    }

    private void validateMandatoryFields(
            String description,
            Money amount) {


        if (description == null || description.isBlank()) {
            throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD, EntityContext.JOURNALENTRY);
        }
        if (amount == null) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_JOURNALENTRY_MISSING_AMOUNT, EntityContext.JOURNALENTRY);
        }
    }

    private void validateAmount(Money amount) {
        if (amount.isNegativeOrZero()) {
            throw new BusinessRuleViolationException(ErrorCatalog.ERR_JOURNALENTRY_INVALID_AMOUNT, EntityContext.JOURNALENTRY);
        }
    }

    public LedgerAccountId getLedgerAccountId() { return ledgerAccountId; }
    public ThirdPartiesId getThirdPartiesId() { return thirdPartiesId; }
    public String getDescription() { return description; }
    public Money getAmount() { return amount; }
    public String getDocumentReference() { return documentReference; }


}