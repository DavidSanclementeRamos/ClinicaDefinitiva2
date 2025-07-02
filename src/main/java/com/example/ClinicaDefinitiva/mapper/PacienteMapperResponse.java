package com.example.ClinicaDefinitiva.mapper;

import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" ,uses = {TurnoMapperResponse.class, ResponsableMapperResponse.class, UsuarioMapperResponse.class})
public interface PacienteMapperResponse {


     @Mapping(target= "turnoDto", source = "unTurno")
    @Mapping(target= "readResponsableDto", source = "responsable")
    @Mapping(target = "readUsuarioDto", source = "unUsuario")
    ReadPacienteDto readPaciente(Paciente paciente);

}
