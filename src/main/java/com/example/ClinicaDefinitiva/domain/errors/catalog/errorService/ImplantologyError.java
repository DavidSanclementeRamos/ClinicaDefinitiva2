package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ImplantologyError implements ErrorCatalog {

    // RN-IMPLANTOLOGY-001
    ERR_IMPLANTOLOGY_INVALID_HEALING_TIME(
            "RN-IMPLANTOLOGY-001",
            "error.implantology.healing.invalid",
            "El tiempo de cicatrización debe estar entre 2 y 12 meses"
    ),

    // RN-IMPLANTOLOGY-002
    ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH(
            "RN-IMPLANTOLOGY-002",
            "error.implantology.bonegraft.healing",
            "Con injerto óseo, el tiempo de cicatrización mínimo es 4 meses"
    ),

    // RN-IMPLANTOLOGY-003
    ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME(
            "RN-IMPLANTOLOGY-003",
            "error.implantology.healing.negative",
            "El tiempo de cicatrización no puede ser negativo"
    ),

    // RN-IMPLANTOLOGY-004 (warning)
    WARN_IMPLANTOLOGY_SHORT_HEALING_TIME(
            "RN-IMPLANTOLOGY-004",
            "warn.implantology.healing.short",
            "Tiempos menores a 3 meses sin injerto son atípicos"
    ),

    // RN-IMPLANTOLOGY-005 (warning)
    WARN_IMPLANTOLOGY_LONG_HEALING_TIME(
            "RN-IMPLANTOLOGY-005",
            "warn.implantology.healing.long",
            "Tiempos mayores a 9 meses sin injerto complejo son atípicos"
    ),

    // RN-IMPLANTOLOGY-006 (warning)
    WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING(
            "RN-IMPLANTOLOGY-006",
            "warn.implantology.zygomatic.healing",
            "Implantes zigomáticos requieren tiempo de cicatrización extendido (6+ meses)"
    ),

    // RN-IMPLANTOLOGY-007
    ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE(
            "RN-IMPLANTOLOGY-007",
            "error.implantology.site.invalid",
            "El sitio de colocación debe tener formato válido si se especifica"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ImplantologyError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }
    @Override
    public String getCode() { return code; }
    @Override
    public String getMessageKey() { return messageKey; }
    @Override
    public String getDefaultMessage() { return defaultMessage; }}
