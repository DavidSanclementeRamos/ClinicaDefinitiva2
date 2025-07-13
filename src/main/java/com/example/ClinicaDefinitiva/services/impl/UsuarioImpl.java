package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.UsuarioNotfountException;
import com.example.ClinicaDefinitiva.mapper.UsuarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.UpdateUsuarioDto;

import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.UsuarioService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
    public Page<ReadUsuarioDto> findAll(Pageable pageable) {

        Page<Usuario> paginaEntity = usuarioRepository.findAll(pageable);
        return paginaEntity.map(usuarioMapper::readUsuarioDto);

    }

    @Override
    public Optional<ReadUsuarioDto> findByEmail(String email) {
        return usuarioRepository.findByCorreoEletronico(email)
                .map(usuarioMapper::readUsuarioDto);
    }

    @Override
    public List<ReadUsuarioDto> findByRol(Roles rol) {
        return usuarioRepository.findByRol(rol).stream()
                .map(usuarioMapper::readUsuarioDto)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<ReadUsuarioDto> findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .map(usuarioMapper::readUsuarioDto);
    }

    @Override
    public ReadUsuarioDto save(CreateUsuarioDto createUsuarioDto) {


            Usuario usu=new Usuario();
            usu.setNombreUsuario(createUsuarioDto.getNombreUsuario());
            usu.setCorreoEletronico(createUsuarioDto.getCorreoEletronico());
            usu.setContrasena(createUsuarioDto.getContrasena());
            usu.setEstado(createUsuarioDto.getEstado());
            usu.setRol(createUsuarioDto.getRol());
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

    @Override
    public void deleaById(long id) {
        if(usuarioRepository.findById(id).isEmpty()){
            throw new UsuarioNotfountException();
        }
        usuarioRepository.deleteById(id);
    }
}
