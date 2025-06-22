package com.example.ClinicaDefinitiva.persistence.dto.usuarioDto;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Roles;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class CreateUsuarioDto {

    @NotNull(message = " No puede ser nulo")
    private String nombreUsuario;
    @NotNull(message = " No puede ser nulo")
    private String correoEletronico;
    @NotNull(message = " No puede ser nulo")
    private String contrasena;
    @NotNull(message = " No puede ser nulo")
    private Date fechaDeCreacion;
    @NotNull(message = " No puede ser nulo")
    private Estado estado;
    private String imagenPerfil;
    private Date ultimaFechaDeCoexion;
    @NotNull(message = " No puede ser nulo")
    private Roles rol;


    public CreateUsuarioDto(){

    }
    public CreateUsuarioDto(String contrasena, String correoEletronico,
                            Estado estado, Date fechaDeCreacion, String nombreUsuario
            , String imagenPerfil, Roles rol, Date ultimaFechaDeCoexion) {
        this.contrasena = contrasena;
        this.correoEletronico = correoEletronico;
        this.estado = estado;
        this.fechaDeCreacion = fechaDeCreacion;
        this.nombreUsuario = nombreUsuario;
        this.imagenPerfil = imagenPerfil;
        this.rol = rol;
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCorreoEletronico(String correoEletronico) {
        this.correoEletronico = correoEletronico;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreoEletronico() {
        return correoEletronico;
    }

    public Estado getEstado() {
        return estado;
    }

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
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

    public Date getUltimaFechaDeCoexion() {
        return ultimaFechaDeCoexion;
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

    public void setUltimaFechaDeCoexion(Date ultimaFechaDeCoexion) {
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }
}
