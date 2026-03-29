package com.example.ClinicaDefinitiva.application.administration.accounting.service;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.CreateOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.PageOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.ReadOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.input.OpeningBalanceUseCase;
import com.example.ClinicaDefinitiva.application.administration.accounting.mapper.openingBalance.OpeningBalanceReadMapper;
import com.example.ClinicaDefinitiva.application.administration.accounting.mapper.openingBalance.OpeningBalanceWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.administration.accounting.OpeningBalanceNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.LedgerAccountRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.OpeningBalanceRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.OpeningBalanceId;
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
public class OpeningBalanceApplicationService implements OpeningBalanceUseCase {

    private final OpeningBalanceRepository openingBalanceRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final CompanyRepository companyRepository;
    private final OpeningBalanceReadMapper readMapper;
    private final OpeningBalanceWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public OpeningBalanceApplicationService(OpeningBalanceRepository openingBalanceRepository,
                                            LedgerAccountRepository ledgerAccountRepository,
                                            CompanyRepository companyRepository,
                                            OpeningBalanceReadMapper readMapper,
                                            OpeningBalanceWriteMapper writeMapper,
                                            AuthorizationHelper authorizationHelper) {
        this.openingBalanceRepository = openingBalanceRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.companyRepository = companyRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.OPENING_BALANCE,
            action = ActionCatalog.BasicAction.READ)
    public ReadOpeningBalanceDto findById(OpeningBalanceId id,
                                          UserIdentityId requesterId,
                                          RolId requesterRolId) {

              authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.OPENING_BALANCE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

                OpeningBalance balance = openingBalanceRepository.findById(id)
                .orElseThrow(() -> new OpeningBalanceNotFoundException("Not found"));


        return readMapper.toReadDto(balance);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.OPENING_BALANCE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageOpeningBalanceDto> findAll(Pageable pageable,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.OPENING_BALANCE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return openingBalanceRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.OPENING_BALANCE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageOpeningBalanceDto> findByAccount(LedgerAccountId accountId,
                                                     Pageable pageable,
                                                     UserIdentityId requesterId,
                                                     RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.OPENING_BALANCE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(accountId.getValue())
                        .build()
        );

        return openingBalanceRepository.findByAccount(accountId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.OPENING_BALANCE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageOpeningBalanceDto> findByCompany(CompanyId companyId,
                                                     Pageable pageable,
                                                     UserIdentityId requesterId,
                                                     RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.OPENING_BALANCE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(companyId.value())
                        .build()
        );

        return openingBalanceRepository.findByCompanyId(companyId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.OPENING_BALANCE,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadOpeningBalanceDto createOpeningBalance(CreateOpeningBalanceDto dto,
                                                      UserIdentityId requesterId,
                                                      RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.OPENING_BALANCE,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        OpeningBalance balance = OpeningBalance.registerOpeningBalance(
            writeMapper.toCompanyId(dto),
            writeMapper.toLedgerAccountId(dto),
            writeMapper.toThirdPartiesId(dto),
            writeMapper.toPrice(dto)
        );

        OpeningBalance saved = openingBalanceRepository.save(balance);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.OPENING_BALANCE,
            action = ActionCatalog.BasicAction.DELETE)
    public void delete(OpeningBalanceId id,
                       UserIdentityId requesterId,
                       RolId requesterRolId) {

               authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.OPENING_BALANCE,
                ActionCatalog.BasicAction.DELETE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
               
                 OpeningBalance balance = openingBalanceRepository.findById(id)
                .orElseThrow(() -> new OpeningBalanceNotFoundException("Not found"));



        openingBalanceRepository.deleteById(balance.getOpeningBalanceId());
    }
}