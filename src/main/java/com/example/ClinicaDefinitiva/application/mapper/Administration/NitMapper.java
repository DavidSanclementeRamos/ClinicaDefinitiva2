package com.example.ClinicaDefinitiva.application.mapper.Administration;

import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Nit;

public class NitMapper {
    public static NitDto toNit(Nit a){return new NitDto(a.value());}
    public static Nit fromDto(NitDto dto){return new Nit(dto.nit());}
}
