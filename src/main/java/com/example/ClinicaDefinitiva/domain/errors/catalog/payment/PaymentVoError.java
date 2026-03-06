
package com.example.ClinicaDefinitiva.domain.errors.catalog.payment;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

public enum PaymentVoError implements ErrorCatalog {

    ERR_PAYMENT_ID_NULL("RN-PAYMENT-VO-001","error.payment.idNull","El identificador del pago no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PAYMENT_METHOD_NULL("RN-PAYMENT-VO-002","error.payment.methodNull","El método de pago no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PAYMENT_METHOD_INVALID("RN-PAYMENT-VO-003","error.payment.methodInvalid","El método de pago especificado no es válido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PAYMENT_INVALID_TRANSITION("RN-PAYMENT-VO-004","error.payment.invalidTransition","La transición de estado del pago no es válida",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),
    ERR_PAYMENT_TRANSACTION_REF_NULL("RN-PAYMENT-VO-005","error.payment.transactionRefNull","La referencia de transacción no puede ser nula",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PAYMENT_TRANSACTION_REF_TOO_LONG("RN-PAYMENT-VO-006","error.payment.transactionRefTooLong","La referencia de transacción excede la longitud permitida",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PAYMENT_PAYER_TYPE_NULL("RN-PAYMENT-VO-007","error.payment.payerTypeNull","El tipo de pagador no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PAYMENT_PAYER_NAME_NULL("RN-PAYMENT-VO-008","error.payment.payerNameNull","El nombre del pagador no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    PaymentVoError(String code, String messageKey, String defaultMessage,
                   HttpStatus suggestedHttpStatus, ErrorSeverity severity) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
        this.suggestedHttpStatus = suggestedHttpStatus;
        this.severity = severity;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessageKey() { return messageKey; }
    @Override public String getDefaultMessage() { return defaultMessage; }
    @Override public HttpStatus getSuggestedHttpStatus() { return suggestedHttpStatus; }
    @Override public ErrorSeverity getSeverity() { return severity; }
}