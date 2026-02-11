package com.example.ClinicaDefinitiva.application.service.actor;


import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.DentistNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.dentistMapper.DentistReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.dentistMapper.DentistWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.DentistUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
public class DentistApplicationService implements DentistUseCase {
    private final DentistRepository dentistRepository;
    private final ReceptionRepository receptionRepository;
    private final DentistReadMapper dentistReadMapper;
    private final DentistWriteMapper dentistWriteMapper;
    private final AuthorizationService authorizationService;

    public DentistApplicationService(DentistRepository dentistRepository,
                                     ReceptionRepository receptionRepository,
                                     DentistReadMapper dentistReadMapper,
                                     DentistWriteMapper dentistWriteMapper,
                                     AuthorizationService authorizationService) {
        this.dentistRepository = dentistRepository;
        this.receptionRepository = receptionRepository;
        this.dentistReadMapper = dentistReadMapper;
        this.dentistWriteMapper = dentistWriteMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.READ)
    public ReadDentistDto findById(DentistId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        // Construir contexto de seguridad con ownership
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                .withResourceId(id.getValue())
                .withResourceOwnerId(dentist.getUserId());

        // Si el requester es receptionist, agregar sector
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

        return dentistReadMapper.toReadDto(dentist);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageDentistDto> findAll(Pageable pageable,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId);

        // Si el requester es receptionist, agregar sector
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

        // Si es dentist, solo puede ver sus propios datos
        return dentistRepository.findByUserId(requesterId)
                .map(dentist -> {
                    // Validar ownership
                    SecurityContext ownershipContext = SecurityContext
                            .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                            .withResourceOwnerId(dentist.getUserId())
                            .build();

                    if (!authorizationService.isAuthorized(requesterRolId, ownershipContext)) {
                        // Dentist autenticado pero sin permisos completos → devolver solo su registro
                        return new PageImpl<>(
                                List.of(dentistReadMapper.toPageDto(dentist)),
                                pageable,
                                1
                        );
                    }

                    // Tiene permisos completos → devolver todos
                    return dentistRepository.findAll(pageable)
                            .map(dentistReadMapper::toPageDto);
                })
                // Si no se encuentra dentista para ese requesterId → devolver todos
                .orElse(dentistRepository.findAll(pageable)
                        .map(dentistReadMapper::toPageDto));
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageDentistDto> findByAvailability(String status,
                                                   Pageable pageable,
                                                   UserIdentityId requesterId,
                                                   RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId);

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

        return dentistRepository.findByAvailability(status, pageable)
                .map(dentistReadMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageDentistDto> findBySpecialty(String specialty,
                                                Pageable pageable,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId);

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

        return dentistRepository.findBySpecialty(specialty, pageable)
                .map(dentistReadMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadDentistDto save(CreateDentistDto createDentistDto,
                               UserIdentityId requesterId,
                               RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Dentist dentist = dentistWriteMapper.fromCreateDto(createDentistDto);
        Dentist saved = dentistRepository.save(dentist);

        return dentistReadMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadDentistDto updateContactData(UpdateDentistContactDto updateDentistDto,
                                            Long id,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        Dentist dentist = dentistRepository.findById(DentistId.of(id))
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                .withResourceId(id)
                .withResourceOwnerId(dentist.getUserId());

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

        dentistWriteMapper.updateContactFromDto(updateDentistDto, dentist);
        Dentist updated = dentistRepository.save(dentist);

        return dentistReadMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadDentistDto updateSensitiveData(UpdateDentistSensitiveDto updateDentistDto,
                                              Long id,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        Dentist dentist = dentistRepository.findById(DentistId.of(id))
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        // Para datos sensibles, validar sector de RRHH
        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                .withResourceId(id)
                .withSector(receptionist.getSector().Value())
                .withResourceOwnerId(dentist.getUserId())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        dentistWriteMapper.updateSensitiveFromDto(updateDentistDto, dentist);
        Dentist updated = dentistRepository.save(dentist);

        return dentistReadMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public void applyVacation(LocalDateTime start,
                              LocalDateTime end,
                              UserIdentityId requesterId,
                              RolId requesterRolId) {

        Dentist dentist = dentistRepository.findByUserId(requesterId)
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        // Un dentista solo puede aplicar vacaciones a sí mismo (ownership)
        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                .withResourceOwnerId(dentist.getUserId())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

          dentist.applyVacation(start,end);
        //availabilityService.applyVacation(dentist, start, end);
        dentistRepository.save(dentist);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public void applyIncapacity(LocalDateTime start,
                                LocalDateTime end,
                                String note,
                                UserIdentityId requesterId,
                                RolId requesterRolId) {

        Dentist dentist = dentistRepository.findByUserId(requesterId)
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        // Un dentista solo puede aplicar incapacidad a sí mismo (ownership)
        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                .withResourceOwnerId(dentist.getUserId())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        dentist.applyIncapacity(start,end,note);
        dentistRepository.save(dentist);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public void returnToAvailable(UserIdentityId requesterId,
                                  RolId requesterRolId) {

        Dentist dentist = dentistRepository.findByUserId(requesterId)
                .orElseThrow(() ->  new DentistNotFoundException("Not found"));

        // Un dentista solo puede volver a disponible a sí mismo (ownership)
        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                .withResourceOwnerId(dentist.getUserId())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        dentist.returnToAvailable();
        dentistRepository.save(dentist);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteById(DentistId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        // Solo RECEPTIONIST de RECURSOS_HUMANOS puede eliminar dentistas
        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)), requesterId)
                .withResourceId(id.getValue())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        dentistRepository.deleteById(dentist.getDentistId());
    }
}
