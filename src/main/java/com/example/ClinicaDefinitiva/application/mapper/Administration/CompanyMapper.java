package com.example.ClinicaDefinitiva.application.mapper.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.CompanyPageResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.CreateCompanyRequest;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.CompanyResponse;
import com.example.ClinicaDefinitiva.application.mapper.AddressMapper;
import com.example.ClinicaDefinitiva.application.mapper.EmailMapper;
import com.example.ClinicaDefinitiva.application.mapper.PhoneNumberMapper;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.Company;

public class CompanyMapper {
    public CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId() != null ? company.getId().getValue() : null,
                NameMapper.toName(company.getName()),
                NitMapper.toNit(company.getTaxIdentificationNumber()),
                company.getTypePerson(),
                company.getTaxRegime(),
                company.getLegalRepresentative(),
                AddressMapper.toAddress(company.getAddress()),
                PhoneNumberMapper.toPhone(company.getPhoneNumber()),
                EmailMapper.toEmail(company.getEmail() ),
                company.getIncorporationDate(),
                company.getStatus().getStatus()
        );
    }

    public CompanyPageResponse toListResponse(Company company) {
        return new CompanyPageResponse(
                company.getId() != null ? company.getId().getValue() : null,
                NameMapper.toName(company.getName()),
                NitMapper.toNit(company.getTaxIdentificationNumber()),
                company.getStatus().getStatus()
        );
    }


}
