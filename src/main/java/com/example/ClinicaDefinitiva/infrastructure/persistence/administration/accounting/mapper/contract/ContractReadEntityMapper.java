package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.contract;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ContractEntity;
import org.springframework.stereotype.Component;

@Component
public class ContractReadEntityMapper {

    public Contract toDomain(ContractEntity entity) {
        if (entity == null) return null;

        return Contract.builder()
                .withContractId(ContractId.of(entity.getId()))
                .withCompanyId(CompanyId.of(entity.getCompany().getId()))
                .withThirdPartiesId(ThirdPartiesId.of(entity.getThirdParty().getId()))
                .withName(Name.of(entity.getName()))
                .withDescription(entity.getDescription())
                .withOrigin(entity.getOrigin())
                .withStartDate(entity.getStartDate())
                .withEndDate(entity.getEndDate())
                .withCoverageType(entity.getCoverageType())
                .withCoverageRate(entity.getCoverageRate())
                .withStatus(Enum.valueOf(
                        com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus.class,
                        entity.getStatus()))
                .build();
    }
}
