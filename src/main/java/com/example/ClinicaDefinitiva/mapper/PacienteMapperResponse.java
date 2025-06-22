package com.example.ClinicaDefinitiva.mapper;

import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PacienteMapperResponse {
   // @Mapping(target= "unTurno", source = "turnoDto")
   // @Mapping(target= "responsable", source = "readResponsableDto")
  //  @Mapping(target = "unUsuario", source = "readUsuarioDto")
    ReadPacienteDto readPaciente(Paciente paciente);

}
