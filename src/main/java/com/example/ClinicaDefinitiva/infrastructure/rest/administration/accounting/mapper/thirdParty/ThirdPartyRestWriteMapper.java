package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.thirdParty;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.CreateThirdPartyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.UpdateThirdPartyDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty.CreateThirdPartyRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty.UpdateThirdPartyRequest;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyRestWriteMapper {

    public CreateThirdPartyDto toServiceCreate(CreateThirdPartyRequest request) {
        if (request == null) return null;

        return new CreateThirdPartyDto(
                request.companyId(),
                request.name(),
                request.typeDocument(),
                request.documentNumber(),
                request.thirdPartyType(),
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode(),
                request.phoneNumber(),
                request.email()
        );
    }

    public UpdateThirdPartyDto toServiceUpdate(UpdateThirdPartyRequest request) {
        if (request == null) return null;

        return new UpdateThirdPartyDto(
                request.name(),
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                request.postalCode(),
                request.phoneNumber(),
                request.email()
        );
    }
}