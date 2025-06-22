package com.example.ClinicaDefinitiva.persistence.dto.odontologoDto;

import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.fasterxml.jackson.annotation.JsonInclude;


import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadOdontologoDto {
    private final long id;
    private final String dni;
    private final String nombre;
    private final String apellido;
    private final String telefono;
    private final String direcion;
    private final LocalDate fecha_nacimiento;

    private final Especialidades especialidad;

    private final ReadUsuarioDto readUsuarioDto;
    private final HorarioDto horarioDto;
    private final Tipo_sangre tipoSangre;



    public ReadOdontologoDto(String apellido, String direcion, String dni, Especialidades especialidad, LocalDate fecha_nacimiento, HorarioDto horarioDto, long id, String nombre, ReadUsuarioDto readUsuarioDto, String telefono, Tipo_sangre tipoSangre) {
        this.apellido = apellido;
        this.direcion = direcion;
        this.dni = dni;
        this.especialidad = especialidad;
        this.fecha_nacimiento = fecha_nacimiento;
        this.horarioDto = horarioDto;
        this.id = id;
        this.nombre = nombre;
        this.readUsuarioDto = readUsuarioDto;
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDirecion() {
        return direcion;
    }

    public String getDni() {
        return dni;
    }

    public Especialidades getEspecialidad() {
        return especialidad;
    }

    public HorarioDto getHorarioDto() {
        return horarioDto;
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

    public ReadUsuarioDto getReadUsuarioDto() {
        return readUsuarioDto;
    }

    public String getTelefono() {
        return telefono;
    }

    public Tipo_sangre getTipoSangre() {
        return tipoSangre;
    }
}
