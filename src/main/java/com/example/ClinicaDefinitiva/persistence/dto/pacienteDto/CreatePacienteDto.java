package com.example.ClinicaDefinitiva.persistence.dto.pacienteDto;


import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Setter;

import java.time.LocalDate;

@Setter
public class CreatePacienteDto {

    @Pattern(regexp = "[0-9]+", message = "Debe contener solo numeros")
    @NotNull(message = "El dni no puede ser nulo")
    private String dni;
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;
    @NotNull(message = "El telefono no puede ser nulo")
    private String telefono;
    @NotNull(message = "La direcion no puede ser nula")
    private String direcion;
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fecha_nacimiento;
    private boolean tiene_Os;
    private boolean tieneResponsable;
    private long idTurno;
    private long idUsuario;
    private long idResponsable;
    private Tipo_sangre tipoSangre;

    public CreatePacienteDto(){


    }

    public CreatePacienteDto(String apellido, String direcion, String dni, LocalDate fecha_nacimiento, long idResponsable,  long idUsuario, String nombre, String telefono, boolean tiene_Os, boolean tieneResponsable, Tipo_sangre tipoSangre) {
        this.apellido = apellido;
        this.direcion = direcion;
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

    public String getDirecion() {
        return direcion;
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

    public void setDirecion(String direcion) {
        this.direcion = direcion;
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
