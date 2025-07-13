package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombreUsuario;
    private String correoEletronico;
    private String contrasena;
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate fechaDeCreacion;
    private Estado estado;
    private String imagenPerfil;
    private LocalDate ultimaFechaDeCoexion;
    @Enumerated(EnumType.STRING)
    private Roles rol;

    public Usuario(){

    }
    public Usuario(String correoEletronico, String contrasena, Estado estado
            , LocalDate fechaDeCreacion, long id, String imagenPerfil
            , String nombreUsuario, Roles rol, LocalDate ultimaFechaDeCoexion) {
        this.correoEletronico = correoEletronico;
        this.contrasena = contrasena;
        this.estado = estado;
        this.fechaDeCreacion = fechaDeCreacion;
        this.id = id;
        this.imagenPerfil = imagenPerfil;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreoEletronico() {
        return correoEletronico;
    }

    public void setCorreoEletronico(String correoEletronico) {
        this.correoEletronico = correoEletronico;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDate getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(LocalDate fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public Roles getRol() {
        return rol;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public LocalDate getUltimaFechaDeCoexion() {
        return ultimaFechaDeCoexion;
    }

    public void setUltimaFechaDeCoexion(LocalDate ultimaFechaDeCoexion) {
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }
}
