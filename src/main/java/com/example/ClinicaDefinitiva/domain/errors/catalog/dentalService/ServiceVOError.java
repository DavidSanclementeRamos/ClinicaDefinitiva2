package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;


public enum ServiceVOError implements ErrorCatalog {

    ERR_SERVICE_NAME_CUSTOM_INVALID("RN-SERVICE-VO-001","error.serviceName.customInvalid","El nombre personalizado debe tener al menos 3 caracteres y no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DESCRIPTION_INVALID("RN-SERVICE-VO-002","error.serviceDescription.invalid","La descripción debe tener al menos 10 caracteres y no puede ser nula",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DURATION_START_END_REQUIRED("RN-SERVICE-VO-003","error.serviceDuration.startEndRequired","Los tiempos de inicio y fin no pueden ser nulos",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DURATION_START_BEFORE_END("RN-SERVICE-VO-004","error.serviceDuration.startBeforeEnd","El tiempo de inicio debe ser anterior al tiempo de fin",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DURATION_REQUIRED("RN-SERVICE-VO-005","error.serviceDuration.required","La duración no puede ser nula",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DURATION_POSITIVE("RN-SERVICE-VO-006","error.serviceDuration.positive","La duración debe ser positiva",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DURATION_MINIMUM("RN-SERVICE-VO-007","error.serviceDuration.minimum","La duración mínima es {min} minutos",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DURATION_MAXIMUM("RN-SERVICE-VO-008","error.serviceDuration.maximum","La duración máxima es {max} minutos ({hours} horas)",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DURATION_RESULT_POSITIVE("RN-SERVICE-VO-009","error.serviceDuration.resultPositive","La duración resultante debe ser positiva",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_DURATION_FACTOR_POSITIVE("RN-SERVICE-VO-010","error.serviceDuration.factorPositive","El factor de multiplicación debe ser positivo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_CODE_REQUIRED("RN-SERVICE-VO-011","error.serviceCode.required","El código de servicio no puede ser nulo ni estar en blanco",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_CODE_FORMAT_INVALID("RN-SERVICE-VO-012","error.serviceCode.formatInvalid","El código de servicio solo puede contener letras mayúsculas, números y guiones",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_CODE_LENGTH_INVALID("RN-SERVICE-VO-013","error.serviceCode.lengthInvalid","El código de servicio debe tener entre {min} y {max} caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_CODE_DUPLICATE("RN-SERVICE-VO-014","error.serviceCode.duplicate","El código de servicio ya existe y debe ser único",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),
    ERR_SERVICE_ID_NULL("RN-SERVICE-VO-015","error.serviceId.null","El identificador del servicio no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_ID_BLANK("RN-SERVICE-VO-016","error.serviceId.blank","El identificador del servicio no puede estar vacío",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_STATUS_NULL("RN-SERVICE-VO-017","error.serviceStatus.null","El estado del servicio no puede ser nulo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_NAME_NULL_OR_BLANK("RN-SERVICE-VO-018","error.serviceCatalog.nameNullOrBlank","El nombre del servicio no puede ser nulo ni estar vacío",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_CATEGORY_NULL_OR_BLANK("RN-SERVICE-VO-019","error.serviceCatalog.categoryNullOrBlank","La categoría del servicio no puede ser nula ni estar vacía",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_INVALID_MIN_AGE("RN-SERVICE-VO-020","error.service.invalid.minAge","La edad mínima del servicio es inválida",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),
    ERR_SERVICE_INVALID_RANGE("RN-SERVICE-VO-021","error.service.invalid.range","El rango definido para el servicio es inválido",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    ServiceVOError(String code, String messageKey, String defaultMessage,
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