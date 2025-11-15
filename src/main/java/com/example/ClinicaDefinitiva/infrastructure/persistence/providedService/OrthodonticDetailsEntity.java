package com.example.ClinicaDefinitiva.infrastructure.persistence.providedService;


import jakarta.persistence.*;



@Entity
@Table(name = "provided_service_orthodontic",
        indexes = {@Index(name = "idx_orthodontic_treatment_duration", columnList = "treatment_duration_months")})
public class OrthodonticDetailsEntity {

    @Id
    @Column(name = "provided_service_id", length = 36)
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provided_service_id")
    private ProvidedServiceEntity providedService;

    @Column(name = "appliance_type")
    private String applianceType;

    @Column(name = "treatment_duration_months")
    private Integer treatmentDurationMonths;

    @Column(name = "requires_followup")
    private Boolean requiresFollowup;

    public OrthodonticDetailsEntity() {}

    public OrthodonticDetailsEntity(String applianceType, Integer treatmentDurationMonths, Boolean requiresFollowup) {
        this.applianceType = applianceType;
        this.treatmentDurationMonths = treatmentDurationMonths;
        this.requiresFollowup = requiresFollowup;
    }

    public void setProvidedService(ProvidedServiceEntity providedService) {
        this.providedService = providedService;
        this.id = providedService.getId();
    }

    // getters & setters (omitted) - generate them in your IDE

    public String getApplianceType() {
        return applianceType;
    }

    public void setApplianceType(String applianceType) {
        this.applianceType = applianceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProvidedServiceEntity getProvidedService() {
        return providedService;
    }

    public Boolean getRequiresFollowup() {
        return requiresFollowup;
    }

    public void setRequiresFollowup(Boolean requiresFollowup) {
        this.requiresFollowup = requiresFollowup;
    }

    public Integer getTreatmentDurationMonths() {
        return treatmentDurationMonths;
    }

    public void setTreatmentDurationMonths(Integer treatmentDurationMonths) {
        this.treatmentDurationMonths = treatmentDurationMonths;
    }
}
