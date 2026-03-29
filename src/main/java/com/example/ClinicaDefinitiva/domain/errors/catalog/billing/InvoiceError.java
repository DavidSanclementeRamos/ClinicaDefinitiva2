package com.example.ClinicaDefinitiva.domain.errors.catalog.billing;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;



public enum InvoiceError implements ErrorCatalog {

    ERR_INVOICE_NO_ITEMS(
            "RN-INVOICE-001","error.invoice.noItems",
            "La factura debe tener al menos un ítem antes de emitir",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_INVOICE_ZERO_TOTAL(
            "RN-INVOICE-002","error.invoice.zeroTotal",
            "El total de la factura debe ser mayor a cero",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_INVOICE_EXPIRED_RATE(
            "RN-INVOICE-003","error.invoice.expiredRate",
            "No se puede emitir factura con tarifas vencidas. Validar vigencia de Rate al momento de facturar",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_INVOICE_CANNOT_ADD_ITEM(
            "RN-INVOICE-004","error.invoice.cannotAddItem",
            "No se pueden agregar ítems a factura en estado {status}. Solo DRAFT o PENDING permiten modificaciones",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_INVOICE_CANNOT_CANCEL_PAID(
            "RN-INVOICE-005","error.invoice.cannotCancelPaid",
            "Factura pagada no puede cancelarse. Emitir nota crédito según normativa DIAN",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_INVOICE_INVALID_DUE_DATE(
            "RN-INVOICE-006","error.invoice.invalidDueDate",
            "Fecha de vencimiento debe ser posterior a fecha de emisión",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_INVOICE_MISSING_CONTRACT(
            "RN-INVOICE-007","error.invoice.missingContract",
            "Factura a EPS requiere contrato vigente. Asociar ContractId antes de emitir",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_INVOICE_CURRENCY_MISMATCH(
            "RN-INVOICE-008","error.invoice.currencyMismatch",
            "Ítem con moneda {itemCurrency} no coincide con moneda de factura {invoiceCurrency}",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_INVOICE_CANCELLATION_REQUIRES_REASON(
            "RN-INVOICE-009","error.invoice.cancellationRequiresReason",
            "La cancelación requiere motivo de al menos 10 caracteres para auditoría",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_INVOICE_IMMUTABLE_AFTER_EMISSION(
            "RN-INVOICE-010","error.invoice.immutableAfterEmission",
            "Factura emitida no puede modificarse. Crear nota crédito para correcciones",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_INVOICE_TOTAL_MISMATCH(
            "RN-INVOICE-011","error.invoice.totalMismatch",
            "Corrupción de datos: Subtotal + Tax ≠ Total. Subtotal: {subtotal}, Tax: {tax}, Total esperado: {expectedTotal}, Total actual: {actualTotal}",
            HttpStatus.INTERNAL_SERVER_ERROR, ErrorSeverity.FATAL),

    ERR_INVOICE_UNPAID(
            "RN-INVOICE-012","error.invoice.unpaid",
            "No se puede marcar como pagada sin registrar pago. Total factura: {invoiceTotal}, Total pagos: {paymentsTotal}",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_INVOICE_INVALID_NUMBER_SEQUENCE(
            "RN-INVOICE-013","error.invoice.invalidNumberSequence",
            "Número de factura no consecutivo. Último número emitido: {lastNumber}, Número solicitado: {requestedNumber}. Verificar resolución DIAN",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_INVOICE_NOT_EDITABLE(
            "RN-INVOICE-014","error.invoice.notEditable",
            "La factura no puede modificarse en el estado actual. Solo se permiten cambios en estado BORRADOR.",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_INVOICE_CONTRACT_NOT_VALID(
            "RN-INVOICE-015","error.invoice.contract.not.valid",
            "El contrato asociado está vencido o inactivo",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_INVOICE_MUST_BE_PENDING_TO_PAY(
            "RN-INVOICE-016","error.invoice.must.be.pending.to.pay",
            "La factura debe estar en estado PENDING para poder registrarse como pagada",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_INVOICE_INACTIVE_SERVICE(
            "RN-INVOICE-017","error.invoice.inactiveService",
            "No se puede facturar servicio inactivo. Activar servicio o usar otro",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),
    ERR_INVOICE_NOT_FOUND(
    "RN-INVOICE-018", "error.invoice.not.found",
    "La factura solicitada no existe",
    HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    InvoiceError(String code, String messageKey, String defaultMessage,
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