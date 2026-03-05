package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum LedgerAccountError implements ErrorCatalog {
    //si
    ERR_ACCOUNT_INVALID_CODE_LENGTH("RN-LEDGERACCOUNT-001", "error.ledgerAccount.invalidCodeLength",
            "El código de la cuenta debe tener longitud válida (1, 2, 4, 6 u 8 dígitos)"),

    // si
   ERR_ACCOUNT_INVALID_CODE_FORMAT("RN-LEDGERACCOUNT-002", "error.ledgerAccount.invalidCodeFormat",
            "El código de la cuenta solo puede contener dígitos numéricos"),

    //si
    ERR_ACCOUNT_MISSING_NATURE("RN-LEDGERACCOUNT-003", "error.ledgerAccount.missingNature",
            "La naturaleza de la cuenta es obligatoria"),

    // si
    ERR_ACCOUNT_NOT_EDITABLE("RN-LEDGERACCOUNT-004", "error.ledgerAccount.notEditable",
            "La cuenta solo puede editarse si está activa"),

    // si
    ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON("RN-LEDGERACCOUNT-005", "error.ledgerAccount.inactivationRequiresReason",
            "La inactivación de la cuenta requiere un motivo obligatorio"),

     // si
    ERR_ACCOUNT_ALREADY_ACTIVE("RN-LEDGERACCOUNT-006", "error.ledgerAccount.alreadyActive",
            "La cuenta ya está activa"),

    
  
 
    // si
    ERR_ACCOUNT_REQUIRES_THIRD_PARTY("RN-LEDGERACCOUNT-007", "error.ledgerAccount.requiresThirdParty",
            "El movimiento debe cumplir requisitos de tercero si la cuenta lo requiere"),

    // si
    ERR_ACCOUNT_REQUIRES_DOCUMENT("RN-LEDGERACCOUNT-008", "error.ledgerAccount.requiresDocument",
            "El movimiento debe cumplir requisitos de documento si la cuenta lo requiere"),

      // si
    ERR_ACCOUNT_MISSING_CODE("RN-LEDGERACCOUNT-008", "error.ledgerAccount.missingCode",
            "El código de la cuenta es obligatorio")

   ;

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    LedgerAccountError(String code, String messageKey, String defaultMessage) {
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
