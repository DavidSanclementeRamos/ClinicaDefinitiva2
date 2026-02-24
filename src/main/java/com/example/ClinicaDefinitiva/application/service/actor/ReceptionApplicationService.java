package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.ReceptionNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper.ReceptionistReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.receptionMapper.ReceptionistWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.ReceptionUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReceptionApplicationService implements ReceptionUseCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionistReadMapper readMapper;
    private final ReceptionistWriteMapper writeMapper;
    private final AuthorizationService authorizationService;

    public ReceptionApplicationService(ReceptionRepository receptionRepository,
                                       ReceptionistReadMapper readMapper,
                                       ReceptionistWriteMapper writeMapper,
                                       AuthorizationService authorizationService) {
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.READ)
    public ReadReceptionistDto findById(ReceptionId id,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findById(id)
                .orElseThrow(() -> new ReceptionNotFoundException("Not found"));

        // Construir contexto con sector
        Receptionist requester = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RECEPTIONIST)), requesterId)
                .withResourceId(id.getValue())
                .withSector(requester.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return readMapper.toReadDto(receptionist);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageReceptionistDto> findAll(Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        Receptionist requester = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RECEPTIONIST)), requesterId)
                .withSector(requester.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // Opcionalmente, filtrar solo por mismo sector
        // return receptionRepository.findBySector(requester.getSector().Value(), pageable)
        //         .map(readMapper::toPageDto);

        return receptionRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageReceptionistDto> findBySector(String sector,
                                                  Pageable pageable,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {

        Receptionist requester = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RECEPTIONIST)), requesterId)
                .withSector(requester.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return receptionRepository.findBySector(sector, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadReceptionistDto save(CreateReceptionistDto dto,
                                    UserIdentityId requesterId,
                                    RolId requesterRolId) {

        Receptionist requester = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.RECEPTIONIST)), requesterId)
                .withSector(requester.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Receptionist receptionist = writeMapper.fromCreateDto(dto);
        Receptionist saved = receptionRepository.save(receptionist);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadReceptionistDto updateContact(UpdateReceptionistContactDto dto,
                                             ReceptionId id,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findById(id)
                .orElseThrow(() -> new  ReceptionNotFoundException("Not found"));

        Receptionist requester = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.RECEPTIONIST)), requesterId)
                .withResourceId(id.getValue())
                .withSector(requester.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        writeMapper.updateContactFromDto(dto, receptionist);
        Receptionist updated = receptionRepository.save(receptionist);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadReceptionistDto updateSensitive(UpdateReceptionistSensitiveDto dto,
                                               ReceptionId id,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findById(id)
                .orElseThrow(() -> new  ReceptionNotFoundException("Not found"));

        Receptionist requester = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.RECEPTIONIST)), requesterId)
                .withResourceId(id.getValue())
                .withSector(requester.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        writeMapper.updateSensitiveFromDto(dto, receptionist);
        Receptionist updated = receptionRepository.save(receptionist);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteById(ReceptionId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findById(id)
                .orElseThrow(() -> new ReceptionNotFoundException("Not found"));

        Receptionist requester = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.RECEPTIONIST)), requesterId)
                .withResourceId(id.getValue())
                .withSector(requester.getSector().getDescription())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        receptionRepository.deleteById(receptionist.getId());
    }
}
