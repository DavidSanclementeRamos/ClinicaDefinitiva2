package com.example.ClinicaDefinitiva.persistence.dto;



import com.example.ClinicaDefinitiva.Enum.Afeccion;
import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enumvalidation.AfeccionValido;
import com.example.ClinicaDefinitiva.Enumvalidation.EstadoValido;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoDto {

    private long id;

    @Temporal(TemporalType.DATE)
    @NotBlank(message = " La fecha no puede estar vacio")
    @NotNull(message = " La fecha no puede ser nulo")
    private LocalDate fechaTurno;

    @NotBlank(message = " La hora no puede estar vacio")
    @Temporal(TemporalType.TIME)
    @NotNull(message = "La hora no puede ser nulo")
    private LocalTime hora_turno;

    @NotNull(message = " No puede ser nulo")
    @AfeccionValido
    private Afeccion afeccion;

    @EstadoValido
    @NotNull(message = " No puede ser nulo")
    private Estado estado;

    private long idPaciente;
    private long horarioId;
    private long odontologo;


    public TurnoDto() {

    }

    public TurnoDto(Afeccion afeccion, LocalDate fechaTurno, LocalTime hora_turno
            , long idPaciente , long id, long horarioId, long odontologo, Estado estado) {
        this.afeccion = afeccion;
        this.fechaTurno = fechaTurno;
        this.hora_turno = hora_turno;
        this.idPaciente = idPaciente;
        this.id = id;
        this.horarioId = horarioId;
        this.odontologo = odontologo;
        this.estado = estado;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public long getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(long odontologo) {
        this.odontologo = odontologo;
    }

    public long getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(long horarioId) {
        this.horarioId = horarioId;
    }

    public Afeccion getAfeccion() {
        return afeccion;
    }

    public void setAfeccion(Afeccion afeccion) {
        this.afeccion = afeccion;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public LocalDate getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(LocalDate fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public LocalTime getHora_turno() {
        return hora_turno;
    }

    public void setHora_turno(LocalTime hora_turno) {
        this.hora_turno = hora_turno;
    }

}