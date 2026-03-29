package com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PermissionError implements ErrorCatalog {

    ERR_PERMISSION_UNAUTHORIZED_ADD(
            "RN-PERMISSION-001","error.permission.add.unauthorized",
            "Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede agregar permisos",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_PERMISSION_ALREADY_EXISTS(
            "RN-PERMISSION-002","error.permission.already.exists",
            "No puede agregar permiso duplicado a un rol",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_PERMISSION_CANNOT_REMOVE_LAST(
            "RN-PERMISSION-003","error.permission.remove.last",
            "No puede remover el último permiso de un rol editable",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_PERMISSION_UNAUTHORIZED_REMOVE(
            "RN-PERMISSION-004","error.permission.remove.unauthorized",
            "Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede remover permisos",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_PERMISSION_SET_EMPTY(
            "RN-PERMISSION-005","error.permission.set.empty",
            "Al reemplazar permisos, debe haber al menos 1 permiso nuevo",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_PERMISSION_UNAUTHORIZED_SET(
            "RN-PERMISSION-006","error.permission.set.unauthorized",
            "Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede reemplazar permisos",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    PermissionError(String code, String messageKey, String defaultMessage,
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