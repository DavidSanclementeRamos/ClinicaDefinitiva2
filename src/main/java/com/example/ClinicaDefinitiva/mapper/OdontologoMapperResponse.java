package com.example.ClinicaDefinitiva.mapper;



import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {HorarioMapperResponse.class, UsuarioMapperResponse.class})
public interface OdontologoMapperResponse {

   @Mapping(target = "readUsuarioDto", source = "unUsuario")
   @Mapping(target = "horarioDto", source = "unHorario")

    ReadOdontologoDto readOdontologoDto (Odontologo odontologo);
}
