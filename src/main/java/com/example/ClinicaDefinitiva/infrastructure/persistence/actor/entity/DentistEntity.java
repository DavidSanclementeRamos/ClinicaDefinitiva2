package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity;


import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity.ShiftEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;

@Entity
@Table(name = "dentista")
public class DentistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_identidad", nullable = false)
    private UserIdentityEntity userIdentity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turno")
    private ShiftEntity shift;

    @Embedded
    private PersonEmbeddable person;

    @Column(name = "estado_disponibilidad", nullable = false, length = 30)
    private String availabilityStatus;

    @Column(name = "especialidades", length = 200)
    private String specialties;

    @Column(name = "horas_trabajo_json", columnDefinition = "TEXT")
    private String workHoursJson;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime lastUpdate;

    @Column(name = "inicio_vacaciones")
    private LocalDateTime vacationStart;

    @Column(name = "fin_vacaciones")
    private LocalDateTime vacationEnd;

    @Column(name = "inicio_incapacidad")
    private LocalDateTime disabilityStart;

    @Column(name = "fin_incapacidad")
    private LocalDateTime disabilityEnd;

    @Column(name = "nota_incapacidad", columnDefinition = "TEXT")
    private String disabilityNote;

    public DentistEntity() {}

    public Long getId()                      { return id; }
    public UserIdentityEntity getUserIdentity()   { return userIdentity; }
    public ShiftEntity getShift()             { return shift; }
    public PersonEmbeddable getPerson()       { return person; }
    public String getAvailabilityStatus()     { return availabilityStatus; }
    public String getSpecialties()            { return specialties; }
    public String getWorkHoursJson()          { return workHoursJson; }
    public LocalDateTime getLastUpdate()      { return lastUpdate; }
    public LocalDateTime getVacationStart()   { return vacationStart; }
    public LocalDateTime getVacationEnd()     { return vacationEnd; }
    public LocalDateTime getDisabilityStart() { return disabilityStart; }
    public LocalDateTime getDisabilityEnd()   { return disabilityEnd; }
    public String getDisabilityNote()         { return disabilityNote; }

    public void setId(Long id) {
        this.id = id;
    }

    
    
    public void setUserIdentity(UserIdentityEntity userIdentity)      { this.userIdentity = userIdentity; }
    public void setShift(ShiftEntity shift)                           { this.shift = shift; }
    public void setPerson(PersonEmbeddable person)                    { this.person = person; }
    public void setAvailabilityStatus(String availabilityStatus)      { this.availabilityStatus = availabilityStatus; }
    public void setSpecialties(String specialties)                    { this.specialties = specialties; }
    public void setWorkHoursJson(String workHoursJson)                { this.workHoursJson = workHoursJson; }
    public void setLastUpdate(LocalDateTime lastUpdate)               { this.lastUpdate = lastUpdate; }
    public void setVacationStart(LocalDateTime vacationStart)         { this.vacationStart = vacationStart; }
    public void setVacationEnd(LocalDateTime vacationEnd)             { this.vacationEnd = vacationEnd; }
    public void setDisabilityStart(LocalDateTime disabilityStart)     { this.disabilityStart = disabilityStart; }
    public void setDisabilityEnd(LocalDateTime disabilityEnd)         { this.disabilityEnd = disabilityEnd; }
    public void setDisabilityNote(String disabilityNote)              { this.disabilityNote = disabilityNote; }
}