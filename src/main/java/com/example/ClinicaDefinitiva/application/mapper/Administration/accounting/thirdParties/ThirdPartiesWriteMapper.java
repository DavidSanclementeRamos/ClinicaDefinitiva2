package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.thirdParties;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.CreateThirdPartyDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.UpdateThirdPartyDto;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartiesWriteMapper {

    public CompanyId toCompanyId(CreateThirdPartyDto dto) {
        return CompanyId.of(dto.companyId());
    }

    public Name toName(CreateThirdPartyDto dto) {
        return Name.of(dto.name());
    }

    public String toTypeDocument(CreateThirdPartyDto dto) {
        return dto.typeDocument();
    }

    public String toDocumentNumber(CreateThirdPartyDto dto) {
        return dto.documentNumber();
    }

    public TypeThirdParties toTypeThirdParties(CreateThirdPartyDto dto) {
        return TypeThirdParties.valueOf(dto.typeThirdParties());
    }

    public Address toAddress(CreateThirdPartyDto dto) {
        return Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode());
    }

    public PhoneNumber toPhoneNumber(CreateThirdPartyDto dto) {
        return PhoneNumber.of(dto.phoneNumber());
    }

    public Email toEmail(CreateThirdPartyDto dto) {
        return Email.ofOrThrow(dto.email());
    }



    public Name toName(UpdateThirdPartyDto dto) {
        return Name.of(dto.name());
    }

    public Address toAddress(UpdateThirdPartyDto dto) {
        return Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode());
    }

    public PhoneNumber toPhoneNumber(UpdateThirdPartyDto dto) {
        return PhoneNumber.of(dto.phoneNumber());
    }

    public Email toEmail(UpdateThirdPartyDto dto) {
        return Email.ofOrThrow(dto.email());
    }

}