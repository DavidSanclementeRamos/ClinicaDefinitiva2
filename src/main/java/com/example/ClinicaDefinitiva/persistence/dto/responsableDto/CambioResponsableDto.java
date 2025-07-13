package com.example.ClinicaDefinitiva.persistence.dto.responsableDto;


import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.Enumvalidation.TipoSangreValido;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class CambioResponsableDto {


    @NotBlank(message = "El dni no puede estar vacio")
    @NotNull(message = "El dni no puede ser nulo")
    @Pattern(regexp = "[0-9]+", message = "Debe contener solo numeros")
    private String dni;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]{2,30}$", message = "Solo letras permitidas, entre 2 y 30 caracteres")
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacio")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ]{2,30}$", message = "Solo letras permitidas, entre 2 y 30 caracteres")
    @NotNull(message = "El apellido no puede ser nulo")
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

    @NotNull(message = "El tipo de sangre no puede ser nulo")
    @TipoSangreValido
    private Tipo_sangre tipoSangre;

    @NotNull(message = "El tipo de responsable no puede ser nulo")
    @TipoSangreValido
    private TipoResponsable tipoResponsable;

    private long pacienteId;

    public CambioResponsableDto() {

    }

    public CambioResponsableDto(String apellido, String direccion, String dni, LocalDate fecha_nacimiento, String nombre, long pacienteId, String telefono, TipoResponsable tipoResponsable, Tipo_sangre tipoSangre) {
        this.apellido = apellido;
        this.direccion = direccion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.nombre = nombre;
        this.pacienteId = pacienteId;
        this.telefono = telefono;
        this.tipoResponsable = tipoResponsable;
        this.tipoSangre = tipoSangre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public TipoResponsable getTipoResponsable() {
        return tipoResponsable;
    }

    public void setTipoResponsable(TipoResponsable tipoResponsable) {
        this.tipoResponsable = tipoResponsable;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Tipo_sangre getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(Tipo_sangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }
}
