package com.example.ClinicaDefinitiva.vo;

import java.util.Objects;

public final class Telefono {
    private final String numero;

    private Telefono(String numero) {
        if (numero == null || !numero.matches("\\d{7,10}")) {
            throw new IllegalArgumentException("Teléfono inválido: " + numero);
        }
        this.numero = numero;
    }

    public static Telefono of(String numero) {
        return new Telefono(numero);
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Telefono)) return false;
        Telefono that = (Telefono) o;
        return numero.equals(that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
