package com.example.ClinicaDefinitiva.persistence.entity;

import jakarta.persistence.*;


import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Entity

public class Horario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DayOfWeek diaSemana; // Define el día de la semana (lunes, Martes...)
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean estado;

    @OneToMany(mappedBy = "horario")
    private List<Turno> turnos; // Horario conoce los turnos asignados a ese bloque

    @OneToOne()
    @JoinColumn(name = "id_odontologo")
    private Odontologo unOdontologo;


    public Horario(){}

    public Horario(DayOfWeek diaSemana, boolean estado, LocalTime horaFin, LocalTime horaInicio, Long id, List<Turno> turnos, Odontologo unOdontologo) {
        this.diaSemana = diaSemana;
        this.estado = estado;
        this.horaFin = horaFin;
        this.horaInicio = horaInicio;
        this.id = id;
        this.turnos = turnos;
        this.unOdontologo = unOdontologo;
    }

    public Odontologo getUnOdontologo() {
        return unOdontologo;
    }

    public void setUnOdontologo(Odontologo unOdontologo) {
        this.unOdontologo = unOdontologo;
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

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }

}