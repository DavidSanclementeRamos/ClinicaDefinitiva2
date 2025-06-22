package com.example.ClinicaDefinitiva.services;


import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.UpdateSecretarioDto;

import java.util.List;

public interface SecretarioService {


    ReadSecretarioDto findId(long idSecretario);

   List<ReadSecretarioDto> findAll();

   List<ReadSecretarioDto> findBySector(String sector);

   List<ReadSecretarioDto> findByUsuario(long usuarioId);

   ReadSecretarioDto save(CreateSecretarioDto createSecretarioDto);

   ReadSecretarioDto update(long id, UpdateSecretarioDto updateSecretarioDto);

}
