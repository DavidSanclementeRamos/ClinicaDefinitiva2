package com.example.ClinicaDefinitiva.persistence.dto.pacienteDto;

import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

//@Getter
//@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadPacienteDto {
    private final long id;
    private final String dni;
    private final String nombre;
    private final String apellido;
    private final String telefono;
    private final String direcion;
    private final LocalDate fecha_nacimiento;
    private final boolean tiene_Os;
    private final CreateEndReadResponsableDto readResponsableDto;
    private final boolean tieneResponsable;
    private final TurnoDto turnoDto;
    private final Tipo_sangre tipoSangre;
    private final ReadUsuarioDto readUsuarioDto;


    public ReadPacienteDto(String apellido, long id, String nombre, boolean tieneResponsable
            , CreateEndReadResponsableDto readResponsableDto, String telefono
            ,  boolean tiene_Os, TurnoDto turnoDto, ReadUsuarioDto readUsuarioDto
            , String direcion, String dni, LocalDate fecha_nacimiento,Tipo_sangre tipoSangre) {
        this.apellido = apellido;
        this.id = id;
        this.nombre = nombre;
        this.tieneResponsable = tieneResponsable;
        this.readResponsableDto = readResponsableDto;
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
        this.tiene_Os = tiene_Os;
        this.turnoDto = turnoDto;
        this.direcion = direcion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.readUsuarioDto = readUsuarioDto;

    }

    public String getApellido() {
        return apellido;
    }

    public String getDirecion() {
        return direcion;
    }

    public Tipo_sangre getTipoSangre() {
        return tipoSangre;
    }

    public String getDni() {
        return dni;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public CreateEndReadResponsableDto getReadResponsableDto() {
        return readResponsableDto;
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

    public ReadUsuarioDto getReadUsuarioDto() {
        return readUsuarioDto;
    }

    public TurnoDto getTurnoDto() {
        return turnoDto;
    }

}
