package com.example.ClinicaDefinitiva.Enum;

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

    public String getEtiqueta() {
            return etiqueta;
        }

    }
