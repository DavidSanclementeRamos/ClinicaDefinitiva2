package com.example.ClinicaDefinitiva.services;



import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;

import java.time.LocalDate;
import java.util.List;

public interface TurnoService {

    TurnoDto save(TurnoDto turnoDto);
    TurnoDto update( long idTurno, TurnoDto turnoDto);
    List<TurnoDto> findAll();
    void deleaById(long id);
    List<TurnoDto> findByFecha(LocalDate fecha);
    List<TurnoDto> findByPacienteId(long idPaciente);
    //TurnoDto findByOdontologoId(long idOdontologo);
     TurnoDto findById(long idTurno);



}
