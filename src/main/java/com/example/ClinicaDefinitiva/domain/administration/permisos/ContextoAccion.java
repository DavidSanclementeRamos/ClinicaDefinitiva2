package com.example.ClinicaDefinitiva.domain.administration.permisos;

public class ContextoAccion {

    private final String sectorDestino;

    public ContextoAccion(String sectorDestino) {
        this.sectorDestino = sectorDestino;
    }

    public String getSectorDestino() { return sectorDestino; }

}
