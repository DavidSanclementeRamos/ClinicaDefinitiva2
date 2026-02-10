package com.example.ClinicaDefinitiva.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Tipo_sangre {

        A_POSITIVO("A+"),
        A_NEGATIVO("A-"),
        B_POSITIVO("B+"),
        B_NEGATIVO("B-"),
        AB_POSITIVO("AB+"),
        AB_NEGATIVO("AB-"),
        O_POSITIVO("O+"),
        O_NEGATIVO("O-");

        private final String etiqueta;


    Tipo_sangre(String etiqueta) {
        this.etiqueta = etiqueta;
    }


    @JsonCreator
    public static Tipo_sangre desdeEtiqueta(String etiqueta) {
        for (Tipo_sangre tipo : Tipo_sangre.values()) {
            if (tipo.getEtiqueta().equalsIgnoreCase(etiqueta)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de sangre inválido: " + etiqueta);
    }

    @JsonValue
    public String getEtiqueta() {
        return etiqueta;
    }
}
