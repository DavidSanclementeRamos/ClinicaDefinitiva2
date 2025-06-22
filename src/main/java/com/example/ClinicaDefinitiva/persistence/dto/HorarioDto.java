package com.example.ClinicaDefinitiva.persistence.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioDto {

    private long id;
    @NotNull(message = " No puede ser nulo")
    private LocalTime horarInicio;
    @NotNull(message = " No puede ser nulo")
    private LocalTime horaFinal;
    @NotNull(message = " No puede ser nulo")
    private boolean estado;
    @NotNull(message = " No puede ser nulo")

    private DayOfWeek diaSemana;
    private long idOdontologo;

    public HorarioDto(DayOfWeek diaSemana, boolean estado, LocalTime horaFinal, LocalTime horarInicio, long id, long idOdontologo) {
        this.diaSemana = diaSemana;
        this.estado = estado;
        this.horaFinal = horaFinal;
        this.horarInicio = horarInicio;
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
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public LocalTime getHorarInicio() {
        return horarInicio;
    }

    public void setHorarInicio(LocalTime horarInicio) {
        this.horarInicio = horarInicio;
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
