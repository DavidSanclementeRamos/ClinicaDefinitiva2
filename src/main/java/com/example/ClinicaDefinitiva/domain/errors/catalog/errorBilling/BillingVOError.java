package com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum BillingVOError implements ErrorCatalog {

    ERR_INVOICE_ID_NULL(
            "RN-INVOICE-001",
            "error.invoice.id.null",
            "El identificador de la factura (InvoiceId) no puede ser nulo"
    ),

    ERR_RATE_ID_NULL(
            "RN-RATE-001",
            "error.rate.id.null",
            "El identificador de la tarifa (RateId) no puede ser nulo"
    ),

    ERR_INVOICE_ITEM_ID_NULL(
            "RN-INVOICE-001",
            "error.invoice.item.id.null",
            "El identificador del ítem de factura (InvoiceItemId) no puede ser nulo"
    ),
    ERR_QUANTITY_MUST_BE_POSITIVE(
            "RN-BILLING-001",
            "error.billing.quantity.must.be.positive",
            "La cantidad debe ser mayor o igual a 1" ),
    ERR_QUANTITY_EXCEEDS_MAXIMUM(
            "RN-BILLING-002",
            "error.billing.quantity.exceeds.maximum",
            "La cantidad no puede exceder 1000 ítems por línea de factura" ),
    ERR_INVOICE_INVALID_STATUS_TRANSITION(
            "RN-INVOICE-002",
            "Transición de estado inválida. Estado actual: {currentStatus}, Estado solicitado: {requestedStatus}. Verifique la máquina de estados definida para Invoice.",
            "InvoiceStatus"
    ),



    // Payer
    ERR_INVOICE_PAYER_REQUIRED(
            "RN-INVOICE-007",
            "error.invoice.payer.required",
            "El pagador es obligatorio y no puede ser nulo o vacío"
    ),

    // Notes
    ERR_INVOICE_NOTES_TOO_SHORT(
            "RN-INVOICE-009",
            "error.invoice.notes.too.short",
            "Las notas deben tener al menos 3 caracteres si se especifican"
    ),

    // InvoiceNumberGenerator - prefijo requerido
    ERR_INVOICE_NUMBER_PREFIX_REQUIRED(
            "RN-BILLING-011",
            "error.invoice.number.prefix.required",
            "El prefijo del número de factura es obligatorio y no puede ser nulo o vacío"
    ),

    // InvoiceNumberGenerator - secuencia inicial negativa
    ERR_INVOICE_NUMBER_INITIAL_SEQUENCE_NEGATIVE(
            "RN-BILLING-012",
            "error.invoice.number.initial.sequence.negative",
            "La secuencia inicial del número de factura no puede ser negativa"
    ),

    // InvoiceNumberGenerator - reset con valor negativo
    ERR_INVOICE_NUMBER_RESET_NEGATIVE(
            "RN-BILLING-012",
            "error.invoice.number.reset.negative",
            "El valor de reinicio de la secuencia no puede ser negativo"
    ),

    // InvoiceNumber - número requerido
    ERR_INVOICE_NUMBER_REQUIRED(
            "RN-BILLING-010",
            "error.invoice.number.required",
            "El número de factura no puede ser nulo o vacío"
    ),

    // InvoiceNumber - formato inválido
    ERR_INVOICE_NUMBER_INVALID_FORMAT(
            "RN-BILLING-010",
            "error.invoice.number.invalid.format",
            "El número de factura debe seguir el formato PREFIJO-NÚMERO (ej. FAC-0001)"
    ),

    // InvoiceNumber - secuencia negativa
    ERR_INVOICE_NUMBER_NEGATIVE(
            "RN-BILLING-012",
            "error.invoice.number.negative",
            "El número secuencial de la factura no puede ser negativo"
    ),

    // CurrencyCode - requerido
    ERR_INVOICE_CURRENCY_REQUIRED(
            "RN-INVOICE-008",
            "error.invoice.currency.required",
            "La moneda es obligatoria y no puede ser nula o vacía"
    ),

    // CurrencyCode - inválida
    ERR_INVOICE_INVALID_CURRENCY(
            "RN-INVOICE-008",
            "error.invoice.currency.invalid",
            "Código de moneda inválido. Debe ser un código ISO 4217 (ej. COP, USD, EUR)"
    ),

    ERR_INVOICE_PROVIDER_REQUIRED(
            "RN-INVOICE-014",
            "error.invoice.provider.required",
            "La factura debe tener un proveedor válido como emisor oficial"
    );







    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    BillingVOError(String code, String messageKey, String defaultMessage) {
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
