package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.List;

public final class BloodType {

    private static final List<String> VALID_TYPES = Arrays.asList(
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    );

    private final String value;

    private BloodType(String value) {
        if (!VALID_TYPES.contains(value.toUpperCase())) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_BLOODTYPE_INVALID, VOContext.BLOOD_TYPE);
        }
        this.value = value.toUpperCase();
    }

    @JsonCreator
    public static BloodType fromLabel(String label) {
        return new BloodType(label);
    }

    @JsonValue
    public String getValue() {
        return value;
    }


}
