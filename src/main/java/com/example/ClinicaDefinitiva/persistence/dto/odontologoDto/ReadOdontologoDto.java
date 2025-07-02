package com.example.ClinicaDefinitiva.persistence.dto.odontologoDto;

import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;


import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadOdontologoDto {
    private  long id;
    private  String dni;
    private  String nombre;
    private  String apellido;
    private  String telefono;
    private  String direcion;
    private  LocalDate fecha_nacimiento;

    private  Especialidades especialidad;

    private  ReadUsuarioDto readUsuarioDto;
    private  HorarioDto horarioDto;
    private  Tipo_sangre tipoSangre;



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

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public void setHorarioDto(HorarioDto horarioDto) {
        this.horarioDto = horarioDto;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setReadUsuarioDto(ReadUsuarioDto readUsuarioDto) {
        this.readUsuarioDto = readUsuarioDto;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setTipoSangre(Tipo_sangre tipoSangre) {
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
