package com.example.ClinicaDefinitiva.services;



import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HorarioService {

    HorarioDto findId(long horarioId);

    HorarioDto save( HorarioDto horarioDto);

    HorarioDto update(long horarioId, HorarioDto horarioDto);

    Page<HorarioDto> findAll(Pageable pageable);

    List<HorarioDto> findByOdontologo_Id(long idOdontologo);

    List<HorarioDto> findByDiaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(DayOfWeek dia, LocalTime desde, LocalTime hasta);

    List<HorarioDto> findByFechaBetween(LocalDate desde, LocalDate hasta);

    void deleaById(long id);



}
