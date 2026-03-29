package com.example.ClinicaDefinitiva.application.administration.accounting.input;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.CreateContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.PageContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.ReadContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.UpdateContractDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ContractUseCase {
    ReadContractDto findById(ContractId id, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageContractDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageContractDto> findByCompany(CompanyId companyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageContractDto> findByThirdParty(ThirdPartiesId thirdPartyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageContractDto> findByStatus(String status, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageContractDto> findExpiringSoon(int days, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    ReadContractDto createContract(CreateContractDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadContractDto updateInformation(ContractId id, UpdateContractDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadContractDto extendContract(ContractId id, java.time.LocalDate newEndDate, UserIdentityId requesterId, RolId requesterRolId);

    void suspend(ContractId id, String reason, UserIdentityId requesterId, RolId requesterRolId);

    void reactivate(ContractId id, UserIdentityId requesterId, RolId requesterRolId);

    void terminate(ContractId id, String reason, UserIdentityId requesterId, RolId requesterRolId);



}
