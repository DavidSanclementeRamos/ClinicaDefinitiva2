package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.thirdParties;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.CreateThirdPartyDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.UpdateThirdPartyDto;
import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.actor.vo.Address;
import com.example.ClinicaDefinitiva.domain.actor.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Name;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartiesWriteMapper {

    public ThirdParties fromCreate(CreateThirdPartyDto dto){
        Outcome<Email> emailOutcome = Email.of(dto.email());
        if (emailOutcome.isFailure()) {
            throw new DomainAggregateException(
                    VoAccesError.valueOf(""),
                    EntityContext.COMPANY
            );
        }

        return ThirdParties.registerThirdParties(
                CompanyId.of(dto.companyId()),
                Name.of(dto.name()),
                dto.typeDocument(),
                dto.documentNumber(),
                TypeThirdParties.valueOf(dto.typeThirdParties()),
                Address.of(dto.street(),dto.city(),dto.state(),dto.country(),dto.postalCode()),
                PhoneNumber.of(dto.phoneNumber()),
                emailOutcome.getValue().get()

        );
    }

    public  void toUpdate(UpdateThirdPartyDto dto, ThirdParties thirdParties){
        Outcome<Email> emailOutcome = Email.of(dto.email());
        if (emailOutcome.isFailure()) {
            throw new DomainAggregateException(
                    VoAccesError.valueOf(""),
                    EntityContext.COMPANY
            );
        }
        thirdParties.updateContactInformation(
                Name.of(dto.name()),
                Address.of(dto.street(),dto.street(),dto.state(), dto.country(),dto.postalCode()),
                PhoneNumber.of(dto.phoneNumber()),
                emailOutcome.getValue().get()


                );
    }
}
