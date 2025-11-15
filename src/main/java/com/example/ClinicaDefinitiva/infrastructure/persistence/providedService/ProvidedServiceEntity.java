package com.example.ClinicaDefinitiva.infrastructure.persistence.providedService;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "provided_service",
        indexes = {
                @Index(name = "idx_provided_service_code", columnList = "code"),
                @Index(name = "idx_provided_service_type", columnList = "service_type"),
                @Index(name = "idx_provided_service_catalog_category", columnList = "catalog_category")
        })
public class ProvidedServiceEntity {

    @Id
    @Column(length = 36)
    private String id;

    private String name;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "catalog_id", length = 36)
    private String catalogId;

    @Column(name = "catalog_name")
    private String catalogName;

    @Column(name = "catalog_category")
    private String catalogCategory;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "base_rate_amount", precision = 12, scale = 2)
    private BigDecimal baseRateAmount;

    @Column(name = "base_rate_currency", length = 3)
    private String baseRateCurrency;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "requires_authorization")
    private Boolean requiresAuthorization;

    @Lob
    private String description;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "providedService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private OrthodonticDetailsEntity orthodonticDetails;

    @OneToOne(mappedBy = "providedService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private AestheticDetailsEntity aestheticDetailsEntity;

    @OneToOne(mappedBy = "providedService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private ImplantologyDetailsEntity implantologyDetailsEntity;

    @OneToOne(mappedBy = "providedService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private PediatricDetailsEntity pediatricDetailsEntity;

    @OneToOne(mappedBy = "providedService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private ProstheticDetailsEntity prostheticDetailsEntity;

    @OneToOne(mappedBy = "providedService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private SurgicalDetailsEntity surgicalDetailsEntity;

    public ProvidedServiceEntity() {}

    // getters & setters (omitted for brevity) - generate them in your IDE
    // ... include getId/setId, getName/setName, etc.

    public BigDecimal getBaseRateAmount() {
        return baseRateAmount;
    }

    public SurgicalDetailsEntity getSurgicalDetailsEntity() {
        return surgicalDetailsEntity;
    }

    public void setSurgicalDetailsEntity(SurgicalDetailsEntity surgicalDetailsEntity) {
        this.surgicalDetailsEntity = surgicalDetailsEntity;
    }

    public ProstheticDetailsEntity getProstheticDetailsEntity() {
        return prostheticDetailsEntity;
    }

    public void setProstheticDetailsEntity(ProstheticDetailsEntity prostheticDetailsEntity) {
        this.prostheticDetailsEntity = prostheticDetailsEntity;
    }

    public PediatricDetailsEntity getPediatricDetailsEntity() {
        return pediatricDetailsEntity;
    }

    public void setPediatricDetailsEntity(PediatricDetailsEntity pediatricDetailsEntity) {
        this.pediatricDetailsEntity = pediatricDetailsEntity;
    }

    public ImplantologyDetailsEntity getImplantologyDetailsEntity() {
        return implantologyDetailsEntity;
    }

    public void setImplantologyDetailsEntity(ImplantologyDetailsEntity implantologyDetailsEntity) {
        this.implantologyDetailsEntity = implantologyDetailsEntity;
    }

    public AestheticDetailsEntity getAestheticDetailsEntity() {
        return aestheticDetailsEntity;
    }

    public void setAestheticDetailsEntity(AestheticDetailsEntity aestheticDetailsEntity) {
        this.aestheticDetailsEntity = aestheticDetailsEntity;
    }

    public void setBaseRateAmount(BigDecimal baseRateAmount) {
        this.baseRateAmount = baseRateAmount;
    }

    public String getBaseRateCurrency() {
        return baseRateCurrency;
    }

    public void setBaseRateCurrency(String baseRateCurrency) {
        this.baseRateCurrency = baseRateCurrency;
    }

    public String getCatalogCategory() {
        return catalogCategory;
    }

    public void setCatalogCategory(String catalogCategory) {
        this.catalogCategory = catalogCategory;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrthodonticDetailsEntity getOrthodonticDetails() {
        return orthodonticDetails;
    }

    public void setOrthodonticDetails(OrthodonticDetailsEntity orthodonticDetails) {
        this.orthodonticDetails = orthodonticDetails;
    }

    public Boolean getRequiresAuthorization() {
        return requiresAuthorization;
    }

    public void setRequiresAuthorization(Boolean requiresAuthorization) {
        this.requiresAuthorization = requiresAuthorization;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
