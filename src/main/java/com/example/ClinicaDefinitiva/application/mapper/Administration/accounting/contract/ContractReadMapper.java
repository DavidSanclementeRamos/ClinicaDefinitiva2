package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.contract;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.PageContractDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.ReadContractDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import org.springframework.stereotype.Component;

@Component
public class ContractReadMapper {

    public ReadContractDto toReadDto(Contract contract, ThirdParties thirdParties) {
        return new ReadContractDto(
                contract.getContractId() != null ? contract.getContractId().getValue() : null,
                contract.getCompanyId() != null ? contract.getCompanyId().getValue() : null,
                contract.getThirdPartiesId() != null ? contract.getThirdPartiesId().getValue() : null,
                contract.getName().getValue(),
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

    public PageContractDto toPageDto(Contract contract) {
        return new PageContractDto(
                contract.getContractId() != null ? contract.getContractId().getValue() : null,
                contract.getName().getValue(),
                contract.getThirdPartiesId().getValue(),
                contract.getCoverageType(),
                contract.getCoverageRate(),
                contract.getEndDate(),
                contract.getStatus(),
                contract.isExpired()
        );
    }
}
