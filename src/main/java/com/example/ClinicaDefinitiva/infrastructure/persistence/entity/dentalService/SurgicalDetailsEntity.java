package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.dentalService;

import jakarta.persistence.*;

@Entity
@Table(name = "provided_service_surgical")
public class SurgicalDetailsEntity {

    @Id
    @Column(name = "provided_service_id", length = 36)
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provided_service_id")
    private ProvidedServiceEntity providedService;

    private String surgeryType;
    private String complexityLevel;
    private Boolean requiresAnesthesia;
    private Boolean operatingRoomNeeded;

    public SurgicalDetailsEntity() {}

    public SurgicalDetailsEntity(String surgeryType, String complexityLevel, Boolean requiresAnesthesia, Boolean operatingRoomNeeded) {
        this.surgeryType = surgeryType;
        this.complexityLevel = complexityLevel;
        this.requiresAnesthesia = requiresAnesthesia;
        this.operatingRoomNeeded = operatingRoomNeeded;
    }

    public void setProvidedService(ProvidedServiceEntity providedService) {
        this.providedService = providedService;
        this.id = providedService.getId();
    }

    // getters/setters...

    public String getSurgeryType() {
        return surgeryType;
    }

    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    public Boolean getRequiresAnesthesia() {
        return requiresAnesthesia;
    }

    public void setRequiresAnesthesia(Boolean requiresAnesthesia) {
        this.requiresAnesthesia = requiresAnesthesia;
    }

    public ProvidedServiceEntity getProvidedService() {
        return providedService;
    }

    public Boolean getOperatingRoomNeeded() {
        return operatingRoomNeeded;
    }

    public void setOperatingRoomNeeded(Boolean operatingRoomNeeded) {
        this.operatingRoomNeeded = operatingRoomNeeded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComplexityLevel() {
        return complexityLevel;
    }

    public void setComplexityLevel(String complexityLevel) {
        this.complexityLevel = complexityLevel;
    }
}
