package com.example.ClinicaDefinitiva.services;



import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.UpdateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OdontologoService {

   Optional<ReadOdontologoDto> findId(long idOdontologo);

   Page<ReadOdontologoDto> findAll(Pageable pageable);

   Optional<ReadOdontologoDto> findByUsuario_Id(long idUsuario);

   List<ReadOdontologoDto> findByEspecialidad(Especialidades especialidad);

   List<ReadOdontologoDto> findConTurnosEntreFechas(LocalDate desde, LocalDate hasta);

   ReadOdontologoDto save(CreateOdontologoDto createOdontologoDto);

   ReadOdontologoDto update(long idOdontologo, UpdateOdontologoDto updateOdontologoDto);
}
