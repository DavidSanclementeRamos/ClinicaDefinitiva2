package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

public enum ProvidedServiceError implements ErrorCatalog {

    ERR_SERVICE_INACTIVE(
            "RN-SERVICE-001","error.service.inactive",
            "No se puede operar sobre un servicio inactivo",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_SERVICE_CATEGORY_MISMATCH(
            "RN-SERVICE-002","error.service.category.mismatch",
            "La categoría del servicio no coincide con el tipo de detalles",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_SERVICE_HAS_APPOINTMENTS(
            "RN-SERVICE-003","error.service.appointments.exist",
            "No puede desactivarse porque tiene citas programadas en las próximas 48 horas",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_SERVICE_TYPE_IMMUTABLE(
            "RN-SERVICE-004","error.service.type.immutable",
            "No puede cambiar el tipo de detalles una vez establecido",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION(
            "RN-SERVICE-005","error.service.rate.justification.required",
            "Cambios en tarifa requieren justificación si hay citas programadas",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_SERVICE_MISSING_REQUIRED_FIELDS(
            "RN-SERVICE-006","error.service.fields.required",
            "El nombre y descripción no pueden estar en blanco",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_SERVICE_NOT_BILLABLE(
            "RN-SERVICE-007","error.service.not.billable",
            "Servicios inactivos no pueden ser utilizados en facturación",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE(
            "RN-SERVICE-008","error.service.rate.outofrange",
            "El cambio de tarifa debe estar dentro del rango razonable (50%-300% del valor actual)",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_SERVICE_HAS_PENDING_INVOICES(
            "RN-SERVICE-009","error.service.invoices.pending",
            "No puede desactivarse porque tiene facturas pendientes",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_SERVICE_DEACTIVATION_REASON_REQUIRED(
            "RN-SERVICE-010","error.service.deactivation.reason",
            "Debe registrar motivo de desactivación con mínimo 10 caracteres",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    ProvidedServiceError(String code, String messageKey, String defaultMessage,
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