package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.company;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.PageCompanyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.ReadCompanyDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company.PageCompanyResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company.ReadCompanyResponse;
import org.springframework.stereotype.Component;

@Component
public class CompanyRestReadMapper {

    public ReadCompanyResponse toRest(ReadCompanyDto dto) {
        if (dto == null) return null;

        return new ReadCompanyResponse(
                dto.id(),
                dto.name(),
                dto.taxIdentificationNumber(),
                dto.typePerson(),
                dto.taxRegime(),
                dto.legalRepresentative(),
                dto.street(),
                dto.city(),
                dto.state(),
                dto.country(),
                dto.postalCode(),
                dto.phoneNumber(),
                dto.email(),
                dto.incorporationDate(),
                dto.status()
        );
    }

    public PageCompanyResponse toPageRest(PageCompanyDto dto) {
        if (dto == null) return null;

        return new PageCompanyResponse(
                dto.id(),
                dto.name(),
                dto.taxIdentificationNumber(),
                dto.status()
        );
    }
}
