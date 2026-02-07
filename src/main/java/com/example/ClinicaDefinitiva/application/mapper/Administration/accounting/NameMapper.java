package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Name;

public class NameMapper {
    public static NameDto toName(Name a){return new NameDto(a.getName());}
    public static Name fromDto(NameDto dto){return new Name(dto.name());}
}
