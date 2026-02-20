package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.Price;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.JournalEntryError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.TemporalValidationException;
//import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa un asiento accounting (movimiento accounting) en el sistema.
 * Implementa el principio de partida doble: débitos = créditos.
 */
public final class JournalEntry {

    private final JournalEntryId id;
    private final CompanyId companyId;
    private final LocalDate date;
    private String documentNumber;
    private String description;
    private final List<JournalEntryLine> lines;
    private boolean balanced;
    private boolean posted;

    private JournalEntry(
            JournalEntryId id,
            CompanyId companyId,
            LocalDate date,
            String documentNumber,
            String description,
            List<JournalEntryLine> lines
            ) {

        validateMandatoryFields(date, documentNumber, description);

        this.id = id;
        this.companyId = companyId;
        this.date = date;
        this.documentNumber = documentNumber.trim();
        this.description = description.trim();
        this.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
        this.balanced = false;
        this.posted = false;
    }

    /**
     * Factory method para registrar un nuevo asiento accounting.
     */
    public static JournalEntry registerJournalEntry(
            CompanyId companyId,
            LocalDate date,
            String documentNumber,
            String description,
            List<JournalEntryLine> lines) {

        JournalEntry entry = new JournalEntry(
                null,
                companyId,
                date,
                documentNumber,
                description,
                lines

        );

        // Validar balance automáticamente
        entry.validateBalance();

        return entry;
    }

    /**
     * Agrega una línea al asiento accounting.
     * Solo permite agregar líneas si el asiento no está contabilizado.
     */
    public void addLine(JournalEntryLine line) {
        ensureNotPosted();
        Objects.requireNonNull(line, "La línea no puede ser nula");

        this.lines.add(line);
        this.balanced = false; // Marcar como desbalanceado hasta validar
    }

    /**
     * Remueve una línea del asiento accounting.
     */
    public void removeLine(JournalEntryLine line) {
        ensureNotPosted();
        Objects.requireNonNull(line, "La línea no puede ser nula");

        if (!this.lines.remove(line)) {
            throw new DomainAggregateException(JournalEntryError.ERR_JOURNALENTRY_LINE_NOT_FOUND, EntityContext.JOURNALENTRY);
        }
        this.balanced = false;
    }

    /**
     * Actualiza la información general del asiento.
     */
    public void updateInformation(String description, String documentNumber) {
        ensureNotPosted();
        validateDescription(description);
        validateDocumentNumber(documentNumber);

        this.description = description.trim();
        this.documentNumber = documentNumber.trim();
    }

    /**
     * Valida que el asiento esté balanceado (débitos = créditos).
     */
    public void validateBalance() {
        if (this.lines.isEmpty()) {
            throw new BusinessRuleViolationException(JournalEntryError.ERR_JOURNALENTRY_EMPTY, EntityContext.JOURNALENTRY);
        }

        if (this.lines.size() < 2) {
            throw new BusinessRuleViolationException(JournalEntryError.ERR_JOURNALENTRY_INSUFFICIENT_LINES, EntityContext.JOURNALENTRY);
        }

        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        for (JournalEntryLine line : this.lines) {
            if (line.isDebit()) {
                totalDebits = totalDebits.add(line.getAmount().asBigDecimal());
            } else {
                totalCredits = totalCredits.add(line.getAmount().asBigDecimal());
            }
        }

        if (totalDebits.compareTo(totalCredits) != 0) {
            throw new BusinessRuleViolationException(JournalEntryError.ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH, EntityContext.JOURNALENTRY);
        }

        this.balanced = true;
    }

    /**
     * Contabiliza el asiento. Una vez contabilizado, no puede modificarse.
     */
    public void post() {
        if (this.posted) {
            throw new BusinessRuleViolationException(JournalEntryError.ERR_JOURNALENTRY_ALREADY_POSTED, EntityContext.JOURNALENTRY);
        }

        if (!this.balanced) {
            validateBalance();
        }

        if (this.date.isAfter(LocalDate.now())) {
            throw new TemporalValidationException(JournalEntryError.ERR_JOURNALENTRY_FUTURE_DATE, EntityContext.JOURNALENTRY);
        }

        this.posted = true;
    }

    /**
     * Reversa el asiento accounting creando un asiento de ajuste.
     * El asiento original permanece pero se marca como reversado.
     */
    public JournalEntry reverse(String reason) {
        if (!this.posted) {
            throw new BusinessRuleViolationException(JournalEntryError.ERR_JOURNALENTRY_NOT_POSTED_REVERSAL, EntityContext.JOURNALENTRY);
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleViolationException(JournalEntryError.ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON, EntityContext.JOURNALENTRY);
        }

        // Crear líneas reversas (invertir débitos y créditos)
        List<JournalEntryLine> reversedLines = new ArrayList<>();
        for (JournalEntryLine line : this.lines) {
            reversedLines.add(line.reverse());
        }

        return JournalEntry.registerJournalEntry(
                this.companyId,
                LocalDate.now(),
                this.documentNumber + "-REV",
                "REVERSA: " + reason + " - " + this.description,
                reversedLines
        );
    }

    /**
     * Calcula el total de débitos del asiento.
     */
    public Price getTotalDebits() {
        BigDecimal total = this.lines.stream()
                .filter(JournalEntryLine::isDebit)
                .map(line -> line.getAmount().asBigDecimal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return null;
                //Price.of(total, "COP");
    }

    /**
     * Calcula el total de créditos del asiento.
     */
    public Price getTotalCredits() {
        BigDecimal total = this.lines.stream()
                .filter(JournalEntryLine::isCredit)
                .map(line -> line.getAmount().asBigDecimal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return null; //Price.of(total, "COP");
    }

    /**
     * Obtiene las líneas del asiento de forma inmutable.
     */
    public List<JournalEntryLine> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    /**
     * Verifica si el asiento afecta a un tercero específico.
     */
    public boolean affectsThirdParty(ThirdPartiesId thirdPartyId) {
        return this.lines.stream()
                .anyMatch(line -> line.getThirdPartiesId() != null &&
                        line.getThirdPartiesId().equals(thirdPartyId));
    }

    /**
     * Verifica si el asiento afecta a una cuenta específica.
     */
    public boolean affectsAccount(LedgerAccountId accountId) {
        return this.lines.stream()
                .anyMatch(line -> line.getLedgerAccountId().equals(accountId));
    }

    /**
     * Obtiene el número total de líneas.
     */
    public int getLineCount() {
        return this.lines.size();
    }

    private void ensureNotPosted() {
        if (this.posted) {
            throw new BusinessRuleViolationException(JournalEntryError.ERR_JOURNALENTRY_NOT_EDITABLE, EntityContext.JOURNALENTRY);
        }
    }

    private void validateMandatoryFields(
            LocalDate date,
            String documentNumber,
            String description) {


        if (date == null) {
            throw new DomainAggregateException(JournalEntryError.ERR_JOURNALENTRY_MISSING_DATE, EntityContext.JOURNALENTRY);
        }
        validateDocumentNumber(documentNumber);
        validateDescription(description);
    }

    private void validateDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new DomainAggregateException(JournalEntryError.ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER, EntityContext.JOURNALENTRY);
        }
        if (documentNumber.trim().isEmpty()) {
            throw new DomainAggregateException(JournalEntryError.ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER, EntityContext.JOURNALENTRY);
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new DomainAggregateException(JournalEntryError.ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD, EntityContext.JOURNALENTRY);
        }
        if (description.trim().length() < 5) {
            throw new DomainAggregateException(JournalEntryError.ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH, EntityContext.JOURNALENTRY);
        }
    }

    public JournalEntryId getId() { return id; }
    public CompanyId getCompanyId() { return companyId; }
    public LocalDate getDate() { return date; }
    public String getDocumentNumber() { return documentNumber; }
    public String getDescription() { return description; }
    public boolean isBalanced() { return balanced; }
    public boolean isPosted() { return posted; }

}