package com.example.ClinicaDefinitiva.mapper;

import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HorarioMapperResponse {

   // @Mapping(target= "unOdontologo", source = "idOdontologo")
    HorarioDto horarioDto(Horario horario);





}
