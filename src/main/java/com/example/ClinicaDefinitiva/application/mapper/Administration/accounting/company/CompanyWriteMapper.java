package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.company;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.CreateCompanyDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.UpdateCompanyContactDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.UpdateCompanyTaxDto;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Nit;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.springframework.stereotype.Component;

@Component
public class CompanyWriteMapper {


    public Company fromCreateDto(CreateCompanyDto dto){

        Outcome<Email> emailOutcome = Email.of(dto.email());
        if (emailOutcome.isFailure()) {
            throw new DomainAggregateException(
                    VoAccesError.valueOf(""),
                    EntityContext.COMPANY
            );
        }


        return Company.registerCompany(
                Name.of(dto.name()),
                Nit.of( dto.taxIdentificationNumber()),
                TypePerson.valueOf( dto.typePerson()),
                TaxRegime.valueOf(dto.taxRegime()),
                dto.legalRepresentative(),
                Address.of(dto.street(),dto.city(),dto.state(),dto.country(),dto.postalCode()),
                PhoneNumber.of(dto.phoneNumber()),
                emailOutcome.getValue().get()

        );

    }

    public void toUpdateContactDto(UpdateCompanyContactDto dto, Company company){
        Outcome<Email> emailOutcome = Email.of(dto.email());
        if (emailOutcome.isFailure()) {
            throw new DomainAggregateException(
                    VoAccesError.valueOf(""),
                    EntityContext.COMPANY
            );
        }
        company.updateContactInformation(
                Name.of(dto.name()),
                dto.legalRepresentative(),
                Address.of(dto.street(),dto.city(),dto.country(),dto.country(),dto.postalCode()),
                PhoneNumber.of( dto.phoneNumber()),
                emailOutcome.getValue().get()

        );
    }

    public void toUpdateTaxDto(UpdateCompanyTaxDto dto, Company company){
        company.updateTaxInformation(
               Nit.of( dto.taxIdentificationNumber()),
               TaxRegime.valueOf( dto.taxRegime()),
              TypePerson.valueOf(  dto.typePerson()),
                dto.incorporationDate()
        );
    }
}
