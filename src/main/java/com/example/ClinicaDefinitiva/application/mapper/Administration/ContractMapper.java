package com.example.ClinicaDefinitiva.application.mapper.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.ContractPageResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.ContractResponse;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import org.springframework.stereotype.Component;

@Component
public class ContractMapper {

    public ContractResponse toResponse(Contract contract, ThirdParties thirdParties) {
        return new ContractResponse(
                contract.getContractId() != null ? contract.getContractId().getValue() : null,
                contract.getCompanyId() != null ? contract.getCompanyId().getValue() : null,
                contract.getThirdPartiesId() != null ? contract.getThirdPartiesId().getValue() : null,
                thirdParties != null ? thirdParties.getName().getName() : null,
                NameMapper.toName(contract.getName()),
                contract.getDescription(),
                contract.getOrigin(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getCoverageType(),
                contract.getCoverageRate(),
                contract.getStatus(),
                contract.isExpired(),
                contract.isNearExpiration(),
                contract.getDaysRemaining()
        );
    }

    public ContractPageResponse toListResponse(Contract contract) {
        return new ContractPageResponse(
                contract.getContractId() != null ? contract.getContractId().getValue() : null,
                NameMapper.toName(contract.getName()),
                contract.getThirdPartiesId().getValue(),
                contract.getCoverageType(),
                contract.getCoverageRate(),
                contract.getEndDate(),
                contract.getStatus(),
                contract.isExpired()
        );
    }
}
