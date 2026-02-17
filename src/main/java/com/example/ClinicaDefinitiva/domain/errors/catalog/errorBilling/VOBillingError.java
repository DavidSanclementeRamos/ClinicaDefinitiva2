package com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum VOBillingError implements ErrorCatalog {

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
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    VOBillingError(String code, String messageKey, String defaultMessage) {
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
