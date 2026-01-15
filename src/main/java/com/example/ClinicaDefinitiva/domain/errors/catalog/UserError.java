package com.example.ClinicaDefinitiva.domain.errors.catalog;

public enum UserError implements ErrorCatalog {
    ERR_USER_INACTIVE(
            "RN-USER-001",
            "error.user.inactive",
            "No se puede realizar la operación porque el usuario está inactivo"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    UserError(String code, String messageKey, String defaultMessage) {
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
