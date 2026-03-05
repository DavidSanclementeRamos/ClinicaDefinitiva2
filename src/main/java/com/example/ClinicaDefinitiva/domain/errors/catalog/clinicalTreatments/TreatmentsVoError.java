package com.example.ClinicaDefinitiva.domain.errors.catalog.clinicalTreatments;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum TreatmentsVoError implements ErrorCatalog {

    ERR_TREATMENTS_ID_NULL(
            "RN-TREATMENTS-VO-001",
            "error.serviceId.null",
            "El identificador del tratamiento no puede ser nulo"
    ),

    ERR_TREATMENTS_PHASE_DATE_INVALID(
        "RN-TREATMENT-007",
        "error.treatments.phaseDateInvalid",
        "La fecha de la fase del tratamiento es inválida"
);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    TreatmentsVoError(String code, String messageKey, String defaultMessage) {
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
