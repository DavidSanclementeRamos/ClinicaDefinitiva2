package com.example.ClinicaDefinitiva.persistence.entity;



import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Odontologo extends Persona implements Serializable {

    @Enumerated(EnumType.STRING)
    private Especialidades especialidad;

    @OneToOne
    @JoinColumn(name = "usuario_id") // Clave foránea está en la clase hija
    private Usuario unUsuario;

    @OneToMany(mappedBy = "unOdontologo",  cascade = CascadeType.ALL, orphanRemoval = true) // No  almacena clave foránea, solo referencia
    private List<Disponibilidad> listaDisponibilidads = new ArrayList<>();

    @OneToMany(mappedBy = "odontologo") // No  almacena clave foránea, solo referencia
    private List<Turno> listaTurnos =  new ArrayList<>();    // NO SE PONE LISTA, PARA NO PERJUDICAR EL RENDIMIENTO


    public Odontologo() {

    }

    public Odontologo(String apellido, String direccion, String dni, LocalDate fecha_nacimiento, Long id, String nombre, String telefono, Tipo_sangre tipoSangre, Especialidades especialidad, List<Disponibilidad> listaDisponibilidads, List<Turno> listaTurnos, Usuario unUsuario) {
        super(apellido, direccion, dni, fecha_nacimiento, id, nombre, telefono, tipoSangre);
        this.especialidad = especialidad;
        this.listaDisponibilidads = listaDisponibilidads;
        this.listaTurnos = listaTurnos;
        this.unUsuario = unUsuario;
    }
    public void eliminarHorario(Disponibilidad disponibilidad) {
        if (disponibilidad != null && this.listaDisponibilidads.contains(disponibilidad)) {
            disponibilidad.setUnOdontologo(null); // romper la relación inversa
            this.listaDisponibilidads.remove(disponibilidad); // quitar de la colección
        }
    }
    public List<Turno> getListaTurnos() {
        return listaTurnos;
    }

    public void setListaTurnos(List<Turno> listaTurnos) {
        this.listaTurnos = listaTurnos;
    }

    public List<Disponibilidad> getListaHorarios() {
        return listaDisponibilidads;
    }

    public void setListaHorarios(List<Disponibilidad> listaDisponibilidads) {
        this.listaDisponibilidads = listaDisponibilidads;
    }

    public Especialidades getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidades especialidad) {
        this.especialidad = especialidad;
    }


    public Usuario getUnUsuario() {
        return unUsuario;
    }

    public void setUnUsuario(Usuario unUsuario) {
        this.unUsuario = unUsuario;
    }
}