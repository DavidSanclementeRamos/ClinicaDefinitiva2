package com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum VoAccesError implements ErrorCatalog {
   ERR_USER_ID_INVALID(
        "RN-USER-001",
        "error.user.id.invalid",
        "El Id de usuario no es válido"
),

ERR_USER_DUPLICATE_EMAIL(
        "RN-USER-002",
        "error.user.email.duplicate",
        "El email debe ser único al crear usuario"
),

ERR_USER_PASSWORD_HASH_NULL(
        "RN-USER-003",
        "error.user.password.hash.null",
        "El hash de la contraseña no puede ser null"
),

ERR_USER_PASSWORD_HASH_EMPTY(
        "RN-USER-004",
        "error.user.password.hash.empty",
        "El hash de la contraseña no puede estar vacío"
),

ERR_USER_NAME_NULL(
        "RN-USER-005",
        "error.user.name.null",
        "El nombre no puede ser null"
),

ERR_USER_NAME_EMPTY(
        "RN-USER-006",
        "error.user.name.empty",
        "El nombre no puede estar vacío"
),

ERR_USER_NAME_TOO_SHORT(
        "RN-USER-007",
        "error.user.name.too.short",
        "El nombre debe tener al menos 3 caracteres"
),

ERR_USER_NAME_TOO_LONG(
        "RN-USER-008",
        "error.user.name.too.long",
        "El nombre no puede exceder 15 caracteres"
),

ERR_USER_INACTIVE(
        "RN-USER-009",
        "error.user.inactive",
        "El usuario no se encuentra activo para realizar la operación"
),

ERR_USER_STATUS_NULL(
        "RN-USER-010",
        "error.user.status.null",
        "El estado del usuario no puede ser nulo"
),

ERR_USER_INVALID_TRANSITION(
        "RN-USER-011",
        "error.user.invalid.transition",
        "La transición de estado del usuario no es válida"
);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    VoAccesError(String code, String messageKey, String defaultMessage) {
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
