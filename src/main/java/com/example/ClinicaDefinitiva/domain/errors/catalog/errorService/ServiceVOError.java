package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ServiceVOError implements ErrorCatalog {
    ERR_SERVICE_NAME_CUSTOM_INVALID(
            "RN-SERV-001",
            "error.serviceName.customInvalid",
            "El nombre personalizado debe tener al menos 3 caracteres y no puede ser nulo"
    ),
    ERR_SERVICE_DESCRIPTION_INVALID(
            "RN-SERV-001",
            "error.serviceDescription.invalid",
            "La descripción debe tener al menos 10 caracteres y no puede ser nula"
    ),
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
    ERR_SERVICE_DURATION_START_END_REQUIRED(
            "RN-DURATION-001",
            "error.serviceDuration.startEndRequired",
            "Los tiempos de inicio y fin no pueden ser nulos"
    ),

    ERR_SERVICE_DURATION_START_BEFORE_END(
            "RN-DURATION-002",
            "error.serviceDuration.startBeforeEnd",
            "El tiempo de inicio debe ser anterior al tiempo de fin"
    ),

    ERR_SERVICE_DURATION_REQUIRED(
            "RN-DURATION-003",
            "error.serviceDuration.required",
            "La duración no puede ser nula"
    ),

    ERR_SERVICE_DURATION_POSITIVE(
            "RN-DURATION-004",
            "error.serviceDuration.positive",
            "La duración debe ser positiva"
    ),

    ERR_SERVICE_DURATION_MINIMUM(
            "RN-DURATION-005",
            "error.serviceDuration.minimum",
            "La duración mínima es {min} minutos"
    ),

    ERR_SERVICE_DURATION_MAXIMUM(
            "RN-DURATION-006",
            "error.serviceDuration.maximum",
            "La duración máxima es {max} minutos ({hours} horas)"
    ),

    ERR_SERVICE_DURATION_RESULT_POSITIVE(
            "RN-DURATION-007",
            "error.serviceDuration.resultPositive",
            "La duración resultante debe ser positiva"
    ),

    ERR_SERVICE_DURATION_FACTOR_POSITIVE(
            "RN-DURATION-008",
            "error.serviceDuration.factorPositive",
            "El factor de multiplicación debe ser positivo"
    ),
    ERR_SERVICE_CODE_REQUIRED(
            "RN-SERVICECODE-001",
            "error.serviceCode.required",
            "El código de servicio no puede ser nulo ni estar en blanco"
    ),

    ERR_SERVICE_CODE_FORMAT_INVALID(
            "RN-SERVICECODE-002",
            "error.serviceCode.formatInvalid",
            "El código de servicio solo puede contener letras mayúsculas, números y guiones"
    ),

    ERR_SERVICE_CODE_LENGTH_INVALID(
            "RN-SERVICECODE-003",
            "error.serviceCode.lengthInvalid",
            "El código de servicio debe tener entre {min} y {max} caracteres"
    ),
    ERR_SERVICE_CODE_DUPLICATE(
            "RN-SERVICECODE-004",
            "error.serviceCode.duplicate",
            "El código de servicio ya existe y debe ser único"
    ),


    ERR_SERVICE_ID_NULL(
            "RN-SERVICEID-001",
            "error.serviceId.null",
            "El identificador del servicio no puede ser nulo"
    ),

    ERR_SERVICE_ID_BLANK(
            "RN-SERVICEID-002",
            "error.serviceId.blank",
            "El identificador del servicio no puede estar vacío"
    ),
    ERR_SERVICE_STATUS_NULL(
            "RN-SERVICESTATUS-001",
            "error.serviceStatus.null",
            "El estado del servicio no puede ser nulo"
    ),
    ERR_SERVICE_NAME_NULL_OR_BLANK(
            "RN-SERVICECATALOG-001",
            "error.serviceCatalog.nameNullOrBlank",
            "El nombre del servicio no puede ser nulo ni estar vacío"
    ),

    ERR_SERVICE_CATEGORY_NULL_OR_BLANK(
            "RN-SERVICECATALOG-002",
            "error.serviceCatalog.categoryNullOrBlank",
            "La categoría del servicio no puede ser nula ni estar vacía"
    ),
    ERR_PEDIATRIC_INVALID_MIN_AGE("","",""),
    ERR_PEDIATRIC_INVALID_RANGE("","","");

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ServiceVOError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }

}


