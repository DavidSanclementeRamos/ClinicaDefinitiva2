package com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.nulo;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.shared.person.exception.NullValueException;

public class NullDentistAvailabilityStatus extends NullValueException {
    public NullDentistAvailabilityStatus(ErrorCatalog catalogo, ContextoEntidad contexto, String detalle) {
        super(catalogo, contexto, detalle);
    }
}
