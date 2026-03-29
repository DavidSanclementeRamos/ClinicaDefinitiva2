package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.company;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Nit;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyReadEntityMapper {

    public Company toDomain(CompanyEntity entity) {
        if (entity == null) return null;

        return Company.builder()
                .withId(CompanyId.of(entity.getId()))
                .withName(Name.of(entity.getName()))
                .withTaxIdentificationNumber(Nit.of(entity.getTaxId()))
                .withTypePerson(Enum.valueOf(
                        com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson.class, 
                        entity.getLegalEntityType()))
                .withTaxRegime(Enum.valueOf(
                        com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime.class, 
                        entity.getTaxRegime()))
                .withLegalRepresentative(entity.getLegalRepresentative())
                .withAddress(parseAddress(entity.getAddress()))
                .withPhoneNumber(PhoneNumber.of(entity.getPhoneNumber()))
                .withEmail(Email.ofOrThrow(entity.getEmail()))
                .withIncorporationDate(entity.getIncorporationDate())
                .withStatus(CompanyStatus.of(
                        CompanyStatus.Status.valueOf(entity.getStatus())))
                .build();
    }

    private Address parseAddress(String addressStr) {
        if (addressStr == null) return null;
        // Asumiendo formato: "calle|ciudad|estado|pais|codigo"
        String[] parts = addressStr.split("\\|", 5);
        if (parts.length == 5) {
            return Address.of(parts[0], parts[1], parts[2], parts[3], parts[4]);
        }
        return null;
    }
}
