package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.JournalEntryError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
//import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;


/**
 * Representa una línea individual dentro de un asiento accounting.
 * Cada línea afecta a una cuenta específica con un débito o crédito.
 */
public final class JournalEntryLine {

    private final LedgerAccountId ledgerAccountId;
    private final ThirdPartiesId thirdPartiesId;
    private final String description;
    private final Price amount;
    private final boolean isDebit;
    private final String documentReference;

    private JournalEntryLine(
            LedgerAccountId ledgerAccountId,
            ThirdPartiesId thirdPartiesId,
            String description,
            Price amount,
            boolean isDebit,
            String documentReference) {


        this.ledgerAccountId = ledgerAccountId;
        this.thirdPartiesId = thirdPartiesId;
        this.description = description.trim();
        this.amount = amount;
        this.isDebit = isDebit;
        this.documentReference = documentReference;
    }

    
    
    
    
    
    public static JournalEntryLine of(
        LedgerAccountId ledgerAccountId,
        ThirdPartiesId thirdPartiesId,
        String description,
        Price amount,
        boolean isDebit,
        String documentReference
) {
    return new JournalEntryLine(
            ledgerAccountId,
            thirdPartiesId,
            description,
            amount,
            isDebit,
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
            String description
            ) {


        if (description == null || description.isBlank()) {
            throw new DomainAggregateException(JournalEntryError.ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD, EntityContext.JOURNALENTRY);
        }
       
    }

   

    public LedgerAccountId getLedgerAccountId() { return ledgerAccountId; }
    public ThirdPartiesId getThirdPartiesId() { return thirdPartiesId; }
    public String getDescription() { return description; }
    public Price getAmount() { return amount; }
    public String getDocumentReference() { return documentReference; }


}