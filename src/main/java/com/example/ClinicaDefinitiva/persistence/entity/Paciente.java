package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Paciente extends Persona implements Serializable {

    private boolean tiene_Os;

    @OneToOne
    private Responsable unResponsable;

    private boolean tieneResponsable;

    @OneToMany(mappedBy = "paciente") // solo indica quién gestiona la relación
    private List<Turno> unTurno = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "usuario_id") // Clave foránea está en la clase hija
    private Usuario unUsuario;

    @ManyToOne()
    @JoinColumn( name = "id_responsable")
    private Responsable responsable;


    public Paciente() {

    }

    public Paciente(String apellido, String direccion, String dni, LocalDate fecha_nacimiento, Long id, String nombre, String telefono, Tipo_sangre tipoSangre, Responsable responsable, boolean tiene_Os, boolean tieneResponsable, Responsable unResponsable, List<Turno> unTurno, Usuario unUsuario) {
        super(apellido, direccion, dni, fecha_nacimiento, id, nombre, telefono, tipoSangre);
        this.responsable = responsable;
        this.tiene_Os = tiene_Os;
        this.tieneResponsable = tieneResponsable;
        this.unResponsable = unResponsable;
        this.unTurno = unTurno;
        this.unUsuario = unUsuario;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    public boolean isTiene_Os() {
        return tiene_Os;
    }

    public void setTiene_Os(boolean tiene_Os) {
        this.tiene_Os = tiene_Os;
    }

    public boolean isTieneResponsable() {
        return tieneResponsable;
    }

    public void setTieneResponsable(boolean tieneResponsable) {
        this.tieneResponsable = tieneResponsable;
    }


    public Responsable getUnResponsable() {
        return unResponsable;
    }

    public void setUnResponsable(Responsable unResponsable) {
        this.unResponsable = unResponsable;
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