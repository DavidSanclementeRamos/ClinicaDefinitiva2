package com.example.ClinicaDefinitiva.domain.schedule.valueObject;


public class AvailabilityId   {

    private String value;

    protected AvailabilityId() {}

    private AvailabilityId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AvailabilityId cannot be null or blank");
        }
        this.value = value;
    }


    public static AvailabilityId from(String value) {
        return new AvailabilityId(value);
    }

    public String getValue() { return value; }

}
