package com.example.ClinicaDefinitiva.util;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.config.EdadMinimaConfig;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DniDuplicadoException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.EdadNoPermitidaException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.TelefonoDuplicadoException;
import com.example.ClinicaDefinitiva.repository.ResponsableRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Period;


public class ValidarEdades {

    @Autowired
    private EdadMinimaConfig edadMinimaConfig;

    ResponsableRepository responsableRepository;

     public void validarEdades (LocalDate fecha, ContextoEntidad rol){
        int edadActual = Period.between(fecha, LocalDate.now()).getYears();
        int edadMinima;

        switch (rol) {
            case ODONTOLOGO -> edadMinima = edadMinimaConfig.getOdontologo();
            case SECRETARIO -> edadMinima = edadMinimaConfig.getSecretario();
            case RESPONSABLE -> edadMinima = edadMinimaConfig.getResponsable();
            default -> throw new IllegalArgumentException("Rol desconocido");
        }

        if (edadActual < edadMinima) {
            throw new EdadNoPermitidaException(rol,"Edad insuficiente");
        }
    }


    public void verificarDuplicados(String telefono, String dni) {
        if (responsableRepository.existsByTelefono(telefono)) {
            throw new TelefonoDuplicadoException(ContextoEntidad.RESPONSABLE, "Teléfono duplicado: " + telefono);
        }
        if (responsableRepository.existsByDni(dni)) {
            throw new DniDuplicadoException(ContextoEntidad.RESPONSABLE, "DNI duplicado: " + dni);
        }
    }

}