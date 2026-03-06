package com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;


public enum UserIdentityError implements ErrorCatalog {

    ERR_USER_ACCOUNT_LOCKED(
            "RN-USER-001","error.user.account.locked",
            "El usuario está bloqueado y no puede iniciar sesión",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_USER_NOT_VERIFIED(
            "RN-USER-002","error.user.not.verified",
            "El usuario activo debe estar verificado para realizar acciones sensibles",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_USER_FAILED_ATTEMPTS_NOT_RESET(
            "RN-USER-003","error.user.failed.attempts.not.reset",
            "El contador de intentos fallidos solo se reinicia en login exitoso",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS(
            "RN-USER-004","error.user.account.locked.failed.attempts",
            "La cuenta ha sido bloqueada debido a múltiples intentos fallidos de inicio de sesión",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_USER_INVALID_CREDENTIALS(
            "RN-USER-005","error.user.invalid.credentials",
            "Las credenciales proporcionadas son inválidas",
            HttpStatus.UNAUTHORIZED, ErrorSeverity.ERROR),

    ERR_USER_ALREADY_SUSPENDED(
            "RN-USER-006","error.user.already.suspended",
            "El usuario ya se encuentra suspendido",
            HttpStatus.CONFLICT, ErrorSeverity.WARN),

    ERR_USER_SUSPENSION_REQUIRES_REASON(
            "RN-USER-007","error.user.suspension.requires.reason",
            "La suspensión requiere una razón válida",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_USER_ALREADY_ACTIVE(
            "RN-USER-008","error.user.already.active",
            "El usuario ya se encuentra activo",
            HttpStatus.CONFLICT, ErrorSeverity.WARN),

    ERR_USER_NOT_FOUND(
            "RN-USER-009","error.user.not.found",
            "El usuario no fue encontrado en el sistema",
            HttpStatus.NOT_FOUND, ErrorSeverity.ERROR),

    ERR_USER_DEACTIVATION_REASON_REQUIRED(
            "RN-USER-010","error.user.deactivation.reason",
            "Debe especificarse una razón para desactivar al usuario",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_USER_NOT_ELIGIBLE(
            "RN-USER-011","error.user.not.eligible",
            "El usuario no cumple con los criterios de elegibilidad requeridos",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),
    
    ERR_USER_INACTIVE(
    "RN-USER-012",
    "error.user.inactive",
    "El usuario se encuentra inactivo y no puede realizar la operación solicitada",
    HttpStatus.FORBIDDEN,
    ErrorSeverity.ERROR
);
    

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    UserIdentityError(String code, String messageKey, String defaultMessage,
                      HttpStatus suggestedHttpStatus, ErrorSeverity severity) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
        this.suggestedHttpStatus = suggestedHttpStatus;
        this.severity = severity;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessageKey() { return messageKey; }
    @Override public String getDefaultMessage() { return defaultMessage; }
    @Override public HttpStatus getSuggestedHttpStatus() { return suggestedHttpStatus; }
    @Override public ErrorSeverity getSeverity() { return severity; }
}