package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum OpeningBalanceError implements ErrorCatalog {

   

    // si
    ERR_OPENING_BALANCE_MISSING_DATE("RN-OPENINGBALANCE-003", "error.openingBalance.missingDate",
            "La fecha es obligatoria");

   

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
