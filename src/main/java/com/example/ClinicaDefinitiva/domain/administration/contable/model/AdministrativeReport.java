package com.example.ClinicaDefinitiva.domain.administration.contable.model;

import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.*;
import com.example.ClinicaDefinitiva.domain.identity.valueObjectes.UserId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa un reporte administrativo que consolida información financiera y operativa.
 * Gestiona referencias a asientos contables, indicadores y documentos adjuntos.
 */
public final class AdministrativeReport {

    private AdministrativeReportId id;
    private Name title;
    private Period period;
    private LocalDateTime createdAt;
    private UserId createdBy;
    private ReportStatus status;
    private List<JournalEntryId> journalEntryReferences; // Referencias a asientos contables
    private List<Indicator> indicators;
    private String notes;
    private List<Document> attachments;
    private LocalDateTime lastUpdate;
    private UserId approvedBy;

    private AdministrativeReport(
            AdministrativeReportId id,
            Name title,
            Period period,
            LocalDateTime createdAt,
            UserId createdBy,
            ReportStatus status,
            List<JournalEntryId> journalEntryReferences,
            List<Indicator> indicators,
            String notes,
            List<Document> attachments,
            LocalDateTime lastUpdate,
            UserId approvedBy) {

        this.id = id;
        this.title = title;
        this.period = period;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.createdBy = createdBy;
        this.status = status != null ? status : ReportStatus.draft();
        this.journalEntryReferences = journalEntryReferences != null ? new ArrayList<>(journalEntryReferences) : new ArrayList<>();
        this.indicators = indicators != null ? new ArrayList<>(indicators) : new ArrayList<>();
        this.notes = notes;
        this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
        this.lastUpdate = lastUpdate != null ? lastUpdate : LocalDateTime.now();
        this.approvedBy = approvedBy;
    }

    /**
     * Factory method para crear un nuevo reporte administrativo.
     */
    public static AdministrativeReport create(
            Name title,
            Period period,
            UserId createdBy) {

        return new AdministrativeReport(
                null,
                title,
                period,
                LocalDateTime.now(),
                createdBy,
                ReportStatus.draft(),
                new ArrayList<>(),
                new ArrayList<>(),
                null,
                new ArrayList<>(),
                LocalDateTime.now(),
                null
        );
    }

    /**
     * Agrega una referencia a un asiento contable.
     */
    public void addJournalEntryReference(JournalEntryId journalEntryId) {
        ensureEditable();
        Objects.requireNonNull(journalEntryId, "La referencia al asiento contable no puede ser nula");

        if (this.journalEntryReferences.contains(journalEntryId)) {
            throw new InvalidAdministrativeReportException("El asiento contable ya está referenciado en el reporte");
        }

        this.journalEntryReferences.add(journalEntryId);
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Remueve una referencia a un asiento contable.
     */
    public void removeJournalEntryReference(JournalEntryId journalEntryId) {
        ensureEditable();
        Objects.requireNonNull(journalEntryId, "La referencia al asiento contable no puede ser nula");

        if (!this.journalEntryReferences.remove(journalEntryId)) {
            throw new InvalidAdministrativeReportException("El asiento contable no está referenciado en el reporte");
        }
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Agrega un indicador al reporte.
     */
    public void addIndicator(Indicator indicator) {
        ensureEditable();
        Objects.requireNonNull(indicator, "El indicador no puede ser nulo");

        this.indicators.add(indicator);
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Remueve un indicador del reporte.
     */
    public void removeIndicator(Indicator indicator) {
        ensureEditable();
        Objects.requireNonNull(indicator, "El indicador no puede ser nulo");

        if (!this.indicators.remove(indicator)) {
            throw new InvalidAdministrativeReportException("El indicador no existe en el reporte");
        }
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Agrega un documento adjunto al reporte.
     */
    public void addAttachment(Document document) {
        ensureNotArchived();
        Objects.requireNonNull(document, "El documento no puede ser nulo");

        this.attachments.add(document);
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Remueve un documento adjunto del reporte.
     */
    public void removeAttachment(Document document) {
        ensureNotArchived();
        Objects.requireNonNull(document, "El documento no puede ser nulo");

        if (!this.attachments.remove(document)) {
            throw new InvalidAdministrativeReportException("El documento no existe en el reporte");
        }
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Actualiza el título y notas del reporte.
     */
    public void updateInformation(Name title, String notes) {
        ensureEditable();

        this.title = title;
        this.notes = notes != null ? notes.trim() : null;
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Envía el reporte para revisión.
     */
    public void submitForReview() {
        if (!this.status.canBeSubmittedForReview()) {
            throw new InvalidReportStatusException(
                    "Solo se pueden enviar reportes en estado borrador"
            );
        }

        validateReportCompleteness();

        this.status = ReportStatus.underReview();
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Aprueba y publica el reporte.
     */
    public void approve(UserId approver) {
        if (!this.status.canBeApproved()) {
            throw new InvalidReportStatusException(
                    "Solo se pueden aprobar reportes en revisión"
            );
        }

        Objects.requireNonNull(approver, "El aprobador es obligatorio");

        this.status = ReportStatus.published();
        this.approvedBy = approver;
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Rechaza el reporte y lo devuelve a borrador.
     */
    public void reject(String reason) {
        if (!this.status.canBeRejected()) {
            throw new InvalidReportStatusException(
                    "Solo se pueden rechazar reportes en revisión"
            );
        }

        if (reason == null || reason.isBlank()) {
            throw new InvalidAdministrativeReportException(
                    "Se requiere una razón para rechazar el reporte"
            );
        }

        this.status = ReportStatus.draft();
        this.notes = (this.notes != null ? this.notes + "\n\n" : "") +
                "RECHAZADO: " + reason;
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Archiva el reporte. Los reportes archivados no pueden modificarse.
     */
    public void archive() {
        if (!this.status.canBeArchived()) {
            throw new InvalidReportStatusException("El reporte ya está archivado");
        }

        this.status = ReportStatus.archived();
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Restaura un reporte archivado.
     */
    public void unarchive() {
        if (!this.status.isArchived()) {
            throw new InvalidReportStatusException("Solo se pueden desarchivar reportes archivados");
        }

        this.status = ReportStatus.draft();
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Verifica si el reporte está completo y listo para revisión.
     */
    public boolean isComplete() {
        return !journalEntryReferences.isEmpty() || !indicators.isEmpty();
    }

    /**
     * Verifica si el reporte puede ser editado.
     */
    public boolean isEditable() {
        return this.status.isEditable();
    }

    /**
     * Verifica si el reporte está publicado.
     */
    public boolean isPublished() {
        return this.status.isPublished();
    }

    /**
     * Obtiene las listas de forma inmutable.
     */
    public List<JournalEntryId> getJournalEntryReferences() {
        return Collections.unmodifiableList(this.journalEntryReferences);
    }

    public List<Indicator> getIndicators() {
        return Collections.unmodifiableList(this.indicators);
    }

    public List<Document> getAttachments() {
        return Collections.unmodifiableList(this.attachments);
    }

    /**
     * Obtiene el número total de elementos en el reporte.
     */
    public int getTotalItemsCount() {
        return journalEntryReferences.size() + indicators.size();
    }

    /**
     * Verifica si el reporte corresponde al período actual.
     */
    public boolean isCurrentPeriod() {
        return period.isCurrentPeriod();
    }

    /**
     * Verifica si el reporte es de un período pasado.
     */
    public boolean isPastPeriod() {
        return period.isPastPeriod();
    }

    private void ensureEditable() {
        if (!isEditable()) {
            throw new InvalidReportStatusException(
                    "No se puede modificar el reporte en estado " + this.status
            );
        }
    }

    private void ensureNotArchived() {
        if (this.status.isArchived()) {
            throw new InvalidReportStatusException(
                    "No se puede modificar un reporte archivado"
            );
        }
    }

    private void validateReportCompleteness() {
        if (!isComplete()) {
            throw new InvalidAdministrativeReportException(
                    "El reporte debe tener al menos un asiento contable o un indicador"
            );
        }
    }

    // Getters
    public AdministrativeReportId getId() { return id; }
    public Name getTitle() { return title; }
    public Period getPeriod() { return period; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public UserId getCreatedBy() { return createdBy; }
    public ReportStatus getStatus() { return status; }
    public String getNotes() { return notes; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public UserId getApprovedBy() { return approvedBy; }

    // Setters para infraestructura
    public void setId(AdministrativeReportId id) { this.id = id; }
}