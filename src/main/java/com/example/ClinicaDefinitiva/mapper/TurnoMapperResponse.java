package com.example.ClinicaDefinitiva.mapper;


import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {MapperAuxiliarOdontologo.class, HorarioMapperResponse.class,MapperAuxiliarPaciente.class})
public interface TurnoMapperResponse {

    @Mapping(target= "idPaciente", source = "paciente")
    @Mapping(target= "horarioId", source = "horario")
   @Mapping(target= "odontologo", source = "odontologo")

    TurnoDto turnoDto(Turno turno);

}
