package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.thirdParties;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.PageThirdPartyDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.ReadThirdPartyDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import org.springframework.stereotype.Component;

/**
 * Mapper: ThirdParties (Domain → DTO)
 */
@Component
public class ThirdPartiesReadMapper {

    public ReadThirdPartyDto toReadDto(ThirdParties thirdParties) {
        return new ReadThirdPartyDto(
                thirdParties.getPartiesId().getValue(),
                thirdParties.getCompanyId().getValue(),
                thirdParties.getName().getValue(),
                thirdParties.getTypeDocument(),
                thirdParties.getDocumentNumber(),
                thirdParties.getTypeThirdParties().name(),
                thirdParties.getAddress().Street(),
                thirdParties.getAddress().City(),
                thirdParties.getAddress().State(),
                thirdParties.getAddress().Country(),
                thirdParties.getAddress().PostalCode(),
                thirdParties.getPhoneNumber().toString(),
                thirdParties.getEmail().value(),
                thirdParties.isActive()
        );
    }

    public PageThirdPartyDto toPageDto(ThirdParties thirdParties) {
        return new PageThirdPartyDto(
                thirdParties.getPartiesId().getValue(),
                thirdParties.getName().getValue(),
                thirdParties.getDocumentNumber(),
                thirdParties.getTypeThirdParties().name(),
                thirdParties.isActive()
        );
    }
}