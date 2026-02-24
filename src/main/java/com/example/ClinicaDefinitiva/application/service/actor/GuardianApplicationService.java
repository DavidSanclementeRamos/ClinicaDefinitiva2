package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.*;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.GuardianNoFoundException;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper.GuardianReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper.GuardianWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.GuardianUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
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
public class GuardianApplicationService implements GuardianUseCase {

    private final GuardianRepository guardianRepository;
    private final ReceptionRepository receptionRepository;
    private final GuardianReadMapper readMapper;
    private final GuardianWriteMapper writeMapper;
    private final AuthorizationService authorizationService;

    public GuardianApplicationService(GuardianRepository guardianRepository,
                                      ReceptionRepository receptionRepository,
                                      GuardianReadMapper readMapper,
                                      GuardianWriteMapper writeMapper,
                                      AuthorizationService authorizationService) {
        this.guardianRepository = guardianRepository;
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.READ)
    public ReadGuardianDto findById(GuardianId id,
                                    UserIdentityId requesterId,
                                    RolId requesterRolId) {

        Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNoFoundException("Not fount"));

        // Construir contexto con ownership
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)), requesterId)
                .withResourceId(id.value())
                .withResourceOwnerId(guardian.getUserId());

        // Si es receptionist, agregar sector
        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return readMapper.toReadDto(guardian);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageGuardianDto> findAll(Pageable pageable,
                                         UserIdentityId requesterId,
                                         RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)), requesterId);

        // Si es receptionist, agregar sector
        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return guardianRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageGuardianDto> findByPatientId(PatientId patientId,
                                                 Pageable pageable,
                                                 UserIdentityId requesterId,
                                                 RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)), requesterId)
                .withResourceId(patientId.value());

        // Si es receptionist, agregar sector
        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return guardianRepository.findByPatientId(patientId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadGuardianDto save(CreateGuardianDto createGuardianDto,
                                UserIdentityId requesterId,
                                RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Guardian guardian = writeMapper.fromCreateDto(createGuardianDto);
        Guardian saved = guardianRepository.save(guardian);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadGuardianDto updateContactData(UpdateGuardianContactDto updateGuardian,
                                             GuardianId id,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNoFoundException("Not found"));

        // Construir contexto con ownership
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)), requesterId)
                .withResourceId(id.value())
                .withResourceOwnerId(guardian.getUserId());

        // Si es receptionist, agregar sector
        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        writeMapper.updateContactFromDto(updateGuardian, guardian);
        Guardian updated = guardianRepository.save(guardian);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadGuardianDto updateSensitiveData(UpdateGuardianSensitiveDto updateGuardian,
                                               GuardianId id,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {

        Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNoFoundException("Not found"));

        // Para datos sensibles, requiere sector (receptionist)
        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)), requesterId)
                .withResourceId(id.value())
                .withSector(receptionist.getSector().Value())
                .withResourceOwnerId(guardian.getUserId())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        writeMapper.updateSensitiveFromDto(updateGuardian, guardian);
        Guardian updated = guardianRepository.save(guardian);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteById(GuardianId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNoFoundException("Not found"));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)), requesterId)
                .withResourceId(id.value())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        guardianRepository.deleteById(guardian.getGuardianId());
    }
}
