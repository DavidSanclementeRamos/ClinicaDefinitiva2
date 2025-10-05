package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.domain.exceptions.TelefonoDuplicadoException;
import com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.exceptions.entityNotFount.ReceptionistNotFoundException;
import com.example.ClinicaDefinitiva.mapper.SecretarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.UpdateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Secretario;
import com.example.ClinicaDefinitiva.repository.SecretarioRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.SecretarioService;
import com.example.ClinicaDefinitiva.util.ValidarEdades;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service

public class SecretarioImpl implements SecretarioService {

    private final SecretarioRepository secretarioRepository;
    private final SecretarioMapperResponse secretarioReadMapper;
    private final UsuarioRepository usuarioRepository;
    String requestId = RequestIdFilter.getRequestId();
    private static final Logger logger = LoggerFactory.getLogger(SecretarioImpl.class);



    public SecretarioImpl(SecretarioRepository secretarioRepository, UsuarioRepository usuarioRepository
            , SecretarioMapperResponse secretarioReadMapper) {
        this.secretarioRepository = secretarioRepository;
        this.secretarioReadMapper = secretarioReadMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ReadSecretarioDto findId(long idSecretario) {

        Secretario secretario = secretarioRepository.findById(idSecretario)
                 .orElseThrow(() -> {
                     //odontologoMetrics.contarOdontologoNoEncontrado(requestId);
                     logger.warn("Secretari@ no encontrado [idSecretario={}, requestId={}]", idSecretario, requestId);
                     return new DentistNotFoundException(
                             ContextoEntidad.SECRETARIO,
                             "No se encontró el secretari@ con ID: " + idSecretario
                     );
                 });

        logger.info("Secretari@s recuperado [id={}, requestId={}]", idSecretario, requestId);
                return secretarioReadMapper.readSecretarioDto(secretario);
    }


    @Override
    public Page<ReadSecretarioDto> findAll(Pageable pageable) {
        // Le decimos al repo que haga la búsqueda paginada+ordenada
        Page<Secretario> pageEntidades = secretarioRepository.findAll(pageable);
        if(pageEntidades.isEmpty()){
            throw new ReceptionistNotFoundException(ContextoEntidad.SECRETARIO
                    , "No existen registros de secretari@ para los filtros dados"
            );
        }

        // Convertimos cada Entidad a DTO
        return pageEntidades.map(secretarioReadMapper::readSecretarioDto);

    }

    @Override
    public List<ReadSecretarioDto> findByNombreContainingIgnoreCase(String nombre) {
        List< Secretario>  lista = secretarioRepository.findByNombreContainingIgnoreCase(nombre);
        if(lista.isEmpty()){
            logger.info("Se encontraron {} secretari@ con nombre [{}], requestId={}",
                    lista.size(),nombre , requestId);

            throw new ReceptionistNotFoundException(ContextoEntidad.SECRETARIO
            ,"No se encontro secretari@ con ese nombre:" + nombre);
        }
                return lista.stream().map(secretarioReadMapper::readSecretarioDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadSecretarioDto> findBySector(Sector sector) {

        List< Secretario>  lista =  secretarioRepository.findBySector(sector);
        if (lista.isEmpty()){
            throw new ReceptionistNotFoundException(ContextoEntidad.SECRETARIO
                    ,"No se encontro secretari@ en ese sector:" + sector);
        }

                return lista.stream()
                .map(secretarioReadMapper::readSecretarioDto)
                .collect(Collectors.toList());

    }

    @Override
    public ReadSecretarioDto findByUsuarioId(long idUsuario) {
        Secretario secretario =  secretarioRepository.findByUnUsuario_Id(idUsuario)
                .orElseThrow(()-> {
                    logger.warn("Secretari@ no encontrado [idUsuario={}, requestId={}]", idUsuario, requestId);
                    return  new ReceptionistNotFoundException(ContextoEntidad.SECRETARIO, "No se encontro secretari@ con ese  id usuario:" + idUsuario);

                });

                return secretarioReadMapper.readSecretarioDto(secretario);
    }


    @Override
    public ReadSecretarioDto save(CreateSecretarioDto createSecretarioDto) {

        // validar que la edad del secretario sea la adecuada
        ValidarEdades validar =new ValidarEdades();
        validar.validarEdades (createSecretarioDto.getFecha_nacimiento(),ContextoEntidad.SECRETARIO);

        // Validar que el telefono no este dublicado
        if (secretarioRepository.existsByTelefono(createSecretarioDto.getTelefono())) {
            throw new TelefonoDuplicadoException(ContextoEntidad.ODONTOLOGO,"El numero de telefono ya exciste" + createSecretarioDto );
        }
        // Validar que el dni no este dublicado
        if (secretarioRepository.existsByDni(createSecretarioDto.getDni())) {
            throw new TelefonoDuplicadoException(ContextoEntidad.SECRETARIO,"El dni  ya exciste" + createSecretarioDto.getDni() );
        }
        return usuarioRepository.findById(createSecretarioDto.getIdUsuario())
                .map(usuario -> {
                    Secretario secretario = new Secretario();
                    secretario.setDni(createSecretarioDto.getDni());
                    secretario.setNombre(createSecretarioDto.getNombre());
                    secretario.setApellido(createSecretarioDto.getApellido());
                    secretario.setDireccion(createSecretarioDto.getDireccion());
                    secretario.setTelefono(createSecretarioDto.getTelefono());
                    secretario.setSector(createSecretarioDto.getSector());
                    secretario.setFecha_nacimiento(createSecretarioDto.getFecha_nacimiento());
                    secretario.setUnUsuario(usuario);

                    return secretarioRepository.save(secretario);
                }).map(secretarioReadMapper::readSecretarioDto)
                .orElseThrow(() -> new ReceptionistNotFoundException(ContextoEntidad.SECRETARIO, "No existe un usuario con el id:" + createSecretarioDto.getIdUsuario())) ;
    }

    @Override
    public ReadSecretarioDto update(long id, UpdateSecretarioDto updateSecretarioDto) {
        // Validar que el telefono no este dublicado
        if (secretarioRepository.existsByTelefono(updateSecretarioDto.getTelefono())) {
            logger.warn("EL numero ya existe [{}], requestId={}", updateSecretarioDto.getTelefono(), requestId);

            throw new TelefonoDuplicadoException(ContextoEntidad.SECRETARIO,"El numero de telefono ya exciste" + updateSecretarioDto.getTelefono() );
        }
        return secretarioRepository.findById(id)
                .map(secretario -> {

                    secretario.setDireccion(updateSecretarioDto.getDireccion());
                    secretario.setSector(updateSecretarioDto.getSector());
                    secretario.setTelefono(updateSecretarioDto.getTelefono());
                    return secretarioRepository.save(secretario);
                }).map(secretarioReadMapper::readSecretarioDto)
                .orElseThrow(() -> {
                        logger.warn("EL id del secretario que quiere editar no existe [{}], requestId={}", id, requestId);
                        return new ReceptionistNotFoundException(ContextoEntidad.SECRETARIO,"No se encontro el id: " + id );});



    }

    @Override
    public void deleaById(long id) {
        if(secretarioRepository.findById(id).isEmpty()){
            logger.warn("No existe el id: [{}], requestId={}", id, requestId);

            throw new ReceptionistNotFoundException(ContextoEntidad.SECRETARIO,
                    "No se encontro el id: " + id + " del secretari@ que quiere eliminar" );
        }
        secretarioRepository.deleteById(id);
    }
}
