package com.example.ClinicaDefinitiva.mapper;


import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UsuarioMapperResponse.class})
public interface ResponsableMapperResponse {

    @Mapping(target = "readUsuarioDto", source = "unUsuario")
   // @Mapping(target = "readPacienteDto", source = "paciente")

    CreateEndReadResponsableDto createEndReadResponsableDto(Responsable responsable);

}
