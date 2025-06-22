package com.example.ClinicaDefinitiva.persistence.dto.usuarioDto;




import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Roles;

import java.util.Date;

public class ReadUsuarioDto {
    private long id_usuario;
    private String nombreUsuario;
    private String correoEletronico;
    private Date fechaDeCreacion;
    private Estado estado;
    private String imagenPerfil;
    private Date ultimaFechaDeCoexion;
    private Roles rol;

    public ReadUsuarioDto(){

    }
    public ReadUsuarioDto(Estado estado, String correoEletronico, long id_usuario
            , Date fechaDeCreacion, String imagenPerfil, Roles rol
            , String nombreUsuario, Date ultimaFechaDeCoexion) {
        this.estado = estado;
        this.correoEletronico = correoEletronico;
        this.id_usuario = id_usuario;
        this.fechaDeCreacion = fechaDeCreacion;
        this.imagenPerfil = imagenPerfil;
        this.rol = rol;
        this.nombreUsuario = nombreUsuario;
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
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

    public Long getId_usuario() {
        return id_usuario;
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
}
