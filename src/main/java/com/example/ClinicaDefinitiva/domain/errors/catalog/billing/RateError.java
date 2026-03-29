package com.example.ClinicaDefinitiva.domain.errors.catalog.billing;


import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;


public enum RateError implements ErrorCatalog {

    ERR_RATE_INVALID_VALIDITY_RANGE(
            "RN-RATE-001","error.rate.invalidValidityRange",
            "La fecha de fin de vigencia debe ser posterior a la fecha de inicio. Inicio: {validFrom}, Fin: {validTo}",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_RATE_EXPIRED(
            "RN-RATE-002","error.rate.expired",
            "La tarifa está vencida al momento de facturar. Vigencia: {validFrom} - {validTo}, Fecha factura: {invoiceDate}",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_RATE_CANNOT_SHORTEN_VALIDITY(
            "RN-RATE-003","error.rate.cannotShortenValidity",
            "No se puede acortar la vigencia porque existen facturas emitidas después de {newValidTo}",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_RATE_INACTIVE_SERVICE(
            "RN-RATE-004","error.rate.inactiveService",
            "No se puede crear tarifa para un servicio inactivo. Servicio: {serviceCode}",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_RATE_CURRENCY_MISMATCH(
            "RN-RATE-005","error.rate.currencyMismatch",
            "La moneda de la tarifa ({rateCurrency}) no coincide con la moneda del servicio ({serviceCurrency})",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_RATE_NOT_VALID_AT_DATE(
            "RN-RATE-006","error.rate.notValidAtDate",
            "La tarifa no es válida en la fecha solicitada",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),
    ERR_RATE_NOT_FOUND(
    "RN-RATE-007", "error.rate.not.found",
    "La tarifa solicitada no existe",
    HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    RateError(String code, String messageKey, String defaultMessage,
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