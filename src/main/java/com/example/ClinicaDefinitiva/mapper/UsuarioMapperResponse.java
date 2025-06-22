package com.example.ClinicaDefinitiva.mapper;


import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapperResponse {
    ReadUsuarioDto readUsuarioDto (Usuario usuario);
}
