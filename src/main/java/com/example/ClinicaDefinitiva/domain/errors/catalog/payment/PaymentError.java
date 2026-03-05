
package com.example.ClinicaDefinitiva.domain.errors.catalog.payment;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PaymentError implements ErrorCatalog {

    ERR_PAYMENT_TRANSACTION_REF_REQUIRED(
            "RN-PAYMENT-001",
            "error.payment.transactionRefRequired",
            "La transacción de pago requiere referencia"
    ),

    ERR_PAYMENT_NOT_CASH(
            "RN-PAYMENT-002",
            "error.payment.notCash",
            "El pago debe ser en efectivo"
    ),

    ERR_PAYMENT_CANCELLATION_REQUIRES_REASON(
            "RN-PAYMENT-003",
            "error.payment.cancellationRequiresReason",
            "La cancelación del pago requiere un motivo"
    ),

    ERR_PAYMENT_REFUND_INVALID_AMOUNT(
            "RN-PAYMENT-004",
            "error.payment.refundInvalidAmount",
            "El monto de reembolso no es válido"
    ),

    ERR_PAYMENT_REFUND_EXCEEDS_AMOUNT(
            "RN-PAYMENT-005",
            "error.payment.refundExceedsAmount",
            "El reembolso excede el monto original"
    ),

    ERR_PAYMENT_AMOUNT_INVALID(
            "RN-PAYMENT-006",
            "error.payment.amountInvalid",
            "El monto del pago es inválido"
    ),

    ERR_PAYMENT_INSTITUTIONAL_REQUIRES_PAYER(
            "RN-PAYMENT-007",
            "error.payment.institutionalRequiresPayer",
            "El pago institucional requiere un pagador definido"
    ),

    ERR_PAYMENT_NOT_PENDING(
            "RN-PAYMENT-008",
            "error.payment.notPending",
            "El pago no está en estado pendiente"
    ),

    ERR_PAYMENT_NOT_CONFIRMED(
            "RN-PAYMENT-009",
            "error.payment.notConfirmed",
            "El pago no está confirmado"
    );



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
