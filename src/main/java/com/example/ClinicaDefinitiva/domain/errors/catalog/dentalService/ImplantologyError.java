package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ImplantologyError implements ErrorCatalog {

    ERR_IMPLANTOLOGY_INVALID_HEALING_TIME(
            "RN-IMPLANTOLOGY-001","error.implantology.healing.invalid",
            "El tiempo de cicatrización debe estar entre 2 y 12 meses",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH(
            "RN-IMPLANTOLOGY-002","error.implantology.bonegraft.healing",
            "Con injerto óseo, el tiempo de cicatrización mínimo es 4 meses",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME(
            "RN-IMPLANTOLOGY-003","error.implantology.healing.negative",
            "El tiempo de cicatrización no puede ser negativo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE(
            "RN-IMPLANTOLOGY-004","error.implantology.site.invalid",
            "El sitio de colocación debe tener formato válido si se especifica",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    ImplantologyError(String code, String messageKey, String defaultMessage,
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