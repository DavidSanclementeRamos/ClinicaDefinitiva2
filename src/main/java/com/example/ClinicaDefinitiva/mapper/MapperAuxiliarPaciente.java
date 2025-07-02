package com.example.ClinicaDefinitiva.mapper;

import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperAuxiliarPaciente {
    default long map(Paciente paciente) {
        return paciente != null ? paciente.getId() : null;
    }

}
