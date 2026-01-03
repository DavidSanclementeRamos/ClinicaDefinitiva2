package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ServiceError implements ErrorCatalog {

    // RN-SERVICE-001
    ERR_SERVICE_INVALID_RATE(
            "RN-SERVICE-001",
            "error.service.rate.invalid",
            "La tarifa base debe ser mayor a 0"
    ),

    // RN-SERVICE-002
    ERR_SERVICE_INVALID_DURATION(
            "RN-SERVICE-002",
            "error.service.duration.invalid",
            "La duración debe estar entre 15 minutos y 4 horas (240 minutos)"
    ),

    // RN-SERVICE-003
    ERR_SERVICE_INACTIVE(
            "RN-SERVICE-003",
            "error.service.inactive",
            "No se puede operar sobre un servicio inactivo"
    ),

    // RN-SERVICE-004
    ERR_SERVICE_CATEGORY_MISMATCH(
            "RN-SERVICE-004",
            "error.service.category.mismatch",
            "La categoría del servicio no coincide con el tipo de detalles"
    ),

    // RN-SERVICE-005
    ERR_SERVICE_HAS_APPOINTMENTS(
            "RN-SERVICE-005",
            "error.service.appointments.exist",
            "No puede desactivarse porque tiene citas programadas en las próximas 48 horas"
    ),

    // RN-SERVICE-006
    ERR_SERVICE_TYPE_IMMUTABLE(
            "RN-SERVICE-006",
            "error.service.type.immutable",
            "No puede cambiar el tipo de detalles una vez establecido"
    ),

    // RN-SERVICE-007
    ERR_SERVICE_CODE_DUPLICATE(
            "RN-SERVICE-007",
            "error.service.code.duplicate",
            "Ya existe otro servicio con este código"
    ),

    // RN-SERVICE-008
    ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION(
            "RN-SERVICE-008",
            "error.service.rate.justification.required",
            "Cambios en tarifa requieren justificación si hay citas programadas"
    ),

    // RN-SERVICE-009
    ERR_SERVICE_MISSING_REQUIRED_FIELDS(
            "RN-SERVICE-009",
            "error.service.fields.required",
            "El nombre y descripción no pueden estar en blanco"
    ),

    // RN-SERVICE-010
    ERR_SERVICE_NOT_BILLABLE(
            "RN-SERVICE-010",
            "error.service.not.billable",
            "Servicios inactivos no pueden ser utilizados en facturación"
    ),

    // RN-SERVICE-011
    ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE(
            "RN-SERVICE-011",
            "error.service.rate.outofrange",
            "El cambio de tarifa debe estar dentro del rango razonable (50%-300% del valor actual)"
    ),

    // RN-SERVICE-012
    ERR_SERVICE_HAS_PENDING_INVOICES(
            "RN-SERVICE-012",
            "error.service.invoices.pending",
            "No puede desactivarse porque tiene facturas pendientes"
    ),

    // RN-SERVICE-013
    ERR_SERVICE_INVALID_CODE_FORMAT(
            "RN-SERVICE-013",
            "error.service.code.format",
            "El código de servicio debe tener entre 4 y 15 caracteres alfanuméricos"
    ),

    // RN-SERVICE-014
    ERR_SERVICE_DESCRIPTION_TOO_SHORT(
            "RN-SERVICE-014",
            "error.service.description.short",
            "La descripción debe tener al menos 20 caracteres"
    ),

    // RN-SERVICE-015
    ERR_SERVICE_DEACTIVATION_REASON_REQUIRED(
            "RN-SERVICE-015",
            "error.service.deactivation.reason",
            "Debe registrar motivo de desactivación con mínimo 10 caracteres"
    );


    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ServiceError(String code, String messageKey, String defaultMessage) {
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
