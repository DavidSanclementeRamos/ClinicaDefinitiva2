package com.example.ClinicaDefinitiva.persistence.dto.responsableDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateResponsableDto {

    @NotNull(message = " No puede ser nulo")
    @NotBlank(message = "El el telefono no puede estar vacio")
    @Pattern(
            regexp = "^\\+57\\s3\\d{2}\\d{7}$",
            message = "El teléfono debe tener el formato: +57 3XXYYYYYYY")
    private String telefono;

    @NotBlank(message = "La direccion no puede estar vacio ")
    @NotNull(message = " No puede ser nulo")
    private String direccion;


    public UpdateResponsableDto(){

    }
    public UpdateResponsableDto(String direccion, String telefono ) {
        this.direccion = direccion;
        this.telefono = telefono;

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
}
