package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ContractRepository {
    Contract save(Contract contract);
    Optional<Contract> findById(ContractId id);
    Page<Contract> findByCompanyId(CompanyId companyId,Pageable pageable);
    Page<Contract> findByThirdPartiesId(ThirdPartiesId thirdPartiesId, Pageable pageable);
    Page<Contract> findByStatus(String status,Pageable pageable);
    Page<Contract> findActiveContracts();
    Page<Contract> findExpiringContracts(LocalDate beforeDate, Pageable pageable);
    Page<Contract> findExpiredContracts(LocalDate beforeDate);
    boolean existsActiveContractForThirdParty(ThirdPartiesId thirdPartiesId);
    boolean existsActiveContractForCompany(ContractId contractId);

    Page<Contract> findAll(Pageable pageable);




    }
