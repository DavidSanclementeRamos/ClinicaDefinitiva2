package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;


/**
 * Value Object que representa un nombre para agregados contables.
 * Inmutable y con validaciones de negocio.
 */
public final class Name {
    private static final int MAX_NAME_LENGTH = 255;

    private String name;

    public Name(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del documento es obligatorio");
        }
        if (name.trim().length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("El nombre no puede exceder %d caracteres", MAX_NAME_LENGTH)
            );
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
