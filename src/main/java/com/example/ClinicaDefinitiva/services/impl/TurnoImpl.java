package com.example.ClinicaDefinitiva.services.impl;



import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.HorarioNotfoundException;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.OdontologoNotfoundException;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.PacienteNotFoundException;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.TurnoNotFoundException;
import com.example.ClinicaDefinitiva.mapper.TurnoMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Disponibilidad;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import com.example.ClinicaDefinitiva.repository.HorarioRepository;
import com.example.ClinicaDefinitiva.repository.OdontologoRepository;
import com.example.ClinicaDefinitiva.repository.PacienteRepository;
import com.example.ClinicaDefinitiva.repository.TurnoRepository;
import com.example.ClinicaDefinitiva.services.TurnoService;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurnoImpl implements TurnoService {
    private final TurnoRepository turnoRepository;
    private final TurnoMapperResponse turnoMapper;
    private final PacienteRepository pacienteRepository;
    private final HorarioRepository horarioRepository;
    private final OdontologoRepository odontologoRepository;
    String requestId = RequestIdFilter.getRequestId();
    private static final Logger logger = LoggerFactory.getLogger(TurnoImpl.class);




    public TurnoImpl(TurnoRepository turnoRepository, TurnoMapperResponse turnoMapper
            , PacienteRepository pacienteRepository, HorarioRepository horarioRepository, OdontologoRepository odontologoRepository) {
        this.turnoRepository = turnoRepository;
        this.turnoMapper = turnoMapper;
        this.pacienteRepository = pacienteRepository;

        this.horarioRepository = horarioRepository;
        this.odontologoRepository = odontologoRepository;
    }

    @Override
    public TurnoDto save(TurnoDto turnoDto) {
        // validar la existencia del paciente asociado
        Paciente paciente = pacienteRepository.findById(turnoDto.getIdPaciente() )
                .orElseThrow(() -> {
                    logger.warn("No hay paciente asociado a ese id:  [{}], requestId={}",turnoDto.getIdPaciente() , requestId);
                    return new PacienteNotFoundException(ContextoEntidad.TURNO
                , "No se encontro un paciente asociado al turno con el id: " + turnoDto.getIdPaciente() );});

        // validar la existencia del disponibilidad asociado
        Disponibilidad disponibilidad = horarioRepository.findById(turnoDto.getHorarioId())
                .orElseThrow(() -> {
                    logger.warn("No hay disponibilidad asociado a ese id:  [{}], requestId={}",turnoDto.getHorarioId() , requestId);
                  return new  HorarioNotfoundException(ContextoEntidad.HORARIO,
                          "No se encontro un disponibilidad asociado al turno con el id: " + turnoDto.getHorarioId() );});


        // validar el odontólogo asociado
        Odontologo odontologo = odontologoRepository.findById(turnoDto.getOdontologo())
                .orElseThrow(() -> {
                        logger.warn("No hay odontologo asociado a ese id:  [{}], requestId={}",turnoDto.getOdontologo() , requestId);
                        return new OdontologoNotfoundException(ContextoEntidad.TURNO,
                                "No hay odontologo asociado a ese id: " + turnoDto.getOdontologo());});

        // Crear la entidad Turno
        Turno turno = new Turno();
        turno.setFechaTurno(turnoDto.getFechaTurno());
        turno.setHoraTurno(turnoDto.getHora_turno());
        turno.setAfeccion(turnoDto.getAfeccion());
        turno.setPaciente(paciente);
        turno.setHorario(disponibilidad);
        turno.setOdontologo(odontologo);


        // Guardar el turno y mapear a DTO
        Turno turnoGuardado = turnoRepository.save(turno);
        return turnoMapper.turnoDto(turnoGuardado);
    }

    @Override
    public TurnoDto update(long idTurno, TurnoDto turnoDto) {

        return turnoRepository.findById(idTurno)
                 .map(turno -> {
                            turno.setAfeccion(turnoDto.getAfeccion());
                            turno.setFechaTurno(turnoDto.getFechaTurno());
                            turno.setEstado(turnoDto.getEstado());

                            turno.setHoraTurno(turnoDto.getHora_turno());

                            return turnoRepository.save(turno);
                        }).map(turnoMapper::turnoDto)
                .orElseThrow(() -> {
                    logger.warn("No existe un turno asociado a ese id:  [{}], requestId={}",idTurno , requestId);
                   return new  TurnoNotFoundException(ContextoEntidad.TURNO,
                           "No existe el id: " + idTurno + " del turno que quiere editar");});
    }

    @Override
    public Page<TurnoDto> findAll(Pageable pageable) {

        // Le decimos al repo que haga la búsqueda paginada+ordenada
        Page<Turno> pageEntidades = turnoRepository.findAll(pageable);

        if (pageEntidades.isEmpty()) {
            logger.warn("No existen registros de turno para los filtros [id={}, requestId={}]", pageable, requestId);
            throw new TurnoNotFoundException(
            ContextoEntidad.TURNO,
                    "No existen registros de turnos para los filtros dados"
            );}
        // Convertimos cada Entidad a DTO
        return pageEntidades.map(turnoMapper::turnoDto);



    }

    @Override
    public void deleaById(long id) {
        if (turnoRepository.findById(id).isEmpty()){
            logger.warn("No existe el el id: " + id + " que quiere eliminar [id={}, requestId={}]", id, requestId);

            throw new TurnoNotFoundException(ContextoEntidad.TURNO,
                    "No exixte el id "  + id + " que quiere eliminar");
        }
           turnoRepository.deleteById(id);
    }



    @Override
    public List<TurnoDto> findByPacienteId(long idPaciente) {

        Paciente paciente = pacienteRepository.findById(idPaciente).orElseThrow(()->{
                    logger.warn("No existe el el id: " + idPaciente + " del paciente [id={}, requestId={}]", idPaciente, requestId);
                    return new PacienteNotFoundException(ContextoEntidad.TURNO,
                            "No existe el id: " + idPaciente);

                    });
        List<Turno> lista = turnoRepository.findByPaciente_Id(idPaciente);

       if(lista.isEmpty()){
           logger.warn("No se encontro resultado de la busqueda [idPaciente={}, requestId={}]", idPaciente, requestId);
           throw new TurnoNotFoundException(ContextoEntidad.TURNO,
            " No se encontro resultado de la busqueda de turnos por el id: " + idPaciente);
       }

        logger.info("Turnos encontrado por idPaciente [idPaciente={}, requestId={}]",
                idPaciente, requestId);
       return lista.stream()
               .map(turnoMapper::turnoDto)
               .collect(Collectors.toList());

    }



    @Override
    public TurnoDto findById(long idTurno) {
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> {
            //odontologoMetrics.contarOdontologoNoEncontrado(requestId);
            logger.warn("Turno no encontrado [idTurno={}, requestId={}]", idTurno, requestId);
            return new  TurnoNotFoundException(ContextoEntidad.TURNO," Turno no encontrado por el id:" + idTurno);

        });

        logger.info("Turno recuperado [idTurno={}, requestId={}]", idTurno, requestId);
        return turnoMapper.turnoDto(turno);
    }

    @Override
    public List<TurnoDto> findByFechaBetween(LocalDate desde, LocalDate hasta) {
        List<Turno> lista = turnoRepository.findByFechaTurnoBetween(desde, hasta);
        if( lista.isEmpty()) {
            logger.warn("fecha no encontrada [desde={}], [hasta={}] requestId={}]", desde,hasta, requestId);

            throw new TurnoNotFoundException(ContextoEntidad.TURNO,
                    " No se encontro un turno en esa fecha, desde:" + desde + " hata: " + hasta);

        }
        logger.info("Turno recuperado [desde={}, hasta={}, requestId={}]", desde, hasta,requestId);

        return  lista.stream().map(turnoMapper::turnoDto).collect(Collectors.toList());
    }

    @Override
    public List<TurnoDto> findByOdontologoId(long idOdontologo) {
        List<Turno> lista =  turnoRepository.findByOdontologo_Id(idOdontologo);
        if(lista.isEmpty()){
            logger.warn("Turno no encontrado por id odontologo: [idOdontologo={},  requestId={}]", idOdontologo, requestId);

            throw new TurnoNotFoundException(ContextoEntidad.TURNO,
                    " No se encontro un turno por ese id odontologo:" + idOdontologo );

        }
             logger.info("Turno recuperado por idOdontologo  [idOdontologo={},  requestId={}]", idOdontologo ,requestId);
                return lista.stream()
                .map(turnoMapper::turnoDto).collect(Collectors.toList());
    }

    @Override
    public List<TurnoDto> findByEstado(Estado estado) {

        List<Turno> lista = turnoRepository.findByEstado(estado);
        if(lista.isEmpty()){

            logger.warn("Turno no encontrado por estado: [estado={},  requestId={}]", estado.name(), requestId);

            throw new TurnoNotFoundException(ContextoEntidad.TURNO,
                    " No se encontroron turnos por ese estado:" + estado );}
            logger.info("Turnos encotrados {} , estado  [estado={},  requestId={}]",lista.size(), estado.name() ,requestId);

                return  lista.stream()
                .map(turnoMapper::turnoDto).collect(Collectors.toList());
    }

    @Override
    public boolean existsByOdontologo_IdAndFechaAndHora(long idOdontologo, LocalDate fecha, LocalTime hora) {
        return turnoRepository.existsByOdontologo_IdAndFechaTurnoAndHoraTurno(idOdontologo,fecha,hora);

    }
}
