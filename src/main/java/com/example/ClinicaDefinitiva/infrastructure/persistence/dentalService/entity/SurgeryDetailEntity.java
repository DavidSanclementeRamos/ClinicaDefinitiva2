package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "servicio_detalle_cirugia")
public class SurgeryDetailEntity {

    @Id
    @Column(name = "id_servicio", updatable = false, nullable = false)
    private Long serviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_servicio")
    private DentalServiceEntity dentalService;

    @Column(name = "tipo_cirugia", nullable = false, length = 100)
    private String surgeryType;

    @Column(name = "nivel_complejidad", length = 10)
    private String complexityLevel;

    @Column(name = "requiere_anestesia", nullable = false)
    private boolean requiresAnesthesia;

    @Column(name = "requiere_quirofano", nullable = false)
    private boolean requiresOperatingRoom;

    public SurgeryDetailEntity() {}

    public Long getServiceId()                      { return serviceId; }
    public DentalServiceEntity getDentalService()   { return dentalService; }
    public String getSurgeryType()                   { return surgeryType; }
    public String getComplexityLevel()               { return complexityLevel; }
    public boolean isRequiresAnesthesia()            { return requiresAnesthesia; }
    public boolean isRequiresOperatingRoom()         { return requiresOperatingRoom; }

    public void setDentalService(DentalServiceEntity dentalService) { this.dentalService = dentalService; }
    public void setSurgeryType(String surgeryType)                   { this.surgeryType = surgeryType; }
    public void setComplexityLevel(String complexityLevel)           { this.complexityLevel = complexityLevel; }
    public void setRequiresAnesthesia(boolean requiresAnesthesia)    { this.requiresAnesthesia = requiresAnesthesia; }
    public void setRequiresOperatingRoom(boolean requiresOperatingRoom) { this.requiresOperatingRoom = requiresOperatingRoom; }
}