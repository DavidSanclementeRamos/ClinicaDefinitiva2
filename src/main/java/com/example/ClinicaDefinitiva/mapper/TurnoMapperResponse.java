package com.example.ClinicaDefinitiva.mapper;


import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TurnoMapperResponse {

   // @Mapping(target= "readePacienteDto", source = "paciente")
  // @Mapping(target= "readeOdontologoDto", source = "odontologo")

    TurnoDto turnoDto(Turno turno);

}
