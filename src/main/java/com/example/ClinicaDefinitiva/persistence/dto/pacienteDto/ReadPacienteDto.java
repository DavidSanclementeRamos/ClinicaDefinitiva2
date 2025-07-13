package com.example.ClinicaDefinitiva.persistence.dto.pacienteDto;

import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

//@Getter
//@Bui
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadPacienteDto {
    private  long id;
    private  String dni;
    private  String nombre;
    private  String apellido;
    private  String telefono;
    private  String direccion;
    private  LocalDate fecha_nacimiento;
    private  boolean tiene_Os;
    private  CreateEndReadResponsableDto readResponsableDto;
    private  boolean tieneResponsable;
    private  List<TurnoDto> turnoDto;
    private  Tipo_sangre tipoSangre;
    private  ReadUsuarioDto readUsuarioDto;


    public ReadPacienteDto(String apellido, long id, String nombre, boolean tieneResponsable
            , CreateEndReadResponsableDto readResponsableDto, String telefono
            ,  boolean tiene_Os, List<TurnoDto> turnoDto, ReadUsuarioDto readUsuarioDto
            , String direccion, String dni, LocalDate fecha_nacimiento,Tipo_sangre tipoSangre) {
        this.apellido = apellido;
        this.id = id;
        this.nombre = nombre;
        this.tieneResponsable = tieneResponsable;
        this.readResponsableDto = readResponsableDto;
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
        this.tiene_Os = tiene_Os;
        this.turnoDto = turnoDto;
        this.direccion = direccion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.readUsuarioDto = readUsuarioDto;

    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setReadResponsableDto(CreateEndReadResponsableDto readResponsableDto) {
        this.readResponsableDto = readResponsableDto;
    }

    public void setReadUsuarioDto(ReadUsuarioDto readUsuarioDto) {
        this.readUsuarioDto = readUsuarioDto;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setTiene_Os(boolean tiene_Os) {
        this.tiene_Os = tiene_Os;
    }

    public void setTieneResponsable(boolean tieneResponsable) {
        this.tieneResponsable = tieneResponsable;
    }

    public void setTipoSangre(Tipo_sangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public void setTurnoDto(List<TurnoDto> turnoDto) {
        this.turnoDto = turnoDto;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public List<TurnoDto> getTurnoDto() {
        return turnoDto;
    }

}
