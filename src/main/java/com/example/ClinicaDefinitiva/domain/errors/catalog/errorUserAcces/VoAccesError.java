package com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum VoAccesError implements ErrorCatalog {
    ERR_USER_ID_INVALID(
        "RN-USER-000",
        "error.user.id.invalid",
        "El Id de usuario no es válido"
    ),

    ERR_USER_DUPLICATE_EMAIL(
     "RN-USER-001",
     "error.user.email.duplicate",
     "El email debe ser único al crear usuario"
     ),
    ERR_USER_PASSWORD_HASH_NULL(
            "RN-PASS-001",
            "error.user.password.hash.null",
            "El hash de la contraseña no puede ser null"
    ),

    ERR_USER_PASSWORD_HASH_EMPTY(
            "RN-PASS-002",
            "error.user.password.hash.empty",
            "El hash de la contraseña no puede estar vacío"
    ),


    // Catálogo de errores para VO Email
    ERR_EMAIL_NULL(
            "RN-EMAIL-001",
            "error.email.null",
            "El email no puede ser null"
    ),

    ERR_EMAIL_EMPTY(
            "RN-EMAIL-002",
            "error.email.empty",
            "El email no puede estar vacío"
    ),

    ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN(
            "RN-EMAIL-003",
            "error.email.missing.parts",
            "El email debe contener una parte local y un dominio"
    ),

    ERR_EMAIL_LENGTH_EXCEEDED(
            "RN-EMAIL-004",
            "error.email.length.exceeded",
            "La longitud del email excede el máximo permitido de 254 caracteres"
    ),

    ERR_EMAIL_LOCAL_LENGTH_EXCEEDED(
            "RN-EMAIL-005",
            "error.email.local.length.exceeded",
            "La parte local del email excede 64 caracteres"
    ),

    ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED(
            "RN-EMAIL-006",
            "error.email.domain.length.exceeded",
            "El dominio del email excede 253 caracteres"
    ),

    ERR_EMAIL_INVALID_FORMAT(
            "RN-EMAIL-007",
            "error.email.invalid.format",
            "El formato del email no es válido"
    ),

    ERR_EMAIL_DOMAIN_INVALID_DASH(
            "RN-EMAIL-008",
            "error.email.domain.invalid.dash",
            "El dominio no puede iniciar ni terminar con guion"
    ),

    ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS(
            "RN-EMAIL-009",
            "error.email.domain.consecutive.dots",
            "El dominio no puede contener puntos consecutivos"
    ),

    ERR_USER_NAME_NULL(
            "RN-NAME-001",
            "error.user.name.null",
            "El nombre no puede ser null"
    ),

    ERR_USER_NAME_EMPTY(
            "RN-NAME-002",
            "error.user.name.empty",
            "El nombre no puede estar vacío"
    ),

    ERR_USER_NAME_TOO_SHORT(
            "RN-NAME-003",
            "error.user.name.too.short",
            "El nombre debe tener al menos 3 caracteres"
    ),

    ERR_USER_NAME_TOO_LONG(
            "RN-NAME-004",
            "error.user.name.too.long",
            "El nombre no puede exceder 15 caracteres"
    ),
    ERR_USER_INACTIVE(
            "RN-USER-001",
            "error.user.inactive",
            "El usuario no se encuentra activo para realizar la operación"
    ), ERR_USER_STATUS_NULL("","" ,"" );


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
