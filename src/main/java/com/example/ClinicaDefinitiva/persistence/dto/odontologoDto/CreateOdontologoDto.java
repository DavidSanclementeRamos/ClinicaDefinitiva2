package com.example.ClinicaDefinitiva.persistence.dto.odontologoDto;

import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDate;
@Getter
public class CreateOdontologoDto {

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
    @NotNull(message = "La especialida no puede ser nula")
    private Especialidades especialidad;
    private long idUsuario;
   // private long idHorario;
    private Tipo_sangre tipoSangre;

    public CreateOdontologoDto(String apellido, String direcion, String dni
            , Especialidades especialidad,   LocalDate fecha_nacimiento
            , long idUsuario, String nombre, String telefono,Tipo_sangre tipoSangre ) {
        this.apellido = apellido;
        this.direcion = direcion;
        this.dni = dni;
        this.especialidad = especialidad;
       // this.idHorario = idHorario;
        this.fecha_nacimiento = fecha_nacimiento;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }



    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEspecialidad(Especialidades especialidad) {
        this.especialidad = especialidad;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getDirecion() {
        return direcion;
    }

    public Especialidades getEspecialidad() {
        return especialidad;
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

    public String getNombre() {
        return nombre;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public String getTelefono() {
        return telefono;
    }
}
