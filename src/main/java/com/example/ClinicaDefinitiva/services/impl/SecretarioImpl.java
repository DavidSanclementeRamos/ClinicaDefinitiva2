package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.SecretarioNotFountException;
import com.example.ClinicaDefinitiva.mapper.SecretarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.UpdateSecretarioDto;

import com.example.ClinicaDefinitiva.persistence.entity.Secretario;
import com.example.ClinicaDefinitiva.repository.SecretarioRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.SecretarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class SecretarioImpl implements SecretarioService {

    private final SecretarioRepository secretarioRepository;

    private final SecretarioMapperResponse secretarioReadMapper;

    private final UsuarioRepository usuarioRepository;

    public SecretarioImpl(SecretarioRepository secretarioRepository, UsuarioRepository usuarioRepository
            , SecretarioMapperResponse secretarioReadMapper) {
        this.secretarioRepository = secretarioRepository;
        this.secretarioReadMapper = secretarioReadMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ReadSecretarioDto findId(long idSecretario) {

        return secretarioRepository.findById(idSecretario)
                .map(secretarioReadMapper::readSecretarioDto)
                .orElseThrow(SecretarioNotFountException::new);
    }


    @Override
    public Page<ReadSecretarioDto> findAll(Pageable pageable) {
        // Le decimos al repo que haga la búsqueda paginada+ordenada
        Page<Secretario> pageEntidades = secretarioRepository.findAll(pageable);

        // Convertimos cada Entidad a DTO
        return pageEntidades.map(secretarioReadMapper::readSecretarioDto);

    }

    @Override
    public List<ReadSecretarioDto> findByNombreContainingIgnoreCase(String nombre) {
        return secretarioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(secretarioReadMapper::readSecretarioDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadSecretarioDto> findBySector(Sector sector) {

        return secretarioRepository.findBySector(sector).stream()
                .map(secretarioReadMapper::readSecretarioDto)
                .collect(Collectors.toList());

    }

    @Override
    public Optional<ReadSecretarioDto> findByUsuarioId(long idUsuario) {
        return secretarioRepository.findByUnUsuario_Id(idUsuario)
                .map(secretarioReadMapper::readSecretarioDto);
    }


    @Override
    public ReadSecretarioDto save(CreateSecretarioDto createSecretarioDto) {

        return usuarioRepository.findById(createSecretarioDto.getIdUsuario())
                .map(usuario -> {
                    Secretario secretario = new Secretario();
                    secretario.setDni(createSecretarioDto.getDni());
                    secretario.setNombre(createSecretarioDto.getNombre());
                    secretario.setApellido(createSecretarioDto.getApellido());
                    secretario.setDireccion(createSecretarioDto.getDireccion());
                    secretario.setTelefono(createSecretarioDto.getTelefono());
                    secretario.setSector(createSecretarioDto.getSector());
                    secretario.setFecha_nacimiento(createSecretarioDto.getFecha_nacimiento());
                    secretario.setUnUsuario(usuario);

                    return secretarioRepository.save(secretario);
                }).map(secretarioReadMapper::readSecretarioDto)
                .orElseThrow(SecretarioNotFountException::new) ;
    }

    @Override
    public ReadSecretarioDto update(long id, UpdateSecretarioDto updateSecretarioDto) {
        return secretarioRepository.findById(id)
                .map(secretario -> {

                    secretario.setDireccion(updateSecretarioDto.getDireccion());
                    secretario.setTelefono(updateSecretarioDto.getTelefono());
                    // secretario.setUnUsuario(usuario);
                    secretario.setSector(updateSecretarioDto.getSector());

                    return secretarioRepository.save(secretario);
                })
                .map(secretarioReadMapper::readSecretarioDto)
                .orElseThrow(SecretarioNotFountException::new);


    }

    @Override
    public void deleaById(long id) {
        if(secretarioRepository.findById(id).isEmpty()){
            throw new SecretarioNotFountException();
        }
        secretarioRepository.deleteById(id);
    }
}
