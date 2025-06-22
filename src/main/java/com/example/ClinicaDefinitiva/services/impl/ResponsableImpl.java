package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.exceptions.ResponsableNotFountException;
import com.example.ClinicaDefinitiva.mapper.ResponsableMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CambioResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.UpdateResponsableDto;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import com.example.ClinicaDefinitiva.repository.PacienteRepository;
import com.example.ClinicaDefinitiva.repository.ResponsableRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.ResponsableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
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
    public CreateEndReadResponsableDto updateCambio(long id, CambioResponsableDto cambioResponsableDto) {
        return (CreateEndReadResponsableDto)responsableRepository.findById(id)
                .flatMap(responsable -> pacienteRepository
                        .findById(cambioResponsableDto.getPacienteId())
                        .map(usuario -> {

                            responsable.setDni(cambioResponsableDto.getDni());
                            responsable.setNombre(cambioResponsableDto.getNombre());
                            responsable.setApellido(cambioResponsableDto.getApellido());
                            responsable.setTelefono(cambioResponsableDto.getTelefono());
                            responsable.setDirecion(cambioResponsableDto.getDirecion());
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
                    responsable.setDirecion(updateResponsableDto.getDirecion());
                   // responsable.setUnUsuario(usuario);
                    return responsableRepository.save(responsable);
                }).map(responsableMapperResponse::createEndReadResponsableDto)
                .orElseThrow(ResponsableNotFountException::new);
    }

    @Override
    public CreateEndReadResponsableDto save(CreateEndReadResponsableDto createEndReadResponsableDto) {
        return usuarioRepository.findById(createEndReadResponsableDto.getReadUsuarioDto().getId_usuario())
                .map(usuario -> {

                    Responsable responsable = new Responsable();

                    responsable.setDni(createEndReadResponsableDto.getDni());
                    responsable.setNombre(createEndReadResponsableDto.getNombre());
                    responsable.setApellido(createEndReadResponsableDto.getApellido());
                    responsable.setFecha_nacimiento(createEndReadResponsableDto.getFecha_nacimiento());
                    responsable.setDirecion(createEndReadResponsableDto.getDirecion());
                    responsable.setTelefono(createEndReadResponsableDto.getTelefono());
                    responsable.setTipoSangre(createEndReadResponsableDto.getTipoSangre());
                    responsable.setTipoResponsable(createEndReadResponsableDto.getTipoResponsable());
                    responsable.setUnUsuario(usuario);
                    return responsableRepository.save(responsable);

                }).map(responsableMapperResponse::createEndReadResponsableDto)
                .orElseThrow(ResponsableNotFountException::new);

    }
}
