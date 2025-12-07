package com.example.ClinicaDefinitiva.application.mapper.Administration;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Name;

public class NameMapper {
    public static NameDto toName(Name a){return new NameDto(a.getName());}
    public static Name fromDto(NameDto dto){return new Name(dto.name());}
}
