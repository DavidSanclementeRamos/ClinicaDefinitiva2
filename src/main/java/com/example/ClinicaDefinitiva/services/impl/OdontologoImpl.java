package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.TelefonoDuplicadoException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.UserNotFoundException;
import com.example.ClinicaDefinitiva.mapper.OdontologoMapperResponse;
import com.example.ClinicaDefinitiva.metrics.OdontologoMetrics;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.UpdateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.repository.OdontologoRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.OdontologoService;
import com.example.ClinicaDefinitiva.util.ValidarEdades;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OdontologoImpl  implements OdontologoService {


    private OdontologoMetrics odontologoMetrics;
    String requestId = RequestIdFilter.getRequestId();
    private final OdontologoRepository odontologoRepository;
    private final OdontologoMapperResponse odontologoMapperResponse;
    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(OdontologoImpl.class);

    public OdontologoImpl(OdontologoRepository odontologoRepository, OdontologoMapperResponse odontologoMapperResponse
            , UsuarioRepository usuarioRepository) {
        this.odontologoRepository = odontologoRepository;
        this.odontologoMapperResponse = odontologoMapperResponse;
        this.usuarioRepository = usuarioRepository;
    }



    @Override
    public ReadOdontologoDto findId(long id) {

        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> {
                    odontologoMetrics.contarOdontologoNoEncontrado(requestId);
                    logger.warn("Odontólogo no encontrado [id={}, requestId={}]", id, requestId);
                    return new DentistNotFoundException(
                            ContextoEntidad.ODONTOLOGO,
                            "No se encontró el odontólogo con ID: " + id
                    );
                });

        logger.info("Odontólogo recuperado [id={}, requestId={}]", id, requestId);
        return odontologoMapperResponse.readOdontologoDto(odontologo);
    }

    @Override
    public Page<ReadOdontologoDto> findAll(Pageable pageable) {

        // Le decimos al repo que haga la búsqueda paginada+ordenada
        Page<Odontologo> pageEntidades = odontologoRepository.findAll(pageable);

        if (pageEntidades.isEmpty()) {
            throw new DentistNotFoundException(
                    ContextoEntidad.ODONTOLOGO,
                    "No existen registros de odontólogos para los filtros dados"
            );
        }

        // Convertimos cada Entidad a DTO
        return pageEntidades.map(odontologoMapperResponse::readOdontologoDto);


    }

    @Override
    public ReadOdontologoDto findByUsuario_Id(long idUsuario) {

        Odontologo odontologo = odontologoRepository.findByUnUsuario_Id(idUsuario)
                .orElseThrow(() -> {
                    odontologoMetrics.contarOdontologoNoEncontrado(requestId);
                     return new DentistNotFoundException(
                            ContextoEntidad.ODONTOLOGO,
                            "No se encontró ningún odontólogo asociado al usuario con ID: " + idUsuario
                    );
                });

        odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Odontólogo encontrado [usuarioId={}, odontologoId={}, requestId={}]",
                idUsuario, odontologo.getId(), requestId
        );

        return odontologoMapperResponse.readOdontologoDto(odontologo);
    }

    @Override
    public List<ReadOdontologoDto> findByEspecialidad(Especialidades especialidad) {


        List<Odontologo> lista = odontologoRepository.findByEspecialidad(especialidad);

        if (lista.isEmpty()) {
            odontologoMetrics.contarOdontologoNoEncontrado(requestId);
            logger.warn("No se encontraron odontólogos con especialidad [{}], requestId={}", especialidad.name(), requestId);

            throw new DentistNotFoundException(
                    ContextoEntidad.ODONTOLOGO,
                    "No se encontraron odontólogos con la especialidad: " + especialidad.name()
            );
        }

        odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Se encontraron {} odontólogos con especialidad [{}], requestId={}",
                lista.size(), especialidad.name(), requestId);

        return lista.stream()
                .map(odontologoMapperResponse::readOdontologoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadOdontologoDto> findConTurnosEntreFechas(LocalDate desde, LocalDate hasta) {

        List<Odontologo> lista = odontologoRepository.findDistinctByListaTurnos_FechaTurnoBetween(desde,hasta);

        if (lista.isEmpty()) {
            odontologoMetrics.contarOdontologoNoEncontrado(requestId);
            logger.warn("Sin odontólogos con turnos entre fechas [{} - {}], requestId={}", desde, hasta, requestId);

            throw new DentistNotFoundException(
                    ContextoEntidad.ODONTOLOGO,
                    "No se encontraron odontólogos con turnos entre " + desde + " y " + hasta
            );
        }

        odontologoMetrics.contarOdontologoRecuperado(requestId);
        logger.info("Se encontraron {} odontólogos con turnos entre [{} - {}], requestId={}",
                lista.size(), desde, hasta, requestId);

        return lista.stream()
                .map(odontologoMapperResponse::readOdontologoDto)
                .collect(Collectors.toList());
    }


    @Override
    public ReadOdontologoDto save(CreateOdontologoDto createOdontologoDto ) {

        // validar que la edad del odontologo sea la adecuada
        ValidarEdades validar =new ValidarEdades();
        validar.validarEdades (createOdontologoDto.getFecha_nacimiento(),ContextoEntidad.ODONTOLOGO);

        // Validar que el telefono no este dublicado
        if (odontologoRepository.existsByTelefono(createOdontologoDto.getTelefono())) {
            throw new TelefonoDuplicadoException(ContextoEntidad.ODONTOLOGO,"El numero de telefono ya exciste" + createOdontologoDto.getTelefono() );
        }
        // Validar que el dni no este dublicado
        if (odontologoRepository.existsByDni(createOdontologoDto.getDni())) {
            throw new TelefonoDuplicadoException(ContextoEntidad.ODONTOLOGO,"El dni  ya exciste" + createOdontologoDto.getDni() );
        }

        // 1 VERIFICACION DE LAS RELACIONES
        return usuarioRepository.findById(createOdontologoDto.getIdUsuario())
                .map(usuario -> {

                    // CREA EL OBJECTO ODONTOLOGO CON DEPENDENCIA
                    Odontologo odontologo = new Odontologo();

                    odontologo.setNombre(createOdontologoDto.getNombre());
                    odontologo.setApellido(createOdontologoDto.getApellido());
                    odontologo.setDireccion(createOdontologoDto.getDireccion());
                    odontologo.setFecha_nacimiento(createOdontologoDto.getFecha_nacimiento());
                    odontologo.setDni(createOdontologoDto.getDni());
                    odontologo.setEspecialidad(createOdontologoDto.getEspecialidad());
                    odontologo.setUnUsuario(usuario);
                    odontologo.setTelefono(createOdontologoDto.getTelefono());
                    odontologo.setTipoSangre(createOdontologoDto.getTipoSangre());

                    return odontologoRepository.save(odontologo);
                 }
                ).map(odontologoMapperResponse::readOdontologoDto)
                .orElseThrow(()-> new UserNotFoundException(ContextoEntidad.ODONTOLOGO, "No se encontro el usuario el id" + createOdontologoDto.getIdUsuario()));


    }

    @Override
    public void deleaById(long id) {

        if(odontologoRepository.findById(id).isEmpty()){
            throw new DentistNotFoundException(ContextoEntidad.ODONTOLOGO," No se encontró el odontólogo con ID: " + id );
        }
        odontologoRepository.deleteById(id);
    }

    @Override
    public ReadOdontologoDto update(long idOdontologo, UpdateOdontologoDto updateOdontologoDto) {

        // Validar que el telefono no este dublicado
        if (odontologoRepository.existsByTelefono(updateOdontologoDto.getTelefono())) {
            throw new TelefonoDuplicadoException(ContextoEntidad.ODONTOLOGO,"El numero de telefono ya exciste" + updateOdontologoDto.getTelefono() );
        }
        // 1. Verificar que el odontólogo existe
        Odontologo odontologo = odontologoRepository.findById(idOdontologo)
                .orElseThrow(() -> new DentistNotFoundException(ContextoEntidad.ODONTOLOGO, " No se encontró el odontólogo con ID: " + idOdontologo));

        // 4. Actualizar los datos del odontólogo
        odontologo.setTelefono(updateOdontologoDto.getTelefono());
        odontologo.setDireccion(updateOdontologoDto.getDireccion());
        odontologo.setEspecialidad(updateOdontologoDto.getEspecialidad());

        // 5. Guardar cambios
        Odontologo actualizado = odontologoRepository.save(odontologo);

        return odontologoMapperResponse.readOdontologoDto(actualizado);
    }



}



