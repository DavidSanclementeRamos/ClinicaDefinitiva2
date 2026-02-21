package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ContractNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ThirdPartiesNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.contract.ContractReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.NameMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.contract.ContractWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.ContractUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ThirdPartiesRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.service.ContractDomainService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


/**
 * CONTRACT USE CASE IMPLEMENTATIONS
 */
@Service
@Transactional
public class ContractApplicationService implements ContractUseCase {

    private final ContractRepository contractRepository;
    private final ContractReadMapper readMapper;
    private final ContractWriteMapper writeMapper;
    private final ThirdPartiesRepository thirdPartiesRepository;
    private final CompanyRepository companyRepository;

    public ContractApplicationService(ContractRepository contractRepository, ContractReadMapper readMapper, ContractWriteMapper writeMapper, ThirdPartiesRepository thirdPartiesRepository, CompanyRepository companyRepository) {
        this.contractRepository = contractRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.thirdPartiesRepository = thirdPartiesRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public ReadContractDto findById(ContractId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageContractDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageContractDto> findByCompany(CompanyId companyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageContractDto> findByThirdParty(ThirdPartiesId thirdPartyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageContractDto> findByStatus(String status, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageContractDto> findExpiringSoon(int days, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadContractDto createContract(CreateContractDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadContractDto updateInformation(ContractId id, UpdateContractDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadContractDto extendContract(ContractId id, LocalDate newEndDate, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public void suspend(ContractId id, String reason, UserIdentityId requesterId, RolId requesterRolId) {

    }

    @Override
    public void reactivate(ContractId id, UserIdentityId requesterId, RolId requesterRolId) {

    }

    @Override
    public void terminate(ContractId id, String reason, UserIdentityId requesterId, RolId requesterRolId) {

    }
}
