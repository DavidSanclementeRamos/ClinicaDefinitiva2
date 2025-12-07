package com.example.ClinicaDefinitiva.application.mapper;

// application/mapper/AddressMapper.java
import com.example.ClinicaDefinitiva.application.dto.AddressDto;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Address;
import org.springframework.stereotype.Component;


public final class AddressMapper {
    private AddressMapper() {}

    public static AddressDto toAddress(Address a) {
        return new AddressDto(
                a.Street(),
                a.City(),
                a.State(),
                a.Country(),
                a.PostalCode()
        );
    }

    public static Address fromDto(AddressDto dto){
        return new Address(
                dto.street(),
                dto.city(),
                dto.state(),
                dto.country(),
                dto.zipCode()
        );
    }
}
