package com.example.ClinicaDefinitiva.domain.errors.catalog.errorService;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ProstheticError implements ErrorCatalog {

    // RN-PROSTHETIC-001
    ERR_PROSTHETIC_MISSING_TYPE(
            "RN-PROSTHETIC-001",
            "error.prosthetic.type.missing",
            "Debe especificar si la prótesis es fija o removible"
    ),

    // RN-PROSTHETIC-002
    ERR_PROSTHETIC_INVALID_UNITS(
            "RN-PROSTHETIC-002",
            "error.prosthetic.units.invalid",
            "El número de unidades debe ser mayor o igual a 0"
    ),

    // RN-PROSTHETIC-003
    ERR_PROSTHETIC_EXCESSIVE_UNITS(
            "RN-PROSTHETIC-003",
            "error.prosthetic.units.excessive",
            "Prótesis removibles no pueden tener más de 14 unidades por arcada"
    ),

    // RN-PROSTHETIC-004
    ERR_PROSTHETIC_INVALID_TYPE_VALUE(
            "RN-PROSTHETIC-004",
            "error.prosthetic.type.value",
            "El tipo debe ser FIXED (fija) o REMOVABLE (removible)"
    ),

    // Warnings
    WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS(
            "RN-PROSTHETIC-005",
            "warn.prosthetic.crown.units",
            "Corona individual típicamente tiene 1 unidad"
    ),

    WARN_PROSTHETIC_FULL_DENTURE_UNITS(
            "RN-PROSTHETIC-006",
            "warn.prosthetic.denture.units",
            "Prótesis total típicamente tiene 14 unidades por arcada"
    ),

    WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS(
            "RN-PROSTHETIC-007",
            "warn.prosthetic.bridge.units",
            "Puente fijo típicamente tiene al menos 3 unidades"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ProstheticError(String code, String messageKey, String defaultMessage) {
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

