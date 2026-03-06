
package com.example.ClinicaDefinitiva.domain.errors.catalog.payment;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
public enum PaymentError implements ErrorCatalog {

    ERR_PAYMENT_TRANSACTION_REF_REQUIRED(
            "RN-PAYMENT-001","error.payment.transactionRefRequired",
            "La transacción de pago requiere referencia",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_PAYMENT_NOT_CASH(
            "RN-PAYMENT-002","error.payment.notCash",
            "El pago debe ser en efectivo",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_PAYMENT_CANCELLATION_REQUIRES_REASON(
            "RN-PAYMENT-003","error.payment.cancellationRequiresReason",
            "La cancelación del pago requiere un motivo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_PAYMENT_REFUND_INVALID_AMOUNT(
            "RN-PAYMENT-004","error.payment.refundInvalidAmount",
            "El monto de reembolso no es válido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_PAYMENT_REFUND_EXCEEDS_AMOUNT(
            "RN-PAYMENT-005","error.payment.refundExceedsAmount",
            "El reembolso excede el monto original",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_PAYMENT_AMOUNT_INVALID(
            "RN-PAYMENT-006","error.payment.amountInvalid",
            "El monto del pago es inválido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_PAYMENT_INSTITUTIONAL_REQUIRES_PAYER(
            "RN-PAYMENT-007","error.payment.institutionalRequiresPayer",
            "El pago institucional requiere un pagador definido",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_PAYMENT_NOT_PENDING(
            "RN-PAYMENT-008","error.payment.notPending",
            "El pago no está en estado pendiente",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_PAYMENT_NOT_CONFIRMED(
            "RN-PAYMENT-009","error.payment.notConfirmed",
            "El pago no está confirmado",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    PaymentError(String code, String messageKey, String defaultMessage,
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