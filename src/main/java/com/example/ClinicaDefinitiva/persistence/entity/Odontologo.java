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
    private List<Horario> listaHorarios = new ArrayList<>();

    @OneToMany(mappedBy = "odontologo") // No  almacena clave foránea, solo referencia
    private List<Turno> listaTurnos =  new ArrayList<>();    // NO SE PONE LISTA, PARA NO PERJUDICAR EL RENDIMIENTO



    public Odontologo() {

    }

    public Odontologo(String apellido, String direccion, String dni, LocalDate fecha_nacimiento, Long id, String nombre, String telefono, Tipo_sangre tipoSangre, Especialidades especialidad, List<Horario> listaHorarios, List<Turno> listaTurnos, Usuario unUsuario) {
        super(apellido, direccion, dni, fecha_nacimiento, id, nombre, telefono, tipoSangre);
        this.especialidad = especialidad;
        this.listaHorarios = listaHorarios;
        this.listaTurnos = listaTurnos;
        this.unUsuario = unUsuario;
    }
    public void eliminarHorario(Horario horario) {
        if (horario != null && this.listaHorarios.contains(horario)) {
            horario.setUnOdontologo(null); // romper la relación inversa
            this.listaHorarios.remove(horario); // quitar de la colección
        }
    }
    public List<Turno> getListaTurnos() {
        return listaTurnos;
    }

    public void setListaTurnos(List<Turno> listaTurnos) {
        this.listaTurnos = listaTurnos;
    }

    public List<Horario> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(List<Horario> listaHorarios) {
        this.listaHorarios = listaHorarios;
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