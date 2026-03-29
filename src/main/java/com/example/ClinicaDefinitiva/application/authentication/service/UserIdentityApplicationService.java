package com.example.ClinicaDefinitiva.application.authentication.service;

import com.example.ClinicaDefinitiva.application.authentication.dto.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.input.UserIdentityUseCase;
import com.example.ClinicaDefinitiva.application.authentication.mapper.UserIdentityReadMapper;
import com.example.ClinicaDefinitiva.application.authentication.mapper.UserIdentityWriteMapper;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserAccessValidator;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserDeactivationPolicy;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.AuthenticationVoError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.AggregateBusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/** POLÍTICAS:
 * - SectorBasedPolicy: Solo RECEPTIONIST puede gestionar usuarios
 * - (Futuro) OwnershipPolicy: Usuario puede ver/editar su propio perfil
 */
@Service
@Transactional
public class UserIdentityApplicationService implements UserIdentityUseCase {

    private final UserIdentityRepository userIdentityRepository;
    private final UserDeactivationPolicy userDeactivationPolicy;
    private final UserIdentityReadMapper readMapper;
    private final UserIdentityWriteMapper writeMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationHelper authorizationHelper;
    private final UserAccessValidator userAccessValidator;

    public UserIdentityApplicationService(
            UserIdentityRepository userIdentityRepository,
            UserDeactivationPolicy userDeactivationPolicy,
            UserIdentityReadMapper readMapper,
            UserIdentityWriteMapper writeMapper,
            PasswordEncoder passwordEncoder,
            AuthorizationHelper authorizationHelper,
            UserAccessValidator userAccessValidator) {
        this.userIdentityRepository = userIdentityRepository;
        this.userDeactivationPolicy = userDeactivationPolicy;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationHelper = authorizationHelper;
        this.userAccessValidator = userAccessValidator;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.READ)
    public ReadUserIdentityDto findById(UserIdentityId targetUserId,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(targetUserId.value())
                        .build()
        );

         return userIdentityRepository.findById(targetUserId)
            .map(readMapper::toReadDto)
            .orElseThrow(() -> new BusinessRuleViolationException(
                    UserIdentityError.ERR_USER_NOT_FOUND,
                    EntityContext.USER_IDENTITY
            ));    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageUserIdentityDto> findAll(Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return userIdentityRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.READ)
    public Optional<PageUserIdentityDto> findByEmail(String email,
                                                     UserIdentityId requesterId,
                                                     RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

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
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

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
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(targetUserId.value())
                        .build()
        );

        return userIdentityRepository.findByIdAndStatus(targetUserId, status)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.USER_IDENTITY,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadUserIdentityDto register(CreateUserIdentityDto dto,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        // Verificar que el email no exista
        userIdentityRepository.findByEmail(dto.email())
                .ifPresent(existing -> {
                    throw new BusinessRuleViolationException(
                            AuthenticationVoError.ERR_USER_DUPLICATE_EMAIL,
                            EntityContext.USER_IDENTITY
                    );
                });

        UserIdentity user = UserIdentity.register(
            writeMapper.toEmail(dto),
            writeMapper.toPassword(dto),
            writeMapper.toUserName(dto),
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

        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(targetUserId.value())
                        .build()
        );

        UserIdentity userIdentity = userIdentityRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USER_IDENTITY
                ));
        
         // 🔍 LOGS DE DEBUG
    System.out.println("=== ANTES DE UPDATE ===");
    System.out.println("Nombre actual: " + userIdentity.getName());
    System.out.println("Email actual: " + userIdentity.getEmail());
    System.out.println("DTO name: " + dto.name());
    System.out.println("DTO email: " + dto.email());

        // Si se está actualizando el email, verificar que no exista
        if (dto.email() != null) {
            userIdentityRepository.findByEmail(dto.email())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(targetUserId)) {
                            throw new BusinessRuleViolationException(
                                    AuthenticationVoError.ERR_USER_DUPLICATE_EMAIL,
                                    EntityContext.USER_IDENTITY
                            );
                        }
                    });
        }


        userIdentity.update(
            writeMapper.toUserName(dto),
            writeMapper.toEmail(dto),
            writeMapper.toPassword(dto),
            
            Instant.now()
        );

        // Si se está actualizando la contraseña, encriptarla
        // ✅ CORREGIDO: usar la contraseña del DTO, no el nombre
    if (dto.password() != null && !dto.password().isEmpty()) {
        String encodedPassword = passwordEncoder.encode(dto.password());  // ← CORREGIDO
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
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.DEACTIVATE,
                AuthorizationContext.builder()
                        .withResourceId(targetUserId.value())
                        .build()
        );

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
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.SUSPEND,
                AuthorizationContext.builder()
                        .withResourceId(targetUserId.value())
                        .build()
        );

        UserIdentity userIdentity = userIdentityRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USER_IDENTITY
                ));

        userIdentity.suspend(reason, Instant.now());
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
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.AUTHENTICATE,
                AuthorizationContext.builder().build()
        );

        UserIdentity userIdentity = userIdentityRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_INVALID_CREDENTIALS,
                        EntityContext.USER_IDENTITY
                ));

        // Validar password
        if (!passwordEncoder.matches(rawPassword, userIdentity.getHashedPassword().toString())) {
            userIdentity.recordFailedLogin(Instant.now(), 3, Duration.ofMinutes(5));
            userIdentityRepository.save(userIdentity);
            throw new AggregateBusinessRuleViolationException(
                    List.of(new OutcomeDetail(
                            UserIdentityError.ERR_USER_INVALID_CREDENTIALS,
                            ErrorSeverity.ERROR,
                            Category.TECNICO,
                            EntityContext.USER_IDENTITY
                    ))
            );
        }

        // Verificar estado del usuario
        userAccessValidator.validateUserCanPerformSensitiveAction(
                userIdentity.getId(),
                Instant.now(),
                EntityContext.USER_IDENTITY
        );

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
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.USER_IDENTITY,
                ActionCatalog.BasicAction.REACTIVATE,
                AuthorizationContext.builder()
                        .withResourceId(targetUserId.value())
                        .build()
        );

        UserIdentity userIdentity = userIdentityRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USER_IDENTITY
                ));

        userIdentity.reactivate(Instant.now());
        UserIdentity reactivated = userIdentityRepository.save(userIdentity);
        return readMapper.toReadDto(reactivated);
    }
}
    