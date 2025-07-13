package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.exceptions.entityNotFount.PacienteNotFountException;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.UsuarioNotfountException;
import com.example.ClinicaDefinitiva.mapper.PacienteMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.CreatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.UpdatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.PacienteRepository;
import com.example.ClinicaDefinitiva.repository.ResponsableRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.PacienteServise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class PacienteImpl  implements PacienteServise {

   private final PacienteRepository pacienteRepository;
   private final PacienteMapperResponse pacienteMapperResponse;
   private final UsuarioRepository usuarioRepository;
   private final ResponsableRepository responsableRepository;

    public PacienteImpl(PacienteRepository pacienteRepository, PacienteMapperResponse pacienteMapperResponse
            , UsuarioRepository usuarioRepository, ResponsableRepository responsableRepository) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapperResponse = pacienteMapperResponse;
        this.usuarioRepository = usuarioRepository;
        this.responsableRepository = responsableRepository;
    }


    @Override
    public Optional<ReadPacienteDto> findId(long id) {

        return pacienteRepository.findById(id)
                .map(pacienteMapperResponse::readPaciente);
    }

    @Override
    public Page<ReadPacienteDto> findAll(Pageable pageable) {
        Page<Paciente> entidadPage = pacienteRepository.findAll(pageable);
         return       entidadPage.map(pacienteMapperResponse::readPaciente);


    }

    @Override
    public List<ReadPacienteDto> findByNombreContainingIgnoreCase(String nombre) {
        return pacienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(pacienteMapperResponse::readPaciente).collect(Collectors.toList());
    }

    @Override
    public Optional<ReadPacienteDto> findByDocumento(String documento) {
        return pacienteRepository.findByDni(documento)
                .map(pacienteMapperResponse::readPaciente);
    }

    @Override
    public Optional<ReadPacienteDto> findByUsuario_Id(long idUsuario) {
        return pacienteRepository.findByUnUsuario_Id(idUsuario)
                .map(pacienteMapperResponse::readPaciente);
    }

    @Override
    public List<ReadPacienteDto> findConTurnosParaFecha(LocalDate fecha) {
        return pacienteRepository.findByUnTurno_FechaTurno(fecha)
                .stream().map(pacienteMapperResponse::readPaciente)
                .collect(Collectors.toList());
    }


    @Override
    public ReadPacienteDto update(long id, UpdatePacienteDto updatePacienteDto) {
        // Buscar el paciente existente
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(PacienteNotFountException::new);


        // Actualizar los datos del paciente
        paciente.setDireccion(updatePacienteDto.getDireccion());
        paciente.setTelefono(updatePacienteDto.getTelefono());
        paciente.setTiene_Os(updatePacienteDto.isTiene_Os());

        // Guardar en la base de datos
        Paciente pacienteActualizado = pacienteRepository.save(paciente);

        // Convertir a DTO y devolverlo
        return pacienteMapperResponse.readPaciente(pacienteActualizado);
    }

    @Override
    public void deleaById(long id) {
        if(pacienteRepository.findById(id).isEmpty()){
            throw new PacienteNotFountException();
        }
        pacienteRepository.deleteById(id);
    }


    @Override
public ReadPacienteDto save(CreatePacienteDto createPacienteDto) {
    Usuario usuario = usuarioRepository.findById(createPacienteDto.getIdUsuario())
            .orElseThrow(UsuarioNotfountException::new);


    Responsable responsable = null;
    responsable = Optional.of(createPacienteDto.getIdResponsable()).flatMap(responsableRepository::findById)
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
    paciente.setDireccion(createPacienteDto.getDireccion());
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
