package com.example.ClinicaDefinitiva.persistence.dto.pacienteDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdatePacienteDto {

    @NotBlank(message = "El el telefono no puede estar vacio")
    @Pattern(
            regexp = "^\\+57\\s3\\d{2}\\d{7}$",
            message = "El teléfono debe tener el formato: +57 3XXYYYYYYY")
    @NotNull(message = "El telefono no puede ser nulo")
    private String telefono;

    @NotBlank(message = "La direccion no puede estar vacio ")
    @NotNull(message = "La direción no puede ser nulo")
    private String direccion;

    @NotBlank(message = "El seguro no puede estar vacio ")
    @NotNull(message = "El seguro no puede ser nulo")
    private boolean tiene_Os;



    public UpdatePacienteDto(){

    }

    public UpdatePacienteDto(String direccion, String telefono, boolean tiene_Os) {
        this.direccion = direccion;
        this.telefono = telefono;
        this.tiene_Os = tiene_Os;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
