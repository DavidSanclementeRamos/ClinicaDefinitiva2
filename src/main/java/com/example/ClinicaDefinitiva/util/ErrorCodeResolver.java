package com.example.ClinicaDefinitiva.util;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.CreatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;

import java.util.Map;

public class ErrorCodeResolver {

    private static final Map<Class<?>, ErrorCatalog> errorMapping = Map.ofEntries(
            Map.entry(CreateOdontologoDto.class, ErrorCatalog.INVALID_DENTIST),
            Map.entry(CreatePacienteDto.class, ErrorCatalog.INVALID_PATIENT),
            Map.entry(CreateSecretarioDto.class, ErrorCatalog.INVALID_SECRETARY),
            Map.entry(CreateEndReadResponsableDto.class, ErrorCatalog.INVALID_RESPONSIBLE),
            Map.entry(CreateUsuarioDto.class, ErrorCatalog.INVALID_USER),
            Map.entry(HorarioDto.class, ErrorCatalog.INVALID_SCHEDULE),
            Map.entry(TurnoDto.class, ErrorCatalog.INVALID_SHIFT)
            //  Puedes seguir agregando tus DTO aquí fácilmente
    );

    public static ErrorCatalog resolver(Class<?> dtoClass) {
        return errorMapping.getOrDefault(dtoClass, ErrorCatalog.GENERIC_ERROR);
    }

}
