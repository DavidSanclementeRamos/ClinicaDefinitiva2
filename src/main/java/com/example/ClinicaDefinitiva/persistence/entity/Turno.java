package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.Enum.Afeccion;
import com.example.ClinicaDefinitiva.Enum.Estado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    private Afeccion afeccion;

    @ManyToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;

    private Estado estado;

    public Turno(){

    }

    public Turno(Afeccion afeccion, LocalDate fechaTurno, LocalTime horaTurno, Long id, Odontologo odontologo, Paciente paciente,Horario horario,Estado estado) {
        this.afeccion = afeccion;
        this.fechaTurno = fechaTurno;
        this.horaTurno = horaTurno;
        this.id = id;
        this.odontologo = odontologo;
        this.paciente = paciente;
        this.horario = horario;
        this.estado = estado;
    }

    public Afeccion getAfeccion() {
        return afeccion;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public void setAfeccion(Afeccion afeccion) {
        this.afeccion = afeccion;
    }

    public LocalDate getFechaTurno() {
        return fechaTurno;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
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
