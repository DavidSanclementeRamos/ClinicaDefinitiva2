package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity;



import jakarta.persistence.*;

@Entity
@Table(name = "servicio_detalle_ortodoncia")
public class OrthodonticDetailEntity {

    @Id
    @Column(name = "id_servicio", updatable = false, nullable = false)
    private Long serviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_servicio")
    private DentalServiceEntity dentalService;

    @Column(name = "tipo_aparato", nullable = false, length = 30)
    private String applianceType;

    @Column(name = "duracion_meses")
    private Integer durationMonths;

    @Column(name = "requiere_seguimiento", nullable = false)
    private boolean requiresFollowUp;

    public OrthodonticDetailEntity() {}

    public Long getServiceId()                      { return serviceId; }
    public DentalServiceEntity getDentalService()   { return dentalService; }
    public String getApplianceType()                { return applianceType; }
    public Integer getDurationMonths()              { return durationMonths; }
    public boolean isRequiresFollowUp()             { return requiresFollowUp; }

    public void setDentalService(DentalServiceEntity dentalService) { this.dentalService = dentalService; }
    public void setApplianceType(String applianceType)              { this.applianceType = applianceType; }
    public void setDurationMonths(Integer durationMonths)           { this.durationMonths = durationMonths; }
    public void setRequiresFollowUp(boolean requiresFollowUp)       { this.requiresFollowUp = requiresFollowUp; }
}