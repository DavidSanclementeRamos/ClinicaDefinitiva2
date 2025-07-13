package com.example.ClinicaDefinitiva.persistence.dto.secretarioDto;


import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.Enumvalidation.SectorValido;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class CreateSecretarioDto {

    @NotBlank(message = "El dni no puede estar vacio")
    @NotNull(message = "El dni no puede ser nulo")
    @Pattern(regexp = "[0-9]+", message = "Debe contener solo numeros")
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "El nombre no puede ser nulo")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]{2,30}$", message = "Solo letras permitidas, entre 2 y 30 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacio")
    @NotNull(message = "El apellido no puede ser nulo")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]{2,30}$", message = "Solo letras permitidas, entre 2 y 30 caracteres")
    private String apellido;

    @NotBlank(message = "El el telefono no puede estar vacio")
    @NotNull(message = "El teléfono no puede ser nulo")
    @Pattern(
            regexp = "^\\+57\\s3\\d{2}\\d{7}$",
            message = "El teléfono debe tener el formato: +57 3XXYYYYYYY")
    private String telefono;

    @NotBlank(message = "La direccion no puede estar vacio ")
    @NotNull(message = "La dirección no puede ser nula")
    private String direccion;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fecha_nacimiento;

    @SectorValido
    @NotNull(message = " No puede ser nulo")
    private Sector sector;

    private long idUsuario;


    public CreateSecretarioDto(){

    }

    public CreateSecretarioDto(String apellido, String direccion, String dni, LocalDate fecha_nacimiento, long idUsuario, String nombre, Sector sector, String telefono) {
        this.apellido = apellido;
        this.direccion = direccion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.sector = sector;
        this.telefono = telefono;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
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
