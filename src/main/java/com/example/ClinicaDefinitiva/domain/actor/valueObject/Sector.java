package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.receptionist.exception.BlankSectorException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.receptionist.exception.NullSectorException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.receptionist.exception.SectorNotAllowedException;

import java.util.Set;

public final class Sector {

    private static final Set<String> ALLOWED_VALUES = Set.of(
            "RECEPTION",
            "ADMINISTRATION",
            "BILLING",
            "CUSTOMER_SERVICE",
            "MEDICAL_RECORDS",
            "CALL_CENTER",
            "INVENTORY",
            "DENTAL_TECHNICIAN_SUPPORT",
            "DENTAL_ASSISTANCE"
    );

    private final String value;

   // private Sector(String value) {
     //   this.value = value;
   // }

    public  Sector (String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new NullSectorException(ContextoEntidad.SECTOR, "Sector cannot be empty.");
        }
        if(value.isBlank()) {
            throw new BlankSectorException(ContextoEntidad.SECTOR, "Sector cannot be empty.");
        }

        String normalized = value.trim().toUpperCase();

        if (!ALLOWED_VALUES.contains(normalized)) {
            throw new SectorNotAllowedException(ContextoEntidad.SECTOR,"Sector '" + normalized + "' is not allowed.");
        }

        //return new Sector(normalized);
        this.value=normalized;
    }

    // methods semantic
    public boolean is(String expected) {
        return value.equalsIgnoreCase(expected.trim());
    }

    // methods access
    public String Value() {
        return value;
    }

    // methods utility
    @Override
    public String toString() {
        return value;
    }
}
