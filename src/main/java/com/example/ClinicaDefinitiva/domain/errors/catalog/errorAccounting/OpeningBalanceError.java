package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum OpeningBalanceError implements ErrorCatalog {

    ERR_OPENING_BALANCE_INVALID_AMOUNT("RN-OPENINGBALANCE-001", "error.openingBalance.invalidAmount",
            "El monto debe ser mayor a cero"),

    ERR_OPENING_BALANCE_MISSING_AMOUNT("RN-OPENINGBALANCE-002", "error.openingBalance.missingAmount",
            "El monto es obligatorio"),

    ERR_OPENING_BALANCE_MISSING_DATE("RN-OPENINGBALANCE-003", "error.openingBalance.missingDate",
            "La fecha es obligatoria"),

    ERR_OPENING_BALANCE_MISSING_ACCOUNT("RN-OPENINGBALANCE-004", "error.openingBalance.missingAccount",
            "Debe tener cuenta contable válida"),

    ERR_OPENING_BALANCE_MISSING_COMPANY("RN-OPENINGBALANCE-005", "error.openingBalance.missingCompany",
            "Debe tener compañía válida"),

    ERR_OPENING_BALANCE_IMMUTABLE("RN-OPENINGBALANCE-006", "error.openingBalance.immutable",
            "No permite edición una vez registrado (inmutable)"),

    ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY("RN-OPENINGBALANCE-007", "error.openingBalance.requiresThirdParty",
            "Si la cuenta requiere tercero, debe incluir tercero"),

    ERR_OPENING_BALANCE_DUPLICATE("RN-OPENINGBALANCE-008", "error.openingBalance.duplicate",
            "No puede registrarse saldo duplicado para misma cuenta/tercero/período");

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    OpeningBalanceError(String code, String messageKey, String defaultMessage) {
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
