package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
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
        if (value == null ) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_SECTOR_NULL, VOContext.SECTOR);
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_SECTOR_BLANK, VOContext.SECTOR);
        }
        String normalized = value.trim().toUpperCase();

        if (!ALLOWED_VALUES.contains(normalized)) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_SECTOR_NOT_ALLOWED, VOContext.SECTOR);
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
