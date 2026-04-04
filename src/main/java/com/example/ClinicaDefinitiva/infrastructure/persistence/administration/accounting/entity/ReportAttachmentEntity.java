
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "reporte_adjunto")
public class ReportAttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_reporte", nullable = false)
    private AdministrativeReportEntity report;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String fileName;

    @Column(name = "url_archivo", nullable = false, columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "tipo_archivo", length = 50)
    private String fileType;

    public ReportAttachmentEntity() {}

    public Long getId()                           { return id; }
    public AdministrativeReportEntity getReport()  { return report; }
    public String getFileName()                    { return fileName; }
    public String getFileUrl()                     { return fileUrl; }
    public String getFileType()                    { return fileType; }

     public void setId(Long id) {
        this.id = id;
    }
    public void setReport(AdministrativeReportEntity report)  { this.report = report; }
    public void setFileName(String fileName)                   { this.fileName = fileName; }
    public void setFileUrl(String fileUrl)                     { this.fileUrl = fileUrl; }
    public void setFileType(String fileType)                   { this.fileType = fileType; }
}