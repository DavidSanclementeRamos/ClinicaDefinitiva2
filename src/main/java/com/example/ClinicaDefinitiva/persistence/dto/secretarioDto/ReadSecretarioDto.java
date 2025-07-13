package com.example.ClinicaDefinitiva.persistence.dto.secretarioDto;




import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;

import java.time.LocalDate;

public class ReadSecretarioDto {
    private long id;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private LocalDate fecha_nacimiento;
    private Sector sector;
    private ReadUsuarioDto readUsuarioDto;

    public ReadSecretarioDto(){

    }
    public ReadSecretarioDto(String apellido, String direccion, String dni
            , LocalDate fecha_nacimiento, long id, String nombre
            , ReadUsuarioDto readUsuarioDto, Sector sector, String telefono) {
        this.apellido = apellido;
        this.direccion = direccion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.id = id;
        this.nombre = nombre;
        this.readUsuarioDto = readUsuarioDto;
        this.sector = sector;
        this.telefono = telefono;
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

    public void setReadUsuarioDto(ReadUsuarioDto readUsuarioDto) {
        this.readUsuarioDto = readUsuarioDto;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public ReadUsuarioDto getReadUsuarioDto() {
        return readUsuarioDto;
    }

    public Sector getSector() {
        return sector;
    }

    public String getTelefono() {
        return telefono;
    }
}
