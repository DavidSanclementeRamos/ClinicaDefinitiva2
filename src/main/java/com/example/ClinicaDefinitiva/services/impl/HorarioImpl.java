package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.exceptions.HorarioNotfountException;
import com.example.ClinicaDefinitiva.mapper.HorarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.repository.HorarioRepository;
import com.example.ClinicaDefinitiva.repository.OdontologoRepository;
import com.example.ClinicaDefinitiva.services.HorarioService;
import org.springframework.stereotype.Service;
//import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
@Service
//@RequiredArgsConstructor
public class HorarioImpl implements HorarioService {
    private final HorarioRepository horarioRepository;
    private final HorarioMapperResponse horarioMapper;
    private final OdontologoRepository odontologoRepository;


    public HorarioImpl(HorarioRepository horarioRepository, HorarioMapperResponse horarioMapper, OdontologoRepository odontologoRepository) {
        this.horarioRepository = horarioRepository;
        this.horarioMapper = horarioMapper;

        this.odontologoRepository = odontologoRepository;
    }


    @Override
    public HorarioDto findId(long horarioId) {

        return horarioRepository.findById(horarioId)
                .map(horarioMapper::horarioDto)
                .orElseThrow(HorarioNotfountException::new);
    }

   @Override
    public HorarioDto save(HorarioDto horarioDto) {

        return odontologoRepository.findById(horarioDto.getIdOdontologo())
                .map(odontologo -> {

                    Horario horario = new Horario();

                            horario.setDiaSemana(horarioDto.getDiaSemana());
                    horario.setHoraFin(horarioDto.getHoraFinal());
                    horario.setHoraInicio(horarioDto.getHorarInicio());
                    horario.setUnOdontologo(odontologo);
                    horario.setEstado(horarioDto.isEstado());


                   return horarioRepository.save(horario);

                }).map(horarioMapper::horarioDto)
                .orElseThrow(HorarioNotfountException::new);
    }

    @Override
    public HorarioDto update(long horarioId, HorarioDto horarioDto) {

        return horarioRepository.findById(horarioId)
                .flatMap(horario -> odontologoRepository
                        .findById(horarioDto.getIdOdontologo())
                .map(odontologo -> {
                    horario.setEstado(horarioDto.isEstado());
                    horario.setUnOdontologo(odontologo);

                     return horarioRepository.save(horario);
                }))
                        .map(horarioMapper::horarioDto)
                        .orElseThrow(HorarioNotfountException::new);
    }

    @Override
    public List<HorarioDto> findAll() {

        return horarioRepository.findAll().stream()
                .map(horarioMapper::horarioDto)
                .collect(Collectors.toList());
    }



    @Override
    public void deleaById(long id) {
       if(horarioRepository.findById(id).isEmpty()){

           throw new HorarioNotfountException();

       }
       horarioRepository.deleteById(id);
    }
}
