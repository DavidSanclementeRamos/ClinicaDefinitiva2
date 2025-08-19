package com.example.ClinicaDefinitiva.util;

import com.example.ClinicaDefinitiva.Enum.CatalogoError;
import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.CreatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;

import java.util.Map;

public class ErrorCodeResolver {

    private static final Map<Class<?>, CatalogoError> errorMapping = Map.ofEntries(
            Map.entry(CreateOdontologoDto.class, CatalogoError.INVALID_DENTIST),
            Map.entry(CreatePacienteDto.class, CatalogoError.INVALID_PATIENT),
            Map.entry(CreateSecretarioDto.class, CatalogoError.INVALID_SECRETARY),
            Map.entry(CreateEndReadResponsableDto.class, CatalogoError.INVALID_RESPONSIBLE),
            Map.entry(CreateUsuarioDto.class, CatalogoError.INVALID_USER),
            Map.entry(HorarioDto.class, CatalogoError.INVALID_SCHEDULE),
            Map.entry(TurnoDto.class, CatalogoError.INVALID_SHIFT)
            //  Puedes seguir agregando tus DTO aquí fácilmente
    );

    public static CatalogoError resolver(Class<?> dtoClass) {
        return errorMapping.getOrDefault(dtoClass, CatalogoError.GENERIC_ERROR);
    }

}
