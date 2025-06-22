package com.example.ClinicaDefinitiva.services;

import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.CreatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.UpdatePacienteDto;

import java.util.List;

public interface PacienteServise {

   ReadPacienteDto findId(long idPaciente);

   List<ReadPacienteDto> findAll();

   List<ReadPacienteDto> findByResponsableId(long responsableId);

   List<ReadPacienteDto> findByTurnoId(long turnoId);

   ReadPacienteDto save(CreatePacienteDto createPacienteDto);

   ReadPacienteDto update(long id, UpdatePacienteDto updatePacienteDto);




}
