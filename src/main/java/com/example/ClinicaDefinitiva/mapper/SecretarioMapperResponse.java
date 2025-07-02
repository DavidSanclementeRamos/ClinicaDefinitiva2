package com.example.ClinicaDefinitiva.mapper;


import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;


import com.example.ClinicaDefinitiva.persistence.entity.Secretario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsuarioMapperResponse.class )
public interface SecretarioMapperResponse {

    @Mapping(target= "readUsuarioDto", source = "unUsuario")

    ReadSecretarioDto readSecretarioDto(Secretario secretario);
}
