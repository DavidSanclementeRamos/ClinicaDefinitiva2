package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity;



import jakarta.persistence.*;

@Entity
@Table(name = "servicio_detalle_pediatria")
public class PediatricDetailEntity {

    @Id
    @Column(name = "id_servicio", updatable = false, nullable = false)
    private Long serviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_servicio")
    private DentalServiceEntity dentalService;

    /** AgeRange VO descompuesto en dos columnas (no tiene identidad propia). */
    @Column(name = "rango_edad_min")
    private Integer minAgeRange;

    @Column(name = "rango_edad_max")
    private Integer maxAgeRange;

    @Column(name = "manejo_comportamiento", length = 200)
    private String behaviorManagement;

    @Column(name = "materiales_pediatricos", columnDefinition = "TEXT")
    private String pediatricMaterials;

    public PediatricDetailEntity() {}

    public Long getServiceId()                      { return serviceId; }
    public DentalServiceEntity getDentalService()   { return dentalService; }
    public Integer getMinAgeRange()                 { return minAgeRange; }
    public Integer getMaxAgeRange()                 { return maxAgeRange; }
    public String getBehaviorManagement()           { return behaviorManagement; }
    public String getPediatricMaterials()           { return pediatricMaterials; }

    public void setDentalService(DentalServiceEntity dentalService) { this.dentalService = dentalService; }
    public void setMinAgeRange(Integer minAgeRange)                 { this.minAgeRange = minAgeRange; }
    public void setMaxAgeRange(Integer maxAgeRange)                 { this.maxAgeRange = maxAgeRange; }
    public void setBehaviorManagement(String behaviorManagement)    { this.behaviorManagement = behaviorManagement; }
    public void setPediatricMaterials(String pediatricMaterials)    { this.pediatricMaterials = pediatricMaterials; }
}