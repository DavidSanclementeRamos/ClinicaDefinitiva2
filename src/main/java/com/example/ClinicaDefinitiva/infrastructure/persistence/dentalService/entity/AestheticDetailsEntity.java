package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity;


import jakarta.persistence.*;

/**
 * Detalle estético — PK es FK a servicio_odontologico (Joined 1:1).
 * @PrimaryKeyJoinColumn enlaza las dos tablas por el mismo id.
 */
@Entity
@Table(name = "servicio_detalle_estetico")
public class AestheticDetailsEntity {

    @Id
    @Column(name = "id_servicio", updatable = false, nullable = false)
    private Long serviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_servicio")
    private DentalServiceEntity dentalService;

    @Column(name = "tipo_estetico", nullable = false, length = 30)
    private String aestheticType;

    @Column(name = "material_utilizado", length = 100)
    private String materialUsed;

    @Column(name = "resultado_esperado", columnDefinition = "TEXT")
    private String expectedResult;

    public AestheticDetailsEntity() {}

    public Long getServiceId()                      { return serviceId; }
    public DentalServiceEntity getDentalService()   { return dentalService; }
    public String getAestheticType()                { return aestheticType; }
    public String getMaterialUsed()                 { return materialUsed; }
    public String getExpectedResult()               { return expectedResult; }

    public void setDentalService(DentalServiceEntity dentalService) { this.dentalService = dentalService; }
    public void setAestheticType(String aestheticType)              { this.aestheticType = aestheticType; }
    public void setMaterialUsed(String materialUsed)                { this.materialUsed = materialUsed; }
    public void setExpectedResult(String expectedResult)            { this.expectedResult = expectedResult; }
}