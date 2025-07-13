package com.example.ClinicaDefinitiva.util;

import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.config.EdadMinimaConfig;
import com.example.ClinicaDefinitiva.exceptions.EdadNoPermitidaException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Period;


public class ValidarEdades {

    @Autowired
    private EdadMinimaConfig edadMinimaConfig;

     public void validarEdades (LocalDate fecha, Roles rol){
        int edadActual = Period.between(fecha, LocalDate.now()).getYears();
        int edadMinima;

        switch (rol) {
            case ODONTOLOGO -> edadMinima = edadMinimaConfig.getOdontologo();
            case SECRETARIO -> edadMinima = edadMinimaConfig.getSecretario();
            case RESPONSABLE -> edadMinima = edadMinimaConfig.getResponsable();
            default -> throw new IllegalArgumentException("Rol desconocido");
        }

        if (edadActual < edadMinima) {
            throw new EdadNoPermitidaException();
        }
    }

}