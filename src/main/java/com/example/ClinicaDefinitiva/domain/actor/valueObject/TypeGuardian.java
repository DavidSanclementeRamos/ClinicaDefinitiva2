package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;

public final class  TypeGuardian {

    private final String code;
    private final String description;

    private TypeGuardian(String code, String description) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("El código no puede ser vacío");
        }
        this.code = code.toUpperCase();
        this.description = description;
    }// description

    // Fábrica estática para variantes dinámicas
    public static TypeGuardian of(String code, String description) {
        return new TypeGuardian(code, description);
    }

    // Instancias estáticas (familia nuclear)
    public static final TypeGuardian MAMA     = new TypeGuardian("MAMA", "Madre");
    public static final TypeGuardian PAPA     = new TypeGuardian("PAPA", "Padre");
    public static final TypeGuardian HERMANO  = new TypeGuardian("HERMANO", "Hermano");
    public static final TypeGuardian HERMANA  = new TypeGuardian("HERMANA", "Hermana");
    public static final TypeGuardian ABUELO   = new TypeGuardian("ABUELO", "Abuelo");
    public static final TypeGuardian ABUELA   = new TypeGuardian("ABUELA", "Abuela");
    // … y así con los demás

    // Getters
    public String getCode() { return code; }
    public String getDescription() { return description; }

    // Igualdad semántica
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeGuardian)) return false;
        TypeGuardian that = (TypeGuardian) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return description + " (" + code + ")";
    }


    }

