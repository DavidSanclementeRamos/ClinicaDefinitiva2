package com.example.ClinicaDefinitiva.application.service.billing;


import com.example.ClinicaDefinitiva.application.dto.billing.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.PageRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.ReadRateDto;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.RateNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.billing.rate.RateReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.billing.rate.RateWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.billing.RateUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.output.RateRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
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

import java.time.LocalDateTime;


@Service
@Transactional
public class RateApplicationService implements RateUseCase {

    private final RateRepository rateRepository;
    private final RateReadMapper readMapper;
    private final RateWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public RateApplicationService(RateRepository rateRepository, RateReadMapper readMapper, RateWriteMapper writeMapper, AuthorizationHelper authorizationHelper) {
        this.rateRepository = rateRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }

   

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public ReadRateDto findById(RateId id,
                                UserIdentityId requesterId,
                                RolId requesterRolId) {

             

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new RateNotFoundException("No found"));

           

        return readMapper.toDto(rate);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findAll(Pageable pageable,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

         authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .build()
        );


        return rateRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findByService(ServiceId serviceId,
                                           Pageable pageable,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .build()
        );


        return rateRepository.findByService(serviceId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findByPayerType(String payerType,
                                             Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

       authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .build()
        );


        return rateRepository.findByPayerType(String.valueOf(payerType), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findByContract(ContractId contractId,
                                            Pageable pageable,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

       authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .build()
        );


        return rateRepository.findByContract(contractId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public ReadRateDto findActiveRateForService(ServiceId serviceId,
                                                String payerType,
                                                ContractId contractId,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .build()
        );


        Rate rate = rateRepository.findActiveRateForService(serviceId,contractId )
                .orElseThrow(() -> new RateNotFoundException("Not fount"));

        return readMapper.toDto(rate);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageRateDto> findCurrentlyValid(Pageable pageable,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {
 authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .build()
        );


        return rateRepository.findCurrentlyValid(LocalDateTime.now(), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadRateDto create(CreateRateDto dto,
                              UserIdentityId requesterId,
                              RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .build()
        );


        // RN-RATE-003: Check for overlapping rates
        //validateNoOverlappingRates(dto);

         Rate rate = Rate.create(
                writeMapper.toServiceId(dto),
                writeMapper.toAmount(dto),

                writeMapper.toPayerType(dto),
                writeMapper.toContractId(dto)
         );

        Rate saved = rateRepository.save(rate);

        return readMapper.toDto(saved);
    }

   

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RATE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadRateDto endValidityAt(RateId id,
                                   LocalDateTime endDate,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

         Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new RateNotFoundException("No found"));



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

               
 authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

         Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new RateNotFoundException("No found"));


        
        rate.deactivate();
        rateRepository.save(rate);
    }

    @Override
    public void markAsReplaced(RateId id, UserIdentityId requesterId, RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.RATE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

         Rate rate = rateRepository.findById(id)
                .orElseThrow(() -> new RateNotFoundException("No found"));


            rate.markAsReplaced();
                    rateRepository.save(rate);

            
        
    }
    

 
}