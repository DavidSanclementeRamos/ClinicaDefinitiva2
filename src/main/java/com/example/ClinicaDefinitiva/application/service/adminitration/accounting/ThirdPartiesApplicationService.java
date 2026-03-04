package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.CompanyNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ThirdPartyNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.thirdParties.ThirdPartiesReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.thirdParties.ThirdPartiesWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.ThirdPartiesUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ThirdPartiesRepository;
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
public class ThirdPartiesApplicationService implements ThirdPartiesUseCase {

    private final ThirdPartiesRepository repository;
    private final ThirdPartiesReadMapper readMapper;
    private final ThirdPartiesWriteMapper writeMapper;
    private final CompanyRepository companyRepository;
    private final AuthorizationHelper authorizationHelper;

    public ThirdPartiesApplicationService(ThirdPartiesRepository repository,
                                          ThirdPartiesReadMapper readMapper,
                                          ThirdPartiesWriteMapper writeMapper,
                                          CompanyRepository companyRepository,
                                          AuthorizationHelper authorizationHelper) {
        this.repository = repository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.companyRepository = companyRepository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.READ)
    public ReadThirdPartyDto findById(ThirdPartiesId id,
                                      UserIdentityId requesterId,
                                      RolId requesterRolId) {

        ThirdParties thirdParty = repository.findById(id)
                .orElseThrow(() -> new ThirdPartyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        return readMapper.toReadDto(thirdParty);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageThirdPartyDto> findAll(Pageable pageable,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageThirdPartyDto> findByType(String type,
                                              Pageable pageable,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findByType(type, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.READ)
    public ReadThirdPartyDto findByDocumentNumber(String documentNumber,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findByDocumentNumber(documentNumber)
                .map(readMapper::toReadDto)
                .orElseThrow(() -> new ThirdPartyNotFoundException("Not found"));
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageThirdPartyDto> findByCompany(CompanyId companyId,
                                                 Pageable pageable,
                                                 UserIdentityId requesterId,
                                                 RolId requesterRolId) {

        companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(companyId.getValue())
                        .build()
        );

        return repository.findByCompanyId(companyId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadThirdPartyDto createThirdParty(CreateThirdPartyDto dto,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        ThirdParties thirdParty = ThirdParties.registerThirdParties(
            writeMapper.toCompanyId(dto),
            writeMapper.toName(dto),
            writeMapper.toTypeDocument(dto),
            writeMapper.toDocumentNumber(dto),
            writeMapper.toTypeThirdParties(dto),
            writeMapper.toAddress(dto),
            writeMapper.toPhoneNumber(dto),
            writeMapper.toEmail(dto)
        );

        ThirdParties saved = repository.save(thirdParty);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadThirdPartyDto updateContactInformation(ThirdPartiesId id,
                                                      UpdateThirdPartyDto dto,
                                                      UserIdentityId requesterId,
                                                      RolId requesterRolId) {

        ThirdParties thirdParty = repository.findById(id)
                .orElseThrow(() -> new ThirdPartyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        thirdParty.updateContactInformation(
    writeMapper.toName(dto),
    writeMapper.toAddress(dto),
    writeMapper.toPhoneNumber(dto),
    writeMapper.toEmail(dto)
);
        ThirdParties updated = repository.save(thirdParty);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.UPDATE)
    public void activate(ThirdPartiesId id,
                         UserIdentityId requesterId,
                         RolId requesterRolId) {

        ThirdParties thirdParty = repository.findById(id)
                .orElseThrow(() -> new ThirdPartyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        thirdParty.activate();
        repository.save(thirdParty);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.THIRD_PARTY,
            action = ActionCatalog.BasicAction.DEACTIVATE)
    public void inactivate(ThirdPartiesId id,
                           String reason,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        ThirdParties thirdParty = repository.findById(id)
                .orElseThrow(() -> new ThirdPartyNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.THIRD_PARTY,
                ActionCatalog.BasicAction.DEACTIVATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        thirdParty.inactivate(reason);
        repository.save(thirdParty);
    }
}