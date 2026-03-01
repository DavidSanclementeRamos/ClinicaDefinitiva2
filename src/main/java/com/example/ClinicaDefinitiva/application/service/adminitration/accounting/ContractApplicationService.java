package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.CompanyNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ContractNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ThirdPartyNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.contract.ContractReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.contract.ContractWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.ContractUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ThirdPartiesRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@Transactional
public class ContractApplicationService implements ContractUseCase {

    private final ContractRepository contractRepository;
    private final ContractReadMapper readMapper;
    private final ContractWriteMapper writeMapper;
    private final ThirdPartiesRepository thirdPartiesRepository;
    private final CompanyRepository companyRepository;
    private final AuthorizationHelper authorizationHelper;

    public ContractApplicationService(ContractRepository contractRepository,
                                      ContractReadMapper readMapper,
                                      ContractWriteMapper writeMapper,
                                      ThirdPartiesRepository thirdPartiesRepository,
                                      CompanyRepository companyRepository,
                                      AuthorizationHelper authorizationHelper) {
        this.contractRepository = contractRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.thirdPartiesRepository = thirdPartiesRepository;
        this.companyRepository = companyRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.READ)
    public ReadContractDto findById(ContractId id,
                                    UserIdentityId requesterId,
                                    RolId requesterRolId) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        return readMapper.toReadDto(contract);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageContractDto> findAll(Pageable pageable,
                                         UserIdentityId requesterId,
                                         RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return contractRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageContractDto> findByCompany(CompanyId companyId,
                                               Pageable pageable,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {

        companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(companyId.getValue())
                        .build()
        );

        return contractRepository.findByCompanyId(companyId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageContractDto> findByThirdParty(ThirdPartiesId thirdPartyId,
                                                  Pageable pageable,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {

        thirdPartiesRepository.findById(thirdPartyId)
                .orElseThrow(() -> new ThirdPartyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(thirdPartyId.getValue())
                        .build()
        );

        return contractRepository.findByThirdPartiesId(thirdPartyId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageContractDto> findByStatus(String status,
                                              Pageable pageable,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return contractRepository.findByStatus(status, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageContractDto> findExpiringSoon(int days,
                                                  Pageable pageable,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        LocalDate cutoff = LocalDate.now().plusDays(days);
        return contractRepository.findExpiringContracts(cutoff, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadContractDto createContract(CreateContractDto dto,
                                          UserIdentityId requesterId,
                                          RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        Contract contract = writeMapper.fromCreateDto(dto);
        Contract saved = contractRepository.save(contract);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadContractDto updateInformation(ContractId id,
                                             UpdateContractDto dto,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        writeMapper.toUpdateDto(dto, contract);
        Contract updated = contractRepository.save(contract);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadContractDto extendContract(ContractId id,
                                          LocalDate newEndDate,
                                          UserIdentityId requesterId,
                                          RolId requesterRolId) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        contract.extendContract(newEndDate);
        Contract updated = contractRepository.save(contract);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.UPDATE)
    public void suspend(ContractId id,
                        String reason,
                        UserIdentityId requesterId,
                        RolId requesterRolId) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        contract.suspend(reason);
        contractRepository.save(contract);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.UPDATE)
    public void reactivate(ContractId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        contract.reactivate();
        contractRepository.save(contract);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.DELETE)
    public void terminate(ContractId id,
                          String reason,
                          UserIdentityId requesterId,
                          RolId requesterRolId) {

        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.DELETE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        contract.terminate(reason);
        contractRepository.save(contract);
    }
}