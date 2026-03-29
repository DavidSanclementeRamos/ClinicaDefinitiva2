package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.contract;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.PageContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.ReadContractDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract.PageContractResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract.ReadContractResponse;
import org.springframework.stereotype.Component;

@Component
public class ContractRestReadMapper {

    public ReadContractResponse toRest(ReadContractDto dto) {
        if (dto == null) return null;

        return new ReadContractResponse(
                dto.id(),
                dto.companyId(),
                dto.thirdPartiesId(),
                dto.name(),
                dto.description(),
                dto.origin(),
                dto.startDate(),
                dto.endDate(),
                dto.coverageType(),
                dto.coverageRate(),
                dto.status().toString(),
                dto.isNearExpiration(),
                dto.daysRemaining()
        );
    }

    public PageContractResponse toPageRest(PageContractDto dto) {
        if (dto == null) return null;

        return new PageContractResponse(
                dto.id(),
                dto.name(),
                dto.thirdPartiesId(),
                dto.name(),
                dto.endDate(),
                dto.status().toString()
        );
    }
}