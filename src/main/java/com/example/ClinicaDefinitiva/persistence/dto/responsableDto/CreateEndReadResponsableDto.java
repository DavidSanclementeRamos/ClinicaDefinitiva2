package com.example.ClinicaDefinitiva.persistence.dto.responsableDto;


import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.Enum.Tipo_sangre;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
public class CreateEndReadResponsableDto {
    private long id;
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
    private Tipo_sangre tipoSangre;
    private TipoResponsable tipoResponsable;
    private ReadUsuarioDto readUsuarioDto;
   // private List<ReadPacienteDto> readPacienteDto;


    public CreateEndReadResponsableDto(){

    }

    public CreateEndReadResponsableDto(String apellido, String direcion, String dni, LocalDate fecha_nacimiento, String nombre, long id, /*List<ReadPacienteDto> readPacienteDto,*/ ReadUsuarioDto readUsuarioDto, String telefono, TipoResponsable tipoResponsable, Tipo_sangre tipoSangre) {
        this.apellido = apellido;
        this.direcion = direcion;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.nombre = nombre;
        this.id = id;
      //  this.readPacienteDto = readPacienteDto;
        this.readUsuarioDto = readUsuarioDto;
        this.telefono = telefono;
        this.tipoResponsable = tipoResponsable;
        this.tipoSangre = tipoSangre;
    }

  /*  public List<ReadPacienteDto> getReadPacienteDto() {
        return readPacienteDto;
    }

    public void setReadPacienteDto(List<ReadPacienteDto> readPacienteDto) {
        this.readPacienteDto = readPacienteDto;
    }*/

    public TipoResponsable getTipoResponsable() {
        return tipoResponsable;
    }

    public void setTipoResponsable(TipoResponsable tipoResponsable) {
        this.tipoResponsable = tipoResponsable;
    }

    public Tipo_sangre getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(Tipo_sangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public ReadUsuarioDto getReadUsuarioDto() {
        return readUsuarioDto;
    }

    public void setReadUsuarioDto(ReadUsuarioDto readUsuarioDto) {
        this.readUsuarioDto = readUsuarioDto;
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

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDirecion() {
        return direcion;
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

    public String getTelefono() {
        return telefono;
    }
}
