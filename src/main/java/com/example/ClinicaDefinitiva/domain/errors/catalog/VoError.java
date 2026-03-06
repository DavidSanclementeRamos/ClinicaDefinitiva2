
package com.example.ClinicaDefinitiva.domain.errors.catalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;


public enum VoError implements ErrorCatalog {

    ERR_EMAIL_NULL("RN-EMAIL-001","error.email.null","El email no puede ser null",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_EMAIL_EMPTY("RN-EMAIL-002","error.email.empty","El email no puede estar vacío",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN("RN-EMAIL-003","error.email.missing.parts","El email debe contener una parte local y un dominio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_EMAIL_LENGTH_EXCEEDED("RN-EMAIL-004","error.email.length.exceeded","La longitud del email excede el máximo permitido de 254 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_EMAIL_LOCAL_LENGTH_EXCEEDED("RN-EMAIL-005","error.email.local.length.exceeded","La parte local del email excede 64 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED("RN-EMAIL-006","error.email.domain.length.exceeded","El dominio del email excede 253 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_EMAIL_INVALID_FORMAT("RN-EMAIL-007","error.email.invalid.format","El formato del email no es válido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_EMAIL_DOMAIN_INVALID_DASH("RN-EMAIL-008","error.email.domain.invalid.dash","El dominio no puede iniciar ni terminar con guion",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS("RN-EMAIL-009","error.email.domain.consecutive.dots","El dominio no puede contener puntos consecutivos",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_SERVICE_PRICE_AMOUNT_REQUIRED("RN-PRICE-001","error.price.amountRequired","El monto no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_PRICE_CURRENCY_REQUIRED("RN-PRICE-002","error.price.currencyRequired","La moneda no puede ser nula",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_PRICE_NEGATIVE("RN-PRICE-003","error.price.negative","El monto no puede ser negativo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PRICE_CURRENCY_MISMATCH("RN-PRICE-004","error.price.currencyMismatch","No se pueden operar precios con monedas distintas",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.WARN),

    ERR_PHONE_NULL("RN-PHONE-007","error.phone.null","El número telefónico no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PHONE_BLANK("RN-PHONE-008","error.phone.blank","El número telefónico no puede estar vacío",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_PHONE_INVALID_FORMAT("RN-PHONE-009","error.phone.format","El formato del número telefónico es inválido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ADDRESS_NULL("RN-ADDRESS-010","error.address.null","Los campos de dirección no pueden ser nulos",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_ADDRESS_BLANK("RN-ADDRESS-011","error.address.blank","Los campos de dirección no pueden estar vacíos",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_NAME_NULL("RN-NAME-012","error.name.null","El nombre no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_NAME_BLANK("RN-NAME-013","error.name.blank","El nombre no puede estar vacío",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_NAME_TOO_LONG("RN-NAME-014","error.name.tooLong","El nombre excede la longitud máxima permitida",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    VoError(String code, String messageKey, String defaultMessage,
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
