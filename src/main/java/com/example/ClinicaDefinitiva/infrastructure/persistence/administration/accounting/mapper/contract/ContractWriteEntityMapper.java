package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.contract;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ContractEntity;
import org.springframework.stereotype.Component;

@Component
public class ContractWriteEntityMapper {

    public ContractEntity toEntity(Contract contract) {
        if (contract == null) return null;

        ContractEntity entity = new ContractEntity();

        if (contract.getContractId() != null && contract.getContractId().getValue() != null) {
            entity.setId(contract.getContractId().getValue());
        }

        entity.setName(contract.getName().getValue());
        entity.setDescription(contract.getDescription());
        entity.setOrigin(contract.getOrigin());
        entity.setStartDate(contract.getStartDate());
        entity.setEndDate(contract.getEndDate());
        entity.setCoverageType(contract.getCoverageType());
        entity.setCoverageRate(contract.getCoverageRate());
        entity.setStatus(contract.getStatus().name());

        return entity;
    }
}
