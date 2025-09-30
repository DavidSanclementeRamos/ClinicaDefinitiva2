package com.example.ClinicaDefinitiva.domain.actor.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BloodType {

    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String label;


    BloodType(String label) {
        this.label = label;
    }


    @JsonCreator
    public static BloodType fromLabel (String label) {
        for (BloodType type : BloodType.values()) {
            if (type.getLabel().equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de sangre inválido: " + label);
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
