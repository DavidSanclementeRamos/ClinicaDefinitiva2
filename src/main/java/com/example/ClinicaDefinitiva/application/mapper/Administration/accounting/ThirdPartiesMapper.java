package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.ThirdPartiesListResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.ThirdPartiesResponse;
import com.example.ClinicaDefinitiva.application.mapper.AddressMapper;
import com.example.ClinicaDefinitiva.application.mapper.EmailMapper;
import com.example.ClinicaDefinitiva.application.mapper.PhoneNumberMapper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import org.springframework.stereotype.Component;

/**
 * Mapper: ThirdParties (Domain → DTO)
 */
@Component
public class ThirdPartiesMapper {

    public ThirdPartiesResponse toResponse(ThirdParties thirdParties) {
        return new ThirdPartiesResponse(
                thirdParties.getPartiesId() != null ? thirdParties.getPartiesId().getValue() : null,
                thirdParties.getCompanyId() != null ? thirdParties.getCompanyId().getValue() : null,
                NameMapper.toName(thirdParties.getName()),
                thirdParties.getTypeDocument(),
                thirdParties.getDocumentNumber(),
                thirdParties.getTypeThirdParties().name(),
                AddressMapper.toAddress(thirdParties.getAddress()),
                PhoneNumberMapper.toPhone(thirdParties.getPhoneNumber()),
                EmailMapper.toEmail(thirdParties.getEmail()),
                thirdParties.isActive()
        );
    }

    public ThirdPartiesListResponse toListResponse(ThirdParties thirdParties) {
        return new ThirdPartiesListResponse(
                thirdParties.getPartiesId() != null ? thirdParties.getPartiesId().getValue() : null,
                NameMapper.toName(thirdParties.getName()),
                thirdParties.getDocumentNumber(),
                thirdParties.getTypeThirdParties().name(),
                thirdParties.isActive()
        );
    }
}
