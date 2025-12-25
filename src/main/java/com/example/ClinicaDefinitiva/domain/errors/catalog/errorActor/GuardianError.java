package com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum GuardianError implements ErrorCatalog {

    // RN-GUARDIAN-001: ELIMINADA (2024-12)
    // Motivo: Problema arquitectural de huevo-gallina
    // Original: "No puede crearse sin vínculo legal con un paciente"
    // Solución: Guardian es agregado independiente, vinculación posterior

    // RN-GUARDIAN-002: ELIMINADA (2024-12)
    // Motivo: Delegada a UserAccessError.ERR_USER_INACTIVE
    // Original: "No puede autorizar tratamientos si está inactivo"

    // RN-GUARDIAN-003: POSPUESTA -> APLICADA v1.0 ✅
    ERR_GUARDIAN_CANNOT_REVOKE_STARTED_TREATMENT(
            "RN-GUARDIAN-003",
            "error.guardian.revoke.started",
            "No se puede revocar el consentimiento de un tratamiento que ya ha iniciado"
    ),

    ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE(
            "RN-GUARDIAN-004",
            "error.guardian.relationship.missing",
            "Debe registrarse el tipo de relación con el paciente"
    ),

    ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS(
            "RN-GUARDIAN-005",
            "error.guardian.deactivate.authorizations",
            "No puede desactivarse si tiene autorizaciones clínicas vigentes"
    ),

    // RN-GUARDIAN-006: ELIMINADA (2024-12)
    // Motivo: Delegada a UserAccessError.ERR_USER_INACTIVE
    // Original: "Solo puede editarse si está activo"

    // RN-GUARDIAN-007: ELIMINADA (Catálogo actualizado)
    // Motivo: Validación ocurre en ValueObjects
    // Original: "Debe tener al menos un medio de contacto válido"
    // Reemplazo: ValueObjectError.ERR_PHONE_INVALID_FORMAT, ERR_EMAIL_INVALID_FORMAT

    // RN-GUARDIAN-008: ELIMINADA (Catálogo reemplazado)
    // Original: "Debe ser mayor de edad (≥ 18 años)"
    // Nuevo catálogo específico:
    ERR_RESPONSIBLE_INVALID_AGE(
            "RN-GUARDIAN-008",
            "error.guardian.age.invalid",
            "El responsable debe tener entre 22 y 60 años"
    ),

    ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP(
            "RN-GUARDIAN-009",
            "error.guardian.relationship.modify",
            "No puede modificarse el vínculo legal si ha autorizado tratamientos previamente"
    ),

    ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON(
            "RN-GUARDIAN-010",
            "error.guardian.deactivate.reason",
            "La desactivación requiere motivo obligatorio"
    ),

    // ========== NUEVOS ==========
    ERR_REVOCATION_REQUIRES_REASON(
            "RN-GUARDIAN-011",
            "error.guardian.revoke.reason",
            "La revocación de consentimiento requiere motivo obligatorio"
    ),
    ERR_GUARDIAN_PATIENT_LIMIT_EXCEEDED(
            "RN-GUARDIAN-012",
            "error.guardian.patients.limit",
            "El responsable ha alcanzado el límite de pacientes a cargo"
    );

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    GuardianError(String code, String messageKey, String defaultMessage) {
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
