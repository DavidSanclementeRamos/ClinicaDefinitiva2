
package com.example.ClinicaDefinitiva.domain.errors.catalog.payment;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PaymentError implements ErrorCatalog  {
    ERR_PAYMENT_TRANSACTION_REF_REQUIRED("","",""),
    ERR_PAYMENT_NOT_CASH("","",""),
    ERR_PAYMENT_CANCELLATION_REQUIRES_REASON("","",""),
    ERR_PAYMENT_REFUND_INVALID_AMOUNT("","",""),
    ERR_PAYMENT_REFUND_EXCEEDS_AMOUNT("","",""),
    ERR_PAYMENT_AMOUNT_INVALID("","",""),
    ERR_PAYMENT_INSTITUTIONAL_REQUIRES_PAYER("","",""),
    ERR_PAYMENT_NOT_PENDING("","",""),
    ERR_PAYMENT_NOT_CONFIRMED("","","")

    ;

 private final String code;
    private final String messageKey;
    private final String defaultMessage;

    PaymentError(String code, String messageKey, String defaultMessage) {
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
