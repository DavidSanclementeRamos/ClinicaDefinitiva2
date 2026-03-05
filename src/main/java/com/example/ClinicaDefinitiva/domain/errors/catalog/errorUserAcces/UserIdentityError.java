package com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

/**
 CATÁLOGOS DE ERROR CONSOLIDADOS - MÓDULO USERIDENTITY v1.0
 Convención de numeración:
 - Los números preservan trazabilidad histórica
 - Las reglas eliminadas se documentan con comentarios
 - Los nuevos errores continúan la numeración secuencial
 **/
public enum UserIdentityError implements ErrorCatalog {


    // si
ERR_USER_ACCOUNT_LOCKED(
        "RN-USER-001",
        "error.user.account.locked",
        "El usuario está bloqueado y no puede iniciar sesión"
),

// si
ERR_USER_NOT_VERIFIED(
        "RN-USER-002",
        "error.user.not.verified",
        "El usuario activo debe estar verificado para realizar acciones sensibles"
),

// si
ERR_USER_FAILED_ATTEMPTS_NOT_RESET(
        "RN-USER-003",
        "error.user.failed.attempts.not.reset",
        "El contador de intentos fallidos solo se reinicia en login exitoso"
),

// si
ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS(
        "RN-USER-004",
        "error.user.account.locked.failed.attempts",
        "La cuenta ha sido bloqueada debido a múltiples intentos fallidos de inicio de sesión"
),

// si
ERR_USER_INVALID_CREDENTIALS(
        "RN-USER-005",
        "error.user.invalid.credentials",
        "Las credenciales proporcionadas son inválidas"
),

// si
ERR_USER_ALREADY_SUSPENDED(
        "RN-USER-006",
        "error.user.already.suspended",
        "El usuario ya se encuentra suspendido"
),

// si
ERR_USER_SUSPENSION_REQUIRES_REASON(
        "RN-USER-007",
        "error.user.suspension.requires.reason",
        "La suspensión requiere una razón válida"
),

// si
ERR_USER_ALREADY_ACTIVE(
        "RN-USER-008",
        "error.user.already.active",
        "El usuario ya se encuentra activo"
),

// si
ERR_USER_NOT_FOUND(
        "RN-USER-009",
        "error.user.not.found",
        "El usuario no fue encontrado en el sistema"
),

// si
ERR_USER_DEACTIVATION_REASON_REQUIRED(
        "RN-USER-010",
        "error.user.deactivation.reason",
        "Debe especificarse una razón para desactivar al usuario"
);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    UserIdentityError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() { return code; }
    @Override
    public String getMessageKey() { return messageKey; }
    @Override
    public String getDefaultMessage() { return defaultMessage; }
}

