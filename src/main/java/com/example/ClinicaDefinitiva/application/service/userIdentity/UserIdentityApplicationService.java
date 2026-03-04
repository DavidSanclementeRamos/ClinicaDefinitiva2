package com.example.ClinicaDefinitiva.application.service.userIdentity;

import com.example.ClinicaDefinitiva.application.dto.authentication.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.mapper.authentication.UserIdentityReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.authentication.UserIdentityWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.authentication.UserIdentityUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserAccessValidator;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserDeactivationPolicy;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.AggregateBusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.Severity;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserIdentityApplicationService implements UserIdentityUseCase {

    private final UserIdentityRepository userIdentityRepository;
    private final UserDeactivationPolicy userDeactivationPolicy;
    private final UserIdentityReadMapper readMapper;
    private final UserIdentityWriteMapper writeMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;
    private final UserAccessValidator userAccessValidator;


    public UserIdentityApplicationService(UserIdentityRepository userIdentityRepository,
                                          UserDeactivationPolicy userDeactivationPolicy,
                                          UserIdentityReadMapper readMapper,
                                          UserIdentityWriteMapper writeMapper,
                                          PasswordEncoder passwordEncoder,
                                          AuthorizationService authorizationService, UserAccessValidator userAccessValidator) {
        this.userIdentityRepository = userIdentityRepository;
        this.userDeactivationPolicy = userDeactivationPolicy;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
        this.userAccessValidator = userAccessValidator;
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.READ)
    public Optional<ReadUserIdentityDto> findById(UserIdentityId targetUserId,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY)), requesterId)
                .withResourceId(targetUserId.value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return userIdentityRepository.findById(targetUserId)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageUserIdentityDto> findAll(Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return userIdentityRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.READ)
    public Optional<PageUserIdentityDto> findByEmail(String email,
                                                     UserIdentityId requesterId,
                                                     RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return userIdentityRepository.findByEmail(email)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.READ)
    public Optional<PageUserIdentityDto> findByEmailAndStatus(String email,
                                                              String status,
                                                              UserIdentityId requesterId,
                                                              RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return userIdentityRepository.findByEmailAndStatus(email, status)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.READ)
    public Optional<PageUserIdentityDto> findByIdAndStatus(UserIdentityId targetUserId,
                                                           String status,
                                                           UserIdentityId requesterId,
                                                           RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY)), requesterId)
                .withResourceId(targetUserId.value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return userIdentityRepository.findByIdAndStatus(targetUserId, status)
                .map(readMapper::toPageDto);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadUserIdentityDto register(CreateUserIdentityDto dto,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY)), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // Verificar que el email no exista
        userIdentityRepository.findByEmail(dto.email())
                .ifPresent(existing -> {
                    throw new BusinessRuleViolationException(
                            VoAccesError.ERR_USER_DUPLICATE_EMAIL,
                            EntityContext.USER_IDENTITY
                    );
                });


        UserIdentity user = UserIdentity.register(
            writeMapper.toEmail(dto).getValue().get(),
            writeMapper.toPassword(dto).getValue().get(),
            writeMapper.toUserName(dto).getValue().get(),
            Instant.now()
        );

        // Encriptar la contraseña
        String encodedPassword = passwordEncoder.encode(dto.password());
        user.setPassword(encodedPassword);

        UserIdentity saved = userIdentityRepository.save(user);
        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadUserIdentityDto update(UpdateUserIdentityDto dto,
                                      Long id,
                                      UserIdentityId requesterId,
                                      RolId requesterRolId) {

        UserIdentityId targetUserId = UserIdentityId.from(id);

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY)), requesterId)
                .withResourceId(targetUserId.value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        UserIdentity userIdentity = userIdentityRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USER_IDENTITY
                ));

        // Si se está actualizando el email, verificar que no exista
        if (dto.email() != null) {
            userIdentityRepository.findByEmail(dto.email())
                    .ifPresent(existing -> {
                        throw new BusinessRuleViolationException(
                                VoAccesError.ERR_USER_DUPLICATE_EMAIL,
                                EntityContext.USER_IDENTITY
                        );
                    });
        }

         userIdentity.update(
            writeMapper.toUserName(dto).getValue().get(),
            writeMapper.toEmail(dto).getValue().get(),
            writeMapper.toPassword(dto).getValue().get(),
            Instant.now()
        );


        // Si se está actualizando la contraseña, encriptarla
        if (dto.password() != null && !dto.password().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(dto.password());
            userIdentity.setPassword(encodedPassword);
        }

        UserIdentity updated = userIdentityRepository.save(userIdentity);
        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.DEACTIVATE)
    public ReadUserIdentityDto deactivate(UserIdentityId targetUserId,
                                          String reason,
                                          UserIdentityId requesterId,
                                          RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY),
                        ActionCatalog.of(ActionCatalog.BasicAction.DEACTIVATE)
                ), requesterId)
                .withResourceId(targetUserId.value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        UserIdentity userIdentity = userIdentityRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USER_IDENTITY
                ));

        // Validar política de desactivación
        Outcome<UserIdentity> outcome = userIdentity.deactivate(userDeactivationPolicy, Instant.now(), reason);
        if (outcome.isFailure()) {
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }

        UserIdentity deactivated = userIdentityRepository.save(userIdentity);
        return readMapper.toReadDto(deactivated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.SUSPEND)
    public ReadUserIdentityDto suspend(UserIdentityId targetUserId,
                                       String reason,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY),
                        ActionCatalog.of(ActionCatalog.BasicAction.SUSPEND)
                ), requesterId)
                .withResourceId(targetUserId.value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        UserIdentity userIdentity = userIdentityRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USER_IDENTITY
                ));

        userIdentity.suspend(reason,Instant.now());
        UserIdentity suspended = userIdentityRepository.save(userIdentity);
        return readMapper.toReadDto(suspended);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.AUTHENTICATE)
    public ReadUserIdentityDto authenticate(String email,
                                            String rawPassword,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY),
                        ActionCatalog.of(ActionCatalog.BasicAction.AUTHENTICATE)
                ), requesterId)
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        UserIdentity userIdentity = userIdentityRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_INVALID_CREDENTIALS,
                        EntityContext.USER_IDENTITY
                ));

       /** // Verificar contraseña
        if (!passwordEncoder.matches(rawPassword, userIdentity.getHashedPassword())) {
            throw new BusinessRuleViolationException(
                    UserIdentityError.ERR_USER_INVALID_CREDENTIALS,
                    EntityContext.USER_IDENTITY
            );
        }*/
        // Validar password
        if (!passwordEncoder.matches(rawPassword, userIdentity.getHashedPassword().toString())) {
            userIdentity.recordFailedLogin(Instant.now(), 3, Duration.ofMinutes(5));
            userIdentityRepository.save(userIdentity);
            throw new AggregateBusinessRuleViolationException(
                    List.of(new OutcomeDetail(UserIdentityError.ERR_USER_INVALID_CREDENTIALS,
                            Severity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY)));
        }

        // Verificar estado del usuario
        userAccessValidator.validateUserCanPerformSensitiveAction(userIdentity.getId(),Instant.now(),EntityContext.USER_IDENTITY);


        /*// Validar reglas de negocio
        Outcome<UserIdentity> eligibility = userIdentity.canPerformSensitiveAction(Instant.now());
        if (eligibility.isFailure()) {
            throw new AggregateBusinessRuleViolationException(eligibility.getDetalles());
        }*/

        // Login exitoso
        userIdentity.recordSuccessfulLogin(Instant.now());
        userIdentityRepository.save(userIdentity);




        return readMapper.toReadDto(userIdentity);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.REACTIVATE)
    public ReadUserIdentityDto reactivate(UserIdentityId targetUserId,
                                          String reason,
                                          UserIdentityId requesterId,
                                          RolId requesterRolId) {

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.USER_IDENTITY),
                        ActionCatalog.of(ActionCatalog.BasicAction.REACTIVATE)
                ), requesterId)
                .withResourceId(targetUserId.value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        UserIdentity userIdentity = userIdentityRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USER_IDENTITY
                ));

        userIdentity.reactivate( Instant.now());
        UserIdentity reactivated = userIdentityRepository.save(userIdentity);
        return readMapper.toReadDto(reactivated);
    }
}