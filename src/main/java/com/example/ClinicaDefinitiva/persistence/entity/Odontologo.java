package com.example.ClinicaDefinitiva.persistence.entity;



import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Entity

public class Odontologo extends Persona implements Serializable {

    @Enumerated(EnumType.STRING)
    private Especialidades especialidad;

    @OneToOne
    @JoinColumn(name = "usuario_id") // Clave foránea está en la clase hija
    private Usuario unUsuario;

    @OneToOne(mappedBy = "unOdontologo") // No  almacena clave foránea, solo referencia
    private Horario unHorario;

    @OneToMany(mappedBy = "odontologo") // No  almacena clave foránea, solo referencia
    private List<Turno> unTurno;     // NO SE PONE LISTA, PARA NO PERJUDICAR EL RENDIMIENTO



    public Odontologo() {

    }

    public Odontologo(String apellido, String direcion, String dni, LocalDate fecha_nacimiento, int id, String nombre, String telefono, Tipo_sangre tipoSangre, Especialidades especialidad, Horario unHorario, List<Turno> unTurno, Usuario unUsuario) {
        super(apellido, direcion, dni, fecha_nacimiento, id, nombre, telefono, tipoSangre);
        this.especialidad = especialidad;
        this.unHorario = unHorario;
        this.unTurno = unTurno;
        this.unUsuario = unUsuario;
    }

    public Especialidades getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidades especialidad) {
        this.especialidad = especialidad;
    }

    public Horario getUnHorario() {
        return unHorario;
    }

    public void setUnHorario(Horario unHorario) {
        this.unHorario = unHorario;
    }

    public List<Turno> getUnTurno() {
        return unTurno;
    }

    public void setUnTurno(List<Turno> unTurno) {
        this.unTurno = unTurno;
    }

    public Usuario getUnUsuario() {
        return unUsuario;
    }

    public void setUnUsuario(Usuario unUsuario) {
        this.unUsuario = unUsuario;
    }
}