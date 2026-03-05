
package com.example.ClinicaDefinitiva.domain.errors.catalog;


public enum VoError implements ErrorCatalog {
    
    // Catálogo de errores para VO Email
    ERR_EMAIL_NULL(
            "RN-EMAIL-001",
            "error.email.null",
            "El email no puede ser null"
    ),

    ERR_EMAIL_EMPTY(
            "RN-EMAIL-002",
            "error.email.empty",
            "El email no puede estar vacío"
    ),

    ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN(
            "RN-EMAIL-003",
            "error.email.missing.parts",
            "El email debe contener una parte local y un dominio"
    ),

    ERR_EMAIL_LENGTH_EXCEEDED(
            "RN-EMAIL-004",
            "error.email.length.exceeded",
            "La longitud del email excede el máximo permitido de 254 caracteres"
    ),

    ERR_EMAIL_LOCAL_LENGTH_EXCEEDED(
            "RN-EMAIL-005",
            "error.email.local.length.exceeded",
            "La parte local del email excede 64 caracteres"
    ),

    ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED(
            "RN-EMAIL-006",
            "error.email.domain.length.exceeded",
            "El dominio del email excede 253 caracteres"
    ),

    ERR_EMAIL_INVALID_FORMAT(
            "RN-EMAIL-007",
            "error.email.invalid.format",
            "El formato del email no es válido"
    ),

    ERR_EMAIL_DOMAIN_INVALID_DASH(
            "RN-EMAIL-008",
            "error.email.domain.invalid.dash",
            "El dominio no puede iniciar ni terminar con guion"
    ),

    ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS(
            "RN-EMAIL-009",
            "error.email.domain.consecutive.dots",
            "El dominio no puede contener puntos consecutivos"
    ),
    
    
    // PRICE 
    
     ERR_SERVICE_PRICE_AMOUNT_REQUIRED(
            "RN-PRICE-001",
            "error.price.amountRequired",
            "El monto no puede ser nulo"
    ),

    ERR_SERVICE_PRICE_CURRENCY_REQUIRED(
            "RN-PRICE-002",
            "error.price.currencyRequired",
            "La moneda no puede ser nula"
    ),

    ERR_SERVICE_PRICE_NEGATIVE(
            "RN-PRICE-003",
            "error.price.negative",
            "El monto no puede ser negativo"
    ),
    ERR_PRICE_CURRENCY_MISMATCH(
            "RN-PRICE-004",
            "error.price.currencyMismatch",
            "No se pueden operar precios con monedas distintas"
    ),
    
      // PhoneNumber errors
    ERR_PHONE_NULL(
            "RN-PHONE-007",
            "error.phone.null",
            "El número telefónico no puede ser nulo"
    ),
    ERR_PHONE_BLANK(
            "RN-PHONE-008",
            "error.phone.blank",
            "El número telefónico no puede estar vacío"
    ),
    ERR_PHONE_INVALID_FORMAT(
            "RN-PHONE-009",
            "error.phone.format",
            "El formato del número telefónico es inválido"
    ),

    // Address errors
    ERR_ADDRESS_NULL(
            "RN-ADDRESS-010",
            "error.address.null",
            "Los campos de dirección no pueden ser nulos"
    ),
    ERR_ADDRESS_BLANK(
            "RN-ADDRESS-011",
            "error.address.blank",
            "Los campos de dirección no pueden estar vacíos"
    ),
    // ===== Name =====
ERR_NAME_NULL(
        "ERR-NAME-012",
        "error.name.null",
        "El nombre no puede ser nulo"
),
ERR_NAME_BLANK(
        "ERR-NAME-013",
        "error.name.blank",
        "El nombre no puede estar vacío"
),
ERR_NAME_TOO_LONG(
        "ERR-NAME-014",
        "error.name.tooLong",
        "El nombre excede la longitud máxima permitida"
),

    
            ;
    
    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    VoError(String code, String messageKey, String defaultMessage) {
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
