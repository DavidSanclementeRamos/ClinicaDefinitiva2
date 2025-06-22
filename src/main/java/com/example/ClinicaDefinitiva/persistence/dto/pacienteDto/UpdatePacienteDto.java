package com.example.ClinicaDefinitiva.persistence.dto.pacienteDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePacienteDto {

    @NotNull(message = "El telefono no puede ser nulo")
    private String telefono;
    @NotNull(message = "La direción no puede ser nulo")
    private String direcion;
    private boolean tiene_Os;
   // private long idTurno;
   // private long idUsuario;
   // private long idResponsable;


    public UpdatePacienteDto(){

    }

    public UpdatePacienteDto(String direcion, String telefono, boolean tiene_Os) {
        this.direcion = direcion;
        this.telefono = telefono;
        this.tiene_Os = tiene_Os;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isTiene_Os() {
        return tiene_Os;
    }

    public void setTiene_Os(boolean tiene_Os) {
        this.tiene_Os = tiene_Os;
    }


}
