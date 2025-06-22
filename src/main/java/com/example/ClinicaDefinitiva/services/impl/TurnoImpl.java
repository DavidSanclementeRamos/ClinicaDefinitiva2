package com.example.ClinicaDefinitiva.services.impl;


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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
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
        turno.setFecha_turno(turnoDto.getFecha_turno());
        turno.setHora_turno(turnoDto.getHora_turno());
        turno.setAfeccion(turnoDto.getAfeccion());
        turno.setPaciente(paciente);
        turno.setHorario(horario);
        turno.setOdontonlogo(odontologo);

        // Guardar el turno y mapear a DTO
        Turno turnoGuardado = turnoRepository.save(turno);
        return turnoMapper.turnoDto(turnoGuardado);
    }

    @Override
    public TurnoDto update(long idTurno, TurnoDto turnoDto) {

        return turnoRepository.findById(idTurno)
                 .map(turno -> {
                            turno.setAfeccion(turnoDto.getAfeccion());
                            turno.setFecha_turno(turnoDto.getFecha_turno());

                            turno.setHora_turno(turnoDto.getHora_turno());

                            return turnoRepository.save(turno);
                        }).map(turnoMapper::turnoDto)
                .orElseThrow(TurnoNotFountException::new);
    }

    @Override
    public List<TurnoDto> findAll() {

        return turnoRepository.findAll().stream()
                .map(turnoMapper::turnoDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleaById(long id) {
        if (turnoRepository.findById(id).isEmpty()){
            throw new TurnoNotFountException();
        }
           turnoRepository.deleteById(id);
    }

    @Override
    public List<TurnoDto> findByFecha(LocalDate fecha) {
        return turnoRepository.findAll().stream()
                .filter(turno -> turno.getFecha_turno().equals(fecha))
                .map(turnoMapper::turnoDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<TurnoDto> findByPacienteId(long idPaciente) {

        return   pacienteRepository.findById(idPaciente)
                .map( paciente -> turnoRepository.findByPacienteId(idPaciente))
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
}
