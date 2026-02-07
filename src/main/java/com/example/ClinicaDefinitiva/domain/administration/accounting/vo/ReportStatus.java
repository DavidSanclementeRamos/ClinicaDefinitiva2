package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;


/**
 * Value Object que representa el estado de un reporte administrativo.
 * Inmutable y con validaciones de negocio.
 */
public final class ReportStatus {

    private final Status status;

    private ReportStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.status = status;
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

    public static ReportStatus of(Status status) {
        return new ReportStatus(status);
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

    public Status getStatus() {
        return status;
    }


    public enum Status {
        DRAFT,
        UNDER_REVIEW,
        PUBLISHED,
        ARCHIVED
    }
}
