package com.example.ClinicaDefinitiva.persistence.dto.secretarioDto;


import com.example.ClinicaDefinitiva.Enum.Sector;
import jakarta.validation.constraints.NotNull;

public class UpdateSecretarioDto {
    @NotNull(message = " No puede ser nulo")
    private String telefono;
    @NotNull(message = " No puede ser nulo")
    private String direcion;
    @NotNull(message = " No puede ser nulo")
    private Sector sector;


    public UpdateSecretarioDto(){

    }



    public UpdateSecretarioDto(String direcion, Sector sector, String telefono, long idUsuario) {
        this.direcion = direcion;
        this.sector = sector;
        this.telefono = telefono;

    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public Sector getSector() {
        return sector;
    }


    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
