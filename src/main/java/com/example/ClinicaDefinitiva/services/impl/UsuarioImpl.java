package com.example.ClinicaDefinitiva.services.impl;


import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.HorarioNotfoundException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.entityNotFount.UserNotFoundException;
import com.example.ClinicaDefinitiva.mapper.UsuarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.UpdateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.UsuarioService;
import com.example.ClinicaDefinitiva.web.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapperResponse usuarioMapper;
    String requestId = RequestIdFilter.getRequestId();
    private static final Logger logger = LoggerFactory.getLogger(UsuarioImpl.class);



    public UsuarioImpl(UsuarioRepository usuarioRepository, UsuarioMapperResponse usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public ReadUsuarioDto findId(long idUsuario) {
        Usuario usuario =  usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> {
            //odontologoMetrics.contarOdontologoNoEncontrado(requestId);
            logger.warn("Usuario no encontrado [idUsuario={}, requestId={}]", idUsuario, requestId);
            return new UserNotFoundException(EntityContext.TURNO," Usuario no encontrado por el id:" + idUsuario);
        });

        logger.info("Usuario recuperado [idUsuario={}, requestId={}]", idUsuario, requestId);

                return usuarioMapper.readUsuarioDto(usuario);
    }

    @Override
    public Page<ReadUsuarioDto> findAll(Pageable pageable) {

        Page<Usuario> pageEntity = usuarioRepository.findAll(pageable);
        if (pageEntity.isEmpty()) {
            throw new HorarioNotfoundException(
                    EntityContext.HORARIO,
                    "No existen registros de usuario para los filtros dados"
            );}
        long total = pageEntity.getTotalElements();
        logger.info("Se encontraron {} eventos en total para el filtro aplicado, [requestId={}]", total, requestId);
        return pageEntity.map(usuarioMapper::readUsuarioDto);

    }

    @Override
    public ReadUsuarioDto findByEmail(String email) {
        Usuario usu = usuarioRepository.findByCorreoEletronicoIgnoreCase(email)
                .orElseThrow(() -> {

                    logger.warn("No se encontro usuario el email: [email={}, requestId={}]", email, requestId);
                    return new UserNotFoundException(EntityContext.USUARIO,
                            "No se encontro usuario con ese email");});

        logger.info("Email recuperado [email={}, requestId={}]", email, requestId);
        return usuarioMapper.readUsuarioDto(usu);
    }

   /* @Override
    public List<ReadUsuarioDto> findByRol(Roles rol) {
        List<Usuario> lista = usuarioRepository.findByRol(rol);
        if(lista.isEmpty()){

            logger.warn("Usuario no encontrado por rol: [rol={},  requestId={}]", rol.name(), requestId);

            throw new UserNotFoundException(EntityContext.USUARIO,
                    " No se encontron roles de tipo : " + rol.name() );

        }
        logger.info("Rol  recuperado   [rol={},  requestId={}]", rol.name() ,requestId);


        return lista.stream()
                .map(usuarioMapper::readUsuarioDto)
                .collect(Collectors.toList());
    }*/


    @Override
    public ReadUsuarioDto findByNombreUsuario(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(nombreUsuario)
                .orElseThrow(() -> {
                    logger.warn("No se encontro el user [nombreUsuario={}, requestId={}]", nombreUsuario, requestId);

                   return new UserNotFoundException(EntityContext.USUARIO,
                            "No se encontro el nombre de usuario: " + nombreUsuario );
                });
        logger.info("Nombre de usuario recuperado [nombreUsuario={}, requestId={}]", nombreUsuario, requestId);
        return usuarioMapper.readUsuarioDto(usuario);
    }

    @Override
    public ReadUsuarioDto save(CreateUsuarioDto createUsuarioDto) {


            Usuario usu=new Usuario();
            usu.setNombreUsuario(createUsuarioDto.getNombreUsuario());
            usu.setCorreoEletronico(createUsuarioDto.getCorreoEletronico());
            usu.setContrasena(createUsuarioDto.getContrasena());
            usu.setEstado(createUsuarioDto.getEstado());
          //  usu.setRol(createUsuarioDto.getRol());
            usu.setImagenPerfil(createUsuarioDto.getImagenPerfil());
            usu.setUltimaFechaDeCoexion(createUsuarioDto.getUltimaFechaDeCoexion());
        logger.info("Usuario creado [usuario={}, requestId={}]", createUsuarioDto.getNombreUsuario(), requestId);

        return usuarioMapper.readUsuarioDto( usuarioRepository.save(usu));

    }

    @Override
    public ReadUsuarioDto update(long idUsuario, UpdateUsuarioDto updateUsuarioDto) {

        return usuarioRepository.findById(idUsuario)
                .map(usu -> {
                    usu.setNombreUsuario(updateUsuarioDto.getNombreUser());
                    usu.setCorreoEletronico(updateUsuarioDto.getCorreoEletronico());
                    usu.setContrasena(updateUsuarioDto.getContrasena());
                    usu.setImagenPerfil(updateUsuarioDto.getImagenPerfil());
                    usu.setEstado(updateUsuarioDto.getEstado());

                    return usuarioRepository.save(usu);
                }).map(usuarioMapper::readUsuarioDto)
                .orElseThrow(() -> {
                    logger.warn("No se puesde editar, no se encontro el usuario con el id : [idUsuario={}, requestId={}]",
                            idUsuario, requestId);

                    logger.info("Usuario editado [idUsuario={}, requestId={}]", idUsuario, requestId);
                    return new UserNotFoundException(EntityContext.USUARIO, " No se encontro el usuario con id: " + idUsuario);
                });
    }

    @Override
    public void deleaById(long id) {
        if(usuarioRepository.findById(id).isEmpty()){
            logger.warn("No se pudo eliminar, el id no existe [id={}, requestId={}]", id, requestId);

            throw new UserNotFoundException(EntityContext.USUARIO,"No se pudo eliminar, el id no existe :" + id);
        }
        logger.info("Usuario eliminado [idUsuario={}, requestId={}]", id, requestId);

        usuarioRepository.deleteById(id);
    }


}
