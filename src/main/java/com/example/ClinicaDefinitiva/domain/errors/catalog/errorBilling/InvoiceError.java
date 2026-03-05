package com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

/**
 * Catálogo de errores específicos del agregado Invoice.
 * Siguiendo estrategia de ADR-22: Numeración consecutiva por agregado.
 * Contexto: Facturación con cumplimiento normativo colombiano (DIAN).
 */
public enum InvoiceError implements ErrorCatalog {

// si
    ERR_INVOICE_NO_ITEMS(
            "RN-INVOICE-001",
            "La factura debe tener al menos un ítem antes de emitir",
            "Invoice"
    ),

// si
    ERR_INVOICE_ZERO_TOTAL(
            "RN-INVOICE-002",
            "El total de la factura debe ser mayor a cero",
            "Invoice"
    ),
   

// si
    ERR_INVOICE_EXPIRED_RATE(
            "RN-INVOICE-003",
            "No se puede emitir factura con tarifas vencidas. Validar vigencia de Rate al momento de facturar",
            "Invoice"
    ),

// au no se usa
    ERR_INVOICE_INACTIVE_SERVICE(
            "RN-INVOICE-014",
            "No se puede facturar servicio inactivo. Activar servicio o usar otro",
            "Invoice"
    ),

// no se usa
    ERR_INVOICE_CANNOT_ADD_ITEM(
            "RN-INVOICE-004",
            "No se pueden agregar ítems a factura en estado {status}. Solo DRAFT o PENDING permiten modificaciones",
            "Invoice"
    ),

// no se usa
    ERR_INVOICE_IMMUTABLE_AFTER_EMISSION(
            "RN-INVOICE-010",
            "Factura emitida no puede modificarse. Crear nota crédito para correcciones",
            "Invoice"
    ),

// si
    ERR_INVOICE_CURRENCY_MISMATCH(
            "RN-INVOICE-008",
            "Ítem con moneda {itemCurrency} no coincide con moneda de factura {invoiceCurrency}",
            "Invoice"
    ),


    ERR_INVOICE_INVALID_DUE_DATE(
            "RN-INVOICE-006",
            "Fecha de vencimiento debe ser posterior a fecha de emisión",
            "Invoice"
    ),


    // si
    ERR_INVOICE_MISSING_CONTRACT(
            "RN-INVOICE-007",
            "Factura a EPS requiere contrato vigente. Asociar ContractId antes de emitir",
            "Invoice"
    ),


    // si
    ERR_INVOICE_CANNOT_CANCEL_PAID(
            "RN-INVOICE-005",
            "Factura pagada no puede cancelarse. Emitir nota crédito según normativa DIAN",
            "Invoice"
    ),

    // si
    ERR_INVOICE_CANCELLATION_REQUIRES_REASON(
            "RN-INVOICE-009",
            "La cancelación requiere motivo de al menos 10 caracteres para auditoría",
            "Invoice"
    ),

    ERR_INVOICE_UNPAID(
            "RN-INVOICE-012",
            "No se puede marcar como pagada sin registrar pago. Total factura: {invoiceTotal}, Total pagos: {paymentsTotal}",
            "Invoice"
    ),

    ERR_INVOICE_TOTAL_MISMATCH(
            "RN-INVOICE-011",
            "Corrupción de datos: Subtotal + Tax ≠ Total. Subtotal: {subtotal}, Tax: {tax}, Total esperado: {expectedTotal}, Total actual: {actualTotal}",
            "Invoice"
    ),

    ERR_INVOICE_INVALID_NUMBER_SEQUENCE(
            "RN-INVOICE-013",
            "Número de factura no consecutivo. Último número emitido: {lastNumber}, Número solicitado: {requestedNumber}. Verificar resolución DIAN",
            "Invoice"
    ),
    // sii
    ERR_INVOICE_NOT_EDITABLE(
            "RN-INVOICE-014",
            "La factura no puede modificarse en el estado actual. Solo se permiten cambios en estado BORRADOR.",
            "Invoice"
    ),
    
    // si
    ERR_INVOICE_CONTRACT_NOT_VALID(
            "RN-INVOICE-015",
            "error.invoice.contract.not.valid",
            "El contrato asociado está vencido o inactivo"
    ),
    //no se usa
    ERR_INVOICE_MUST_BE_PENDING_TO_PAY(
            "RN-INVOICE-016",
            "error.invoice.must.be.pending.to.pay",
            "La factura debe estar en estado PENDING para poder registrarse como pagada"
    );



    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    InvoiceError(String code, String messageKey, String defaultMessage) {
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