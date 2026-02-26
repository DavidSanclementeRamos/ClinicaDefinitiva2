package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.contract;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.CreateContractDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.UpdateContractDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import org.springframework.stereotype.Component;

@Component
public class ContractWriteMapper {
    public Contract fromCreateDto(CreateContractDto dto){
        return Contract.registerContract(
                CompanyId.of( dto.companyId()),
                ThirdPartiesId.of( dto.thirdPartiesId()),
                Name.of( dto.name()),
                dto.description(),
                dto.origin(),
                dto.endDate(),
                dto.coverageType(),
                dto.coverageRate()
        );
    }

    public void toUpdateDto(UpdateContractDto dto, Contract contract){
        contract.updateInformation(
                Name.of(dto.name()),
                dto.description(),
                dto.origin(),
                dto.coverageType()


        );
    }
}
