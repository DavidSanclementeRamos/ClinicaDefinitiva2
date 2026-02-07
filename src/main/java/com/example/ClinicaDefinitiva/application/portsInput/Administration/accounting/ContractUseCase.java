package com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ContractUseCase {
    ContractResponse findContractByI(String id);
    Page<ContractPageResponse> istActiveContracts(LocalDate beforeDate);
    Page<ContractPageResponse> findExpiringContracts(LocalDate beforeDate);
    ContractResponse registerContract(CreateContractRequest request);
    ContractResponse updateContract(String contractId, UpdateContractRequest request);
    ContractResponse extendContract(String contractId, ExtendContractRequest request);
    ContractResponse suspendContract(String contractId, SuspendContractRequest request);
    ContractResponse reactivateContract(String contractId);
    ContractResponse terminateContract(String contractId, TerminateContractRequest request);



}
