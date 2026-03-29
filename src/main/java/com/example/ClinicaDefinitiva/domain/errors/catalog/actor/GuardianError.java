package com.example.ClinicaDefinitiva.domain.errors.catalog.actor;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum GuardianError implements ErrorCatalog {

    ERR_GUARDIAN_CANNOT_REVOKE_STARTED_TREATMENT(
            "RN-GUARDIAN-001","error.guardian.revoke.started",
            "No se puede revocar el consentimiento de un tratamiento que ya ha iniciado",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE(
            "RN-GUARDIAN-002","error.guardian.relationship.missing",
            "Debe registrarse el tipo de relación con el paciente",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS(
            "RN-GUARDIAN-003","error.guardian.deactivate.authorizations",
            "No puede desactivarse si tiene autorizaciones clínicas vigentes",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_RESPONSIBLE_INVALID_AGE(
            "RN-GUARDIAN-004","error.guardian.age.invalid",
            "El responsable debe tener entre 22 y 60 años",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP(
            "RN-GUARDIAN-005","error.guardian.relationship.modify",
            "No puede modificarse el vínculo legal si ha autorizado tratamientos previamente",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON(
            "RN-GUARDIAN-006","error.guardian.deactivate.reason",
            "La desactivación requiere motivo obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_REVOCATION_REQUIRES_REASON(
            "RN-GUARDIAN-007","error.guardian.revoke.reason",
            "La revocación de consentimiento requiere motivo obligatorio",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_GUARDIAN_PATIENT_LIMIT_EXCEEDED(
            "RN-GUARDIAN-008","error.guardian.patients.limit",
            "El responsable ha alcanzado el límite de pacientes a cargo",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),
    ERR_GUARDIAN_NOT_FOUND(
    "RN-GUARDIAN-009", "error.guardian.not.found",
    "El responsable legal solicitado no existe",
    HttpStatus.NOT_FOUND, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    GuardianError(String code, String messageKey, String defaultMessage,
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