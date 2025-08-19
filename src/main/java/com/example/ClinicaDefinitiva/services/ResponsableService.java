package com.example.ClinicaDefinitiva.services;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CambioResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.UpdateResponsableDto;


import java.util.List;
import java.util.Optional;

public interface ResponsableService {

    CreateEndReadResponsableDto findByUsuario_Id(long idUsuario);

    CreateEndReadResponsableDto findByPacientes_Id(long idPaciente);

    CreateEndReadResponsableDto findByDocumento(String documento);

    CreateEndReadResponsableDto findByTelefono(String telefono);

    List<CreateEndReadResponsableDto> findByTipoRelacion(TipoResponsable tipoRelacion);

    CreateEndReadResponsableDto updateCambio(long id, CambioResponsableDto cambioResponsableDto);

    CreateEndReadResponsableDto update(long id, UpdateResponsableDto updateResponsableDto);

    CreateEndReadResponsableDto save(CreateEndReadResponsableDto createEndReadResponsableDto);

    void deleaById(long id);
}
