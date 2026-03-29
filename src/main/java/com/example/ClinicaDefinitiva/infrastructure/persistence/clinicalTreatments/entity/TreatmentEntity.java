package com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.entity;

import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.DentistEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PatientEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.RateEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.DentalServiceEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tratamiento")
public class TreatmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dentista", nullable = false)
    private DentistEntity dentist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servicio", nullable = false)
    private DentalServiceEntity dentalService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarifa")
    private RateEntity rate;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate startDate;

    @Column(name = "fecha_fin_esperada")
    private LocalDate expectedEndDate;

    @Column(name = "fecha_fin_real")
    private LocalDate actualEndDate;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TreatmentPhaseEntity> phases = new ArrayList<>();

    public TreatmentEntity() {}

    public Long getId()                           { return id; }
    public PatientEntity getPatient()              { return patient; }
    public DentistEntity getDentist()              { return dentist; }
    public DentalServiceEntity getDentalService()  { return dentalService; }
    public RateEntity getRate()                    { return rate; }
    public String getStatus()                      { return status; }
    public LocalDate getStartDate()                { return startDate; }
    public LocalDate getExpectedEndDate()          { return expectedEndDate; }
    public LocalDate getActualEndDate()            { return actualEndDate; }
    public String getNotes()                       { return notes; }
    public List<TreatmentPhaseEntity> getPhases()  { return phases; }

    public void setPatient(PatientEntity patient)                         { this.patient = patient; }
    public void setDentist(DentistEntity dentist)                         { this.dentist = dentist; }
    public void setDentalService(DentalServiceEntity dentalService)       { this.dentalService = dentalService; }
    public void setRate(RateEntity rate)                                  { this.rate = rate; }
    public void setStatus(String status)                                  { this.status = status; }
    public void setStartDate(LocalDate startDate)                         { this.startDate = startDate; }
    public void setExpectedEndDate(LocalDate expectedEndDate)             { this.expectedEndDate = expectedEndDate; }
    public void setActualEndDate(LocalDate actualEndDate)                 { this.actualEndDate = actualEndDate; }
    public void setNotes(String notes)                                    { this.notes = notes; }
    public void setPhases(List<TreatmentPhaseEntity> phases)              { this.phases = phases; }
}
