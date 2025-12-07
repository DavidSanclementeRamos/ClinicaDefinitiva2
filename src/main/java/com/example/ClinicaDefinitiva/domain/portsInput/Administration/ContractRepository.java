package com.example.ClinicaDefinitiva.domain.portsInput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ThirdPartiesId;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface ContractRepository {
    Contract save(Contract contract);
    Optional<Contract> findById(ContractId id);
    Page<Contract> findByCompanyId(CompanyId companyId);
    Page<Contract> findByThirdPartiesId(ThirdPartiesId thirdPartiesId);
    Page<Contract> findByStatus(ContractStatus status);
    Page<Contract> findActiveContracts();
    Page<Contract> findExpiringContracts(LocalDate beforeDate);
    Page<Contract> findExpiredContracts(LocalDate beforeDate);
    boolean existsActiveContractForThirdParty(ThirdPartiesId thirdPartiesId);
    boolean existsActiveContractForCompany(ContractId contractId);
}
