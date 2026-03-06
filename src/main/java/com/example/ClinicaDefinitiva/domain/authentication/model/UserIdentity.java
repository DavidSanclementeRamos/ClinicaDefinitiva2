package com.example.ClinicaDefinitiva.domain.authentication.model;

import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
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


/**
 * Agregado UserIdentity - Representa la identidad de un usuario en el sistema.
 *
 * Responsabilidades:
 * - Gestionar autenticación (intentos fallidos, bloqueos)
 * - Controlar verificación de cuenta
 * - Mantener estado operativo (activo/inactivo/suspendido)
 * - Validar elegibilidad para acciones sensibles
 * - Proteger invariantes de negocio relacionados con acceso
 *
 * Invariantes:
 * - Email debe ser único (validado en repositorio)
 * - Password siempre es HashedPassword
 * - Usuario activo debe estar verificado para acciones sensibles
 * - Usuario bloqueado no puede iniciar sesión
 * - Contador de intentos fallidos solo se resetea en login exitoso
 */
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

    private UserIdentity(UserIdentityId id, Email email, HashedPassword hashedPassword, UserIdentityName name, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.createdAt = createdAt;
        this.status = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        this.verified = false;
    }

    public static UserIdentity register(Email email, HashedPassword hashedPassword, UserIdentityName name, Instant now) {
        return new UserIdentity(null, email, hashedPassword, name, now);
    }


    /**
     * Registra un login exitoso.
     * - Resetea contador de intentos fallidos
     * - Actualiza timestamp del último login
     * - Falla si la cuenta está bloqueada
     */
    public Outcome<UserIdentity> recordSuccessfulLogin(Instant now) {
        if (isLocked(now)) return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_FAILED_ATTEMPTS_NOT_RESET,
                ErrorSeverity.WARN,
                Category.TECNICO,
                EntityContext.USER_IDENTITY));
        this.failedLoginAttempts = 0;
        this.lastLoginAt = now;
        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public Outcome<UserIdentity> update(UserIdentityName newName, Email newEmail, HashedPassword newPassword, Instant now) {

        Outcome<UserIdentity> eligibility = canPerformSensitiveAction(now);
        if (!eligibility.isSuccess()) {
            return eligibility;
        }


        this.name = newName;
        this.email = newEmail;
        this.hashedPassword = newPassword;

        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    /**
     * Registra un intento de login fallido.
     * - Incrementa contador de intentos
     * - Bloquea cuenta si supera máximo de intentos
     * - Falla si ya está bloqueada
     */
    public Outcome<UserIdentity> recordFailedLogin(Instant now, int maxAttempts, Duration lockDuration) {
        if (isLocked(now)) return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                ErrorSeverity.WARN, Category.TECNICO,EntityContext.USER_IDENTITY));
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= maxAttempts) {
            this.lockedUntil = now.plus(lockDuration);
            return Outcome.fail(new OutcomeDetail(UserIdentityError.ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS, ErrorSeverity.ERROR, Category.TECNICO,EntityContext.USER_IDENTITY));
        }
        return Outcome.fail(new OutcomeDetail(UserIdentityError.ERR_USER_INVALID_CREDENTIALS, ErrorSeverity.INFO, Category.TECNICO,EntityContext.USER_IDENTITY));
    }

    public boolean isLocked(Instant now) {
        return lockedUntil != null && now.isBefore(lockedUntil);
    }

    public Outcome<UserIdentity> verify() {
        if (this.verified) return Outcome.fail(new OutcomeDetail(UserIdentityError.ERR_USER_NOT_VERIFIED, ErrorSeverity.INFO, Category.TECNICO,EntityContext.USER_IDENTITY));
        this.verified = true;
        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public Outcome<UserIdentity> deactivate(UserDeactivationPolicy policy, Instant now, String reason) {

        Outcome<UserIdentity> eligibility = canPerformSensitiveAction(now);
        if (!eligibility.isSuccess()) {
            return eligibility;
        }

        if (reason == null || reason.isBlank()) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_DEACTIVATION_REASON_REQUIRED,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    EntityContext.USER_IDENTITY));
        }

        Outcome<Void> validation = policy.validate(this);
        if (validation.isFailure()) {
            return Outcome.fail(validation.getDetalles());
        }

        this.status = status.transitionTo(UserIdentityStatus.Status.INACTIVE);

        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }



    /**
     * Verifica si el usuario puede realizar acciones sensibles.
     * Valida tres condiciones:
     * 1. Usuario debe estar verificado
     * 2. Usuario no debe estar bloqueado
     * 3. Usuario debe estar activo
     *
     * @param now Instante actual para verificar bloqueo
     * @return Outcome.ok() si elegible, Outcome con error específico si no
     */
    public Outcome<UserIdentity> canPerformSensitiveAction(Instant now) {
        if (!verified) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_NOT_VERIFIED,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    EntityContext.USER_IDENTITY
            ));
        }

        if (isLocked(now)) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    EntityContext.USER_IDENTITY
            ));
        }

        if (status.getValue()!= UserIdentityStatus.Status.ACTIVE) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_USER_INACTIVE,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    EntityContext.USER_IDENTITY
            ));
        }

        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    /**
     * Suspende temporalmente el usuario.
     * A diferencia de desactivación, la suspensión es reversible.
     *
     * @param reason Razón de la suspensión (para auditoría)
     * @param now Instante actual
     * @return Outcome indicando éxito o fallo
     */
    public Outcome<UserIdentity> suspend(String reason, Instant now) {
        if (!status.canTransitionTo(UserIdentityStatus.Status.SUSPENDED)) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ALREADY_SUSPENDED,
                    ErrorSeverity.INFO,
                    Category.TECNICO,
                    EntityContext.USER_IDENTITY
            ));
        }

        if (reason == null || reason.isBlank()) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_SUSPENSION_REQUIRES_REASON,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    EntityContext.USER_IDENTITY
            ));
        }

        this.status = status.transitionTo(UserIdentityStatus.Status.SUSPENDED);


        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }


    /**
     * Reactiva un usuario previamente suspendido o inactivo.
     *
     * @param now Instante actual
     * @return Outcome indicando éxito o fallo
     */
    public Outcome<UserIdentity> reactivate(Instant now) {
        if (!status.canTransitionTo(UserIdentityStatus.Status.ACTIVE)) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ALREADY_ACTIVE,
                    ErrorSeverity.INFO,
                    Category.TECNICO,
                    EntityContext.USER_IDENTITY
            ));
        }

        if (!verified) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_NOT_VERIFIED,
                    ErrorSeverity.ERROR,
                    Category.TECNICO,
                    EntityContext.USER_IDENTITY
            ));
        }

        this.status = status.transitionTo( UserIdentityStatus.Status.ACTIVE);
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;

        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public UserIdentityId getId() { return id; }
    public Email getEmail() { return email; }
    public HashedPassword getHashedPassword() { return hashedPassword; }
    public UserIdentityName getName() { return name; }
    public Instant getLastLoginAt() { return lastLoginAt; }

    public boolean isVerified() {
        return verified;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public long getVersion() {
        return version;
    }

    public UserIdentityStatus getStatus() {
        return status;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setPassword(String encodedPassword) {

    }
}
