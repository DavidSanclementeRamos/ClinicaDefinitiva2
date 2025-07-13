package com.example.ClinicaDefinitiva.persistence.dto.pacienteDto;


import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.Enumvalidation.TipoSangreValido;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class CreatePacienteDto {

    @Pattern(regexp = "[0-9]+", message = "Debe contener solo numeros")
    @NotBlank(message = "El dni no puede estar vacio ")
    @NotNull(message = "El dni no puede ser nulo")
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
    @NotNull(message = "El telefono no puede ser nulo")
    @Pattern(
            regexp = "^\\+57\\s3\\d{2}\\d{7}$",
            message = "El teléfono debe tener el formato: +57 3XXYYYYYYY")
    private String telefono;

    @NotBlank(message = "La direccion no puede estar vacio ")
    @NotNull(message = "La direcion no puede ser nula")
    private String direccion;

    @NotBlank(message = "La fecha de nacimiento no puede estar vacia")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fecha_nacimiento;

    @NotBlank(message = "El seguro no puede estar vacio ")
    @NotNull(message = "El seguro no puede ser nulo")
    private boolean tiene_Os;

    @NotBlank(message = "El responsable no puede estar vacio ")
    @NotNull(message = "El reponsable no puede ser nulo")
    private boolean tieneResponsable;

    private long idUsuario;
    private long idResponsable;

    @NotNull(message = "El tipo de sangre no puede ser nulo")
    @TipoSangreValido
    private Tipo_sangre tipoSangre;

    public CreatePacienteDto(){


    }

    public CreatePacienteDto(String apellido, String direccion, String dni, LocalDate fecha_nacimiento, long idResponsable,  long idUsuario, String nombre, String telefono, boolean tiene_Os, boolean tieneResponsable, Tipo_sangre tipoSangre) {
        this.apellido = apellido;
        this.direccion = direccion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.idResponsable = idResponsable;

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tiene_Os = tiene_Os;
        this.tieneResponsable = tieneResponsable;
        this.tipoSangre = tipoSangre;
    }

    public String getApellido() {
        return apellido;
    }


    public void setIdResponsable(long idResponsable) {
        this.idResponsable = idResponsable;
    }


    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Tipo_sangre getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(Tipo_sangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getDni() {
        return dni;
    }

    public long getIdResponsable() {
        return idResponsable;
    }


    public long getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isTiene_Os() {
        return tiene_Os;
    }

    public boolean isTieneResponsable() {
        return tieneResponsable;
    }


    public void setTiene_Os(boolean tiene_Os) {
        this.tiene_Os = tiene_Os;
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

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setTieneResponsable(boolean tieneResponsable) {
        this.tieneResponsable = tieneResponsable;
    }


}
