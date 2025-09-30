package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.vo.EstadoTurno;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Turno implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDate fechaTurno;
    private LocalTime horaTurno;

    @ManyToOne
    @JoinColumn(name = "id_odontologo") // Clave foránea en la entidad dependiente
    private Odontologo odontologo;

    @ManyToOne
    @JoinColumn(name = "id_paciente") // Clave foránea en la entidad dependiente
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "horario_id")
    private Disponibilidad disponibilidad;


    private EstadoTurno estadoTurno;


    public Turno(){

    }

    public Turno(LocalDate fechaTurno, LocalTime horaTurno, Long id, Odontologo odontologo, Paciente paciente, Disponibilidad disponibilidad, EstadoTurno estadoTurno) {

        this.fechaTurno = fechaTurno;
        this.horaTurno = horaTurno;
        this.id = id;
        this.odontologo = odontologo;
        this.paciente = paciente;
        this.disponibilidad = disponibilidad;
        this.estadoTurno = estadoTurno;
    }


    public Disponibilidad getHorario() {
        return disponibilidad;
    }

    public void setHorario(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public LocalDate getFechaTurno() {
        return fechaTurno;
    }


    public EstadoTurno getEstadoTurno() {
        return estadoTurno;
    }

    public void setEstadoTurno(EstadoTurno estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(Odontologo odontologo) {
        this.odontologo = odontologo;
    }

    public void setFechaTurno(LocalDate fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getHoraTurno() {
        return horaTurno;
    }

    public void setHoraTurno(LocalTime horaTurno) {
        this.horaTurno = horaTurno;
    }

    public Long getId_turno() {
        return id;
    }

    public void setId_turno(Long id_turno) {
        this.id = id_turno;
    }


    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
