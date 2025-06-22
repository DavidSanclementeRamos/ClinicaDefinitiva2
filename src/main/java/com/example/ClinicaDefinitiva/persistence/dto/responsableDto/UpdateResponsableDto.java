package com.example.ClinicaDefinitiva.persistence.dto.responsableDto;

import jakarta.validation.constraints.NotNull;

public class UpdateResponsableDto {
    @NotNull(message = " No puede ser nulo")
    private String telefono;
    @NotNull(message = " No puede ser nulo")
    private String direcion;


    public UpdateResponsableDto(){

    }
    public UpdateResponsableDto(String direcion, String telefono ) {
        this.direcion = direcion;
        this.telefono = telefono;

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
}
