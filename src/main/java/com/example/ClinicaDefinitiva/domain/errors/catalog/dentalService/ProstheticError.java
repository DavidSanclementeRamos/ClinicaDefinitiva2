package com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;


public enum ProstheticError implements ErrorCatalog {

    ERR_PROSTHETIC_MISSING_TYPE(
            "RN-PROSTHETIC-001","error.prosthetic.type.missing",
            "Debe especificar si la prótesis es fija o removible",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_PROSTHETIC_INVALID_UNITS(
            "RN-PROSTHETIC-002","error.prosthetic.units.invalid",
            "El número de unidades debe ser mayor o igual a 0",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_PROSTHETIC_EXCESSIVE_UNITS(
            "RN-PROSTHETIC-003","error.prosthetic.units.excessive",
            "Prótesis removibles no pueden tener más de 14 unidades por arcada",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_PROSTHETIC_INVALID_TYPE_VALUE(
            "RN-PROSTHETIC-004","error.prosthetic.type.value",
            "El tipo debe ser FIXED (fija) o REMOVABLE (removible)",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    ProstheticError(String code, String messageKey, String defaultMessage,
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