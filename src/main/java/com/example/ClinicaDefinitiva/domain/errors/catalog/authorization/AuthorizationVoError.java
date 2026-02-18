package com.example.ClinicaDefinitiva.domain.errors.catalog.authorization;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum AuthorizationVoError implements ErrorCatalog {

    ERR_ROL_ID_NULL(
            "RN-AUTHORIZATION-001",
            "error.rol.id.null",
            "El identificador del rol (rolId) no puede ser nulo"
    ),
    ERR_USER_ROL_ASSIGNMENT_ID_NULL(
            "RN-AUTHORIZATION-002",
            "error.userRolAssignment.id.null",
            "El identificador de la asignación (userRolAssignmentId) no puede ser nulo"
    ),;


    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    AuthorizationVoError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessageKey() { return messageKey; }
    @Override public String getDefaultMessage() { return defaultMessage; }
}
