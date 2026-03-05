package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ProvidedServiceError implements ErrorCatalog {

    

   ERR_SERVICE_INACTIVE(
        "RN-SERVICE-001",
        "error.service.inactive",
        "No se puede operar sobre un servicio inactivo"
),

ERR_SERVICE_CATEGORY_MISMATCH(
        "RN-SERVICE-002",
        "error.service.category.mismatch",
        "La categoría del servicio no coincide con el tipo de detalles"
),

ERR_SERVICE_HAS_APPOINTMENTS( // Pospuesta
        "RN-SERVICE-003",
        "error.service.appointments.exist",
        "No puede desactivarse porque tiene citas programadas en las próximas 48 horas"
),

ERR_SERVICE_TYPE_IMMUTABLE(
        "RN-SERVICE-004",
        "error.service.type.immutable",
        "No puede cambiar el tipo de detalles una vez establecido"
),

ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION( // Evaluar
        "RN-SERVICE-005",
        "error.service.rate.justification.required",
        "Cambios en tarifa requieren justificación si hay citas programadas"
),

ERR_SERVICE_MISSING_REQUIRED_FIELDS( // Eliminar
        "RN-SERVICE-006",
        "error.service.fields.required",
        "El nombre y descripción no pueden estar en blanco"
),

ERR_SERVICE_NOT_BILLABLE( // Eliminar
        "RN-SERVICE-007",
        "error.service.not.billable",
        "Servicios inactivos no pueden ser utilizados en facturación"
),

ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE( // Falta
        "RN-SERVICE-008",
        "error.service.rate.outofrange",
        "El cambio de tarifa debe estar dentro del rango razonable (50%-300% del valor actual)"
),

ERR_SERVICE_HAS_PENDING_INVOICES( // Falta
        "RN-SERVICE-009",
        "error.service.invoices.pending",
        "No puede desactivarse porque tiene facturas pendientes"
),

ERR_SERVICE_DEACTIVATION_REASON_REQUIRED( // Hecho
        "RN-SERVICE-010",
        "error.service.deactivation.reason",
        "Debe registrar motivo de desactivación con mínimo 10 caracteres"
);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ProvidedServiceError(String code, String messageKey, String defaultMessage) {
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
