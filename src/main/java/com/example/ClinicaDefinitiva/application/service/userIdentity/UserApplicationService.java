package com.example.ClinicaDefinitiva.application.service.userIdentity;

import com.example.ClinicaDefinitiva.application.dto.authentication.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.exceptions.UserIdentityNoFoundException;
import com.example.ClinicaDefinitiva.application.mapper.authentication.UserIdentityReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.authentication.UserIdentityWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.authentication.SecurityPolicy;
import com.example.ClinicaDefinitiva.application.portsInput.authentication.UserIdentityUseCase;
import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.AggregateBusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserDeactivationPolicy;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.Severity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;

import java.util.List;
import java.util.Optional;

public class UserApplicationService implements UserIdentityUseCase {
    private final UserIdentityRepository userIdentityRepository;
    private final UserDeactivationPolicy userDeactivationPolicy;
    private final UserIdentityReadMapper readMapper;
    private final UserIdentityWriteMapper writeMapper;
    private final SecurityPolicy securityPolicy;
    // de donde probiene el password encoder
    private final PasswordEncoder passwordEncoder;

    public UserApplicationService(UserIdentityRepository userIdentityRepository,
                                  UserDeactivationPolicy userDeactivationPolicy,
                                  UserIdentityReadMapper readMapper,
                                  UserIdentityWriteMapper writeMapper, SecurityPolicy securityPolicy, PasswordEncoder passwordEncoder) {
        this.userIdentityRepository = userIdentityRepository;
        this.userDeactivationPolicy = userDeactivationPolicy;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.securityPolicy = securityPolicy;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ReadUserIdentityDto findById(Long id) {
        UserIdentity user = userIdentityRepository.findById(UserIdentityId.from(id))
                .orElseThrow(() -> new UserIdentityNoFoundException(UserIdentityError
                        .ERR_USER_NOT_FOUND, EntityContext.USUARIO, UserIdentityId.from(id)));

        return readMapper.toDto(user);
    }

    @Override
    public Page<PageUserIdentityDto> findAll(Pageable pageable) {
        Page<UserIdentity> users = userIdentityRepository.findAll(pageable);
        if(users.isEmpty()){
            throw new UserIdentityNoFoundException(UserIdentityError
                    .ERR_USER_NOT_FOUND, EntityContext.USUARIO, null);
        }
        return users.map(readMapper::pageToDto);
    }

    @Override
    public Optional<PageUserIdentityDto> findByEmail(String email) {
        UserIdentity users = userIdentityRepository.findByEmail(email)
                .orElseThrow(() -> new UserIdentityNoFoundException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USUARIO,
                        null
                ));

        return Optional.of(readMapper.pageToDto(users));

    }

    @Override
    public Page<PageUserIdentityDto> findByEmailAndStatus(String email, String status, Pageable pageable) {
        Page<UserIdentity> users = userIdentityRepository.findByEmailAndStatus(email, status, pageable);
        if (users.isEmpty()) {
            throw new UserIdentityNoFoundException(
                    UserIdentityError.ERR_USER_NOT_FOUND,
                    EntityContext.USUARIO,
                    null
            );
        }
        return users.map(readMapper::pageToDto);
    }

    @Override
    public Page<PageUserIdentityDto> findByIdAndStatus(Long id, String status, Pageable pageable) {
        Page<UserIdentity> users = userIdentityRepository.findByIdAndStatus(id, status, pageable);
        if (users.isEmpty()) {
            throw new UserIdentityNoFoundException(
                    UserIdentityError.ERR_USER_NOT_FOUND,
                    EntityContext.USUARIO,
                    null
            );
        }
        return users.map(readMapper::pageToDto);
    }



    @Override
    public ReadUserIdentityDto register(CreateUserIdentityDto dto) {
        UserIdentity user = writeMapper.dtoCreateToUserIdentity(dto);
        userIdentityRepository.save(user);
        return readMapper.toDto(user);
    }

    @Override
    public ReadUserIdentityDto recordSuccessfulLogin(Long userId) {
        UserIdentity user = userIdentityRepository.findById(UserIdentityId.from(userId))
        .orElseThrow(() -> new UserIdentityNoFoundException(
                UserIdentityError
                .ERR_USER_NOT_FOUND,
                EntityContext.USUARIO,
                UserIdentityId.from(userId)));

        Outcome<UserIdentity> outcome = user.recordSuccessfulLogin(Instant.now());
        if (outcome.isFailure()) {
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }
        userIdentityRepository.save(user);
        return readMapper.toDto(user);
    }

    @Override
    public ReadUserIdentityDto recordFailedLogin(Long userId) {
        UserIdentity user = userIdentityRepository.findById(UserIdentityId.from(userId))
                .orElseThrow(() -> new UserIdentityNoFoundException(
                UserIdentityError
                        .ERR_USER_NOT_FOUND,
                EntityContext.USUARIO,
                UserIdentityId.from(userId)));

        Outcome<UserIdentity> outcome = user.recordFailedLogin(
                Instant.now(),
                securityPolicy.getMaxAttempts(),
                securityPolicy.getLockDuration()
        );
        if (outcome.isFailure() && outcome.getDetalles().stream().anyMatch(d -> d.getSeverity() == Severity.ERROR)) {
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }
        userIdentityRepository.save(user);
        return readMapper.toDto(user);
    }

    @Override
    public ReadUserIdentityDto editUserData(UpdateUserIdentityDto dto, Long id) {
        UserIdentity user = userIdentityRepository.findById(UserIdentityId.from(id))
                .orElseThrow(() -> new UserIdentityNoFoundException(
                        UserIdentityError
                                .ERR_USER_NOT_FOUND,
                        EntityContext.USUARIO,
                        UserIdentityId.from(id)));


        Outcome<UserIdentity> outcome = user.editUserData(
                new UserIdentityName(dto.name()),
                new Email(dto.email()),
                new HashedPassword(dto.password()),
                Instant.now()
        );

        if (outcome.isFailure()) {
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }

        userIdentityRepository.save(user);
        return readMapper.toDto(user);
    }


    @Override
    public ReadUserIdentityDto verify(Long userId) {
        UserIdentity user = userIdentityRepository.findById(UserIdentityId.from(userId))
                .orElseThrow(() -> new UserIdentityNoFoundException(
                        UserIdentityError
                                .ERR_USER_NOT_FOUND,
                        EntityContext.USUARIO,
                        UserIdentityId.from(userId)));

        Outcome<UserIdentity> outcome = user.verify();
        if (outcome.isFailure()) {
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }
        userIdentityRepository.save(user);
        return readMapper.toDto(user);
    }

    @Override
    public ReadUserIdentityDto deactivate(Long userId, String reason) {
        UserIdentity user = userIdentityRepository.findById(UserIdentityId.from(userId))
                .orElseThrow(() -> new UserIdentityNoFoundException(
                        UserIdentityError
                                .ERR_USER_NOT_FOUND,
                        EntityContext.USUARIO,
                        UserIdentityId.from(userId)));


        Outcome<UserIdentity> outcome = user.deactivate(userDeactivationPolicy, Instant.now(), reason);
        if (outcome.isFailure()) {
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }
        userIdentityRepository.save(user);
        return readMapper.toDto(user);
    }

    @Override
    public ReadUserIdentityDto suspend(Long userId, String reason) {
        UserIdentity user = userIdentityRepository.findById(UserIdentityId.from(userId))
                .orElseThrow(() -> new UserIdentityNoFoundException(
                        UserIdentityError
                                .ERR_USER_NOT_FOUND,
                        EntityContext.USUARIO,
                        UserIdentityId.from(userId)));

        Outcome<UserIdentity> outcome = user.suspend(reason, Instant.now());
        if (outcome.isFailure()) {
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }
        userIdentityRepository.save(user);
        return readMapper.toDto(user);
    }

    @Override
    public ReadUserIdentityDto canPerformSensitiveAction(Long userId) {
        UserIdentity user = userIdentityRepository.findById(UserIdentityId.from(userId))
                .orElseThrow(() -> new UserIdentityNoFoundException(
                        UserIdentityError
                                .ERR_USER_NOT_FOUND,
                        EntityContext.USUARIO,
                        UserIdentityId.from(userId)));

        Outcome<UserIdentity> outcome = user.canPerformSensitiveAction(Instant.now());
        if (outcome.isFailure()) {
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }
        return readMapper.toDto(user);
    }

    @Override
    public ReadUserIdentityDto authenticate(String email, String rawPassword) {
        UserIdentity user = userIdentityRepository.findByEmail(email)
                .orElseThrow(() -> new UserIdentityNoFoundException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        EntityContext.USUARIO,
                        null));

        // Validar password
        if (!passwordEncoder.matches(rawPassword, user.getHashedPassword().toString())) {
            user.recordFailedLogin(Instant.now(), 3, Duration.ofMinutes(5));
            userIdentityRepository.save(user);
            throw new AggregateBusinessRuleViolationException(
                    List.of(new OutcomeDetail(UserIdentityError.ERR_USER_INVALID_CREDENTIALS,
                            Severity.ERROR, Category.TECNICO, EntityContext.USUARIO)));
        }

        // Validar reglas de negocio
        Outcome<UserIdentity> eligibility = user.canPerformSensitiveAction(Instant.now());
        if (eligibility.isFailure()) {
            throw new AggregateBusinessRuleViolationException(eligibility.getDetalles());
        }

        // Login exitoso
        user.recordSuccessfulLogin(Instant.now());
        userIdentityRepository.save(user);

        return readMapper.toDto(user);
    }

}
