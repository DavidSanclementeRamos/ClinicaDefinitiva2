package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.exceptions.SecretarioNotFountException;
import com.example.ClinicaDefinitiva.mapper.SecretarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.UpdateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Secretario;
import com.example.ClinicaDefinitiva.repository.SecretarioRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.SecretarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class SecretarioImpl implements SecretarioService {

    private final SecretarioRepository secretarioRepository;

    private final SecretarioMapperResponse secretarioReadMapper;

    private final UsuarioRepository usuarioRepository;

    public SecretarioImpl(SecretarioRepository secretarioRepository, UsuarioRepository usuarioRepository, SecretarioMapperResponse secretarioReadMapper) {
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
    public List<ReadSecretarioDto> findAll() {

        return secretarioRepository.findAll()
                .stream()
                .map(secretarioReadMapper::readSecretarioDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadSecretarioDto> findBySector(String sector) {

        return secretarioRepository.findAll().stream()
                .filter(secretario -> false)
                .map(secretarioReadMapper::readSecretarioDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<ReadSecretarioDto> findByUsuario(long usuarioId) {

        return  usuarioRepository.findById(usuarioId)
                .map(secretarioRepository::findByUnUsuario)
                .map(listaSecretario -> listaSecretario.stream()
                        .map(secretarioReadMapper::readSecretarioDto)
                        .collect(Collectors.toList())).orElseThrow(SecretarioNotFountException::new);
    }

    @Override
    public ReadSecretarioDto save(CreateSecretarioDto createSecretarioDto) {

        return usuarioRepository.findById(createSecretarioDto.getSecretarioId())
                .map(usuario -> {
                    Secretario secretario = new Secretario();
                    secretario.setDni(createSecretarioDto.getDni());
                    secretario.setNombre(createSecretarioDto.getNombre());
                    secretario.setApellido(createSecretarioDto.getApellido());
                    secretario.setDirecion(createSecretarioDto.getDirecion());
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

                    secretario.setDirecion(updateSecretarioDto.getDirecion());
                    secretario.setTelefono(updateSecretarioDto.getTelefono());
                    // secretario.setUnUsuario(usuario);
                    secretario.setSector(updateSecretarioDto.getSector());

                    return secretarioRepository.save(secretario);
                })
                .map(secretarioReadMapper::readSecretarioDto)
                .orElseThrow(SecretarioNotFountException::new);


    }
}
