package com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import org.springframework.http.HttpStatus;

public enum AuthorizationVoError implements ErrorCatalog {

    ERR_ROL_ID_NULL("RN-AUTHORIZATION-VO-001","error.rol.id.null","El identificador del rol (rolId) no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_USER_ROL_ASSIGNMENT_ID_NULL("RN-AUTHORIZATION-VO-002","error.userRolAssignment.id.null","El identificador de la asignación (userRolAssignmentId) no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);



    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    AuthorizationVoError(String code, String messageKey, String defaultMessage,
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