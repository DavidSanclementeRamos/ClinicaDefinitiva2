package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.contract;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.contract.PageContractDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.contract.ReadContractDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import org.springframework.stereotype.Component;

@Component
public class ContractReadMapper {

    public ReadContractDto toReadDto(Contract contract) {
        return new ReadContractDto(
                contract.getContractId().getValue(),
                contract.getCompanyId().value(),
                contract.getThirdPartiesId().getValue(),
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
                contract.getContractId().getValue(),
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