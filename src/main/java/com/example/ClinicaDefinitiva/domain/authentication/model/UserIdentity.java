package com.example.ClinicaDefinitiva.domain.authentication.model;

import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authentication.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserDeactivationPolicy;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityStatus;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class UserIdentity {
    private final UserIdentityId id;
    private Email email;
    private HashedPassword hashedPassword;
    private UserIdentityName name;
    private final Instant createdAt;
    private Instant lastLoginAt;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    private boolean verified;
    private UserIdentityStatus status;
    private long version;

    private UserIdentity(UserIdentityId id, Email email, HashedPassword hashedPassword,
                         UserIdentityName name, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.createdAt = createdAt;
        this.status = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        this.verified = false;
    }

    public static UserIdentity register(Email email, HashedPassword hashedPassword,
                                        UserIdentityName name, Instant now) {
        return new UserIdentity(null, email, hashedPassword, name, now);
    }

    public Outcome<UserIdentity> recordSuccessfulLogin(Instant now) {
        if (isLocked(now)) return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_FAILED_ATTEMPTS_NOT_RESET,
                ErrorSeverity.WARN, Category.TECNICO, EntityContext.USER_IDENTITY));
        this.failedLoginAttempts = 0;
        this.lastLoginAt = now;
        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public Outcome<UserIdentity> update(Optional<UserIdentityName> newName,
                                    Optional<Email> newEmail,
                                    Optional<HashedPassword> newPassword,
                                    Instant now) {
    Outcome<UserIdentity> eligibility = canPerformSensitiveAction(now);
    if (!eligibility.isSuccess()) return eligibility;
    
    newName.ifPresent(name -> this.name = name);
    newEmail.ifPresent(email -> this.email = email);
    newPassword.ifPresent(pwd -> this.hashedPassword = pwd);
    
    return Outcome.ok(this);
}

    public Outcome<UserIdentity> recordFailedLogin(Instant now, int maxAttempts, Duration lockDuration) {
        if (isLocked(now)) return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                ErrorSeverity.WARN, Category.TECNICO, EntityContext.USER_IDENTITY));
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= maxAttempts) {
            this.lockedUntil = now.plus(lockDuration);
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS,
                    ErrorSeverity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY));
        }
        return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_INVALID_CREDENTIALS,
                ErrorSeverity.INFO, Category.TECNICO, EntityContext.USER_IDENTITY));
    }

    public boolean isLocked(Instant now) {
        return lockedUntil != null && now.isBefore(lockedUntil);
    }

    public Outcome<UserIdentity> verify() {
        if (this.verified) return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_NOT_VERIFIED,
                ErrorSeverity.INFO, Category.TECNICO, EntityContext.USER_IDENTITY));
        this.verified = true;
        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public Outcome<UserIdentity> deactivate(UserDeactivationPolicy policy, Instant now, String reason) {
    // Validar solo lock y estado activo (NO verificado)
    if (isLocked(now)) {
        return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                ErrorSeverity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY));
    }
    if (status.getValue() != UserIdentityStatus.Status.ACTIVE) {
        return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_INACTIVE,
                ErrorSeverity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY));
    }

    if (reason == null || reason.isBlank()) {
        return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_DEACTIVATION_REASON_REQUIRED,
                ErrorSeverity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY));
    }

    Outcome<Void> validation = policy.validate(this);
    if (validation.isFailure()) return Outcome.fail(validation.getDetalles());

    this.status = status.transitionTo(UserIdentityStatus.Status.INACTIVE);
    return Outcome.ok(this);
}

    public Outcome<UserIdentity> canPerformSensitiveAction(Instant now) {
        if (!verified) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_NOT_VERIFIED,
                    ErrorSeverity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY));
        }
        if (isLocked(now)) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                    ErrorSeverity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY));
        }
        if (status.getValue() != UserIdentityStatus.Status.ACTIVE) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_INACTIVE,
                    ErrorSeverity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY));
        }
        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public Outcome<UserIdentity> suspend(String reason, Instant now) {
        if (!status.canTransitionTo(UserIdentityStatus.Status.SUSPENDED)) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ALREADY_SUSPENDED,
                    ErrorSeverity.INFO, Category.TECNICO, EntityContext.USER_IDENTITY));
        }
        if (reason == null || reason.isBlank()) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_SUSPENSION_REQUIRES_REASON,
                    ErrorSeverity.ERROR, Category.TECNICO, EntityContext.USER_IDENTITY));
        }
        this.status = status.transitionTo(UserIdentityStatus.Status.SUSPENDED);
        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public Outcome<UserIdentity> reactivate(Instant now) {
    // Ya no se valida verified
    if (!status.canTransitionTo(UserIdentityStatus.Status.ACTIVE)) {
        return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_ALREADY_ACTIVE,
                ErrorSeverity.INFO, Category.TECNICO, EntityContext.USER_IDENTITY));
    }
    
    this.status = status.transitionTo(UserIdentityStatus.Status.ACTIVE);
    this.failedLoginAttempts = 0;
    this.lockedUntil = null;
    return Outcome.ok(this);
}

    public static UserIdentity reconstruct(UserIdentityId id, Email email,
                                           HashedPassword hashedPassword, UserIdentityName name,
                                           Instant createdAt, Instant lastLoginAt,
                                           int failedLoginAttempts, Instant lockedUntil,
                                           boolean verified, UserIdentityStatus status,
                                           long version) {
        UserIdentity user = new UserIdentity(id, email, hashedPassword, name, createdAt);
        user.lastLoginAt = lastLoginAt;
        user.failedLoginAttempts = failedLoginAttempts;
        user.lockedUntil = lockedUntil;
        user.verified = verified;
        user.status = status;
        user.version = version;
        return user;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // setPassword — CORREGIDO: almacena el hash en el VO de dominio
    //
    // Por qué existe este método:
    // El hashing de contraseñas es responsabilidad de la infraestructura (PasswordEncoder).
    // El ApplicationService encripta la contraseña DESPUÉS de crear el agregado,
    // y usa este método para inyectarla de vuelta al dominio como HashedPassword.
    //
    // El método DEBE actualizar el campo hashedPassword — antes estaba vacío.
    // ─────────────────────────────────────────────────────────────────────────
    public void setPassword(String encodedPassword) {
        this.hashedPassword = HashedPassword.of(encodedPassword);
    }

    public UserIdentityId getId()              { return id; }
    public Email getEmail()                    { return email; }
    public HashedPassword getHashedPassword()  { return hashedPassword; }
    public UserIdentityName getName()          { return name; }
    public Instant getLastLoginAt()            { return lastLoginAt; }
    public boolean isVerified()                { return verified; }
    public Instant getCreatedAt()              { return createdAt; }
    public int getFailedLoginAttempts()        { return failedLoginAttempts; }
    public Instant getLockedUntil()            { return lockedUntil; }
    public long getVersion()                   { return version; }
    public UserIdentityStatus getStatus()      { return status; }
    public void setVersion(long version)       { this.version = version; }
}