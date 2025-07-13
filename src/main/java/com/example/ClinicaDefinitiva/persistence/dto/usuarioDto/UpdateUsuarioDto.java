package com.example.ClinicaDefinitiva.persistence.dto.usuarioDto;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enumvalidation.EstadoValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateUsuarioDto {
    @NotNull(message = " El correo no puede ser nulo")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", message = "El correo debe ser valido")
    @NotBlank(message = " El correo no puede estar vacio")
    private String correoEletronico;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{8,}$",
            message = "Debe tener al menos una minúscula, una mayúscula y un carácter especial"
    )
    @NotBlank(message = " La  contrase no puede esta vacio")
    @NotNull(message = " La contrasena no puede ser nulo")
    private String contrasena;

    private String imagenPerfil;

    @NotNull(message = " El estado no puede ser nulo")
    @EstadoValido
    private Estado estado;

    @NotBlank(message = " El user no puede estar vacio")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$", message = "El nombre debe tener entre 6 y 12 caracteres alfanuméricos")
    @NotNull(message = " El user no puede ser nulo")
    private String nombreUser;

    public UpdateUsuarioDto(){

    }
    public UpdateUsuarioDto(String contrasena, String correoEletronico
            , Estado estado, String imagenPerfil , String nombreUser) {
        this.contrasena = contrasena;
        this.correoEletronico = correoEletronico;
        this.estado = estado;
        this.imagenPerfil = imagenPerfil;
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


}
