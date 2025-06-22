package com.example.ClinicaDefinitiva.persistence.dto.secretarioDto;


import com.example.ClinicaDefinitiva.Enum.Sector;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class CreateSecretarioDto {

    @NotNull(message = "El dni no puede ser nulo")
    @Pattern(regexp = "[0-9]+", message = "Debe contener solo numeros")
    private String dni;
    @NotNull(message = "El nombre no puede ser nulo")

    private String nombre;
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;
    // @Pattern(regexp = "[0-9]+", message = "Debe contener solo numeros")
    @NotNull(message = "El teléfono no puede ser nulo")
    private String telefono;
    @NotNull(message = "La dirección no puede ser nula")
    private String direcion;
    @Temporal(TemporalType.DATE)
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fecha_nacimiento;
    @NotNull(message = " No puede ser nulo")
    private Sector sector;
    private long secretarioId;

    public CreateSecretarioDto(){

    }
    public CreateSecretarioDto(String apellido, String direcion, String dni
            , LocalDate fecha_nacimiento, String nombre, Sector sector, String telefono, long secretarioId) {
        this.apellido = apellido;
        this.direcion = direcion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.nombre = nombre;
        this.sector = sector;
        this.telefono = telefono;
        this.secretarioId = secretarioId;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getDirecion() {
        return direcion;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public long getSecretarioId() {
        return secretarioId;
    }

    public void setSecretarioId(long secretarioId) {
        this.secretarioId = secretarioId;
    }

    public Sector getSector() {
        return sector;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
