package com.example.ClinicaDefinitiva.mapper;


import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponsableMapperResponse {

   // @Mapping(target = "unUsuario", source = "readUsuarioDto")
   // @Mapping(target = "paciente", source = "readPacienteDto")

    CreateEndReadResponsableDto createEndReadResponsableDto(Responsable responsable);

}
