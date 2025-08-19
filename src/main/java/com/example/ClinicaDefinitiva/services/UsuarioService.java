package com.example.ClinicaDefinitiva.services;



import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.UpdateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UsuarioService  {

    ReadUsuarioDto findId(long idUsuario);

    Page<ReadUsuarioDto> findAll(Pageable pageable);

    ReadUsuarioDto findByEmail(String email);

   // List<ReadUsuarioDto> findByRol(Roles rol);

    ReadUsuarioDto findByNombreUsuario(String nombreUsuario);

    ReadUsuarioDto save(CreateUsuarioDto createUsuarioDto);

    ReadUsuarioDto update(long idUsuario, UpdateUsuarioDto updateUsuarioDto);

    void deleaById(long id);

}
