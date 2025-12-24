package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Objects;

public final class Address {
    private final String street;
    private final String city;
    private final String state;
    private final String country;
    private final String postalCode;


    public Address(String street, String city, String state, String country, String postalCode) {
        if (street == null || city == null || state == null || country == null || postalCode == null) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ADDRESS_NULL, VOContext.ADDRESS );
        }
        if (street.isBlank() || city.isBlank() || state.isBlank() || country.isBlank() || postalCode.isBlank()) {
            throw new ValueObjectValidationException(ErrorCatalog.ERR_ADDRESS_BLANK, VOContext.ADDRESS);
        }

        this.street = street.trim();
        this.city = city.trim();
        this.state = state.trim();
        this.country = country.trim();
        this.postalCode = postalCode.trim();
    }

    //  methods semantic
    public boolean isInCountry(String expectedCountry) {
        return country.equalsIgnoreCase(expectedCountry.trim());
    }

    public boolean isLocalTo(String expectedCity) {
        return city.equalsIgnoreCase(expectedCity.trim());
    }

    public String fullAddress() {
        return street + ", " + city + ", " + state + ", " + country + " - " + postalCode;
    }

    public String postalZone() {
        return postalCode.substring(0, Math.min(3, postalCode.length()));
    }

    public String asText() {
        return fullAddress();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // methods access

    public String Country() {
        return country;
    }

    public String City() {
        return city;
    }

    public String PostalCode() {
        return postalCode;
    }

    public String State() {
        return state;
    }

    public String Street() {
        return street;
    }

    // methods utility
    @Override
    public String toString() {
        return fullAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return street.equals(address.street) &&
                city.equals(address.city) &&
                state.equals(address.state) &&
                country.equals(address.country) &&
                postalCode.equals(address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, country, postalCode);
    }




}
