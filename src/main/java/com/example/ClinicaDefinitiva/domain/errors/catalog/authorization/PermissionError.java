package com.example.ClinicaDefinitiva.domain.errors.catalog.authorization;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PermissionError implements ErrorCatalog {

    // si
    ERR_PERMISSION_UNAUTHORIZED_ADD(
            "RN-PERMISSION-001",
            "error.permission.add.unauthorized",
            "Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede agregar permisos"
    ),
    ERR_PERMISSION_ALREADY_EXISTS(
            "RN-PERMISSION-002",
            "error.permission.already.exists",
            "No puede agregar permiso duplicado a un rol"
    ),
    ERR_PERMISSION_CANNOT_REMOVE_LAST(
            "RN-PERMISSION-003",
            "error.permission.remove.last",
            "No puede remover el último permiso de un rol editable"
    ),
    // si
    ERR_PERMISSION_UNAUTHORIZED_REMOVE(
            "RN-PERMISSION-004",
            "error.permission.remove.unauthorized",
            "Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede remover permisos"
    ),
    ERR_PERMISSION_SET_EMPTY(
            "RN-PERMISSION-005",
            "error.permission.set.empty",
            "Al reemplazar permisos, debe haber al menos 1 permiso nuevo"
    ),
    // si
    ERR_PERMISSION_UNAUTHORIZED_SET(
            "RN-PERMISSION-006",
            "error.permission.set.unauthorized",
            "Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede reemplazar permisos"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    PermissionError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessageKey() { return messageKey; }
    @Override public String getDefaultMessage() { return defaultMessage; }}

