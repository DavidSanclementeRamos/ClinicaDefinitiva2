package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.company;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.company.PageCompanyDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.company.ReadCompanyDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyReadMapper {

    public ReadCompanyDto toReadDto(Company company) {
        return new ReadCompanyDto(
                company.getId().value(),
                company.getName().getValue(),
                company.getTaxIdentificationNumber().getValue(),
                company.getTypePerson().name(),
                company.getTaxRegime().name(),
                company.getLegalRepresentative(),
                company.getAddress().Street(),
                company.getAddress().City(),
                company.getAddress().State(),
                company.getAddress().Country(),
                company.getAddress().PostalCode(),
                company.getPhoneNumber().toString(),
                company.getEmail().value(),
                company.getIncorporationDate(),
                company.getStatus().getStatus().name()
        );
    }

    public PageCompanyDto toPageDto(Company company) {
        return new PageCompanyDto(
                company.getId().value(),
                company.getName().getValue(),
                company.getTaxIdentificationNumber().getValue(),
                company.getStatus().getStatus().name()
        );
    }
}

