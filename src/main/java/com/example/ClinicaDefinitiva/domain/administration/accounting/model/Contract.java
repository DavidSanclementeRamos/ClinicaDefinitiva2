package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting.ContractError;
//import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalogXD;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.vo.AuditoriaInfo;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Representa un convenio o contrato con terceros (EPS, aseguradoras, empresas).
 * Gestiona la cobertura, vigencia y condiciones del convenio.
 */
public final class Contract {

    private static final int EXPIRATION_WARNING_DAYS = 30;

    private final ContractId contractId;
    private final CompanyId companyId;
    private final ThirdPartiesId thirdPartiesId;
    private Name name;
    private String description;
    private String origin;
    private final LocalDate startDate;
    private LocalDate endDate;
    private String coverageType;
    private final BigDecimal coverageRate;
    private ContractStatus status;
    private AuditoriaInfo audit;

    private Contract(Builder builder) {
        validateMandatoryFields(builder.startDate, builder.endDate, builder.coverageType);
        validateDates(builder.startDate, builder.endDate);

        this.contractId = builder.contractId;
        this.companyId = builder.companyId;
        this.thirdPartiesId = builder.thirdPartiesId;
        this.name = builder.name;
        this.description = builder.description ;
        this.origin = builder.origin ;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.coverageType = builder.coverageType.trim().toUpperCase();
        this.coverageRate = builder.coverageRate;
        this.status = builder.status != null ? builder.status : ContractStatus.ACTIVE;
        this.audit = builder.audit;
    }

   
    

    public static Contract registerContract(
            CompanyId company,
            ThirdPartiesId thirdParties,
            Name name,
            String description,
            String origin,
            LocalDate endDate,
            String coverageType,
            BigDecimal coverageRate) {

        return Contract.builder()
                .withCompanyId(company)
                .withThirdPartiesId(thirdParties)
                .withName(name)
                .withDescription(description)
                .withOrigin(origin)
                .withStartDate(LocalDate.now())
                .withEndDate(endDate)
                .withCoverageType(coverageType)
                .withCoverageRate(coverageRate)
                .withStatus(ContractStatus.ACTIVE)
                .build();
    }

    public void updateInformation(Name name, String description, String origin, String coverageType) {
        ensureEditable();
        validateCoverageType(coverageType);
        this.name = name;
        this.description = description != null ? description.trim() : null;
        this.origin = origin != null ? origin.trim() : null;
        this.coverageType = coverageType.trim().toUpperCase();
    }

    public void extendContract(LocalDate newEndDate) {
        ensureEditable();
        if (newEndDate == null) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_MISSING_NEW_END_DATE, EntityContext.CONTRACT);
        if (newEndDate.isBefore(this.endDate)) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_INVALID_DATES, EntityContext.CONTRACT);
        if (newEndDate.isBefore(LocalDate.now())) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_NEW_END_DATE_IN_PAST, EntityContext.CONTRACT);
        this.endDate = newEndDate;
    }

    public void suspend(String reason) {
        if (this.status != ContractStatus.ACTIVE) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_CANNOT_SUSPEND, EntityContext.CONTRACT);
        if (reason == null || reason.isBlank()) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_TERMINATION_REQUIRES_REASON, EntityContext.CONTRACT);
        this.status = ContractStatus.SUSPENDED;
    }

    public void reactivate() {
        if (this.status != ContractStatus.SUSPENDED) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_CANNOT_REACTIVATE, EntityContext.CONTRACT);
        if (isExpired()) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE, EntityContext.CONTRACT);
        this.status = ContractStatus.ACTIVE;
    }

    public void terminate(String reason) {
        if (this.status == ContractStatus.TERMINATED) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_ALREADY_TERMINATED, EntityContext.CONTRACT);
        if (reason == null || reason.isBlank()) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_TERMINATION_REQUIRES_REASON, EntityContext.CONTRACT);
        this.status = ContractStatus.TERMINATED;
    }

    public boolean isExpiredAt(LocalDateTime when) {
        if (endDate == null) return false;
        return when.toLocalDate().isAfter(endDate);
    }

    public boolean isExpired() { return isExpiredAt(LocalDateTime.now()); }

    public boolean isActiveAndValid() { return this.status == ContractStatus.ACTIVE && !isExpired(); }

    public boolean isNearExpiration() {
        if (endDate == null) return false;
        long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return daysUntilExpiration > 0 && daysUntilExpiration <= EXPIRATION_WARNING_DAYS;
    }

    public long getDaysRemaining() {
        if (endDate == null) return Long.MAX_VALUE;
        if (isExpired()) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    // -------- Validaciones internas --------
    private void ensureEditable() {
        if (this.status != ContractStatus.ACTIVE) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_NOT_EDITABLE, EntityContext.CONTRACT);
        if (isExpired()) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_EXPIRED_NOT_EDITABLE, EntityContext.CONTRACT);
    }

    private void validateMandatoryFields(LocalDate startDate, LocalDate endDate, String coverageType) {
        validateCoverageType(coverageType);
        if (startDate == null) throw new DomainAggregateException(ContractError.ERR_CONTRACT_MISSING_START_DATE, EntityContext.CONTRACT);
        if (endDate == null) throw new DomainAggregateException(ContractError.ERR_CONTRACT_MISSING_END_DATE, EntityContext.CONTRACT);
    }

    private void validateCoverageType(String coverageType) {
        if (coverageType == null || coverageType.isBlank()) throw new DomainAggregateException(ContractError.ERR_CONTRACT_MISSING_COVERAGE_TYPE, EntityContext.CONTRACT);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) throw new BusinessRuleViolationException(ContractError.ERR_CONTRACT_INVALID_DATES, EntityContext.CONTRACT);
    }

    // -------- Getters --------
    public ContractId getContractId() { return contractId; }
    public CompanyId getCompanyId() { return companyId; }
    public ThirdPartiesId getThirdPartiesId() { return thirdPartiesId; }
    public Name getName() { return name; }
    public String getDescription() { return description; }
    public String getOrigin() { return origin; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getCoverageType() { return coverageType; }
    public BigDecimal getCoverageRate() { return coverageRate; }
    public ContractStatus getStatus() { return status; }
    public AuditoriaInfo getAudit() { return audit; }


    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private ContractId contractId;
        private CompanyId companyId;
        private ThirdPartiesId thirdPartiesId;
        private Name name;
        private String description;
        private String origin;
        private LocalDate startDate = LocalDate.now();
        private LocalDate endDate;
        private String coverageType;
        private BigDecimal coverageRate;
        private ContractStatus status = ContractStatus.ACTIVE;
        private AuditoriaInfo audit;

        public Builder withContractId(ContractId id) { this.contractId = id; return this; }
        public Builder withCompanyId(CompanyId id) { this.companyId = id; return this; }
        public Builder withThirdPartiesId(ThirdPartiesId id) { this.thirdPartiesId = id; return this; }
        public Builder withName(Name name) { this.name = name; return this; }
        public Builder withDescription(String description) { this.description = description; return this; }
        public Builder withOrigin(String origin) { this.origin = origin; return this; }
        public Builder withStartDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder withEndDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder withCoverageType(String coverageType) { this.coverageType = coverageType; return this; }
        public Builder withCoverageRate(BigDecimal coverageRate) { this.coverageRate = coverageRate; return this; }
        public Builder withStatus(ContractStatus status) { this.status = status; return this; }
        public Builder withAudit(AuditoriaInfo audit) { this.audit = audit; return this; }

        public Contract build() { return new Contract(this); }
    }

}