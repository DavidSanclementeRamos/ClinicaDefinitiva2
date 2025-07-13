package com.example.ClinicaDefinitiva.persistence.dto.secretarioDto;


import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.Enumvalidation.SectorValido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateSecretarioDto {

    @NotBlank(message = "El el telefono no puede estar vacio")
    @NotNull(message = " No puede ser nulo")
    @Pattern(
            regexp = "^\\+57\\s3\\d{2}\\d{7}$",
            message = "El teléfono debe tener el formato: +57 3XXYYYYYYY")
    private String telefono;

    @NotBlank(message = "La direccion no puede estar vacio ")
    @NotNull(message = " No puede ser nulo")
    private String direccion;

    @SectorValido
    @NotNull(message = " No puede ser nulo")
    private Sector sector;


    public UpdateSecretarioDto(){

    }



    public UpdateSecretarioDto(String direccion, Sector sector, String telefono, long idUsuario) {
        this.direccion = direccion;
        this.sector = sector;
        this.telefono = telefono;

    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
