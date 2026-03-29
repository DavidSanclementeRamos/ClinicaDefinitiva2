package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.thirdParties;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ThirdPartyEntity;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyReadEntityMapper {

    public ThirdParties toDomain(ThirdPartyEntity entity) {
        if (entity == null) return null;

        return ThirdParties.builder()
                .withPartiesId(ThirdPartiesId.of(entity.getId()))
                .withCompanyId(CompanyId.of(entity.getCompany().getId()))
                .withName(Name.of(entity.getName()))
                .withTypeDocument(entity.getDocumentType())
                .withDocumentNumber(entity.getDocumentNumber())
                .withTypeThirdParties(Enum.valueOf(
                        com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties.class,
                        entity.getThirdPartyType()))
                .withAddress(parseAddress(entity.getAddress()))
                .withPhoneNumber(PhoneNumber.of(entity.getPhoneNumber()))
                .withEmail(Email.ofOrThrow(entity.getEmail()))
                .withActive(entity.isActive())
                .build();
    }

    private Address parseAddress(String addressStr) {
        if (addressStr == null) return null;
        String[] parts = addressStr.split("\\|", 5);
        if (parts.length == 5) {
            return Address.of(parts[0], parts[1], parts[2], parts[3], parts[4]);
        }
        return null;
    }
}
