package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.contract;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.CreateContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.UpdateContractDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract.CreateContractRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract.UpdateContractRequest;
import org.springframework.stereotype.Component;

@Component
public class ContractRestWriteMapper {

    public CreateContractDto toServiceCreate(CreateContractRequest request) {
        if (request == null) return null;

        return new CreateContractDto(
                request.companyId(),
                request.thirdPartyId(),
                request.name(),
                request.description(),
                request.origin(),
                request.endDate(),
                request.coverageType(),
                request.coverageRate()
        );
    }

    public UpdateContractDto toServiceUpdate(UpdateContractRequest request) {
        if (request == null) return null;

        return new UpdateContractDto(
                request.name(),
                request.description(),
                request.origin(),
                request.coverageType()
        );
    }
}
