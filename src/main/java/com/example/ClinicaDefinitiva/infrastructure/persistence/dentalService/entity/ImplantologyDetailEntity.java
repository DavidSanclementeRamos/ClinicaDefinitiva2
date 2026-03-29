package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "servicio_detalle_implantologia")
public class ImplantologyDetailEntity {

    @Id
    @Column(name = "id_servicio", updatable = false, nullable = false)
    private Long serviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_servicio")
    private DentalServiceEntity dentalService;

    @Column(name = "meses_cicatrizacion")
    private Integer healingMonths;

    @Column(name = "tipo_implante", length = 100)
    private String implantType;

    @Column(name = "sitio_colocacion", length = 100)
    private String placementSite;

    @Column(name = "requiere_injerto_oseo", nullable = false)
    private boolean requiresBoneGraft;

    public ImplantologyDetailEntity() {}

    public Long getServiceId()                      { return serviceId; }
    public DentalServiceEntity getDentalService()   { return dentalService; }
    public Integer getHealingMonths()               { return healingMonths; }
    public String getImplantType()                  { return implantType; }
    public String getPlacementSite()                { return placementSite; }
    public boolean isRequiresBoneGraft()            { return requiresBoneGraft; }

    public void setDentalService(DentalServiceEntity dentalService) { this.dentalService = dentalService; }
    public void setHealingMonths(Integer healingMonths)             { this.healingMonths = healingMonths; }
    public void setImplantType(String implantType)                  { this.implantType = implantType; }
    public void setPlacementSite(String placementSite)              { this.placementSite = placementSite; }
    public void setRequiresBoneGraft(boolean requiresBoneGraft)     { this.requiresBoneGraft = requiresBoneGraft; }
}