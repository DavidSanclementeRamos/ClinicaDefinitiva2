package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PediatricError implements ErrorCatalog {

    // RN-PEDIATRIC-001
    ERR_PEDIATRIC_INVALID_AGE_RANGE(
            "RN-PEDIATRIC-001",
            "error.pediatric.age.invalid",
            "El rango de edad debe especificar edades pediátricas válidas (0-18 años)"
    ),

   
 
    // RN-PEDIATRIC-002
    ERR_PEDIATRIC_MATERIALS_TOO_SHORT(
            "RN-PEDIATRIC-002",
            "error.pediatric.materials.short",
            "Materiales pediátricos deben describirse adecuadamente (mínimo 5 caracteres)"
    ),

    ;

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    PediatricError(String code, String messageKey, String defaultMessage) {
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