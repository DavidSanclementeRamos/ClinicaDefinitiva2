package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.Enum.Afeccion;
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
    private Long id_turno;
    @Column(nullable = false)
    private LocalDate fecha_turno;
    private LocalTime hora_turno;

    @ManyToOne
    @JoinColumn(name = "id_odontologo") // Clave foránea en la entidad dependiente
    private Odontologo odontonlogo;

    @ManyToOne
    @JoinColumn(name = "id_paciente") // Clave foránea en la entidad dependiente
    private Paciente paciente;
    @Enumerated(EnumType.STRING)
    private Afeccion afeccion;
    @ManyToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;

    public Turno(){

    }

    public Turno(Afeccion afeccion, LocalDate fecha_turno, LocalTime hora_turno, Long id_turno, Odontologo odontonlogo, Paciente paciente,Horario horario) {
        this.afeccion = afeccion;
        this.fecha_turno = fecha_turno;
        this.hora_turno = hora_turno;
        this.id_turno = id_turno;
        this.odontonlogo = odontonlogo;
        this.paciente = paciente;
        this.horario = horario;
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

    public LocalDate getFecha_turno() {
        return fecha_turno;
    }

    public void setFecha_turno(LocalDate fecha_turno) {
        this.fecha_turno = fecha_turno;
    }

    public LocalTime getHora_turno() {
        return hora_turno;
    }

    public void setHora_turno(LocalTime hora_turno) {
        this.hora_turno = hora_turno;
    }

    public Long getId_turno() {
        return id_turno;
    }

    public void setId_turno(Long id_turno) {
        this.id_turno = id_turno;
    }

    public Odontologo getOdontonlogo() {
        return odontonlogo;
    }

    public void setOdontonlogo(Odontologo odontonlogo) {
        this.odontonlogo = odontonlogo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
