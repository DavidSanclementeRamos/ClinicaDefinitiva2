package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.HorarioNotfoundException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.DentistNotFoundException;
import com.example.ClinicaDefinitiva.mapper.HorarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Disponibilidad;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.repository.HorarioRepository;
import com.example.ClinicaDefinitiva.repository.OdontologoRepository;
import com.example.ClinicaDefinitiva.services.HorarioService;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
@Service

public class HorarioImpl implements HorarioService {
    private final HorarioRepository horarioRepository;
    private final HorarioMapperResponse horarioMapper;
    private final OdontologoRepository odontologoRepository;
    String requestId = RequestIdFilter.getRequestId();
    private static final Logger logger = LoggerFactory.getLogger(HorarioImpl.class);




    public HorarioImpl(HorarioRepository horarioRepository, HorarioMapperResponse horarioMapper
            , OdontologoRepository odontologoRepository) {
        this.horarioRepository = horarioRepository;
        this.horarioMapper = horarioMapper;

        this.odontologoRepository = odontologoRepository;
    }


    @Override
    public HorarioDto findId(long horarioId) {

        Disponibilidad disponibilidad = horarioRepository.findById(horarioId)
                .orElseThrow(() -> {
                   // odontologoMetrics.contarOdontologoNoEncontrado(requestId);
                    logger.warn("Disponibilidad no encontrado [id={}, requestId={}]", horarioId, requestId);
                    return new DentistNotFoundException(
                            EntityContext.HORARIO,
                            "No se encontró el disponibilidad con ID: " + horarioId
                    );
                });

                return horarioMapper.horarioDto(disponibilidad);

    }

   @Override
    public HorarioDto save(HorarioDto horarioDto) {

        return odontologoRepository.findById(horarioDto.getIdOdontologo())
                .map(odontologo -> {

                    Disponibilidad disponibilidad = new Disponibilidad();
                    disponibilidad.setDiaSemana(horarioDto.getDiaSemana());
                    disponibilidad.setHoraFin(horarioDto.getHoraFinal());
                    disponibilidad.setHoraInicio(horarioDto.getHoraInicio());
                    disponibilidad.setUnOdontologo(odontologo);
                    disponibilidad.setEstado(horarioDto.getEstado());


                     return horarioRepository.save(disponibilidad);

                }).map(horarioMapper::horarioDto)
                .orElseThrow(()-> new DentistNotFoundException(EntityContext.ODONTOLOGO, "No fue encontrado el odontologo con id: " + horarioDto.getIdOdontologo()));
    }

    @Override
    public HorarioDto update(long horarioId, HorarioDto horarioDto) {

                Disponibilidad disponibilidad = horarioRepository.findById(horarioId)
                        .orElseThrow(()-> new HorarioNotfoundException(EntityContext.HORARIO, "No fue encontrado en disponibilidad con id: " + horarioId));
                Odontologo odontologo = odontologoRepository.findById(horarioDto.getIdOdontologo())
                        .orElseThrow(()-> new DentistNotFoundException(EntityContext.ODONTOLOGO, "No fue encontrado el odontologo con id: " + horarioDto.getIdOdontologo() ));
                    disponibilidad.setEstado(horarioDto.getEstado());
                    disponibilidad.setUnOdontologo(odontologo);
                horarioMapper.horarioDto(disponibilidad);
                Disponibilidad actualizar = horarioRepository.save(disponibilidad);

        return horarioMapper.horarioDto(actualizar);
    }

    @Override
    public Page<HorarioDto> findAll(Pageable pageable) {

        // Le decimos al repo que haga la búsqueda paginada+ordenada
        Page<Disponibilidad> pageEntidades = horarioRepository.findAll(pageable);

        if (pageEntidades.isEmpty()) {
            throw new HorarioNotfoundException(
                    EntityContext.HORARIO,
                    "No existen registros de horarios para los filtros dados"
            );
        }
        return pageEntidades.map(horarioMapper::horarioDto);

    }

    @Override
    public List<HorarioDto> findByOdontologo_Id(long idOdontologo) {
        if (!odontologoRepository.existsById(idOdontologo)) {
            logger.warn("El odontólogo con id [{}] no existe, requestId={}", idOdontologo, requestId);
            throw new DentistNotFoundException(
                    EntityContext.ODONTOLOGO,
                    "No existe un odontólogo con el id: " + idOdontologo
            );
        }
        List<Disponibilidad>  lista = horarioRepository.findByUnOdontologo_Id(idOdontologo);

        if (lista.isEmpty()) {
           // odontologoMetrics.contarOdontologoNoEncontrado(requestId);
            logger.warn("No se encontraron horarios  con ese id [{}], requestId={}",idOdontologo, requestId);

            throw new HorarioNotfoundException(
                    EntityContext.HORARIO,
                    "No se encontraron horarios con el id del odontolgo: " + idOdontologo
            );
        }

       // odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Se encontraron {} horarios con ese id de odontologo [{}], requestId={}",
                lista.size(), idOdontologo, requestId);
        return lista.stream()
                .map(horarioMapper::horarioDto).collect(Collectors.toList());
    }

    @Override
    public List<HorarioDto> findByDiaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
            DayOfWeek dia, LocalTime desde, LocalTime hasta) {
        List<Disponibilidad> lista = horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(dia, desde,hasta);
        if (lista.isEmpty()) {
            // odontologoMetrics.contarOdontologoNoEncontrado(requestId);
            logger.warn("No se encontraron horarios  en el dia [{}], dede[{}], hasta[{}] ,requestId={}"
                    ,dia, desde, hasta,requestId);

            throw new HorarioNotfoundException(
                    EntityContext.HORARIO,
                    "No se encontraron horarios en el dia: " + dia +  " desde:" + desde +  " hasta:" + hasta
            );
        }

        // odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Se encontraron {} horarios en el dia [{}], desde[{}], hasta[{}], requestId={}",
                lista.size(), dia,desde,hasta, requestId);


            return lista.stream().map(horarioMapper::horarioDto).collect(Collectors.toList());
    }

    @Override
    public List<HorarioDto> findByFechaBetween(LocalDate desde, LocalDate hasta) {
        List<Disponibilidad> lista = horarioRepository.findDistinctByTurnos_FechaTurnoBetween(desde,hasta);

        if (lista.isEmpty()) {
            // odontologoMetrics.contarOdontologoNoEncontrado(requestId);
            logger.warn("No se encontraron horarios dede[{}], hasta[{}] ,requestId={}"
                    ,desde, hasta,requestId);

            throw new HorarioNotfoundException(
                    EntityContext.HORARIO,
                    "No se encontraron horarios desde:" + desde +  " hasta:" + hasta
            );
        }

        // odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Se encontraron {} horarios desde[{}], hasta[{}], requestId={}",
                lista.size(), desde,hasta, requestId);
        return lista.stream().map(horarioMapper::horarioDto).collect(Collectors.toList());
    }


    @Override
    public void deleaById(long id) {
       if(horarioRepository.findById(id).isEmpty()){
           logger.warn("No existe el id: [{}] ,requestId={}"
                   ,id,requestId);

           throw new HorarioNotfoundException(EntityContext.HORARIO, "No existe el id: " + id);

       }
       horarioRepository.deleteById(id);
    }
}
