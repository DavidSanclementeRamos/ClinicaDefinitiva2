package com.example.ClinicaDefinitiva.services;



import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurnoService {

    TurnoDto save(TurnoDto turnoDto);

    TurnoDto update( long idTurno, TurnoDto turnoDto);

    Page<TurnoDto> findAll(Pageable pageable);

    void deleaById(long id);

    List<TurnoDto> findByPacienteId(long idPaciente);

    TurnoDto findById(long idTurno);

    List<TurnoDto> findByFechaBetween(LocalDate desde, LocalDate hasta);

    List<TurnoDto> findByOdontologoId(long idOdontologo);

    List<TurnoDto> findByEstado(Estado estado);

    boolean existsByOdontologo_IdAndFechaAndHora(long idOdontologo, LocalDate fecha, LocalTime hora);

}
