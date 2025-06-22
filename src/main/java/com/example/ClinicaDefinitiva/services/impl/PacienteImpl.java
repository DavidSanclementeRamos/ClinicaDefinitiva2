package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.exceptions.PacienteNotFountException;
import com.example.ClinicaDefinitiva.exceptions.UsuarioNotfountException;
import com.example.ClinicaDefinitiva.mapper.PacienteMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.CreatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.UpdatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.PacienteRepository;
import com.example.ClinicaDefinitiva.repository.ResponsableRepository;
import com.example.ClinicaDefinitiva.repository.TurnoRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.PacienteServise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class PacienteImpl  implements PacienteServise {

   private final PacienteRepository pacienteRepository;
   private final PacienteMapperResponse pacienteMapperResponse;
   private final TurnoRepository turnoRepository;
   private final UsuarioRepository usuarioRepository;
   private final ResponsableRepository responsableRepository;

    public PacienteImpl(PacienteRepository pacienteRepository,  PacienteMapperResponse pacienteMapperResponse, TurnoRepository turnoRepository, UsuarioRepository usuarioRepository, ResponsableRepository responsableRepository) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapperResponse = pacienteMapperResponse;
        this.turnoRepository = turnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.responsableRepository = responsableRepository;
    }

    @Override
    public ReadPacienteDto findId(long id) {

        return pacienteRepository.findById(id)
                .map(pacienteMapperResponse::readPaciente)
                .orElseThrow(com.example.ClinicaDefinitiva.exceptions.PacienteNotFountException::new);
    }

    @Override
    public List<ReadPacienteDto> findAll() {
        return pacienteRepository.findAll()
                .stream().map(pacienteMapperResponse::readPaciente)
                .collect(Collectors.toList());

    }

    @Override
    public List<ReadPacienteDto> findByResponsableId(long responsableId) {

        return   responsableRepository.findById(responsableId)
                .map(pacienteRepository::findByResponsable)
                .map(listaPaciente -> listaPaciente.stream()
                .map(pacienteMapperResponse::readPaciente)
                        .collect(Collectors.toList()))
                .orElseThrow(PacienteNotFountException::new);

    }

    @Override
    public List<ReadPacienteDto> findByTurnoId(long turnoId) {

        return  turnoRepository.findById(turnoId)
                .map(pacienteRepository::findByUnTurnoContaining)
                .map(listaPaciente -> listaPaciente.stream()
                        .map(pacienteMapperResponse::readPaciente)
                        .collect(Collectors.toList()))
                .orElseThrow(PacienteNotFountException::new);
    }

   /* @Override
    public ReadPacienteDto save(CreatePacienteDto createPacienteDto) {
        Usuario usuario = usuarioRepository.findById(createPacienteDto.getIdUsuario())
                .orElseThrow(UsuarioNotfountException::new);

        Responsable responsable = responsableRepository.findById(createPacienteDto.getIdResponsable())
                .orElseThrow(ResponsableNotFountException::new);

        Turno turno = turnoRepository.findById(createPacienteDto.getIdTurno())
                .orElseThrow(TurnoNotFountException::new);

        // Crear el paciente manualmente sin Optional ni map()
        Paciente paciente = new Paciente();
        paciente.setDni(createPacienteDto.getDni());
        paciente.setNombre(createPacienteDto.getNombre());
        paciente.setApellido(createPacienteDto.getApellido());
        paciente.setTipoSangre(createPacienteDto.getTipoSangre());
        paciente.setFecha_nacimiento(createPacienteDto.getFecha_nacimiento());
        paciente.setDirecion(createPacienteDto.getDirecion());
        paciente.setTiene_Os(createPacienteDto.isTiene_Os());
        paciente.setUnResponsable(responsable);
        paciente.setUnTurno(turno);
        paciente.setResponsable(createPacienteDto.isTieneResponsable());
        paciente.setUnUsuario(usuario);

        // Guardar el paciente en la base de datos
        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        // Convertir a DTO
        return pacienteMapperResponse.readPaciente(pacienteGuardado);
    }*/

    @Override
    public ReadPacienteDto update(long id, UpdatePacienteDto updatePacienteDto) {
        // Buscar el paciente existente
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(PacienteNotFountException::new);


        // Actualizar los datos del paciente
        paciente.setDirecion(updatePacienteDto.getDirecion());
        paciente.setTelefono(updatePacienteDto.getTelefono());
       // paciente.setUnResponsable(responsable);
       // paciente.setUnTurno(turno);
        paciente.setTiene_Os(updatePacienteDto.isTiene_Os());
        //paciente.setUnUsuario(usuario);

        // Guardar en la base de datos
        Paciente pacienteActualizado = pacienteRepository.save(paciente);

        // Convertir a DTO y devolverlo
        return pacienteMapperResponse.readPaciente(pacienteActualizado);
    }



@Override
public ReadPacienteDto save(CreatePacienteDto createPacienteDto) {
    Usuario usuario = usuarioRepository.findById(createPacienteDto.getIdUsuario())
            .orElseThrow(UsuarioNotfountException::new);

  /*  Responsable responsable = null;
    if (createPacienteDto.getIdResponsable() != null) {
        responsable = responsableRepository.findById(createPacienteDto.getIdResponsable())
                .orElseThrow(ResponsableNotFountException::new);
    }*/
    Responsable responsable = null;
    responsable = Optional.ofNullable(createPacienteDto.getIdResponsable())
            .map(id -> responsableRepository.findById(id).orElse(null))
            .orElse(null);


    // Calcular la edad del paciente
    int edadPaciente = Period.between(createPacienteDto.getFecha_nacimiento(), LocalDate.now()).getYears();

    // Validar si el paciente es menor y no tiene responsable
    if (edadPaciente < 18 && responsable == null) {
        throw new IllegalArgumentException("El paciente menor de edad debe tener un responsable, primero registre al responsable.");
    }

    // Crear el paciente con datos validados
    Paciente paciente = new Paciente();
    paciente.setDni(createPacienteDto.getDni());
    paciente.setNombre(createPacienteDto.getNombre());
    paciente.setApellido(createPacienteDto.getApellido());
    paciente.setTipoSangre(createPacienteDto.getTipoSangre());
    paciente.setFecha_nacimiento(createPacienteDto.getFecha_nacimiento());
    paciente.setDirecion(createPacienteDto.getDirecion());
    paciente.setTiene_Os(createPacienteDto.isTiene_Os());
    paciente.setUnResponsable(responsable);
   // paciente.setUnTurno(turno);
    paciente.setUnUsuario(usuario);

    // Guardar en la base de datos
    Paciente pacienteGuardado = pacienteRepository.save(paciente);

    // Convertir a DTO y devolverlo
    return pacienteMapperResponse.readPaciente(pacienteGuardado);
}
}
