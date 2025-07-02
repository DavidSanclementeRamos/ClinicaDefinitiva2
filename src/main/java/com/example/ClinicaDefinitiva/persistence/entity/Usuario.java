package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombreUsuario;
    private String correoEletronico;
    private String contrasena;
    private Date fechaDeCreacion;
    private Estado estado;
    private String imagenPerfil;
    private Date ultimaFechaDeCoexion;
    @Enumerated(EnumType.STRING)
    private Roles rol;

    public Usuario(){

    }
    public Usuario(String correoEletronico, String contrasena, Estado estado
            , Date fechaDeCreacion, long id, String imagenPerfil
            , String nombreUsuario, Roles rol, Date ultimaFechaDeCoexion) {
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

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
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

    public Date getUltimaFechaDeCoexion() {
        return ultimaFechaDeCoexion;
    }

    public void setUltimaFechaDeCoexion(Date ultimaFechaDeCoexion) {
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }
}
