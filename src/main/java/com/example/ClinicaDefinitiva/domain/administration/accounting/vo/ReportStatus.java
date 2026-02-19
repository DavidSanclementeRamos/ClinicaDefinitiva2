package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class ReportStatus {



    public enum Status {
        DRAFT, UNDER_REVIEW, PUBLISHED, ARCHIVED
    }

    private final Status status;

    private ReportStatus(Status status) {
        if (status == null) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_REPORT_STATUS_NULL,
                    VOContext.ACCOUNTING
            );
        }
        this.status = status;
    }

    public static ReportStatus of(Status status) {
        return new ReportStatus(status);
    }

    public static ReportStatus draft() {
        return new ReportStatus(Status.DRAFT);
    }

    public static ReportStatus underReview() {
        return new ReportStatus(Status.UNDER_REVIEW);
    }

    public static ReportStatus published() {
        return new ReportStatus(Status.PUBLISHED);
    }

    public static ReportStatus archived() {
        return new ReportStatus(Status.ARCHIVED);
    }


    public boolean isDraft() {
        return status == Status.DRAFT;
    }

    public boolean isUnderReview() {
        return status == Status.UNDER_REVIEW;
    }

    public boolean isPublished() {
        return status == Status.PUBLISHED;
    }

    public boolean isArchived() {
        return status == Status.ARCHIVED;
    }

    public boolean isEditable() {
        return status == Status.DRAFT;
    }

    public boolean canBeSubmittedForReview() {
        return status == Status.DRAFT;
    }

    public boolean canBeApproved() {
        return status == Status.UNDER_REVIEW;
    }

    public boolean canBeRejected() {
        return status == Status.UNDER_REVIEW;
    }

    public boolean canBeArchived() {
        return status != Status.ARCHIVED;
    }

    public String getDisplayName() {
        return switch (status) {
            case DRAFT -> "Borrador";
            case UNDER_REVIEW -> "En Revisión";
            case PUBLISHED -> "Publicado";
            case ARCHIVED -> "Archivado";
        };
    }

    public Status getStatus() { return status; }


}
