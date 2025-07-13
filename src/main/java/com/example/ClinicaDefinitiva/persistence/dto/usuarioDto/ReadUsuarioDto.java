package com.example.ClinicaDefinitiva.persistence.dto.usuarioDto;




import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Roles;

import java.time.LocalDate;
import java.util.Date;

public class ReadUsuarioDto {
    private long id;
    private String nombreUsuario;
    private String correoEletronico;
    private LocalDate fechaDeCreacion;
    private Estado estado;
    private String imagenPerfil;
    private LocalDate ultimaFechaDeCoexion;
    private Roles rol;

    public ReadUsuarioDto(){

    }
    public ReadUsuarioDto(Estado estado, String correoEletronico, long id
            , LocalDate fechaDeCreacion, String imagenPerfil, Roles rol
            , String nombreUsuario, LocalDate ultimaFechaDeCoexion) {
        this.estado = estado;
        this.correoEletronico = correoEletronico;
        this.id = id;
        this.fechaDeCreacion = fechaDeCreacion;
        this.imagenPerfil = imagenPerfil;
        this.rol = rol;
        this.nombreUsuario = nombreUsuario;
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }

    public void setCorreoEletronico(String correoEletronico) {
        this.correoEletronico = correoEletronico;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setFechaDeCreacion(LocalDate fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }


    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public void setUltimaFechaDeCoexion(LocalDate ultimaFechaDeCoexion) {
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }

    public String getCorreoEletronico() {
        return correoEletronico;
    }

    public Estado getEstado() {
        return estado;
    }

    public LocalDate getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Roles getRol() {
        return rol;
    }

    public LocalDate getUltimaFechaDeCoexion() {
        return ultimaFechaDeCoexion;
    }
}
