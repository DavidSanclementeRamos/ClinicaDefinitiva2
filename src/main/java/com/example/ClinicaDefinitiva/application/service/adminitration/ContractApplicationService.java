package com.example.ClinicaDefinitiva.application.service.adminitration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ContractNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ThirdPartiesNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.ContractMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.NameMapper;
import com.example.ClinicaDefinitiva.application.usecase.Administration.ContractUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.ContractRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.ThirdPartiesRepository;
import com.example.ClinicaDefinitiva.domain.service.ContractDomainService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


/**
 * CONTRACT USE CASE IMPLEMENTATIONS
 */
@Service
@Transactional
public class ContractApplicationService implements ContractUseCase {

    private final ContractRepository contractRepository;
    private final ContractMapper mapper;
    private final ThirdPartiesRepository thirdPartiesRepository;
    private final CompanyRepository companyRepository;


    public ContractApplicationService(ContractRepository contractRepository, ContractMapper mapper, ThirdPartiesRepository thirdPartiesRepository, CompanyRepository companyRepository) {
        this.contractRepository = contractRepository;
        this.mapper = mapper;
        this.thirdPartiesRepository = thirdPartiesRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public ContractResponse findContractByI(String id) {
        ContractId contractId = ContractId.fromString(String.valueOf(id));
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException("No se encontro el ID:" + contractId));
        ThirdParties thirdParties = thirdPartiesRepository.findById(contract.getThirdPartiesId()).orElse(null);

        return mapper.toResponse(contract,thirdParties);
    }


    @Override
    public Page<ContractPageResponse> istActiveContracts(LocalDate beforeDate) {
        return null;
    }

    @Override
    public Page<ContractPageResponse> findExpiringContracts(LocalDate beforeDate) {
          Page<Contract> contractPage = contractRepository.findExpiredContracts(beforeDate);

        if(contractPage.isEmpty()){
            throw new ContractNotFoundException(" ");
        }
        return contractPage.map(mapper::toListResponse);
    }


    @Override
    public ContractResponse registerContract(CreateContractRequest request) {



        ThirdPartiesId partiesId = ThirdPartiesId.fromString(request.thirdPartiesId());
        ThirdParties parties = thirdPartiesRepository.findById(partiesId)
                .orElseThrow(() -> new ThirdPartiesNotFoundException(""));

        Contract contract = ContractDomainService.registerContract(
                null,
                parties,
                NameMapper.fromDto(request.name()),
                request.description(),
                request.origin(),
                request.endDate(),
                request.coverageType(),
                request.coverageRate()
        );

        return mapper.toResponse(contract, parties);

    }

    @Override
    public ContractResponse updateContract(String contractId, UpdateContractRequest request) {
        ContractId id = ContractId.fromString(contractId);
        Contract contract = contractRepository.findById(id)
                .orElseThrow(()-> new ContractNotFoundException(""));
        contract.updateInformation(
                NameMapper.fromDto(request.name()),
                request.description(),
                request.origin(),
                request.coverageType()
        );
        contractRepository.save(contract);
        ThirdParties thirdParties = thirdPartiesRepository.findById(contract.getThirdPartiesId()).orElse(null);

        return mapper.toResponse(contract, thirdParties);
    }

    @Override
    public ContractResponse extendContract(String contractId, ExtendContractRequest request) {
        ContractId id = ContractId.fromString(contractId);
        Contract contract = contractRepository.findById(id)
                .orElseThrow(()-> new ContractNotFoundException(""));

        contract.extendContract(request.newEndDate());

        contractRepository.save(contract);
        ThirdParties thirdParties = thirdPartiesRepository.findById(contract.getThirdPartiesId()).orElse(null);
        return mapper.toResponse(contract, thirdParties);
    }

    @Override
    public ContractResponse suspendContract(String contractId, SuspendContractRequest request) {

        ContractId id = ContractId.fromString(contractId);
        Contract contract = contractRepository.findById(id)
                        .orElseThrow(()-> new ContractNotFoundException(""));
          contract.suspend(
                request.reason()
        );

          contractRepository.save(contract);
        ThirdParties thirdParties = thirdPartiesRepository.findById(contract.getThirdPartiesId()).orElse(null);
        return mapper.toResponse(contract, thirdParties);
    }

    @Override
    public ContractResponse reactivateContract(String contractId) {
        ContractId id = ContractId.fromString(contractId);
        Contract contract = contractRepository.findById(id)
                .orElseThrow(()-> new ContractNotFoundException(""));
        contract.reactivate();
        contractRepository.save(contract);
        ThirdParties thirdParties = thirdPartiesRepository.findById(contract.getThirdPartiesId()).orElse(null);
        return mapper.toResponse(contract, thirdParties);
    }

    @Override
    public ContractResponse terminateContract(String contractId, TerminateContractRequest request) {
        ContractId id = ContractId.fromString(contractId);
        Contract contract = contractRepository.findById(id)
                .orElseThrow(()-> new ContractNotFoundException(""));
        contract.terminate(request.reason());

        contractRepository.save(contract);

        ThirdParties thirdParties = thirdPartiesRepository.findById(contract.getThirdPartiesId()).orElse(null);
        return mapper.toResponse(contract, thirdParties);
    }

}
