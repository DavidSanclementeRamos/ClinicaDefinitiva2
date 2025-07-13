package com.example.ClinicaDefinitiva.persistence.dto.odontologoDto;


import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.Enumvalidation.EspecialidadesValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateOdontologoDto {
    @NotNull(message = "El teléfono no puede ser nulo")
    @NotBlank(message = "El el telefono no puede estar vacio")
    @Pattern(
            regexp = "^\\+57\\s3\\d{2}\\d{7}$",
            message = "El teléfono debe tener el formato: +57 3XXYYYYYYY")
    private String telefono;

    @NotBlank(message = "La direccion no puede estar vacio ")
    @NotNull(message = "La direción no puede ser nula")
    private String direccion;

    @NotNull(message = "La especialida no puede ser nula")
    @EspecialidadesValido
    private Especialidades especialidad;

    public UpdateOdontologoDto() {

    }

    public UpdateOdontologoDto(String direccion, Especialidades especialidad, String telefono) {
        this.direccion = direccion;
        this.especialidad = especialidad;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Especialidades getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidades especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
