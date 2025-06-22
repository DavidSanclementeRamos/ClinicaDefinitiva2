package com.example.ClinicaDefinitiva.services;



import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;

import java.util.List;

public interface HorarioService {

    HorarioDto findId(long horarioId);

    HorarioDto save( HorarioDto horarioDto);

    HorarioDto update(long horarioId, HorarioDto horarioDto);

    List<HorarioDto> findAll();

    //List<HorarioDto> findByOdontologo(long odontologoId);

    void deleaById(long id);



}
