package com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling;


import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

/**
 * Catálogo de errores específicos del agregado Rate.
 * Siguiendo estrategia de ADR-22: Numeración consecutiva por agregado.
 * Contexto: Tarifas con vigencia temporal y contratos con EPSs (Colombia).
 */
public enum RateError implements ErrorCatalog {

   




    ERR_RATE_INVALID_VALIDITY_RANGE(
        "RN-RATE-001",
        "error.rate.invalidValidityRange",
        "La fecha de fin de vigencia debe ser posterior a la fecha de inicio. Inicio: {validFrom}, Fin: {validTo}"
),

ERR_RATE_EXPIRED(
        "RN-RATE-002",
        "error.rate.expired",
        "La tarifa está vencida al momento de facturar. Vigencia: {validFrom} - {validTo}, Fecha factura: {invoiceDate}"
),

ERR_RATE_CANNOT_SHORTEN_VALIDITY(
        "RN-RATE-003",
        "error.rate.cannotShortenValidity",
        "No se puede acortar la vigencia porque existen facturas emitidas después de {newValidTo}"
),

ERR_RATE_INACTIVE_SERVICE(
        "RN-RATE-004",
        "error.rate.inactiveService",
        "No se puede crear tarifa para un servicio inactivo. Servicio: {serviceCode}"
),

ERR_RATE_CURRENCY_MISMATCH(
        "RN-RATE-005",
        "error.rate.currencyMismatch",
        "La moneda de la tarifa ({rateCurrency}) no coincide con la moneda del servicio ({serviceCurrency})"
),

ERR_RATE_NOT_VALID_AT_DATE(
        "RN-RATE-006",
        "error.rate.notValidAtDate",
        "La tarifa no es válida en la fecha solicitada"
)
   
    ;

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    RateError(String code, String messageKey, String defaultMessage) {
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
