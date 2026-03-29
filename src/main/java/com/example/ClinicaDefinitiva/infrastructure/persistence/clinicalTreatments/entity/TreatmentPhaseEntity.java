package com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fase_tratamiento")
public class TreatmentPhaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tratamiento", nullable = false)
    private TreatmentEntity treatment;

    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "fecha_planificada")
    private LocalDate plannedDate;

    @Column(name = "fecha_completada")
    private LocalDate completedDate;

    public TreatmentPhaseEntity() {}

    public Long getId()                           { return id; }
    public TreatmentEntity getTreatment()          { return treatment; }
    public String getName()                        { return name; }
    public String getDescription()                 { return description; }
    public String getStatus()                      { return status; }
    public LocalDate getPlannedDate()              { return plannedDate; }
    public LocalDate getCompletedDate()            { return completedDate; }

    public void setTreatment(TreatmentEntity treatment)    { this.treatment = treatment; }
    public void setName(String name)                       { this.name = name; }
    public void setDescription(String description)         { this.description = description; }
    public void setStatus(String status)                   { this.status = status; }
    public void setPlannedDate(LocalDate plannedDate)      { this.plannedDate = plannedDate; }
    public void setCompletedDate(LocalDate completedDate)  { this.completedDate = completedDate; }
}