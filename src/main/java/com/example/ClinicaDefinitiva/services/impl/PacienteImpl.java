package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.EdadNoPermitidaException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.TelefonoDuplicadoException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.PatientNotFoundException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.UserNotFoundException;
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
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    String requestId = RequestIdFilter.getRequestId();
    private static final Logger logger = LoggerFactory.getLogger(PacienteImpl.class);


    public PacienteImpl(PacienteRepository pacienteRepository, PacienteMapperResponse pacienteMapperResponse
            , UsuarioRepository usuarioRepository, ResponsableRepository responsableRepository) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapperResponse = pacienteMapperResponse;
        this.usuarioRepository = usuarioRepository;
        this.responsableRepository = responsableRepository;
    }


    @Override
    public ReadPacienteDto findId(long id) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> {
                   // odontologoMetrics.contarOdontologoNoEncontrado(requestId);
                    logger.warn("Paciente no encontrado [id={}, requestId={}]", id, requestId);
                    return new PatientNotFoundException(
                            ContextoEntidad.PACIENTE,
                            "No se encontró el paciente con ID: " + id
                    );
                });

        logger.info("Paciente recuperado [id={}, requestId={}]", id, requestId);
                return pacienteMapperResponse.readPaciente(paciente);
    }

    @Override
    public Page<ReadPacienteDto> findAll(Pageable pageable) {
        Page<Paciente> entidadPage = pacienteRepository.findAll(pageable);
        if(entidadPage.isEmpty()){
        throw new PatientNotFoundException(ContextoEntidad.PACIENTE,
                "No existen registros de pacientes para los filtros dados"
        );}
         return       entidadPage.map(pacienteMapperResponse::readPaciente);


    }

    @Override
    public List<ReadPacienteDto> findByNombreContainingIgnoreCase(String nombre) {
        List<Paciente> lista = pacienteRepository.findByNombreContainingIgnoreCase(nombre);

        if (lista.isEmpty()) {
            //odontologoMetrics.contarOdontologoNoEncontrado(requestId);
            logger.warn("No se encontraron pacientes con ese nombre [{}], requestId={}", nombre, requestId);

            throw new PatientNotFoundException(
                    ContextoEntidad.PACIENTE,
                    "No se encontraron pacientes con el nombre: " + nombre
            );
        }

        //odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Se encontraron {} pacientes con el nombre [{}], requestId={}",
                lista.size(), nombre, requestId);

        return lista.stream()
                .map(pacienteMapperResponse::readPaciente).collect(Collectors.toList());
    }

    @Override
    public ReadPacienteDto findByDocumento(String documento) {
        Paciente paciente = pacienteRepository.findByDni(documento)
                .orElseThrow(() -> {
                   // odontologoMetrics.contarOdontologoNoEncontrado(requestId);
                    logger.warn("El documento no existe  [documento={}, requestId={}]", documento, requestId);
                    return new PatientNotFoundException(
                            ContextoEntidad.PACIENTE,
                            "No se encontró ningún paciente asociado al documento: " + documento
                    );
                });

        //odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Documentos encontrado [documento={}, requestId={}]",
                documento, requestId
        );
                return pacienteMapperResponse.readPaciente(paciente);
    }

    @Override
    public ReadPacienteDto findByUsuario_Id(long idUsuario) {
        Paciente paciente = pacienteRepository.findByUnUsuario_Id(idUsuario)
                .orElseThrow(() -> {
                   // odontologoMetrics.contarOdontologoNoEncontrado(requestId);
                    logger.warn("Usuario sin paciente asociado [usuarioId={}, requestId={}]", idUsuario, requestId);
                    return new DentistNotFoundException(
                            ContextoEntidad.ODONTOLOGO,
                            "No se encontró ningún paciente asociado al usuario con ID: " + idUsuario
                    );
                });

       // odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Paciente encontrado [usuarioId={}, pacienteId={}, requestId={}]",
                idUsuario, paciente.getId(), requestId
        );
                return pacienteMapperResponse.readPaciente(paciente);
    }

    @Override
    public List<ReadPacienteDto> findConTurnosParaFecha(LocalDate fecha) {
        List<Paciente> lista = pacienteRepository.findByUnTurno_FechaTurno(fecha);

        if(lista.isEmpty()){
            logger.warn("No se encontraron pacientes en el turno con fecha [fecha={}, requestId={}]", fecha, requestId);
            throw new PatientNotFoundException(ContextoEntidad.PACIENTE, "No fue encontrado el turno del paciente en la fecha:" + fecha);
        }

        return  lista.stream().map(pacienteMapperResponse::readPaciente)
                .collect(Collectors.toList());
    }


    @Override
    public ReadPacienteDto update(long id, UpdatePacienteDto updatePacienteDto) {

        // verificar que el telefono no este duplicado
        if (pacienteRepository.existsByTelefono(updatePacienteDto.getTelefono())) {
            logger.warn("Ya existe el numero [updatePacienteDto.getTelefono()={}, requestId={}]",updatePacienteDto.getTelefono(), requestId);

            throw new TelefonoDuplicadoException(ContextoEntidad.PACIENTE,"El numero de telefono ya exciste" + updatePacienteDto.getTelefono() );
        }
        // verificar que el id exista
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(ContextoEntidad.PACIENTE, "No exsiste el id: " + id));

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
            logger.info("No exite el id [id={}, requestId={}]", id,requestId);

            throw new PatientNotFoundException(ContextoEntidad.PACIENTE,"No fue en contrado el paciente con id: " + id);
        }
        pacienteRepository.deleteById(id);
    }


    @Override
public ReadPacienteDto save(CreatePacienteDto createPacienteDto) {
    Usuario usuario = usuarioRepository.findById(createPacienteDto.getIdUsuario())
            .orElseThrow(() -> new UserNotFoundException(ContextoEntidad.PACIENTE,
                    "El paciente: " + createPacienteDto.getIdUsuario() + " no tiene un usuario asignado "));


    Responsable responsable = null;
    responsable = Optional.of(createPacienteDto.getIdResponsable()).flatMap(responsableRepository::findById)
            .orElse(null);

    // Calcular la edad del paciente
    int edadPaciente = Period.between(createPacienteDto.getFecha_nacimiento(), LocalDate.now()).getYears();

    // Validar si el paciente es menor y no tiene responsable
    if (edadPaciente < 18 && responsable == null) {
        logger.info("La edad del paciente es [id={}, requestId={}]",edadPaciente ,requestId);

        throw new EdadNoPermitidaException(ContextoEntidad.PACIENTE,
                "El paciente: " + createPacienteDto.getNombre() + " " + createPacienteDto.getApellido() + "  necesita un responsable por ser menor de edad: + ");
    }
        // Validar que el dni no este dublicado
        if (pacienteRepository.existsByDni(createPacienteDto.getDni())) {
            throw new TelefonoDuplicadoException(ContextoEntidad.PACIENTE,"El dni de telefono ya exciste" + createPacienteDto.getDni() );
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
