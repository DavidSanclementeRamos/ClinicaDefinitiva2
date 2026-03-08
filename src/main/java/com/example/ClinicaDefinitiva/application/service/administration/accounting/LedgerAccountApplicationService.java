package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.administration.contavilidad.LedgerAccountNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.LedgerAccount.LedgerAccountReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.LedgerAccount.LedgerAccountWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.LedgerAccountUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.LedgerAccountRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LedgerAccountApplicationService implements LedgerAccountUseCase {

    private final LedgerAccountReadMapper readMapper;
    private final LedgerAccountWriteMapper writeMapper;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final CompanyRepository companyRepository;
    private final AuthorizationHelper authorizationHelper;

    public LedgerAccountApplicationService(LedgerAccountReadMapper readMapper,
                                           LedgerAccountWriteMapper writeMapper,
                                           LedgerAccountRepository ledgerAccountRepository,
                                           CompanyRepository companyRepository,
                                           AuthorizationHelper authorizationHelper) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.companyRepository = companyRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.READ)
    public ReadLedgerAccountDto findById(LedgerAccountId id,
                                         UserIdentityId requesterId,
                                         RolId requesterRolId) {

        
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
        LedgerAccount account = ledgerAccountRepository.findById(id)
                .orElseThrow(() -> new LedgerAccountNotFoundException("Not found"));


        return readMapper.toReadDto(account);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageLedgerAccountDto> findAll(Pageable pageable,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return ledgerAccountRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.READ)
    public ReadLedgerAccountDto findByCode(String code,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return ledgerAccountRepository.findByCode(code)
                .map(readMapper::toReadDto)
                .orElseThrow(() -> new LedgerAccountNotFoundException("Not found"));
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageLedgerAccountDto> findByNature(String nature,
                                                   Pageable pageable,
                                                   UserIdentityId requesterId,
                                                   RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return ledgerAccountRepository.findByNature(nature, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageLedgerAccountDto> findByLevel(int level,
                                                  Pageable pageable,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return ledgerAccountRepository.findByLevel(level, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageLedgerAccountDto> findByAccountType(String type,
                                                        Pageable pageable,
                                                        UserIdentityId requesterId,
                                                        RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return ledgerAccountRepository.findByAccountType(type, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadLedgerAccountDto createLedgerAccount(CreateLedgerAccountDto dto,
                                                    UserIdentityId requesterId,
                                                    RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

         LedgerAccount account = LedgerAccount.registerLedgerAccount(
            writeMapper.toCompanyId(dto),
            writeMapper.toCode(dto),
            writeMapper.toName(dto),
            writeMapper.toNature(dto),
            writeMapper.toRequiresThirdParty(dto),
            writeMapper.toRequiresDocument(dto)
        );
        LedgerAccount saved = ledgerAccountRepository.save(account);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadLedgerAccountDto updateAccountInformation(LedgerAccountId id,
                                                         UpdateLedgerAccountDto dto,
                                                         UserIdentityId requesterId,
                                                         RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
         LedgerAccount account = ledgerAccountRepository.findById(id)
                .orElseThrow(() -> new LedgerAccountNotFoundException("Not found"));

        
         account.updateAccountInformation(
                           writeMapper.toName(dto),
                           writeMapper.toRequiresThirdParty(dto),
                           writeMapper.toRequiresDocument(dto)
);

        LedgerAccount updated = ledgerAccountRepository.save(account);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.UPDATE)
    public void activate(LedgerAccountId id,
                         UserIdentityId requesterId,
                         RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

         LedgerAccount account = ledgerAccountRepository.findById(id)
                .orElseThrow(() -> new LedgerAccountNotFoundException("Not found"));

        account.activate();
        ledgerAccountRepository.save(account);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
            action = ActionCatalog.BasicAction.DEACTIVATE)
    public void inactivate(LedgerAccountId id,
                           String reason,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.LEDGER_ACCOUNT,
                ActionCatalog.BasicAction.DEACTIVATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
         LedgerAccount account = ledgerAccountRepository.findById(id)
                .orElseThrow(() -> new LedgerAccountNotFoundException("Not found"));


        account.inactivate(reason);
        ledgerAccountRepository.save(account);
    }
}