package com.example.ClinicaDefinitiva.services;

import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.CreatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.UpdatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PacienteServise {

   ReadPacienteDto findId(long idPaciente);

   Page<ReadPacienteDto> findAll(Pageable pageable);

   List<ReadPacienteDto> findByNombreContainingIgnoreCase(String nombre);

   ReadPacienteDto findByDocumento(String documento);

   ReadPacienteDto findByUsuario_Id(long idUsuario);

   List<ReadPacienteDto> findConTurnosParaFecha(LocalDate fecha);

   ReadPacienteDto save(CreatePacienteDto createPacienteDto);

   ReadPacienteDto update(long id, UpdatePacienteDto updatePacienteDto);

   void deleaById(long id);




}
