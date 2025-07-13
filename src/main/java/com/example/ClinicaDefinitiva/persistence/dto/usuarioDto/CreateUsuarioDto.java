package com.example.ClinicaDefinitiva.persistence.dto.usuarioDto;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.Enumvalidation.EstadoValido;
import com.example.ClinicaDefinitiva.Enumvalidation.RolValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class CreateUsuarioDto {

    @NotBlank(message = " No puede estar vacio")
    @Pattern(regexp = "^[a-zA-Z0-9]{2,12}$", message = "El nombre debe tener entre 2 y 12 caracteres alfanuméricos")
    @NotNull(message = " No puede ser nulo")
    private String nombreUsuario;

    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", message = "El correo debe ser valido")
    @NotNull(message = " No puede ser nulo")
    @NotBlank(message = " No puede estar vacio")
    private String correoEletronico;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{8,}$",
            message = "Debe tener al menos una minúscula, una mayúscula y un carácter especial"
    )
    @NotNull(message = " La contrasena no puede ser nulo")
    @NotBlank(message = " No  contrase no puede esta vacio")
    private String contrasena;

    @NotNull(message = " El estado puede ser null")
    @EstadoValido
    private Estado estado;

    private String imagenPerfil;
    private LocalDate ultimaFechaDeCoexion;

    @NotNull(message = " No puede ser nulo")
    @RolValido
    private Roles rol;


    public CreateUsuarioDto(){

    }
    public CreateUsuarioDto(String contrasena, String correoEletronico,
                            Estado estado, String nombreUsuario
            , String imagenPerfil, Roles rol, LocalDate ultimaFechaDeCoexion) {
        this.contrasena = contrasena;
        this.correoEletronico = correoEletronico;
        this.estado = estado;
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


    public String getContrasena() {
        return contrasena;
    }

    public String getCorreoEletronico() {
        return correoEletronico;
    }

    public Estado getEstado() {
        return estado;
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
}
