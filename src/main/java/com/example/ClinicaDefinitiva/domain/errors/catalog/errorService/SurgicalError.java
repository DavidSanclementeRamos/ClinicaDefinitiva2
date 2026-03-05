package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum SurgicalError implements ErrorCatalog {

  ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH(
        "RN-SURGICAL-001",
        "error.surgical.anesthesia.complexity",
        "Si requiere anestesia, el nivel de complejidad debe ser al menos MEDIUM"
),

ERR_SURGICAL_INVALID_COMPLEXITY(
        "RN-SURGICAL-002",
        "error.surgical.complexity.invalid",
        "El nivel de complejidad debe ser: LOW, MEDIUM, HIGH o CRITICAL"
),

ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS(
        "RN-SURGICAL-003",
        "error.surgical.critical.requirements",
        "Cirugías CRITICAL deben requerir anestesia y quirófano"
),

ERR_SURGICAL_TYPE_TOO_SHORT(
        "RN-SURGICAL-004",
        "error.surgical.type.short",
        "El tipo de cirugía debe tener al menos 3 caracteres si se especifica"
),

ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH(
        "RN-SURGICAL-005",
        "error.surgical.operatingroom.complexity",
        "Cirugías que requieren quirófano deben tener complejidad al menos MEDIUM"
);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    SurgicalError(String code, String messageKey, String defaultMessage) {
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
