package com.example.ClinicaDefinitiva.infrastructure.persistence.providedService;

import jakarta.persistence.*;
@Entity
@Table(name = "provided_service_implantology",
        indexes = {@Index(name = "idx_implant_healing_time", columnList = "healing_time_months")})
public class ImplantologyDetailsEntity {

    @Id
    @Column(name = "provided_service_id", length = 36)
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provided_service_id")
    private ProvidedServiceEntity providedService;

    private Integer healingTimeMonths;
    private String implantType;
    private String placementSite;
    private Boolean requiresBoneGraft;

    public ImplantologyDetailsEntity() {}

    public ImplantologyDetailsEntity(Integer healingTimeMonths, String implantType, String placementSite, Boolean requiresBoneGraft) {
        this.healingTimeMonths = healingTimeMonths;
        this.implantType = implantType;
        this.placementSite = placementSite;
        this.requiresBoneGraft = requiresBoneGraft;
    }

    public void setProvidedService(ProvidedServiceEntity providedService) {
        this.providedService = providedService;
        this.id = providedService.getId();
    }

    // getters/setters...

    public Boolean getRequiresBoneGraft() {
        return requiresBoneGraft;
    }

    public void setRequiresBoneGraft(Boolean requiresBoneGraft) {
        this.requiresBoneGraft = requiresBoneGraft;
    }

    public ProvidedServiceEntity getProvidedService() {
        return providedService;
    }

    public String getPlacementSite() {
        return placementSite;
    }

    public void setPlacementSite(String placementSite) {
        this.placementSite = placementSite;
    }

    public String getImplantType() {
        return implantType;
    }

    public void setImplantType(String implantType) {
        this.implantType = implantType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHealingTimeMonths() {
        return healingTimeMonths;
    }

    public void setHealingTimeMonths(Integer healingTimeMonths) {
        this.healingTimeMonths = healingTimeMonths;
    }
}
