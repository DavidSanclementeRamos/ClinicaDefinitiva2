package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.company;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.CreateCompanyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.UpdateCompanyContactDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.UpdateCompanyTaxDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company.CreateCompanyRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company.UpdateCompanyContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company.UpdateCompanyTaxRequest;
import org.springframework.stereotype.Component;

@Component
public class CompanyRestWriteMapper {

    public CreateCompanyDto toServiceCreate(CreateCompanyRequest request) {
        if (request == null) return null;

        return new CreateCompanyDto(
                request.name(),
                request.taxIdentificationNumber(),
                request.typePerson(),
                request.taxRegime(),
                request.legalRepresentative(),
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode(),
                request.phoneNumber(),
                request.email()
        );
    }

    public UpdateCompanyContactDto toServiceUpdateContact(UpdateCompanyContactRequest request) {
        if (request == null) return null;

        return new UpdateCompanyContactDto(
                request.name(),
                request.legalRepresentative(),
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode(),
                request.phoneNumber(),
                request.email()
        );
    }

    public UpdateCompanyTaxDto toServiceUpdateTax(UpdateCompanyTaxRequest request) {
        if (request == null) return null;

        return new UpdateCompanyTaxDto(
                request.taxIdentificationNumber(),
                request.taxRegime(),
                request.typePerson(),
                request.incorporationDate()
        );
    }
}
