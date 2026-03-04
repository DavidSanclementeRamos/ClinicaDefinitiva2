package com.example.ClinicaDefinitiva.domain.errors.catalog.errorAccounting;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum ContractError implements ErrorCatalog {
    // si
    ERR_CONTRACT_INVALID_DATES("RN-CONTRACT-001", "error.contract.invalidDates",
            "La fecha de fin debe ser posterior a la fecha de inicio"),

    // si
    ERR_CONTRACT_NOT_EDITABLE("RN-CONTRACT-002", "error.contract.notEditable",
            "Solo puede editarse si está en estado ACTIVE y no vencido"),

    // si
    ERR_CONTRACT_CANNOT_SUSPEND("RN-CONTRACT-003", "error.contract.cannotSuspend",
            "Solo puede suspenderse si está en estado ACTIVE"),

    // si
    ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE("RN-CONTRACT-004", "error.contract.expiredCannotReactivate",
            "No puede reactivarse si está vencido"),
// no
   /** ERR_CONTRACT_INVALID_EXTENSION("RN-CONTRACT-005", "error.contract.invalidExtension",
            "La extensión de vigencia solo permite fechas posteriores"),*/

    // si 
    ERR_CONTRACT_MISSING_COVERAGE_TYPE("RN-CONTRACT-006", "error.contract.missingCoverageType",
            "Debe tener tipo de cobertura válido"),

// RN-CONTRACT-007: Evento de sistema (ContractNearExpirationEvent) — no genera error de catálogo.

    // si
    ERR_CONTRACT_TERMINATION_REQUIRES_REASON("RN-CONTRACT-008", "error.contract.terminationRequiresReason",
            "La terminación requiere motivo obligatorio"),
    // --- Contract (Contrato) ---
    // si
    ERR_CONTRACT_MISSING_NEW_END_DATE("RN-CONTRACT-009", "error.contract.missingNewEndDate",
            "La nueva fecha de fin es obligatoria"),
    // --- Contract (Contrato) ---
    // si
    ERR_CONTRACT_NEW_END_DATE_IN_PAST("RN-CONTRACT-010", "error.contract.newEndDateInPast",
            "La nueva fecha de fin no puede estar en el pasado"),
    // --- Contract (Contrato) ---
    
    // si
    ERR_CONTRACT_CANNOT_REACTIVATE("RN-CONTRACT-011", "error.contract.cannotReactivate",
            "Solo se pueden reactivar contratos suspendidos"),
    // --- Contract (Contrato) ---
    // si
    ERR_CONTRACT_ALREADY_TERMINATED("RN-CONTRACT-012", "error.contract.alreadyTerminated",
            "El contrato ya está terminado"),
    // --- Contract (Contrato) ---
    // si
    ERR_CONTRACT_MISSING_START_DATE("RN-CONTRACT-014", "error.contract.missingStartDate",
            "La fecha de inicio es obligatoria"),

    // si
    ERR_CONTRACT_MISSING_END_DATE("RN-CONTRACT-015", "error.contract.missingEndDate",
            "La fecha de fin es obligatoria"),
    // --- Contract (Contrato) ---
    // si
    ERR_CONTRACT_EXPIRED_NOT_EDITABLE("RN-CONTRACT-013", "error.contract.expiredNotEditable",
            "No se puede editar un contrato vencido");

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    ContractError(String code, String messageKey, String defaultMessage) {
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
