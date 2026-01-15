package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum CompanyError implements ErrorCatalog {
    ERR_COMPANY_MISSING_TAX_ID("RN-COMPANY-001", "error.company.missingTaxId",
            "La empresa debe tener NIT único y válido"),

    ERR_COMPANY_FUTURE_INCORPORATION_DATE("RN-COMPANY-002", "error.company.futureIncorporationDate",
            "La fecha de constitución no puede ser futura"),

    ERR_COMPANY_NOT_EDITABLE("RN-COMPANY-003", "error.company.notEditable",
            "La empresa solo puede editarse si está en estado ACTIVE o SUSPENDED"),

    ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY("RN-COMPANY-004", "error.company.cannotReactivateDirectly",
            "Una empresa inactiva no puede reactivarse sin proceso formal"),

    ERR_COMPANY_MISSING_PERSON_TYPE("RN-COMPANY-005", "error.company.missingPersonType",
            "El tipo de persona es obligatorio"),

    ERR_COMPANY_CANNOT_MODIFY_TAX_ID("RN-COMPANY-006", "error.company.cannotModifyTaxId",
            "El NIT no puede modificarse una vez registrado"),

    ERR_COMPANY_MISSING_INCORPORATION_DATE("RN-COMPANY-007", "error.company.missingIncorporationDate",
            "La fecha de constitución es obligatoria"),

    ERR_COMPANY_MISSING_CONTACT("RN-COMPANY-008", "error.company.missingContact",
            "Debe registrarse al menos un medio de contacto válido (email o teléfono)"),

    ERR_COMPANY_INVALID_INCORPORATION_DATE("RN-COMPANY-009", "error.company.invalidIncorporationDate",
            "La fecha de constitución no es válida (no puede ser anterior a 1800)");

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    CompanyError(String code, String messageKey, String defaultMessage) {
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
