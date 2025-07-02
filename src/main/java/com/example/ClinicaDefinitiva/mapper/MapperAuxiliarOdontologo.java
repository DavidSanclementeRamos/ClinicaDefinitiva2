package com.example.ClinicaDefinitiva.mapper;

import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperAuxiliarOdontologo {
    default long map(Odontologo odontologo) {
        return odontologo != null ? odontologo.getId() : null;
    }

}
