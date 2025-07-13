package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.ResponsableNotFountException;
import com.example.ClinicaDefinitiva.mapper.ResponsableMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CambioResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.UpdateResponsableDto;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import com.example.ClinicaDefinitiva.repository.PacienteRepository;
import com.example.ClinicaDefinitiva.repository.ResponsableRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.ResponsableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResponsableImpl implements ResponsableService {

    private final ResponsableRepository responsableRepository;
    private final ResponsableMapperResponse responsableMapperResponse;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;

    public ResponsableImpl(ResponsableRepository responsableRepository, ResponsableMapperResponse responsableMapperResponse, UsuarioRepository usuarioRepository, PacienteRepository pacienteRepository) {
        this.responsableRepository = responsableRepository;
        this.responsableMapperResponse = responsableMapperResponse;
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
    }


    @Override
    public Optional<CreateEndReadResponsableDto> findByUsuario_Id(long idUsuario) {
        return responsableRepository.findByUnUsuario_Id(idUsuario)
                .map(responsableMapperResponse::createEndReadResponsableDto);
    }

    @Override
    public List<CreateEndReadResponsableDto> findByPacientes_Id(long idPaciente) {
        return responsableRepository.findByPaciente_Id(idPaciente)
                .stream().map(responsableMapperResponse::createEndReadResponsableDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CreateEndReadResponsableDto> findByDocumento(String documento) {
        return responsableRepository.findByDni(documento)
                .map(responsableMapperResponse::createEndReadResponsableDto);
    }

    @Override
    public Optional<CreateEndReadResponsableDto> findByTelefono(String telefono) {
        return responsableRepository.findByTelefono(telefono)
                .map(responsableMapperResponse::createEndReadResponsableDto);
    }

    @Override
    public List<CreateEndReadResponsableDto> findByTipoRelacion(TipoResponsable tipoRelacion) {
        return responsableRepository.findByTipoResponsable(tipoRelacion).stream()
                .map(responsableMapperResponse::createEndReadResponsableDto)
                .collect(Collectors.toList());
    }

    @Override
    public CreateEndReadResponsableDto updateCambio(long id, CambioResponsableDto cambioResponsableDto) {
        return (CreateEndReadResponsableDto)responsableRepository.findById(id)
                .flatMap(responsable -> pacienteRepository
                        .findById(cambioResponsableDto.getPacienteId())
                        .map(usuario -> {

                            responsable.setDni(cambioResponsableDto.getDni());
                            responsable.setNombre(cambioResponsableDto.getNombre());
                            responsable.setApellido(cambioResponsableDto.getApellido());
                            responsable.setTelefono(cambioResponsableDto.getTelefono());
                            responsable.setDireccion(cambioResponsableDto.getDireccion());
                            responsable.setFecha_nacimiento(cambioResponsableDto.getFecha_nacimiento());
                            responsable.setTipoResponsable(cambioResponsableDto.getTipoResponsable());
                            responsable.setTipoSangre(cambioResponsableDto.getTipoSangre());

                          //  responsable.setUnUsuario(usuario);
                            return responsableRepository.save(responsable);
                        })).map(responsableMapperResponse::createEndReadResponsableDto)
                .orElseThrow(ResponsableNotFountException::new);
    }

    @Override
    public CreateEndReadResponsableDto update(long id, UpdateResponsableDto updateResponsableDto) {
        return (CreateEndReadResponsableDto)responsableRepository.findById(id)
                .map(responsable ->  {

                    responsable.setTelefono(updateResponsableDto.getTelefono());
                    responsable.setDireccion(updateResponsableDto.getDireccion());
                   // responsable.setUnUsuario(usuario);
                    return responsableRepository.save(responsable);
                }).map(responsableMapperResponse::createEndReadResponsableDto)
                .orElseThrow(ResponsableNotFountException::new);
    }

    @Override
    public CreateEndReadResponsableDto save(CreateEndReadResponsableDto createEndReadResponsableDto) {
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
                .orElseThrow(ResponsableNotFountException::new);

    }

    @Override
    public void deleaById(long id) {
        if(responsableRepository.findById(id).isEmpty()){
            throw new ResponsableNotFountException();
        }
        responsableRepository.deleteById(id);
    }
}
