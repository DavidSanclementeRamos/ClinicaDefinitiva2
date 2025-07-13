package com.example.ClinicaDefinitiva.persistence.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioDto {

    private long id;

    @Temporal(TemporalType.TIME)
    @NotNull(message = " La hora de inicio no puede ser nulo")
    @NotBlank(message = " La hora de inicio o puede estar vacio")
    private LocalTime horaInicio;

    @Temporal(TemporalType.TIME)
    @NotNull(message = " La hora final no puede ser nulo")
    @NotBlank(message = " La hora final no puede estar vacio")
    private LocalTime horaFin;

    @NotNull(message = " El estado no puede ser nulo")
    @NotBlank(message = " El estado no puede estar vacio")
    private boolean estado;

    @Enumerated(EnumType.STRING)
    @NotNull(message = " No puede ser nulo")
    private DayOfWeek diaSemana;

    private long idOdontologo;

    public HorarioDto(DayOfWeek diaSemana, boolean estado, LocalTime horaFin, LocalTime horaInicio, long id, long idOdontologo) {
        this.diaSemana = diaSemana;
        this.estado = estado;
        this.horaFin = horaFin;
        this.horaInicio = horaInicio;
        this.id = id;
        this.idOdontologo = idOdontologo;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalTime getHoraFinal() {
        return horaFin;
    }

    public void setHoraFinal(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horarInicio) {
        this.horaInicio = horarInicio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdOdontologo() {
        return idOdontologo;
    }

    public void setIdOdontologo(long idOdontologo) {
        this.idOdontologo = idOdontologo;
    }
}
