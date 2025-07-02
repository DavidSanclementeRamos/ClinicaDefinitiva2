package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.exceptions.HorarioNotfountException;
import com.example.ClinicaDefinitiva.exceptions.OdontologoNotfountException;
import com.example.ClinicaDefinitiva.exceptions.PacienteNotFountException;
import com.example.ClinicaDefinitiva.exceptions.TurnoNotFountException;
import com.example.ClinicaDefinitiva.mapper.TurnoMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import com.example.ClinicaDefinitiva.repository.HorarioRepository;
import com.example.ClinicaDefinitiva.repository.OdontologoRepository;
import com.example.ClinicaDefinitiva.repository.PacienteRepository;
import com.example.ClinicaDefinitiva.repository.TurnoRepository;
import com.example.ClinicaDefinitiva.services.TurnoService;
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
        // Buscar el paciente
        Paciente paciente = pacienteRepository.findById(turnoDto.getIdPaciente())
                .orElseThrow(PacienteNotFountException::new);

        // Buscar el horario
        Horario horario = horarioRepository.findById(turnoDto.getHorarioId())
                .orElseThrow(HorarioNotfountException::new);

        // Buscar el odontólogo
        Odontologo odontologo = odontologoRepository.findById(turnoDto.getOdontologo())
                .orElseThrow(OdontologoNotfountException::new);

        // Crear la entidad Turno
        Turno turno = new Turno();
        turno.setFechaTurno(turnoDto.getFechaTurno());
        turno.setHoraTurno(turnoDto.getHora_turno());
        turno.setAfeccion(turnoDto.getAfeccion());
        turno.setPaciente(paciente);
        turno.setHorario(horario);
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
                .orElseThrow(TurnoNotFountException::new);
    }

    @Override
    public Page<TurnoDto> findAll(Pageable pageable) {

        // Le decimos al repo que haga la búsqueda paginada+ordenada
        Page<Turno> pageEntidades = turnoRepository.findAll(pageable);

        // Convertimos cada Entidad a DTO
        return pageEntidades.map(turnoMapper::turnoDto);



    }

    @Override
    public void deleaById(long id) {
        if (turnoRepository.findById(id).isEmpty()){
            throw new TurnoNotFountException();
        }
           turnoRepository.deleteById(id);
    }



    @Override
    public List<TurnoDto> findByPacienteId(long idPaciente) {

        return   pacienteRepository.findById(idPaciente)
                .map( paciente -> turnoRepository.findByPaciente_Id(idPaciente))
                .map(turno -> turno.stream()
                        .map(turnoMapper::turnoDto)
                        .collect(Collectors.toList()))
                .orElseThrow(TurnoNotFountException::new);

    }



    @Override
    public TurnoDto findById(long idTurno) {
        return turnoRepository.findById(idTurno)
                .map(turnoMapper::turnoDto)
                .orElseThrow(TurnoNotFountException::new);
    }

    @Override
    public List<TurnoDto> findByFechaBetween(LocalDate desde, LocalDate hasta) {
        return turnoRepository.findByFechaTurnoBetween(desde, hasta).stream()
                .map(turnoMapper::turnoDto).collect(Collectors.toList());
    }

    @Override
    public List<TurnoDto> findByOdontologoId(long idOdontologo) {
        return turnoRepository.findByOdontologo_Id(idOdontologo).stream()
                .map(turnoMapper::turnoDto).collect(Collectors.toList());
    }

    @Override
    public List<TurnoDto> findByEstado(Estado estado) {
        return turnoRepository.findByEstado(estado).stream()
                .map(turnoMapper::turnoDto).collect(Collectors.toList());
    }

    @Override
    public boolean existsByOdontologo_IdAndFechaAndHora(long idOdontologo, LocalDate fecha, LocalTime hora) {
        return turnoRepository.existsByOdontologo_IdAndFechaTurnoAndHoraTurno(idOdontologo,fecha,hora);

    }
}
