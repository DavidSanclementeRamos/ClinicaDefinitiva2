package com.example.ClinicaDefinitiva.domain.administration.contable.model;

import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa un asiento contable (movimiento contable) en el sistema.
 * Implementa el principio de partida doble: débitos = créditos.
 */
public final class JournalEntry {

    private JournalEntryId id;
    private CompanyId companyId;
    private LocalDate date;
    private String documentNumber;
    private String description;
    private List<JournalEntryLine> lines;
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
     * Factory method para registrar un nuevo asiento contable.
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
     * Agrega una línea al asiento contable.
     * Solo permite agregar líneas si el asiento no está contabilizado.
     */
    public void addLine(JournalEntryLine line) {
        ensureNotPosted();
        Objects.requireNonNull(line, "La línea no puede ser nula");

        this.lines.add(line);
        this.balanced = false; // Marcar como desbalanceado hasta validar
    }

    /**
     * Remueve una línea del asiento contable.
     */
    public void removeLine(JournalEntryLine line) {
        ensureNotPosted();
        Objects.requireNonNull(line, "La línea no puede ser nula");

        if (!this.lines.remove(line)) {
            throw new InvalidJournalEntryException("La línea no existe en el asiento");
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
            throw new InvalidJournalEntryException("El asiento debe tener al menos una línea");
        }

        if (this.lines.size() < 2) {
            throw new InvalidJournalEntryException(
                    "El asiento debe tener al menos dos líneas (partida doble)"
            );
        }

        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        for (JournalEntryLine line : this.lines) {
            if (line.isDebit()) {
                totalDebits = totalDebits.add(line.getAmount().getAmount());
            } else {
                totalCredits = totalCredits.add(line.getAmount().getAmount());
            }
        }

        if (totalDebits.compareTo(totalCredits) != 0) {
            throw new InvalidJournalEntryException(
                    String.format("El asiento no está balanceado. Débitos: %s, Créditos: %s",
                            totalDebits, totalCredits)
            );
        }

        this.balanced = true;
    }

    /**
     * Contabiliza el asiento. Una vez contabilizado, no puede modificarse.
     */
    public void post() {
        if (this.posted) {
            throw new InvalidJournalEntryException("El asiento ya está contabilizado");
        }

        if (!this.balanced) {
            validateBalance();
        }

        if (this.date.isAfter(LocalDate.now())) {
            throw new InvalidJournalEntryException("No se puede contabilizar un asiento con fecha futura");
        }

        this.posted = true;
    }

    /**
     * Reversa el asiento contable creando un asiento de ajuste.
     * El asiento original permanece pero se marca como reversado.
     */
    public JournalEntry reverse(String reason) {
        if (!this.posted) {
            throw new InvalidJournalEntryException("Solo se pueden reversar asientos contabilizados");
        }

        if (reason == null || reason.isBlank()) {
            throw new InvalidJournalEntryException("Se requiere una razón para reversar el asiento");
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
    public Money getTotalDebits() {
        BigDecimal total = this.lines.stream()
                .filter(JournalEntryLine::isDebit)
                .map(line -> line.getAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Money.of(total, "COP");
    }

    /**
     * Calcula el total de créditos del asiento.
     */
    public Money getTotalCredits() {
        BigDecimal total = this.lines.stream()
                .filter(JournalEntryLine::isCredit)
                .map(line -> line.getAmount().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Money.of(total, "COP");
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
            throw new InvalidJournalEntryException(
                    "No se puede modificar un asiento ya contabilizado"
            );
        }
    }

    private void validateMandatoryFields(
            LocalDate date,
            String documentNumber,
            String description) {


        if (date == null) {
            throw new InvalidJournalEntryException("La fecha es obligatoria");
        }
        validateDocumentNumber(documentNumber);
        validateDescription(description);
    }

    private void validateDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new InvalidJournalEntryException("El número de documento es obligatorio");
        }
        if (documentNumber.trim().length() < 1) {
            throw new InvalidJournalEntryException("El número de documento debe tener al menos 1 carácter");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidJournalEntryException("La descripción es obligatoria");
        }
        if (description.trim().length() < 5) {
            throw new InvalidJournalEntryException("La descripción debe tener al menos 5 caracteres");
        }
    }

    // Getters
    public JournalEntryId getId() { return id; }
    public CompanyId getCompanyId() { return companyId; }
    public LocalDate getDate() { return date; }
    public String getDocumentNumber() { return documentNumber; }
    public String getDescription() { return description; }
    public boolean isBalanced() { return balanced; }
    public boolean isPosted() { return posted; }

    // Setters para infraestructura
    public void setId(JournalEntryId id) { this.id = id; }
}