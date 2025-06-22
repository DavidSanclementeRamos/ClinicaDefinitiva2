package com.example.ClinicaDefinitiva.persistence.dto.odontologoDto;


import com.example.ClinicaDefinitiva.Enum.Especialidades;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Setter;

//@Setter
//@Builder
public class UpdateOdontologoDto {
    @NotNull(message = "El teléfono no puede ser nulo")
    private String telefono;
    @NotNull(message = "La direción no puede ser nula")
    private String direcion;
    @NotNull(message = "La especialida no puede ser nula")
    private Especialidades especialidad;
    // private long idUsuario;
    //private long idHorario;

    public UpdateOdontologoDto() {

    }

    public UpdateOdontologoDto(String direcion, Especialidades especialidad, String telefono) {
        this.direcion = direcion;
        this.especialidad = especialidad;
        this.telefono = telefono;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
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
