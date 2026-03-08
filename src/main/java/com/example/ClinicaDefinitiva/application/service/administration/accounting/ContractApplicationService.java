package com.example.ClinicaDefinitiva.application.service.administration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.contract.CreateContractDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.contract.PageContractDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.contract.ReadContractDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.contract.UpdateContractDto;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.ContractNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.ThirdPartyNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;
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

      

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
          Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

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

       

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(thirdPartyId.getValue())
                        .build()
        );
         thirdPartiesRepository.findById(thirdPartyId)
                .orElseThrow(() -> new ThirdPartyNotFoundException("Not found"));

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

         Contract contract = Contract.registerContract(
            writeMapper.toCompanyId(dto),
            writeMapper.toThirdPartiesId(dto),
            writeMapper.toName(dto),
            writeMapper.toDescription(dto),
            writeMapper.toOrigin(dto),
            writeMapper.toEndDate(dto),
           writeMapper.toCoverageType(dto),
            writeMapper.toCoverageRate(dto)
        );

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

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
        
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

        
                   contract.updateInformation(
    writeMapper.toName(dto),
    writeMapper.toDescription(dto),
    writeMapper.toOrigin(dto),
    writeMapper.toCoverageType(dto)
);

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

       

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));


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

       

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
        
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));


        contract.suspend(reason);
        contractRepository.save(contract);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.CONTRACT,
            action = ActionCatalog.BasicAction.UPDATE)
    public void reactivate(ContractId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

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

       

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.CONTRACT,
                ActionCatalog.BasicAction.DELETE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
         Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Not found"));

        contract.terminate(reason);
        contractRepository.save(contract);
    }
}