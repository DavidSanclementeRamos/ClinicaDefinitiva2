
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.thirdParty;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.PageThirdPartyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.ReadThirdPartyDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty.PageThirdPartyResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty.ReadThirdPartyResponse;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyRestReadMapper {

    public ReadThirdPartyResponse toRest(ReadThirdPartyDto dto) {
        if (dto == null) return null;

        return new ReadThirdPartyResponse(
                dto.id(),
                dto.companyId(),
                dto.name(),
                dto.typeDocument(),
                dto.documentNumber(),
                dto.typeThirdParties(),
                dto.street(),
                dto.city(),
                dto.state(),
                dto.country(),
                dto.postalCode(),
                dto.phoneNumber(),
                dto.email(),
                dto.active()
        );
    }

    public PageThirdPartyResponse toPageRest(PageThirdPartyDto dto) {
        if (dto == null) return null;

        return new PageThirdPartyResponse(
                dto.id(),
                dto.name(),
                dto.documentNumber(),
                dto.typeThirdParties(),
                dto.active()
        );
    }
}