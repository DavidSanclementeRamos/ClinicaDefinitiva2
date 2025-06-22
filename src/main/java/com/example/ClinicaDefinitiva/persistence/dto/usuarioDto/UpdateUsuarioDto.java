package com.example.ClinicaDefinitiva.persistence.dto.usuarioDto;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Roles;
import jakarta.validation.constraints.NotNull;

public class UpdateUsuarioDto {
    @NotNull(message = " No puede ser nulo")
    private String correoEletronico;
    @NotNull(message = " No puede ser nulo")
    private String contrasena;
    private String imagenPerfil;
    @NotNull(message = " No puede ser nulo")
    private Roles rol;
    private Estado estado;
    private String nombreUser;

    public UpdateUsuarioDto(){

    }
    public UpdateUsuarioDto(String contrasena, String correoEletronico
            , Estado estado, String imagenPerfil, Roles rol, String nombreUser) {
        this.contrasena = contrasena;
        this.correoEletronico = correoEletronico;
        this.estado = estado;
        this.imagenPerfil = imagenPerfil;
        this.rol = rol;
        this.nombreUser = nombreUser;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
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
}
