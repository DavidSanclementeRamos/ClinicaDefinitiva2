package com.example.ClinicaDefinitiva.infrastructure.persistence.providedService;


import jakarta.persistence.*;

@Entity
@Table(name = "provided_service_prosthetic",
        indexes = {@Index(name = "idx_prosthetic_units", columnList = "units")})
public class ProstheticDetailsEntity {

    @Id
    @Column(name = "provided_service_id", length = 36)
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provided_service_id")
    private ProvidedServiceEntity providedService;

    private String fixedOrRemovable;
    private String material;
    private String prostheticType;
    private Integer units;

    public ProstheticDetailsEntity() {}

    public ProstheticDetailsEntity(String fixedOrRemovable, String material, String prostheticType, Integer units) {
        this.fixedOrRemovable = fixedOrRemovable;
        this.material = material;
        this.prostheticType = prostheticType;
        this.units = units;
    }

    public void setProvidedService(ProvidedServiceEntity providedService) {
        this.providedService = providedService;
        this.id = providedService.getId();
    }

    // getters/setters...

    public String getFixedOrRemovable() {
        return fixedOrRemovable;
    }

    public void setFixedOrRemovable(String fixedOrRemovable) {
        this.fixedOrRemovable = fixedOrRemovable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getProstheticType() {
        return prostheticType;
    }

    public void setProstheticType(String prostheticType) {
        this.prostheticType = prostheticType;
    }

    public ProvidedServiceEntity getProvidedService() {
        return providedService;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }
}
