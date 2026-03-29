
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;


import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "reporte_administrativo")
public class AdministrativeReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_creado_por", nullable = false)
    private UserIdentityEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aprobado_por")
    private UserIdentityEntity approvedBy;

    @Column(name = "titulo", nullable = false, length = 200)
    private String title;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodStart;

    @Column(name = "periodo_fin", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "estado", nullable = false, length = 30)
    private String status;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime lastUpdated;

    /**
     * Referencias a asientos contables modeladas como @ManyToMany con tabla pivote.
     * ADR-54: relación explícita en lugar de @ElementCollection de UUIDs,
     * para permitir navegación bidireccional y futuros índices.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "reporte_referencia_asiento",
        joinColumns = @JoinColumn(name = "id_reporte"),
        inverseJoinColumns = @JoinColumn(name = "id_asiento_contable")
    )
    private Set<JournalEntryEntity> referencedEntries = new HashSet<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportIndicatorEntity> indicators = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportAttachmentEntity> attachments = new ArrayList<>();

    public AdministrativeReportEntity() {}

    public Long getId()                                 { return id; }
    public UserIdentityEntity getCreatedBy()            { return createdBy; }
    public UserIdentityEntity getApprovedBy()           { return approvedBy; }
    public String getTitle()                            { return title; }
    public LocalDate getPeriodStart()                   { return periodStart; }
    public LocalDate getPeriodEnd()                     { return periodEnd; }
    public String getStatus()                           { return status; }
    public String getNotes()                            { return notes; }
    public LocalDateTime getCreatedAt()                 { return createdAt; }
    public LocalDateTime getLastUpdated()                { return lastUpdated; }
    public Set<JournalEntryEntity> getReferencedEntries() { return referencedEntries; }
    public List<ReportIndicatorEntity> getIndicators()  { return indicators; }
    public List<ReportAttachmentEntity> getAttachments() { return attachments; }

    public void setCreatedBy(UserIdentityEntity createdBy)                { this.createdBy = createdBy; }
    public void setApprovedBy(UserIdentityEntity approvedBy)              { this.approvedBy = approvedBy; }
    public void setTitle(String title)                                    { this.title = title; }
    public void setPeriodStart(LocalDate periodStart)                     { this.periodStart = periodStart; }
    public void setPeriodEnd(LocalDate periodEnd)                         { this.periodEnd = periodEnd; }
    public void setStatus(String status)                                  { this.status = status; }
    public void setNotes(String notes)                                    { this.notes = notes; }
    public void setCreatedAt(LocalDateTime createdAt)                     { this.createdAt = createdAt; }
    public void setLastUpdated(LocalDateTime lastUpdated)                 { this.lastUpdated = lastUpdated; }
    public void setReferencedEntries(Set<JournalEntryEntity> referencedEntries) { this.referencedEntries = referencedEntries; }
    public void setIndicators(List<ReportIndicatorEntity> indicators)    { this.indicators = indicators; }
    public void setAttachments(List<ReportAttachmentEntity> attachments) { this.attachments = attachments; }
}
