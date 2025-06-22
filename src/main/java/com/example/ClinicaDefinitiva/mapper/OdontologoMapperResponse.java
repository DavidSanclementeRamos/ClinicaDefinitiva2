package com.example.ClinicaDefinitiva.mapper;



import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OdontologoMapperResponse {

  //  @Mapping(target = "unUsuario", source = "readUsuarioDto")
   // @Mapping(target = "unHorario", source = "horarioDto")

    ReadOdontologoDto readOdontologoDto (Odontologo odontologo);
}
