package com.example.ClinicaDefinitiva.services.impl;

import com.example.ClinicaDefinitiva.exceptions.OdontologoNotfountException;
import com.example.ClinicaDefinitiva.mapper.OdontologoMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.UpdateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.repository.HorarioRepository;
import com.example.ClinicaDefinitiva.repository.OdontologoRepository;
import com.example.ClinicaDefinitiva.repository.TurnoRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.OdontologoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class OdontologoImpl  implements OdontologoService {

    private final OdontologoRepository odontologoRepository;
    private final OdontologoMapperResponse odontologoMapperResponse;
    private final TurnoRepository turnoRepository;
    private final HorarioRepository horarioRepository;
    private final UsuarioRepository usuarioRepository;

    public OdontologoImpl(OdontologoRepository odontologoRepository, OdontologoMapperResponse odontologoMapperResponse, TurnoRepository turnoRepository, HorarioRepository horarioRepository, UsuarioRepository usuarioRepository) {
        this.odontologoRepository = odontologoRepository;
        this.odontologoMapperResponse = odontologoMapperResponse;
        this.turnoRepository = turnoRepository;
        this.horarioRepository = horarioRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public ReadOdontologoDto findId(long id) {

        return odontologoRepository.findById(id)
                .map(odontologoMapperResponse::readOdontologoDto)
                .orElseThrow(OdontologoNotfountException::new);
    }

    @Override
    public List<ReadOdontologoDto> findAll() {

        return odontologoRepository.findAll()
                .stream()
                .map(odontologoMapperResponse::readOdontologoDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<ReadOdontologoDto> findByTurnoId(long turnoId) {
        return  turnoRepository.findById(turnoId)// 1 BUSCA UN TURNO POR ID
                .map(odontologoRepository::findByUnTurnoContaining)// 2 BUSCA EL ODONTOLOGO A SOCIADO A ESE TURNO
                .map(listaOdontologos -> listaOdontologos.stream()// 3 CONVIERTE LA LISTA DE ODONTOLOGO EN STREAM
                        .map(odontologoMapperResponse::readOdontologoDto)// 4 MAPEA CADA ODONTOLOGO  A UN DTO
                        .collect(Collectors.toList())) // 5 RECOGE LA LISTA A UNA COLECCION
                       .orElseThrow(OdontologoNotfountException::new);// 6 SI NO HAY RESULTADO LANZA UNA EXCEPCION



    }

    @Override
    public List<ReadOdontologoDto> findByHorarioId(long horarioId) {
        return  horarioRepository.findById(horarioId)
                .map(odontologoRepository::findByUnHorarioContaining)
                .map(listaOdontologos -> listaOdontologos.stream()
                        .map(odontologoMapperResponse::readOdontologoDto)
                        .collect(Collectors.toList()))
                .orElseThrow(OdontologoNotfountException::new);

    }

    @Override
    public ReadOdontologoDto save(CreateOdontologoDto createOdontologoDto ) {
              // 1 VERIFICACION DE LAS RELACIONES

        return usuarioRepository.findById(createOdontologoDto.getIdUsuario())
                .map(usuario -> {

                       // CREA EL OBJECTO ODONTOLOGO CON DEPENDENCIA
                    Odontologo odontologo = new Odontologo();

                    odontologo.setNombre(createOdontologoDto.getNombre());
                    odontologo.setApellido(createOdontologoDto.getApellido());
                    odontologo.setDirecion(createOdontologoDto.getDirecion());
                    odontologo.setFecha_nacimiento(createOdontologoDto.getFecha_nacimiento());
                    odontologo.setDni(createOdontologoDto.getDni());
                    odontologo.setEspecialidad(createOdontologoDto.getEspecialidad());
                   // odontologo.setUnHorario(horario);
                    odontologo.setUnUsuario(usuario);
                    odontologo.setTelefono(createOdontologoDto.getTelefono());

                    return odontologoRepository.save(odontologo);
                 }
                ).map(odontologoMapperResponse::readOdontologoDto)
                .orElseThrow();
        /*
        LO MISMOS, ES MAS ENTENDIBLE, FACIL DE DEPURAR
        public CreateOdontologoDto save(CreateOdontologoDto createOdontologoDto) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(createOdontologoDto.getIdUsuario());
    Optional<Horario> horarioOpt = horarioRepository.findById(createOdontologoDto.getIdHorario());

    if (usuarioOpt.isPresent() && horarioOpt.isPresent()) {
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre(createOdontologoDto.getNombre());
        odontologo.setApellido(createOdontologoDto.getApellido());
        odontologo.setDirecion(createOdontologoDto.getDireccion());
        odontologo.setFecha_nacimiento(createOdontologoDto.getFechaNacimiento());
        odontologo.setDni(createOdontologoDto.getDni());
        odontologo.setEspecialidad(createOdontologoDto.getEspecialidad());
        odontologo.setUnHorario(horarioOpt.get());
        odontologo.setUnUsuario(usuarioOpt.get());
        odontologo.setTelefono(createOdontologoDto.getTelefono());

        return odontologoCreateMapper.odontologo(odontologoRepository.save(odontologo));
    } else {
        throw new IllegalArgumentException("Usuario o Horario no encontrados");
    }
}



         */

    }

    @Override
    public ReadOdontologoDto update(long idOdontologo, UpdateOdontologoDto updateOdontologoDto) {
        // 1. Verificar que el odontólogo existe
        Odontologo odontologo = odontologoRepository.findById(idOdontologo)
                .orElseThrow(OdontologoNotfountException::new);



        // 4. Actualizar los datos del odontólogo
        odontologo.setTelefono(updateOdontologoDto.getTelefono());
        odontologo.setDirecion(updateOdontologoDto.getDirecion());
        odontologo.setEspecialidad(updateOdontologoDto.getEspecialidad());

        // 5. Guardar cambios
        Odontologo actualizado = odontologoRepository.save(odontologo);

        return odontologoMapperResponse.readOdontologoDto(actualizado);
    }/*

    La programación funcional es poderosa, pero para validaciones de existencia y manejo
    de excepciones, la programación imperativa suele ser más clara y fácil de entender.
    No es que la programación funcional sea incorrecta, sino que en este caso, la claridad
    es más importante que la concisión.

    public ReadOdontologoDto update(long idOdontologo, UpdateOdontologoDto updateOdontologoDto) {
        return odontologoRepository.findById(idOdontologo)
                .flatMap(odontologo -> usuarioRepository.findById(updateOdontologoDto.getIdUsuario())
                        .flatMap(usuario -> horarioRepository.findById(updateOdontologoDto.getIdHorario())
                                .map(horario -> {
                                    odontologo.setTelefono(updateOdontologoDto.getTelefono());
                                    odontologo.setDirecion(updateOdontologoDto.getDirecion());
                                    odontologo.setEspecialidad(updateOdontologoDto.getEspecialidad());
                                    odontologo.setUnUsuario(usuario);
                                    odontologo.setUnHorario(horario);

                                    return odontologoRepository.save(odontologo);
                                })
                        ).orElseThrow(HorarioNotfountException::new) // Ahora la excepción está en el lugar correcto
                ).orElseThrow(UsuarioNotfountException::new)
                .map(odontologoReadMapper::readOdontologoDto)
                .orElseThrow(OdontologoNotfountException::new);
    }*/


}



