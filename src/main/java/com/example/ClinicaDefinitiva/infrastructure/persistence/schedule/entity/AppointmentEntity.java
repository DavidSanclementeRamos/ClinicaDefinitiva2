package com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.entity;

import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.DentistEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PatientEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.DentalServiceEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dentista", nullable = false)
    private DentistEntity dentist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servicio", nullable = false)
    private DentalServiceEntity dentalService;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "fecha_hora_fin", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "estado", nullable = false, length = 30)
    private String status;

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "tipo_cita", nullable = false, length = 30)
    private String appointmentType;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private AppointmentResultEntity result;

    public AppointmentEntity() {}

    public Long getId()                           { return id; }
    public DentistEntity getDentist()              { return dentist; }
    public PatientEntity getPatient()              { return patient; }
    public DentalServiceEntity getDentalService()  { return dentalService; }
    public LocalDateTime getStartDateTime()        { return startDateTime; }
    public LocalDateTime getEndDateTime()          { return endDateTime; }
    public String getStatus()                      { return status; }
    public String getReason()                      { return reason; }
    public String getAppointmentType()             { return appointmentType; }
    public LocalDateTime getCreatedAt()            { return createdAt; }
    public LocalDateTime getUpdatedAt()            { return updatedAt; }
    public AppointmentResultEntity getResult()     { return result; }

    public void setId(Long id) {
        this.id = id;
    }

    
    public void setDentist(DentistEntity dentist)                    { this.dentist = dentist; }
    public void setPatient(PatientEntity patient)                    { this.patient = patient; }
    public void setDentalService(DentalServiceEntity dentalService)  { this.dentalService = dentalService; }
    public void setStartDateTime(LocalDateTime startDateTime)        { this.startDateTime = startDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime)            { this.endDateTime = endDateTime; }
    public void setStatus(String status)                             { this.status = status; }
    public void setReason(String reason)                             { this.reason = reason; }
    public void setAppointmentType(String appointmentType)           { this.appointmentType = appointmentType; }
    public void setCreatedAt(LocalDateTime createdAt)                { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)                { this.updatedAt = updatedAt; }
    public void setResult(AppointmentResultEntity result)            { this.result = result; }
}
