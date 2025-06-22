package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.exceptions.UsuarioNotfountException;
import com.example.ClinicaDefinitiva.mapper.UsuarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.UpdateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class UsuarioImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapperResponse usuarioMapper;

    public UsuarioImpl(UsuarioRepository usuarioRepository, UsuarioMapperResponse usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public ReadUsuarioDto findId(long idUsuario) {

        return usuarioRepository.findById(idUsuario)
                .map(usuarioMapper::readUsuarioDto)
                .orElseThrow(UsuarioNotfountException::new);
    }

    @Override
    public List<ReadUsuarioDto> findAll() {

        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::readUsuarioDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<ReadUsuarioDto> findByCorreo(String correo) {
        return null;
    }

    @Override
    public List<ReadUsuarioDto> findByNombreUsuario(String nombreUsuario) {
        return null;
    }

    @Override
    public ReadUsuarioDto save(CreateUsuarioDto createUsuarioDto) {


            Usuario usu=new Usuario();
            usu.setNombreUsuario(createUsuarioDto.getNombreUsuario());
            usu.setCorreoEletronico(createUsuarioDto.getCorreoEletronico());
            usu.setContrasena(createUsuarioDto.getContrasena());
            usu.setEstado(createUsuarioDto.getEstado());
            usu.setRol(createUsuarioDto.getRol());
            usu.setFechaDeCreacion(createUsuarioDto.getFechaDeCreacion());
            usu.setImagenPerfil(createUsuarioDto.getImagenPerfil());
            usu.setUltimaFechaDeCoexion(createUsuarioDto.getUltimaFechaDeCoexion());
            return usuarioMapper.readUsuarioDto( usuarioRepository.save(usu));

    }

    @Override
    public ReadUsuarioDto update(long idUsuario, UpdateUsuarioDto updateUsuarioDto) {

        return usuarioRepository.findById(idUsuario)
                .map(usu -> {
                    usu.setNombreUsuario(updateUsuarioDto.getNombreUser());
                    usu.setCorreoEletronico(updateUsuarioDto.getCorreoEletronico());
                    usu.setContrasena(updateUsuarioDto.getContrasena());
                    usu.setImagenPerfil(updateUsuarioDto.getImagenPerfil());
                    usu.setEstado(updateUsuarioDto.getEstado());

                    return usuarioRepository.save(usu);
                }).map(usuarioMapper::readUsuarioDto)
                .orElseThrow(UsuarioNotfountException::new);
    }
}
