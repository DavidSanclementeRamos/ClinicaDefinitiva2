package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import java.util.Objects;

public final class ReportStatus {

    public enum Status {
        DRAFT("Borrador"),
        UNDER_REVIEW("En Revisión"),
        PUBLISHED("Publicado"),
        ARCHIVED("Archivado");

        private final String description;
        Status(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    private final Status value;

    private ReportStatus(Status value) {
        if (value == null) {
            throw new ValueObjectValidationException(
                VoAccountingError.ERR_REPORT_STATUS_NULL,
                VOContext.ACCOUNTING
            );
        }
        this.value = value;
    }

    public static ReportStatus of(Status status) { return new ReportStatus(status); }
    public static ReportStatus draft() { return new ReportStatus(Status.DRAFT); }
    public static ReportStatus underReview() { return new ReportStatus(Status.UNDER_REVIEW); }
    public static ReportStatus published() { return new ReportStatus(Status.PUBLISHED); }
    public static ReportStatus archived() { return new ReportStatus(Status.ARCHIVED); }

    // Queries semánticas
    public boolean isDraft() { return value == Status.DRAFT; }
    public boolean isUnderReview() { return value == Status.UNDER_REVIEW; }
    public boolean isPublished() { return value == Status.PUBLISHED; }
    public boolean isArchived() { return value == Status.ARCHIVED; }

    // Reglas de negocio
    public boolean isEditable() { return isDraft(); }
    public boolean canBeSubmittedForReview() { return isDraft(); }
    public boolean canBeApproved() { return isUnderReview(); }
    public boolean canBeRejected() { return isUnderReview(); }
    public boolean canBeArchived() { return !isArchived(); }

    public String getDescription() { return value.getDescription(); }
    public Status getValue() { return value; }

    @Override
    public String toString() { return value.name(); }
    
    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReportStatus)) return false;
    ReportStatus other = (ReportStatus) o;
    return value == other.value;
}

@Override
public int hashCode() {
    return Objects.hash(value);
}

}