package com.example.ClinicaDefinitiva.application.service.billing;


import com.example.ClinicaDefinitiva.application.dto.billing.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.PageRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.ReadRateDto;
import com.example.ClinicaDefinitiva.application.exceptions.RateNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.billing.RateReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.billing.RateWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.billing.RateUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.RateError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.portsOutput.RateRepository;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@Transactional
public class RateApplicationService implements RateUseCase {

    private final RateRepository rateRepository;
    private final ReceptionRepository receptionRepository;
    private final RateReadMapper readMapper;
    private final RateWriteMapper writeMapper;
    private final AuthorizationService authorizationService;

    public RateApplicationService(
            RateRepository rateRepository,
            ReceptionRepository receptionRepository,
            RateReadMapper readMapper,
            RateWriteMapper writeMapper,
            AuthorizationService authorizationService) {
        this.rateRepository = rateRepository;
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public ReadRateDto findById(RateId id,
                                UserIdentityId requesterId,
                                RolId requesterRolId) {

        Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        RateError.ERR_RATE_NOT_FOUND,
                        EntityContext.RATE
                ));

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .withResourceId(id.getValue());

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().getDescription())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return readMapper.toDto(rate);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findAll(Pageable pageable,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId);

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().getDescription())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return rateRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findByService(Long serviceId,
                                           Pageable pageable,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return rateRepository.findByService(ServiceId.of(serviceId), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findByPayerType(Rate.PayerType payerType,
                                             Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return rateRepository.findByPayerType(String.valueOf(payerType), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findByContract(Long contractId,
                                            Pageable pageable,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return rateRepository.findByContract(ContractId.of(contractId), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public ReadRateDto findActiveRateForService(Long serviceId,
                                                Rate.PayerType payerType,
                                                Long contractId,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Rate rate = rateRepository.findActiveRateForService(
                ServiceId.of(serviceId),
                contractId != null ? ContractId.of(contractId) : null
        ).orElseThrow(() -> new RateNotFoundException("Not fount"));

        return readMapper.toDto(rate);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findCurrentlyValid(Pageable pageable,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return rateRepository.findCurrentlyValid(LocalDateTime.now(), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadRateDto create(CreateRateDto dto,
                              UserIdentityId requesterId,
                              RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .withSector(receptionist.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // RN-RATE-003: Check for overlapping rates
        //validateNoOverlappingRates(dto);

        Rate rate = writeMapper.fromCreateDto(dto);
        Rate saved = rateRepository.save(rate);

        return readMapper.toDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadRateDto updateAmount(RateId id,
                                    BigDecimal newAmount,
                                    LocalDateTime validFrom,
                                    UserIdentityId requesterId,
                                    RolId requesterRolId) {

        Rate currentRate = rateRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        RateError.ERR_RATE_NOT_FOUND,
                        EntityContext.RATE
                ));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .withResourceId(id.getValue())
                .withSector(receptionist.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // End validity of current rate
        currentRate.endValidityAt(validFrom);
        rateRepository.save(currentRate);

        // Create new rate with new amount
        Rate newRate = Rate.create(
                currentRate.getServiceId(),
                Price.of(newAmount, currentRate.getAmount().getCurrency()),
                currentRate.getPayerType(),
                currentRate.getContractId()
        );

        Rate savedNewRate = rateRepository.save(newRate);
        return readMapper.toDto(savedNewRate);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadRateDto endValidity(RateId id,
                                   LocalDateTime endDate,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        RateError.ERR_RATE_NOT_FOUND,
                        EntityContext.RATE
                ));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .withResourceId(id.getValue())
                .withSector(receptionist.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // RN-RATE-002: End date must be after start date
        rate.endValidityAt(endDate);
        Rate updated = rateRepository.save(rate);

        return readMapper.toDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.UPDATE)
    public void deactivate(RateId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        RateError.ERR_RATE_NOT_FOUND,
                        EntityContext.RATE
                ));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)), requesterId)
                .withResourceId(id.getValue())
                .withSector(receptionist.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        rate.deactivate();
        rateRepository.save(rate);
    }

    // RN-RATE-003: Validate no overlapping rates
    /**private void validateNoOverlappingRates(CreateRateDto dto) {
        boolean hasOverlap = rateRepository.hasOverlappingRates(
                ServiceId.of(dto.getServiceId()),
                dto.getPayerType(),
                dto.getContractId() != null ? ContractId.of(dto.getContractId()) : null,
                dto.getValidFrom(),
                dto.getValidTo()
        );

        if (hasOverlap) {
            throw new BusinessRuleViolationException(
                    RateError.ERR_RATE_OVERLAPPING_VALIDITY,
                    EntityContext.RATE
            );
        }
    }*/
}