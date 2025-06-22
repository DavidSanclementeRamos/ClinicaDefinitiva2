package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;


@MappedSuperclass
public class Persona implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direcion;
    @Column(nullable = false)
    private LocalDate fecha_nacimiento;
    @Enumerated(EnumType.STRING)
    private Tipo_sangre tipoSangre;

    public Persona() {

    }

    public Persona(String apellido, String direcion, String dni
            , LocalDate fecha_nacimiento, int id, String nombre, String telefono,Tipo_sangre tipoSangre ) {
        this.apellido = apellido;
        this.direcion = direcion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo_sangre getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(Tipo_sangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}