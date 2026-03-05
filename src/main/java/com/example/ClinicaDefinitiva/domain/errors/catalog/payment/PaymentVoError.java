
package com.example.ClinicaDefinitiva.domain.errors.catalog.payment;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PaymentVoError implements ErrorCatalog {

    ERR_PAYMENT_ID_NULL(
            "RN-PAYMENT-VO-001",
            "error.payment.idNull",
            "El identificador del pago no puede ser nulo"
    ),

    ERR_PAYMENT_METHOD_NULL(
            "RN-PAYMENT-VO-002",
            "error.payment.methodNull",
            "El método de pago no puede ser nulo"
    ),

    ERR_PAYMENT_METHOD_INVALID(
            "RN-PAYMENT-VO-003",
            "error.payment.methodInvalid",
            "El método de pago especificado no es válido"
    ),

    ERR_PAYMENT_INVALID_TRANSITION(
            "RN-PAYMENT-VO-004",
            "error.payment.invalidTransition",
            "La transición de estado del pago no es válida"
    ),

    ERR_PAYMENT_TRANSACTION_REF_NULL(
            "RN-PAYMENT-VO-005",
            "error.payment.transactionRefNull",
            "La referencia de transacción no puede ser nula"
    ),

    ERR_PAYMENT_TRANSACTION_REF_TOO_LONG(
            "RN-PAYMENT-VO-006",
            "error.payment.transactionRefTooLong",
            "La referencia de transacción excede la longitud permitida"
    ),

    ERR_PAYMENT_PAYER_TYPE_NULL(
            "RN-PAYMENT-VO-007",
            "error.payment.payerTypeNull",
            "El tipo de pagador no puede ser nulo"
    ),

    ERR_PAYMENT_PAYER_NAME_NULL(
            "RN-PAYMENT-VO-008",
            "error.payment.payerNameNull",
            "El nombre del pagador no puede ser nulo"
    );



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
