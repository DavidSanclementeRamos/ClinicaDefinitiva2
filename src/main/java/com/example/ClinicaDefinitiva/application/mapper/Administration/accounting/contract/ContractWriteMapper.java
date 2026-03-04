package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.contract;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.CreateContractDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.UpdateContractDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Component;


@Component
public class ContractWriteMapper {

    public CompanyId toCompanyId(CreateContractDto dto) {
        return CompanyId.of(dto.companyId());
    }

    public ThirdPartiesId toThirdPartiesId(CreateContractDto dto) {
        return ThirdPartiesId.of(dto.thirdPartiesId());
    }

    public Name toName(CreateContractDto dto) {
        return Name.of(dto.name());
    }

    public String toDescription(CreateContractDto dto) {
        return dto.description();
    }

    public String toOrigin(CreateContractDto dto) {
        return dto.origin();
    }

    public LocalDate toEndDate(CreateContractDto dto) {
        return dto.endDate();
    }

    public String toCoverageType(CreateContractDto dto) {
        return dto.coverageType();
    }

    public BigDecimal toCoverageRate(CreateContractDto dto) {
        return dto.coverageRate();
    }


    public Name toName(UpdateContractDto dto) {
        return Name.of(dto.name());
    }

    public String toDescription(UpdateContractDto dto) {
        return dto.description();
    }

    public String toOrigin(UpdateContractDto dto) {
        return dto.origin();
    }

    public String toCoverageType(UpdateContractDto dto) {
        return dto.coverageType();
    }

}
