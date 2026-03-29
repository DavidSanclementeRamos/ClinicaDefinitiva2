package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.thirdParties;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ThirdPartyEntity;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyWriteEntityMapper {

    public ThirdPartyEntity toEntity(ThirdParties thirdParty) {
        if (thirdParty == null) return null;

        ThirdPartyEntity entity = new ThirdPartyEntity();

       /** if (thirdParty.getPartiesId() != null && thirdParty.getPartiesId().getValue() != null) {
            entity.setId(thirdParty.getPartiesId().getValue());
        }*/

        entity.setName(thirdParty.getName().getValue());
        entity.setDocumentType(thirdParty.getTypeDocument());
        entity.setDocumentNumber(thirdParty.getDocumentNumber());
        entity.setThirdPartyType(thirdParty.getTypeThirdParties().name());
        entity.setAddress(formatAddress(thirdParty.getAddress()));
        entity.setPhoneNumber(thirdParty.getPhoneNumber() != null ?
                thirdParty.getPhoneNumber().toString(): null);
        entity.setEmail(thirdParty.getEmail() != null ?
                thirdParty.getEmail().value() : null);
        entity.setActive(thirdParty.isActive());

        return entity;
    }

    private String formatAddress(Address address) {
        if (address == null) return null;
        return String.join("|",
                address.Street(),
                address.City(),
                address.State(),
                address.Country(),
                address.PostalCode());
    }
}
