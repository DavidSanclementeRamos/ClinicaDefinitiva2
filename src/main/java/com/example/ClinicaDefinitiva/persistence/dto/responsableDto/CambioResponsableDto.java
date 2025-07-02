package com.example.ClinicaDefinitiva.persistence.dto.responsableDto;


import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CambioResponsableDto {


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
    @NotNull(message = "El dni no puede ser nulo")

    private Tipo_sangre tipoSangre;
    private TipoResponsable tipoResponsable;
    private long pacienteId;

    public CambioResponsableDto() {

    }

    public CambioResponsableDto(String apellido, String direcion, String dni, LocalDate fecha_nacimiento, String nombre, long pacienteId, String telefono, TipoResponsable tipoResponsable, Tipo_sangre tipoSangre) {
        this.apellido = apellido;
        this.direcion = direcion;
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

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
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
