package com.example.ClinicaDefinitiva.mapper;


import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Secretario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SecretarioMapperResponse {

   // @Mapping(target= "unUsuario", source = " readUsuarioDto")

    ReadSecretarioDto readSecretarioDto(Secretario secretario);
}
