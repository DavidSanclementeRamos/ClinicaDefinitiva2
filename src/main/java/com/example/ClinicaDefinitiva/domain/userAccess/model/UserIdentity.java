package com.example.ClinicaDefinitiva.domain.userAccess.model;

import com.example.ClinicaDefinitiva.domain.Email;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.VoAccesError;
import com.example.ClinicaDefinitiva.domain.service.UserDeactivationPolicy;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.HashedPassword;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserName;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.Severity;

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
    private final UserId id;
    private Email email;
    private HashedPassword hashedPassword;
    private UserName name;
    private final Instant createdAt;
    private Instant lastLoginAt;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    private boolean verified;
    private UserStatus status;
    private long version;

    private UserIdentity(UserId id, Email email, HashedPassword hashedPassword, UserName name, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.createdAt = createdAt;
        this.status = UserStatus.of(UserStatus.State.ACTIVE);
        this.verified = false;
    }

    public static UserIdentity register(UserId id, Email email, HashedPassword hashedPassword, UserName name, Instant now) {
        return new UserIdentity(id, email, hashedPassword, name, now);
    }


    /**
     * Registra un login exitoso.
     * - Resetea contador de intentos fallidos
     * - Actualiza timestamp del último login
     * - Falla si la cuenta está bloqueada
     */
    public Outcome<UserIdentity> recordSuccessfulLogin(Instant now) {
        if (isLocked(now)) return Outcome.fail(new OutcomeDetail(UserIdentityError.ERR_USER_FAILED_ATTEMPTS_NOT_RESET, Severity.WARNING, Category.TECNICO));
        this.failedLoginAttempts = 0;
        this.lastLoginAt = now;
        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public Outcome<UserIdentity> editUserData(UserName newName, Email newEmail, HashedPassword newPassword, Instant now) {

        // Verificar elegibilidad primero
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
        if (isLocked(now)) return Outcome.fail(new OutcomeDetail(UserIdentityError.ERR_USER_ACCOUNT_LOCKED, Severity.WARNING, Category.TECNICO));
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= maxAttempts) {
            this.lockedUntil = now.plus(lockDuration);
            return Outcome.fail(new OutcomeDetail(UserIdentityError.ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS, Severity.ERROR, Category.TECNICO));
        }
        return Outcome.fail(new OutcomeDetail(UserIdentityError.ERR_USER_INVALID_CREDENTIALS, Severity.INFO, Category.TECNICO));
    }

    public boolean isLocked(Instant now) {
        return lockedUntil != null && now.isBefore(lockedUntil);
    }

    public Outcome<UserIdentity> verify() {
        if (this.verified) return Outcome.fail(new OutcomeDetail(UserIdentityError.ERR_USER_NOT_VERIFIED, Severity.INFO, Category.TECNICO));
        this.verified = true;
        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public Outcome<UserIdentity> deactivate(UserDeactivationPolicy policy, Instant now) {

        // Verificar elegibilidad primero
              Outcome<UserIdentity> eligibility = canPerformSensitiveAction(now);
              if (!eligibility.isSuccess()) {
                  return eligibility;
              }

        if (!policy.canDeactivate(this)) {
            return Outcome.fail(
                    new OutcomeDetail(
                            UserIdentityError.ERR_USER_DEACTIVATION_CONSTRAINTS,
                            Severity.ERROR,
                            Category.TECNICO));
        }
        this.status = UserStatus.of(UserStatus.State.INACTIVE);
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
                    Severity.ERROR,
                    Category.TECNICO
            ));
        }

        if (isLocked(now)) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                    Severity.ERROR,
                    Category.TECNICO
            ));
        }

        if (status.getState() != UserStatus.State.ACTIVE) {
            return Outcome.fail(new OutcomeDetail(
                    VoAccesError.ERR_USER_INACTIVE,
                    Severity.ERROR,
                    Category.TECNICO
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
        if (status.getState() == UserStatus.State.SUSPENDED) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ALREADY_SUSPENDED,
                    Severity.INFO,
                    Category.TECNICO
            ));
        }
        // Validar que haya una razón
        if (reason == null || reason.isBlank()) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_SUSPENSION_REQUIRES_REASON,
                    Severity.ERROR,
                    Category.TECNICO
            ));
        }

        this.status = UserStatus.of(UserStatus.State.SUSPENDED);

        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }


    /**
     * Reactiva un usuario previamente suspendido o inactivo.
     *
     * @param now Instante actual
     * @return Outcome indicando éxito o fallo
     */
    public Outcome<UserIdentity> reactivate(Instant now) {
        if (status.getState() == UserStatus.State.ACTIVE) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_ALREADY_ACTIVE,
                    Severity.INFO,
                    Category.TECNICO
            ));
        }

        // Verificar que esté verificado para reactivar
        if (!verified) {
            return Outcome.fail(new OutcomeDetail(
                    UserIdentityError.ERR_USER_NOT_VERIFIED,
                    Severity.ERROR,
                    Category.TECNICO
            ));
        }

        // Limpiar bloqueos al reactivar
        this.status = UserStatus.of(UserStatus.State.ACTIVE);
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;

        return Outcome.ok(new UserIdentity(id, email, hashedPassword, name, createdAt));
    }

    public UserId getId() { return id; }
    public Email getEmail() { return email; }
    public HashedPassword getHashedPassword() { return hashedPassword; }
    public UserName getName() { return name; }
    public Instant getLastLoginAt() { return lastLoginAt; }

    public void setVersion(long version) {
        this.version = version;
    }
}
