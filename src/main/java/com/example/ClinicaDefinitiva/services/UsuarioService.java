package com.example.ClinicaDefinitiva.services;



import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.UpdateUsuarioDto;

import java.util.List;

public interface UsuarioService {

    ReadUsuarioDto findId(long idUsuario);

    List<ReadUsuarioDto> findAll();

    List<ReadUsuarioDto> findByCorreo(String correo);

    List<ReadUsuarioDto> findByNombreUsuario(String nombreUsuario);

    ReadUsuarioDto save(CreateUsuarioDto createUsuarioDto);

    ReadUsuarioDto update(long idUsuario, UpdateUsuarioDto updateUsuarioDto);


}
