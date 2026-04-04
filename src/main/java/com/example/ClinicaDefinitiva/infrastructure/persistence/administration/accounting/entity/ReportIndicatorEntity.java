
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "reporte_indicador")
public class ReportIndicatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reporte", nullable = false)
    private AdministrativeReportEntity report;

    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Column(name = "valor", nullable = false, length = 100)
    private String value;

    @Column(name = "unidad", length = 30)
    private String unit;

    public ReportIndicatorEntity() {}

    public Long getId()                           { return id; }
    public AdministrativeReportEntity getReport()  { return report; }
    public String getName()                        { return name; }
    public String getValue()                       { return value; }
    public String getUnit()                        { return unit; }

     public void setId(Long id) {
        this.id = id;
    }
    public void setReport(AdministrativeReportEntity report)  { this.report = report; }
    public void setName(String name)                           { this.name = name; }
    public void setValue(String value)                         { this.value = value; }
    public void setUnit(String unit)                           { this.unit = unit; }
}
