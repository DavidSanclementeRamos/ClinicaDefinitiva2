package com.example.ClinicaDefinitiva.domain.errors.catalog.authentication;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

public enum AuthenticationVoError implements ErrorCatalog {

    ERR_USER_ID_INVALID("RN-USER-VO-001","error.user.id.invalid","El Id de usuario no es válido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_DUPLICATE_EMAIL("RN-USER-VO-002","error.user.email.duplicate","El email debe ser único al crear usuario",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),
    ERR_USER_PASSWORD_HASH_NULL("RN-USER-VO-003","error.user.password.hash.null","El hash de la contraseña no puede ser null",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_PASSWORD_HASH_EMPTY("RN-USER-VO-004","error.user.password.hash.empty","El hash de la contraseña no puede estar vacío",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_NAME_NULL("RN-USER-VO-005","error.user.name.null","El nombre no puede ser null",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_NAME_EMPTY("RN-USER-VO-006","error.user.name.empty","El nombre no puede estar vacío",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_NAME_TOO_SHORT("RN-USER-VO-007","error.user.name.too.short","El nombre debe tener al menos 3 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_NAME_TOO_LONG("RN-USER-VO-008","error.user.name.too.long","El nombre no puede exceder 15 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_INACTIVE("RN-USER-VO-009","error.user.inactive","El usuario no se encuentra activo para realizar la operación",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),
    ERR_USER_STATUS_NULL("RN-USER-VO-010","error.user.status.null","El estado del usuario no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_INVALID_TRANSITION("RN-USER-VO-011","error.user.invalid.transition","La transición de estado del usuario no es válida",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    AuthenticationVoError(String code, String messageKey, String defaultMessage,
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