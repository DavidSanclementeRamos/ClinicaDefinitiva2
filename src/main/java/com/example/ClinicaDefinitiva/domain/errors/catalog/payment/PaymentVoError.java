
package com.example.ClinicaDefinitiva.domain.errors.catalog.payment;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PaymentVoError implements ErrorCatalog   {
    ERR_PAYMENT_ID_NULL("","",""),
    ERR_PAYMENT_METHOD_NULL("","",""),
    ERR_PAYMENT_METHOD_INVALID("","",""),
    ERR_PAYMENT_INVALID_TRANSITION("","",""),
    ERR_PAYMENT_TRANSACTION_REF_NULL("","",""),
    ERR_PAYMENT_TRANSACTION_REF_TOO_LONG("","",""),
    ERR_PAYMENT_PAYER_TYPE_NULL("","",""),
    ERR_PAYMENT_PAYER_NAME_NULL("","","")
    
    ;
    
     private final String code;
    private final String messageKey;
    private final String defaultMessage;

    PaymentVoError(String code, String messageKey, String defaultMessage) {
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
