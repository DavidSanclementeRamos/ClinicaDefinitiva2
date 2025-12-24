package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.domain.errors.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.TelefonoDuplicadoException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.PatientNotFoundException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.GuardianNotFoundException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.UserNotFoundException;
import com.example.ClinicaDefinitiva.mapper.ResponsableMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CambioResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.UpdateResponsableDto;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.PacienteRepository;
import com.example.ClinicaDefinitiva.repository.ResponsableRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.ResponsableService;
import com.example.ClinicaDefinitiva.util.ValidarEdades;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponsableImpl implements ResponsableService {

    private final ResponsableRepository responsableRepository;
    private final ResponsableMapperResponse responsableMapperResponse;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    String requestId = RequestIdFilter.getRequestId();
    private static final Logger logger = LoggerFactory.getLogger(ResponsableImpl.class);

    public ResponsableImpl(ResponsableRepository responsableRepository, ResponsableMapperResponse responsableMapperResponse, UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository) {
        this.responsableRepository = responsableRepository;
        this.responsableMapperResponse = responsableMapperResponse;
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
    }


    @Override
    public CreateEndReadResponsableDto findByUsuario_Id(long idUsuario) {
        Responsable responsable = responsableRepository.findByUnUsuario_Id(idUsuario)
                .orElseThrow(() -> {
                    // odontologoMetrics.contarOdontologoNoEncontrado(requestId);
                    logger.warn("Responsable no encontrado [id={}, requestId={}]", idUsuario, requestId);
                    return new PatientNotFoundException(
                            com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE,
                            "No se encontró el responsable con ID: " + idUsuario
                    );
                });

        logger.info("Paciente recuperado [id={}, requestId={}]", idUsuario, requestId);
        return responsableMapperResponse.createEndReadResponsableDto(responsable);
    }

    @Override
    public CreateEndReadResponsableDto findByPacientes_Id(long idPaciente) {
        Responsable responsable = responsableRepository.findByPaciente_Id(idPaciente)
                .orElseThrow(() -> {
                    logger.warn("Responsable no encontrado [idPaciente={}, requestId={}]", idPaciente, requestId);

                    return new GuardianNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE,
                    "No existen responsable con ese idPaciente:" + idPaciente);
        });
        logger.info("Responsable no encontrado [idPaciente={}, requestId={}]",idPaciente , requestId);
        return    responsableMapperResponse.createEndReadResponsableDto(responsable);

    }

    @Override
    public CreateEndReadResponsableDto findByDocumento(String documento) {
        Responsable responsable = responsableRepository.findByDni(documento)
                .orElseThrow(() -> {
                    logger.warn("Responsable no encontrado con el documento[documento={}, requestId={}]", documento, requestId);

                    return new GuardianNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE,
                            "No existen responsable con ese documento:" + documento);
                });
        logger.info("Responsable no encontrado [documento={}, requestId={}]",documento , requestId);
        return    responsableMapperResponse.createEndReadResponsableDto(responsable);
    }

    @Override
    public CreateEndReadResponsableDto findByTelefono(String telefono) {
        Responsable responsable = responsableRepository.findByTelefono(telefono)
                .orElseThrow(() -> {
                    logger.warn("Responsable no encontrado con el telefono[telefono={}, requestId={}]", telefono, requestId);

                    return new GuardianNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE,
                            "No existen responsable con ese telefono:" + telefono);
                });
        logger.info("Responsable no encontrado [telefono={}, requestId={}]",telefono , requestId);
        return    responsableMapperResponse.createEndReadResponsableDto(responsable);

    }

    @Override
    public List<CreateEndReadResponsableDto> findByTipoRelacion(TipoResponsable tipoRelacion) {
        List<Responsable> lista = responsableRepository.findByTipoResponsable(tipoRelacion);

        if(lista.isEmpty()){
            logger.warn("Responsable no encontrado con relacion: [tipoRelacion={}, requestId={}]",tipoRelacion.name() , requestId);
            throw new GuardianNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE,
                    "No hay resultado de la busquedacon esa relacion:" + tipoRelacion.name());
        }
        logger.info("Se encontraron {} responsable con tipoRelacion [{}], requestId={}",
                lista.size(), tipoRelacion.name(), requestId);
        return  lista .stream()
                .map(responsableMapperResponse::createEndReadResponsableDto)
                .collect(Collectors.toList());
    }

    @Override
    public CreateEndReadResponsableDto updateCambio(long id, CambioResponsableDto cambioResponsableDto) {

        // validar que la edad del odontologo sea la adecuada
        ValidarEdades validar = new ValidarEdades();
        validar.validarEdades (cambioResponsableDto.getFecha_nacimiento(), com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE);

        // Validar que el telefono no este dublicado
        // Validar que el dni no este dublicado

        validar.verificarDuplicados(cambioResponsableDto.getTelefono(),cambioResponsableDto.getDni());
        // validar que tenga usuario

        Responsable responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE, "El responsable que quiere cambiar no existe"));



        Usuario usuario = responsableRepository.findByUnUsuario_Id(cambioResponsableDto.getIdUsuario())
                .orElseThrow(() -> new UserNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE, "El responsable no tiene un usuario asignado")).getUnUsuario();

        responsable.setDni(cambioResponsableDto.getDni());
        responsable.setNombre(cambioResponsableDto.getNombre());
        responsable.setApellido(cambioResponsableDto.getApellido());
        responsable.setTelefono(cambioResponsableDto.getTelefono());
        responsable.setDireccion(cambioResponsableDto.getDireccion());
        responsable.setFecha_nacimiento(cambioResponsableDto.getFecha_nacimiento());
        responsable.setTipoResponsable(cambioResponsableDto.getTipoResponsable());
        responsable.setTipoSangre(cambioResponsableDto.getTipoSangre());
        responsable.setUnUsuario(usuario);

        responsableRepository.save(responsable);

        return responsableMapperResponse.createEndReadResponsableDto(responsable);
    }



    @Override
    public CreateEndReadResponsableDto update(long id, UpdateResponsableDto updateResponsableDto) {

        // validar que el telefono no este dublicado
        if(responsableRepository.existsByTelefono(updateResponsableDto.getTelefono())){
            throw new TelefonoDuplicadoException(EntityContext.RESPONSABLE,"El telefono ya exciste" + updateResponsableDto.getTelefono() );

        }
        // validar que el id exista
        return (CreateEndReadResponsableDto)responsableRepository.findById(id)
                .map(responsable ->  {

                    responsable.setTelefono(updateResponsableDto.getTelefono());
                    responsable.setDireccion(updateResponsableDto.getDireccion());
                   // responsable.setUnUsuario(usuario);
                    return responsableRepository.save(responsable);
                }).map(responsableMapperResponse::createEndReadResponsableDto)
                .orElseThrow(() -> new GuardianNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE,"No existe un responsable con ese id:" + id ));
    }

    @Override
    public CreateEndReadResponsableDto save(CreateEndReadResponsableDto createEndReadResponsableDto) {
        // validar que la edad del odontologo sea la adecuada
        ValidarEdades validar = new ValidarEdades();
        validar.validarEdades (createEndReadResponsableDto.getFecha_nacimiento(), com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE);

        // Validar que el telefono no este dublicado
        // Validar que el dni no este dublicado
        validar.verificarDuplicados(createEndReadResponsableDto.getTelefono(),createEndReadResponsableDto.getDni());

        return usuarioRepository.findById(createEndReadResponsableDto.getReadUsuarioDto().getId())
                .map(usuario -> {

                    Responsable responsable = new Responsable();

                    responsable.setDni(createEndReadResponsableDto.getDni());
                    responsable.setNombre(createEndReadResponsableDto.getNombre());
                    responsable.setApellido(createEndReadResponsableDto.getApellido());
                    responsable.setFecha_nacimiento(createEndReadResponsableDto.getFecha_nacimiento());
                    responsable.setDireccion(createEndReadResponsableDto.getDireccion());
                    responsable.setTelefono(createEndReadResponsableDto.getTelefono());
                    responsable.setTipoSangre(createEndReadResponsableDto.getTipoSangre());
                    responsable.setTipoResponsable(createEndReadResponsableDto.getTipoResponsable());
                    responsable.setUnUsuario(usuario);
                    return responsableRepository.save(responsable);

                }).map(responsableMapperResponse::createEndReadResponsableDto)
                .orElseThrow(() -> new GuardianNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE,
                " EL responsable no tiene un usuario asignado"));

    }

    @Override
    public void deleaById(long id) {
        if(responsableRepository.findById(id).isEmpty()){
            logger.warn("Responsable no encontrado con id: [id={}, requestId={}]",id , requestId);
            throw new GuardianNotFoundException(com.example.ClinicaDefinitiva.domain.errors.EntityContext.RESPONSABLE,
                    " El responsable no existe, id:" + id);
        }
        responsableRepository.deleteById(id);
    }
}
