package com.example.ClinicaDefinitiva.domain.portsInput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.contable.enu.ContractStatus;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.ThirdPartiesId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
