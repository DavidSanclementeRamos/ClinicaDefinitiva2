package com.example.ClinicaDefinitiva.services;



import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.UpdateOdontologoDto;

import java.util.List;

public interface OdontologoService {

   ReadOdontologoDto findId(long idOdontologo);

   List<ReadOdontologoDto> findAll();

   List<ReadOdontologoDto> findByTurnoId(long turnoId);

   List<ReadOdontologoDto> findByHorarioId(long horarioId);

   ReadOdontologoDto save(CreateOdontologoDto createOdontologoDto);

   ReadOdontologoDto update(long idOdontologo, UpdateOdontologoDto updateOdontologoDto);
}
