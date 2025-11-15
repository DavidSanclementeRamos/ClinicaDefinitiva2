package com.example.ClinicaDefinitiva.infrastructure.persistence.providedService;


import jakarta.persistence.*;

@Entity
@Table(name = "provided_service_pediatric")
public class PediatricDetailsEntity {

    @Id
    @Column(name = "provided_service_id", length = 36)
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provided_service_id")
    private ProvidedServiceEntity providedService;

    private String ageRange;
    private String behaviorManagement;
    private String pediatricMaterials;

    public PediatricDetailsEntity() {}

    public PediatricDetailsEntity(String ageRange, String behaviorManagement, String pediatricMaterials) {
        this.ageRange = ageRange;
        this.behaviorManagement = behaviorManagement;
        this.pediatricMaterials = pediatricMaterials;
    }

    public void setProvidedService(ProvidedServiceEntity providedService) {
        this.providedService = providedService;
        this.id = providedService.getId();
    }

    // getters/setters...

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getBehaviorManagement() {
        return behaviorManagement;
    }

    public void setBehaviorManagement(String behaviorManagement) {
        this.behaviorManagement = behaviorManagement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPediatricMaterials() {
        return pediatricMaterials;
    }

    public void setPediatricMaterials(String pediatricMaterials) {
        this.pediatricMaterials = pediatricMaterials;
    }

    public ProvidedServiceEntity getProvidedService() {
        return providedService;
    }
}
