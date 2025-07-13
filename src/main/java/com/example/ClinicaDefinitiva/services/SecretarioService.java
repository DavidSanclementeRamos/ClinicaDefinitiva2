package com.example.ClinicaDefinitiva.services;


import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.UpdateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Secretario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SecretarioService {


    ReadSecretarioDto findId(long idSecretario);

   Page<ReadSecretarioDto> findAll(Pageable pageable);

   List<ReadSecretarioDto> findByNombreContainingIgnoreCase(String nombre);

   List<ReadSecretarioDto> findBySector(Sector sector);

   Optional<ReadSecretarioDto> findByUsuarioId(long idUsuario);

   ReadSecretarioDto save(CreateSecretarioDto createSecretarioDto);

   ReadSecretarioDto update(long id, UpdateSecretarioDto updateSecretarioDto);

   void deleaById(long id);
}
