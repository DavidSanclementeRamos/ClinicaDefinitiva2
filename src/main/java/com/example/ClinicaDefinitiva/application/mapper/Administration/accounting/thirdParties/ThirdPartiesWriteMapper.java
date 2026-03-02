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

    public ThirdParties fromCreate(CreateThirdPartyDto dto) {
        return ThirdParties.registerThirdParties(
                CompanyId.of(dto.companyId()),
                Name.of(dto.name()),
                dto.typeDocument(),
                dto.documentNumber(),
                TypeThirdParties.valueOf(dto.typeThirdParties()),
                Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode()),
                PhoneNumber.of(dto.phoneNumber()),
                Email.ofOrThrow(dto.email()) // uso del nuevo método
        );
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