package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.exceptions.HorarioNotfountException;
import com.example.ClinicaDefinitiva.mapper.HorarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.repository.HorarioRepository;
import com.example.ClinicaDefinitiva.repository.OdontologoRepository;
import com.example.ClinicaDefinitiva.services.HorarioService;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service

public class HorarioImpl implements HorarioService {
    private final HorarioRepository horarioRepository;
    private final HorarioMapperResponse horarioMapper;
    private final OdontologoRepository odontologoRepository;


    public HorarioImpl(HorarioRepository horarioRepository, HorarioMapperResponse horarioMapper
            , OdontologoRepository odontologoRepository) {
        this.horarioRepository = horarioRepository;
        this.horarioMapper = horarioMapper;

        this.odontologoRepository = odontologoRepository;
    }


    @Override
    public Optional<HorarioDto> findId(long horarioId) {

        return horarioRepository.findById(horarioId)
                .map(horarioMapper::horarioDto);

    }

   @Override
    public HorarioDto save(HorarioDto horarioDto) {

        return odontologoRepository.findById(horarioDto.getIdOdontologo())
                .map(odontologo -> {

                    Horario horario = new Horario();
                    horario.setDiaSemana(horarioDto.getDiaSemana());
                    horario.setHoraFin(horarioDto.getHoraFinal());
                    horario.setHoraInicio(horarioDto.getHoraInicio());
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
    public Page<HorarioDto> findAll(Pageable pageable) {

        // Le decimos al repo que haga la búsqueda paginada+ordenada
        Page<Horario> pageEntidades = horarioRepository.findAll(pageable);

        // Convertimos cada Entidad a DTO
        return pageEntidades.map(horarioMapper::horarioDto);

    }

    @Override
    public List<HorarioDto> findByOdontologo_Id(long idOdontologo) {
        return  horarioRepository.findByUnOdontologo_Id(idOdontologo).stream()
                .map(horarioMapper::horarioDto).collect(Collectors.toList());
    }

    @Override
    public List<HorarioDto> findByDiaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
            DayOfWeek dia, LocalTime desde, LocalTime hasta) {
        return horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(dia, desde,hasta)
                .stream().map(horarioMapper::horarioDto).collect(Collectors.toList());
    }

    @Override
    public List<HorarioDto> findByFechaBetween(LocalDate desde, LocalDate hasta) {
        return horarioRepository.findByTurnos_FechaTurnoBetween(desde,hasta)
                .stream().map(horarioMapper::horarioDto).collect(Collectors.toList());
    }


    @Override
    public void deleaById(long id) {
       if(horarioRepository.findById(id).isEmpty()){

           throw new HorarioNotfountException();

       }
       horarioRepository.deleteById(id);
    }
}
