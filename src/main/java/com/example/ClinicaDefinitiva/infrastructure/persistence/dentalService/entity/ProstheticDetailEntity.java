package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "servicio_detalle_protesis")
public class ProstheticDetailEntity {

    @Id
    @Column(name = "id_servicio", updatable = false, nullable = false)
    private Long serviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_servicio")
    private DentalServiceEntity dentalService;

    @Column(name = "fija_o_removible", nullable = false, length = 10)
    private String fixedOrRemovable;

    @Column(name = "material", length = 100)
    private String material;

    @Column(name = "tipo_protesis", length = 100)
    private String prostheticType;

    @Column(name = "unidades", nullable = false)
    private int units;

    public ProstheticDetailEntity() {}

    public Long getServiceId()                      { return serviceId; }
    public DentalServiceEntity getDentalService()   { return dentalService; }
    public String getFixedOrRemovable()             { return fixedOrRemovable; }
    public String getMaterial()                     { return material; }
    public String getProstheticType()               { return prostheticType; }
    public int getUnits()                           { return units; }

    public void setDentalService(DentalServiceEntity dentalService) { this.dentalService = dentalService; }
    public void setFixedOrRemovable(String fixedOrRemovable)        { this.fixedOrRemovable = fixedOrRemovable; }
    public void setMaterial(String material)                        { this.material = material; }
    public void setProstheticType(String prostheticType)            { this.prostheticType = prostheticType; }
    public void setUnits(int units)                                 { this.units = units; }
}