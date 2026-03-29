package com.example.ClinicaDefinitiva.application.administration.accounting.mapper.thirdParties;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.PageThirdPartyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.ReadThirdPartyDto;
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
                thirdParties.getCompanyId().value(),
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