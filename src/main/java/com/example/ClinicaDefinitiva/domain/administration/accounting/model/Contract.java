package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Representa un convenio o contrato con terceros (EPS, aseguradoras, empresas).
 * Gestiona la cobertura, vigencia y condiciones del convenio.
 */
public final class Contract {

    private static final int EXPIRATION_WARNING_DAYS = 30;

    private ContractId contractId;
    private CompanyId companyId;
    private ThirdPartiesId thirdPartiesId;
    private Name name;
    private String description;
    private String origin;
    private LocalDate startDate;
    private LocalDate endDate;
    private String coverageType;
    private Double coverageRate;
    private ContractStatus status;
    private AuditoriaInfo audit;

    private Contract(
            ContractId contractId,
            CompanyId companyId,
            ThirdPartiesId thirdPartiesId,
            Name name,
            String description,
            String origin,
            LocalDate startDate,
            LocalDate endDate,
            String coverageType,
            Double coverageRate,
            ContractStatus status) {

        validateMandatoryFields( startDate, endDate, coverageType);
        validateDates(startDate, endDate);

        this.contractId = contractId;
        this.companyId = companyId;
        this.thirdPartiesId = thirdPartiesId;
        this.name = name;
        this.description = description != null ? description.trim() : null;
        this.origin = origin != null ? origin.trim() : null;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coverageType = coverageType.trim().toUpperCase();
        this.coverageRate = coverageRate;
        this.status = status != null ? status : ContractStatus.ACTIVE;
        this.audit = audit;
    }

    /**
     * Factory method para registrar un nuevo contrato.
     */
    public static Contract registerContract(
            Company company,
            ThirdParties thirdParties,
            Name name,
            String description,
            String origin,
            LocalDate endDate,
            String coverageType,
            Double coverageRate) {

        LocalDate startDate = LocalDate.now();

        return new Contract(
                null,
                company.getId(),
                thirdParties.getPartiesId(),
                name,
                description,
                origin,
                startDate,
                endDate,
                coverageType,
                coverageRate,
                ContractStatus.ACTIVE

        );
    }

    /**
     * Actualiza la información general del contrato.
     * Solo permite edición si el contrato está activo.
     */
    public void updateInformation(
            Name name,
            String description,
            String origin,
            String coverageType) {

        ensureEditable();
        validateCoverageType(coverageType);

        this.name = name;
        this.description = description != null ? description.trim() : null;
        this.origin = origin != null ? origin.trim() : null;
        this.coverageType = coverageType.trim().toUpperCase();
    }


    /**
     * Extiende la vigencia del contrato.
     */
    public void extendContract(LocalDate newEndDate) {
        ensureEditable();

        if (newEndDate == null) {
            throw new InvalidContractException("La nueva fecha de fin es obligatoria");
        }
        if (newEndDate.isBefore(this.endDate)) {
            throw new InvalidContractException("La nueva fecha debe ser posterior a la fecha actual de fin");
        }
        if (newEndDate.isBefore(LocalDate.now())) {
            throw new InvalidContractException("La nueva fecha no puede estar en el pasado");
        }

        this.endDate = newEndDate;
    }

    /**
     * Suspende temporalmente el contrato.
     */
    public void suspend(String reason) {
        if (this.status != ContractStatus.ACTIVE) {
            throw new InvalidContractStatusException(
                    "Solo se pueden suspender contratos activos"
            );
        }
        if (reason == null || reason.isBlank()) {
            throw new InvalidContractException("Se requiere una razón para suspender el contrato");
        }
        this.status = ContractStatus.SUSPENDED;
    }

    /**
     * Reactiva un contrato suspendido.
     */
    public void reactivate() {
        if (this.status != ContractStatus.SUSPENDED) {
            throw new InvalidContractStatusException(
                    "Solo se pueden reactivar contratos suspendidos"
            );
        }
        if (isExpired()) {
            throw new InvalidContractException(
                    "No se puede reactivar un contrato vencido"
            );
        }
        this.status = ContractStatus.ACTIVE;
    }

    /**
     * Finaliza el contrato antes de su fecha de vencimiento.
     */
    public void terminate(String reason) {
        if (this.status == ContractStatus.TERMINATED) {
            throw new InvalidContractStatusException("El contrato ya está terminado");
        }
        if (reason == null || reason.isBlank()) {
            throw new InvalidContractException("Se requiere una razón para terminar el contrato");
        }
        this.status = ContractStatus.TERMINATED;
    }

    /**
     * Verifica si el contrato está vencido en una fecha específica.
     */
    public boolean isExpiredAt(LocalDateTime when) {
        if (endDate == null) return false;
        return when.toLocalDate().isAfter(endDate);
    }

    /**
     * Verifica si el contrato está vencido actualmente.
     */
    public boolean isExpired() {
        return isExpiredAt(LocalDateTime.now());
    }

    /**
     * Verifica si el contrato está activo y vigente.
     */
    public boolean isActiveAndValid() {
        return this.status == ContractStatus.ACTIVE && !isExpired();
    }

    /**
     * Verifica si el contrato está próximo a vencer.
     */
    public boolean isNearExpiration() {
        if (endDate == null) return false;
        long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return daysUntilExpiration > 0 && daysUntilExpiration <= EXPIRATION_WARNING_DAYS;
    }

    /**
     * Calcula los días restantes de vigencia del contrato.
     */
    public long getDaysRemaining() {
        if (endDate == null) return Long.MAX_VALUE;
        if (isExpired()) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    private void ensureEditable() {
        if (this.status != ContractStatus.ACTIVE) {
            throw new InvalidContractStatusException(
                    "Solo se pueden editar contratos activos. Estado actual: " + this.status
            );
        }
        if (isExpired()) {
            throw new InvalidContractException("No se puede editar un contrato vencido");
        }
    }

    private void validateMandatoryFields(

            LocalDate startDate,
            LocalDate endDate,
            String coverageType
            ) {

        validateCoverageType(coverageType);

        if (startDate == null) {
            throw new InvalidContractException("La fecha de inicio es obligatoria");
        }
        if (endDate == null) {
            throw new InvalidContractException("La fecha de fin es obligatoria");
        }
    }



    private void validateCoverageType(String coverageType) {
        if (coverageType == null || coverageType.isBlank()) {
            throw new InvalidContractException("El tipo de cobertura es obligatorio");
        }
    }


    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidContractException(
                    "La fecha de fin debe ser posterior a la fecha de inicio"
            );
        }


    }

    // Getters
    public ContractId getContractId() { return contractId; }
    public CompanyId getCompanyId() { return companyId; }
    public ThirdPartiesId getThirdPartiesId() { return thirdPartiesId; }
    public Name getName() { return name; }
    public String getDescription() { return description; }
    public String getOrigin() { return origin; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getCoverageType() { return coverageType; }
    public Double getCoverageRate() { return coverageRate; }
    public ContractStatus getStatus() { return status; }
    public AuditoriaInfo getAudit() { return audit; }

    // Setters para infraestructura
    public void setContractId(ContractId contractId) { this.contractId = contractId; }
}