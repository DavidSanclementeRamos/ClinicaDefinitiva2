package com.example.ClinicaDefinitiva.services;

import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CambioResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.UpdateResponsableDto;

public interface ResponsableService {

    CreateEndReadResponsableDto updateCambio(long id, CambioResponsableDto cambioResponsableDto);

    CreateEndReadResponsableDto update(long id, UpdateResponsableDto updateResponsableDto);

    CreateEndReadResponsableDto save(CreateEndReadResponsableDto createEndReadResponsableDto);
}
