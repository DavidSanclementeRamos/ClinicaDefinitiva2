package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum PediatricError implements ErrorCatalog {

    // RN-PEDIATRIC-001
    ERR_PEDIATRIC_INVALID_AGE_RANGE(
            "RN-PEDIATRIC-001",
            "error.pediatric.age.invalid",
            "El rango de edad debe especificar edades pediátricas válidas (0-18 años)"
    ),

    // RN-PEDIATRIC-002
    ERR_PEDIATRIC_AGE_RANGE_TOO_SHORT(
            "RN-PEDIATRIC-002",
            "error.pediatric.age.short",
            "El rango de edad debe tener formato válido (mínimo 5 caracteres)"
    ),

    // RN-PEDIATRIC-003 (warning)
    WARN_PEDIATRIC_SEALANT_AGE_MISMATCH(
            "RN-PEDIATRIC-003",
            "warn.pediatric.sealant.age",
            "Sellantes típicamente se aplican entre 6-14 años"
    ),

    // RN-PEDIATRIC-004 (warning)
    WARN_PEDIATRIC_SEDATION_UNSPECIFIED(
            "RN-PEDIATRIC-004",
            "warn.pediatric.sedation.type",
            "Técnicas de sedación deben especificar tipo (consciente/profunda)"
    ),

    // RN-PEDIATRIC-005 (warning crítica)
    WARN_PEDIATRIC_PHYSICAL_RESTRAINT(
            "RN-PEDIATRIC-005",
            "warn.pediatric.restraint.ethics",
            "Contención física solo debe usarse en emergencias - restricciones éticas"
    ),

    // RN-PEDIATRIC-006
    ERR_PEDIATRIC_MATERIALS_TOO_SHORT(
            "RN-PEDIATRIC-006",
            "error.pediatric.materials.short",
            "Materiales pediátricos deben describirse adecuadamente (mínimo 5 caracteres)"
    ),

    // RN-PEDIATRIC-007 (warning)
    WARN_PEDIATRIC_INFANT_MANAGEMENT_MISSING(
            "RN-PEDIATRIC-007",
            "warn.pediatric.infant.management",
            "Bebés (0-3 años) requieren técnicas de manejo específicas"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    PediatricError(String code, String messageKey, String defaultMessage) {
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