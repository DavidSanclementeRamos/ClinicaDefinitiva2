package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad principal del servicio odontológico.
 *
 * El campo serviceType actúa como discriminador: indica cuál de las
 * 6 tablas de detalle (dental_service_detail_*) tiene la fila correspondiente.
 * Solo una de las seis relaciones @OneToOne estará poblada por fila.
 * Decisión documentada en ADR-54.
 */
@Entity
@Table(name = "servicio_odontologico")
public class DentalServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String name;

    @Column(name = "categoria", nullable = false, length = 50)
    private String category;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "tarifa_base", nullable = false, precision = 19, scale = 4)
    private BigDecimal baseRate;

    @Column(name = "moneda_tarifa_base", nullable = false, length = 3)
    private String baseRateCurrency;

    @Column(name = "duracion_minutos", nullable = false)
    private int durationMinutes;

    @Column(name = "requiere_autorizacion", nullable = false)
    private boolean requiresAuthorization;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    /**
     * Discriminador lógico: determina cuál tabla de detalle usar.
     * Valores: AESTHETICS | IMPLANTOLOGY | ORTHODONTIC | PEDIATRICS | PROSTHETICS | SURGERY
     */
    @Column(name = "tipo_servicio", nullable = false, length = 30)
    private String serviceType;

    // ── Relaciones a tablas de detalle (Joined 1:0..1) ─────────────────────
    @OneToOne(mappedBy = "dentalService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private AestheticDetailsEntity aestheticDetail;

    @OneToOne(mappedBy = "dentalService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ImplantologyDetailEntity implantologyDetail;

    @OneToOne(mappedBy = "dentalService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private OrthodonticDetailEntity orthodonticDetail;

    @OneToOne(mappedBy = "dentalService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private PediatricDetailEntity pediatricDetail;

    @OneToOne(mappedBy = "dentalService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ProstheticDetailEntity prostheticDetail;

    @OneToOne(mappedBy = "dentalService", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private SurgeryDetailEntity surgeryDetail;

    public DentalServiceEntity() {}

    public Long getId()                           { return id; }
    public String getName()                        { return name; }
    public String getCategory()                    { return category; }
    public String getCode()                        { return code; }
    public BigDecimal getBaseRate()                { return baseRate; }
    public String getBaseRateCurrency()            { return baseRateCurrency; }
    public int getDurationMinutes()                { return durationMinutes; }
    public boolean isRequiresAuthorization()       { return requiresAuthorization; }
    public String getDescription()                 { return description; }
    public String getStatus()                      { return status; }
    public String getServiceType()                  { return serviceType; }
    public AestheticDetailsEntity getAestheticDetail()      { return aestheticDetail; }
    public ImplantologyDetailEntity getImplantologyDetail() { return implantologyDetail; }
    public OrthodonticDetailEntity getOrthodonticDetail()   { return orthodonticDetail; }
    public PediatricDetailEntity getPediatricDetail()       { return pediatricDetail; }
    public ProstheticDetailEntity getProstheticDetail()     { return prostheticDetail; }
    public SurgeryDetailEntity getSurgeryDetail()           { return surgeryDetail; }

    
     public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name)                                   { this.name = name; }
    public void setCategory(String category)                           { this.category = category; }
    public void setCode(String code)                                   { this.code = code; }
    public void setBaseRate(BigDecimal baseRate)                       { this.baseRate = baseRate; }
    public void setBaseRateCurrency(String baseRateCurrency)           { this.baseRateCurrency = baseRateCurrency; }
    public void setDurationMinutes(int durationMinutes)                { this.durationMinutes = durationMinutes; }
    public void setRequiresAuthorization(boolean requiresAuthorization) { this.requiresAuthorization = requiresAuthorization; }
    public void setDescription(String description)                     { this.description = description; }
    public void setStatus(String status)                               { this.status = status; }
    public void setServiceType(String serviceType)                     { this.serviceType = serviceType; }
    public void setAestheticDetail(AestheticDetailsEntity aestheticDetail)           { this.aestheticDetail = aestheticDetail; }
    public void setImplantologyDetail(ImplantologyDetailEntity implantologyDetail) { this.implantologyDetail = implantologyDetail; }
    public void setOrthodonticDetail(OrthodonticDetailEntity orthodonticDetail)    { this.orthodonticDetail = orthodonticDetail; }
    public void setPediatricDetail(PediatricDetailEntity pediatricDetail)          { this.pediatricDetail = pediatricDetail; }
    public void setProstheticDetail(ProstheticDetailEntity prostheticDetail)       { this.prostheticDetail = prostheticDetail; }
    public void setSurgeryDetail(SurgeryDetailEntity surgeryDetail)                { this.surgeryDetail = surgeryDetail; }
}