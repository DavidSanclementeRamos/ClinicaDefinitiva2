package com.example.ClinicaDefinitiva.mapper;

import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Disponibilidad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = MapperAuxiliarOdontologo.class)
public interface HorarioMapperResponse {

    default long map(Disponibilidad disponibilidad) {
        return disponibilidad != null ? disponibilidad.getId() : null;
    }

    @Mapping(target= "idOdontologo", source = "unOdontologo")
    HorarioDto horarioDto(Disponibilidad disponibilidad);





}
