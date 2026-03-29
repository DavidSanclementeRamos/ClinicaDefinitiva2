package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.AdministrativeReportError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;

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

    private final AdministrativeReportId id;
    private Name title;
    private final Period period;
    private final LocalDateTime createdAt;
    private final UserIdentityId createdBy;
    private ReportStatus status;
    private final List<JournalEntryId> journalEntryReferences;
    private final List<Indicator> indicators;
    private String notes;
    private final List<Document> attachments;
    private LocalDateTime lastUpdate;
    private UserIdentityId approvedBy;

    private AdministrativeReport(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.period = builder.period;
        this.createdBy = builder.createdBy;

        this.createdAt =  LocalDateTime.now();
        this.status = builder.status = ReportStatus.draft();
        this.journalEntryReferences = builder.journalEntryReferences;
        this.indicators = builder.indicators;
        this.notes = builder.notes;
        this.attachments = builder.attachments; 
        this.lastUpdate = LocalDateTime.now();
        this.approvedBy = builder.approvedBy;
    }

    public static AdministrativeReport create(Name title, Period period, UserIdentityId createdBy) {
        return AdministrativeReport.builder()
                .withTitle(title)
                .withPeriod(period)
                .withCreatedBy(createdBy)
                .withCreatedAt(LocalDateTime.now())
                .withStatus(ReportStatus.draft())
                .withJournalEntryReferences(new ArrayList<>())
                .withIndicators(new ArrayList<>())
                .withAttachments(new ArrayList<>())
                .withLastUpdate(LocalDateTime.now())
                .build();
    }

    public void addJournalEntryReference(JournalEntryId journalEntryId) {
        ensureEditable();
        if (this.journalEntryReferences.contains(journalEntryId)) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_DUPLICATE_JOURNAL_ENTRY, EntityContext.ADMINISTRATIVEREPORT);
        }
        this.journalEntryReferences.add(journalEntryId);
        this.lastUpdate = LocalDateTime.now();
    }

    public void removeJournalEntryReference(JournalEntryId journalEntryId) {
        ensureEditable();
        if (!this.journalEntryReferences.remove(journalEntryId)) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND, EntityContext.ADMINISTRATIVEREPORT);
        }
        this.lastUpdate = LocalDateTime.now();
    }

    public void addIndicator(Indicator indicator) {
        ensureEditable();
        if (!indicators.isEmpty()) {
            throw new DomainAggregateException(AdministrativeReportError.ERR_REPORT_TOO_MANY_INDICATORS, EntityContext.ADMINISTRATIVEREPORT);
        }
        this.indicators.add(indicator);
        this.lastUpdate = LocalDateTime.now();
    }

    public void removeIndicator(Indicator indicator) {
        ensureEditable();
        if (!this.indicators.remove(indicator)) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_INDICATOR_NOT_FOUND, EntityContext.ADMINISTRATIVEREPORT);
        }
        this.lastUpdate = LocalDateTime.now();
    }

    public void addAttachment(Document document) {
        ensureNotArchived();
        Objects.requireNonNull(document, "El documento no puede ser nulo");
        this.attachments.add(document);
        this.lastUpdate = LocalDateTime.now();
    }

    public void removeAttachment(Document document) {
        ensureNotArchived();
        if (!this.attachments.remove(document)) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_ATTACHMENT_NOT_FOUND, EntityContext.ADMINISTRATIVEREPORT);
        }
        this.lastUpdate = LocalDateTime.now();
    }

    public void updateInformation(Name title, String notes) {
        ensureEditable();
        this.title = title;
        this.notes = notes != null ? notes.trim() : null;
        this.lastUpdate = LocalDateTime.now();
    }

    public void submitForReview() {
        if (!this.status.canBeSubmittedForReview()) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_CANNOT_SUBMIT, EntityContext.ADMINISTRATIVEREPORT);
        }
        validateReportCompleteness();
        this.status = ReportStatus.underReview();
        this.lastUpdate = LocalDateTime.now();
    }

    public void approve(UserIdentityId approver) {
        if (!this.status.canBeApproved()) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_CANNOT_APPROVE, EntityContext.ADMINISTRATIVEREPORT);
        }
        Objects.requireNonNull(approver, "El aprobador es obligatorio");
        this.status = ReportStatus.published();
        this.approvedBy = approver;
        this.lastUpdate = LocalDateTime.now();
    }

    public void reject(String reason) {
        if (!this.status.canBeRejected()) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_CANNOT_REJECT, EntityContext.ADMINISTRATIVEREPORT);
        }
        if (reason == null || reason.isBlank()) {
            throw new DomainAggregateException(AdministrativeReportError.ERR_REPORT_REJECTION_REQUIRES_REASON, EntityContext.ADMINISTRATIVEREPORT);
        }
        this.status = ReportStatus.draft();
        this.notes = (this.notes != null ? this.notes + "\n\n" : "") + "RECHAZADO: " + reason;
        this.lastUpdate = LocalDateTime.now();
    }

    public void archive() {
        if (!this.status.canBeArchived()) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_CANNOT_ARCHIVE, EntityContext.ADMINISTRATIVEREPORT);
        }
        this.status = ReportStatus.archived();
        this.lastUpdate = LocalDateTime.now();
    }

    public void unarchive() {
        if (!this.status.isArchived()) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_CANNOT_UNARCHIVE, EntityContext.ADMINISTRATIVEREPORT);
        }
        this.status = ReportStatus.draft();
        this.lastUpdate = LocalDateTime.now();
    }

    public boolean isComplete() {
        return !journalEntryReferences.isEmpty() || !indicators.isEmpty();
    }

    public boolean isEditable() {
        return this.status.isEditable();
    }

    public boolean isPublished() {
        return this.status.isPublished();
    }

    public List<JournalEntryId> getJournalEntryReferences() {
        return Collections.unmodifiableList(this.journalEntryReferences);
    }

    public List<Indicator> getIndicators() {
        return Collections.unmodifiableList(this.indicators);
    }

    public List<Document> getAttachments() {
        return Collections.unmodifiableList(this.attachments);
    }

    public int getTotalItemsCount() {
        return journalEntryReferences.size() + indicators.size();
    }

    public boolean isCurrentPeriod() {
        return period.isCurrentPeriod();
    }

    public boolean isPastPeriod() {
        return period.isPastPeriod();
    }

    private void ensureEditable() {
        if (!isEditable()) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_NOT_EDITABLE, EntityContext.ADMINISTRATIVEREPORT);
        }
    }

    private void ensureNotArchived() {
        if (this.status.isArchived()) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_CANNOT_UNARCHIVE, EntityContext.ADMINISTRATIVEREPORT);
        }
    }

    private void validateReportCompleteness() {
        if (!isComplete()) {
            throw new BusinessRuleViolationException(AdministrativeReportError.ERR_REPORT_MISSING_APPROVER, EntityContext.ADMINISTRATIVEREPORT);
        }
    }

    // -------- Getters --------
    public AdministrativeReportId getId() { return id; }
    public Name getTitle() { return title; }
    public Period getPeriod() { return period; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public UserIdentityId getCreatedBy() { return createdBy; }
    public ReportStatus getStatus() { return status; }
    public String getNotes() { return notes; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public UserIdentityId getApprovedBy() { return approvedBy; }
    
    public static Builder builder() { return new Builder(); }

        public static class Builder {
        private AdministrativeReportId id;
        private Name title;
        private Period period;
        private LocalDateTime createdAt;
        private UserIdentityId createdBy;
        private ReportStatus status;
        private List<JournalEntryId> journalEntryReferences;
        private List<Indicator> indicators;
        private String notes;
        private List<Document> attachments;
        private LocalDateTime lastUpdate;
        private UserIdentityId approvedBy;

        public Builder withId(AdministrativeReportId id) { this.id = id; return this; }
        public Builder withTitle(Name title) { this.title = title; return this; }
        public Builder withPeriod(Period period) { this.period = period; return this; }
        public Builder withCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder withCreatedBy(UserIdentityId createdBy) { this.createdBy = createdBy; return this; }
        public Builder withStatus(ReportStatus status) { this.status = status; return this; }
        public Builder withJournalEntryReferences(List<JournalEntryId> journalEntryReferences) { this.journalEntryReferences = journalEntryReferences; return this; }
        public Builder withIndicators(List<Indicator> indicators) { this.indicators = indicators; return this; }
        public Builder withNotes(String notes) { this.notes = notes; return this; }
        public Builder withAttachments(List<Document> attachments) { this.attachments = attachments; return this; }
        public Builder withLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; return this; }
        public Builder withApprovedBy(UserIdentityId approvedBy) { this.approvedBy = approvedBy; return this; }

        public AdministrativeReport build() {
            return new AdministrativeReport(this);
        }
    }
}
