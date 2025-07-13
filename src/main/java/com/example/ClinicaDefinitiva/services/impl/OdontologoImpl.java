package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.Enum.ContextoEntidad;
import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.OdontologoNotfountException;
import com.example.ClinicaDefinitiva.mapper.OdontologoMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.UpdateOdontologoDto;

import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.repository.OdontologoRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.OdontologoService;

import com.example.ClinicaDefinitiva.util.EdadMinimaConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OdontologoImpl  implements OdontologoService {

    private final OdontologoRepository odontologoRepository;
    private final OdontologoMapperResponse odontologoMapperResponse;
    private final UsuarioRepository usuarioRepository;

    public OdontologoImpl(OdontologoRepository odontologoRepository, OdontologoMapperResponse odontologoMapperResponse
            , UsuarioRepository usuarioRepository) {
        this.odontologoRepository = odontologoRepository;
        this.odontologoMapperResponse = odontologoMapperResponse;
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public Optional<ReadOdontologoDto> findId(long id) {

        return odontologoRepository.findById(id)
                .map(odontologoMapperResponse::readOdontologoDto);
    }

    @Override
    public Page<ReadOdontologoDto> findAll(Pageable pageable) {

        // Le decimos al repo que haga la búsqueda paginada+ordenada
        Page<Odontologo> pageEntidades = odontologoRepository.findAll(pageable);

        // Convertimos cada Entidad a DTO
        return pageEntidades.map(odontologoMapperResponse::readOdontologoDto);


    }

    @Override
    public Optional<ReadOdontologoDto> findByUsuario_Id(long idUsuario) {
        return odontologoRepository.findByUnUsuario_Id(idUsuario)
                .map(odontologoMapperResponse::readOdontologoDto);
    }

    @Override
    public List<ReadOdontologoDto> findByEspecialidad(Especialidades especialidad) {
        return odontologoRepository.findByEspecialidad(especialidad).stream()
                .map(odontologoMapperResponse::readOdontologoDto).collect(Collectors.toList());
    }

    @Override
    public List<ReadOdontologoDto> findConTurnosEntreFechas(LocalDate desde, LocalDate hasta) {
        return odontologoRepository.findByUnTurno_FechaTurnoBetween(desde, hasta).stream()
                .map(odontologoMapperResponse::readOdontologoDto).collect(Collectors.toList());
    }


    @Override
    public ReadOdontologoDto save(CreateOdontologoDto createOdontologoDto ) {
        EdadMinimaConfig validar = new EdadMinimaConfig();
        validar.validarEdadMinima(createOdontologoDto.getFecha_nacimiento(),25);
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
                .orElseThrow();


    }

    @Override
    public void deleaById(long id) {

        if(odontologoRepository.findById(id).isEmpty()){
            throw new OdontologoNotfountException();
        }
        odontologoRepository.deleteById(id);
    }

    @Override
    public ReadOdontologoDto update(long idOdontologo, UpdateOdontologoDto updateOdontologoDto) {

        // 1. Verificar que el odontólogo existe
        Odontologo odontologo = odontologoRepository.findById(idOdontologo)
                .orElseThrow(() -> new OdontologoNotfountException(ContextoEntidad.ODONTOLOGO, "No se encontró el odontólogo con ID: " + idOdontologo));

        // 4. Actualizar los datos del odontólogo
        odontologo.setTelefono(updateOdontologoDto.getTelefono());
        odontologo.setDireccion(updateOdontologoDto.getDireccion());
        odontologo.setEspecialidad(updateOdontologoDto.getEspecialidad());

        // 5. Guardar cambios
        Odontologo actualizado = odontologoRepository.save(odontologo);

        return odontologoMapperResponse.readOdontologoDto(actualizado);
    }



}



