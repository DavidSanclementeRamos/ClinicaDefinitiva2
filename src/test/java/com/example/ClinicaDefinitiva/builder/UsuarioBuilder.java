package com.example.ClinicaDefinitiva.builder;

import com.example.ClinicaDefinitiva.persistence.entity.Usuario;

import java.time.LocalDate;

public class UsuarioBuilder implements IBuilder<Usuario>{

    // Valores por defecto para evitar NPE y mantener consistencia
    private String nombreUsuario = "defaultUser";
    private LocalDate fechaDeCreacion = LocalDate.of(2025, 1, 1);

    /**
     * Define el nombre de usuario.
     * @param nombreUsuario cadena con el nombre del usuario.
     * @return este builder para encadenamiento fluido.
     */
    public UsuarioBuilder withNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        return this;
    }

    /**
     * Define la fecha de creación del usuario.
     * @param fechaDeCreacion fecha en que fue creado.
     * @return este builder para encadenamiento fluido.
     */
    public UsuarioBuilder withFechaDeCreacion(LocalDate fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
        return this;
    }


   @Override
    public Usuario builder() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setFechaDeCreacion(fechaDeCreacion);
        return usuario;
    }
}



