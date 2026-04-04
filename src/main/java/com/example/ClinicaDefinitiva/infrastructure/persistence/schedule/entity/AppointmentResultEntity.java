package com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resultado_cita")
public class AppointmentResultEntity {

    @Id
    @Column(name = "id_cita", updatable = false, nullable = false)
    private Long appointmentId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_cita")
    private AppointmentEntity appointment;

    @Column(name = "notas_clinicas", columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "duracion_real_minutos")
    private Integer actualDurationMinutes;

    @Column(name = "proximos_pasos", columnDefinition = "TEXT")
    private String nextSteps;

    public AppointmentResultEntity() {}

    public Long getAppointmentId()              { return appointmentId; }
    public AppointmentEntity getAppointment()    { return appointment; }
    public String getClinicalNotes()            { return clinicalNotes; }
    public String getDiagnosis()                { return diagnosis; }
    public Integer getActualDurationMinutes()   { return actualDurationMinutes; }
    public String getNextSteps()                { return nextSteps; }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }


    public void setAppointment(AppointmentEntity appointment)      { this.appointment = appointment; }
    public void setClinicalNotes(String clinicalNotes)            { this.clinicalNotes = clinicalNotes; }
    public void setDiagnosis(String diagnosis)                    { this.diagnosis = diagnosis; }
    public void setActualDurationMinutes(Integer actualDurationMinutes) { this.actualDurationMinutes = actualDurationMinutes; }
    public void setNextSteps(String nextSteps)                    { this.nextSteps = nextSteps; }
}
