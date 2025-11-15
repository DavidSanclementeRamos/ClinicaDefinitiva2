package com.example.ClinicaDefinitiva.domain.identity.valueObjectes;



import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object inmutable que representa el Id de un User como UUID.
 * Uso recomendado:
 *   UserId id = UserId.fromString("...-uuid-...");
 *   UserId id2 = UserId.of(uuid);
 */
public final class UserId implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID value;

    private UserId(UUID value) {
        this.value = Objects.requireNonNull(value, "UserId value cannot be null");
    }

    public static UserId of(UUID uuid) {
        return new UserId(uuid);
    }

    public static UserId fromString(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("UserId string cannot be null or blank");
        }
        try {
            return new UserId(UUID.fromString(str));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for UserId: " + str, e);
        }
    }

    public UUID toUuid() {
        return value;
    }

    public String asString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "UserId{" + value.toString() + '}';
    }
}
