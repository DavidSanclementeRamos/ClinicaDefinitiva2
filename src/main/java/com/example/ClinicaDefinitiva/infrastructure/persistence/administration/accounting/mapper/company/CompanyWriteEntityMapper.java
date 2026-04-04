package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.company;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyWriteEntityMapper {

    public CompanyEntity toEntity(Company company) {
        if (company == null) return null;

        CompanyEntity entity = new CompanyEntity();

        if (company.getId() != null && company.getId().value() != null) {
            entity.setId(company.getId().value());
        }

        entity.setName(company.getName().getValue());
        entity.setTaxId(company.getTaxIdentificationNumber().getValue());
        entity.setLegalEntityType(company.getTypePerson().name());
        entity.setTaxRegime(company.getTaxRegime().name());
        entity.setLegalRepresentative(company.getLegalRepresentative());
        entity.setAddress(formatAddress(company.getAddress()));
        entity.setPhoneNumber(company.getPhoneNumber() != null ? 
                company.getPhoneNumber().Value(): null);
        entity.setEmail(company.getEmail() != null ? 
                company.getEmail().value() : null);
        entity.setIncorporationDate(company.getIncorporationDate());
        entity.setStatus(company.getStatus().getStatus().name());

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
