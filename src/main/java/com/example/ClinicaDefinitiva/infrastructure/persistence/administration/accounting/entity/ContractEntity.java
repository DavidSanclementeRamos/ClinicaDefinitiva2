package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrato")
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tercero", nullable = false)
    private ThirdPartyEntity thirdParty;

    @Column(name = "nombre", nullable = false, length = 200)
    private String name;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "origen", length = 100)
    private String origin;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate endDate;

    @Column(name = "tipo_cobertura", nullable = false, length = 50)
    private String coverageType;

    @Column(name = "tasa_cobertura", precision = 5, scale = 2)
    private BigDecimal coverageRate;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    public ContractEntity() {}

    public Long getId()                   { return id; }
    public CompanyEntity getCompany()      { return company; }
    public ThirdPartyEntity getThirdParty(){ return thirdParty; }
    public String getName()                { return name; }
    public String getDescription()         { return description; }
    public String getOrigin()              { return origin; }
    public LocalDate getStartDate()        { return startDate; }
    public LocalDate getEndDate()          { return endDate; }
    public String getCoverageType()        { return coverageType; }
    public BigDecimal getCoverageRate()    { return coverageRate; }
    public String getStatus()              { return status; }

    public void setCompany(CompanyEntity company)                 { this.company = company; }
    public void setThirdParty(ThirdPartyEntity thirdParty)       { this.thirdParty = thirdParty; }
    public void setName(String name)                              { this.name = name; }
    public void setDescription(String description)                { this.description = description; }
    public void setOrigin(String origin)                          { this.origin = origin; }
    public void setStartDate(LocalDate startDate)                 { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate)                     { this.endDate = endDate; }
    public void setCoverageType(String coverageType)              { this.coverageType = coverageType; }
    public void setCoverageRate(BigDecimal coverageRate)          { this.coverageRate = coverageRate; }
    public void setStatus(String status)                          { this.status = status; }
}