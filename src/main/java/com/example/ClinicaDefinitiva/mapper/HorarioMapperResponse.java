package com.example.ClinicaDefinitiva.mapper;

import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = MapperAuxiliarOdontologo.class)
public interface HorarioMapperResponse {

    default long map(Horario horario) {
        return horario != null ? horario.getId() : null;
    }

    @Mapping(target= "idOdontologo", source = "unOdontologo")
    HorarioDto horarioDto(Horario horario);





}
