package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

import java.io.Serializable;
import java.util.Objects;

public class ShiftId implements Serializable {
    private String value;

    protected ShiftId() {}

    private ShiftId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ShiftId cannot be null or blank");
        }
        this.value = value;
    }



    public static ShiftId from(String value) {
        return new ShiftId(value);
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftId)) return false;
        ShiftId shiftId = (ShiftId) o;
        return Objects.equals(value, shiftId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
