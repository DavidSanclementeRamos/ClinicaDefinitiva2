package com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;

public enum AuthorizationError implements ErrorCatalog {

    ERR_AUTH_SECTOR_REQUIRED(
            "RN-AUTH-001","error.auth.sector.required",
            "DELETE DENTIST solo por RECEPTIONIST del sector RECURSOS_HUMANOS",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_AUTH_OWNERSHIP_REQUIRED(
            "RN-AUTH-002","error.auth.ownership.required",
            "PATIENT solo puede UPDATE sus propios datos",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_AUTH_GUARDIANSHIP_REQUIRED(
            "RN-AUTH-003","error.auth.guardianship.required",
            "GUARDIAN solo puede UPDATE pacientes bajo su tutela",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_AUTH_SPECIALTY_REQUIRED(
            "RN-AUTH-004","error.auth.specialty.required",
            "DENTIST solo puede ver servicios de su especialidad",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_AUTH_PERMISSION_DENIED(
            "RN-AUTH-005","error.auth.permission.denied",
            "El rol no tiene el permiso requerido en la matriz RBAC",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_AUTH_ROLE_DELETE_UNAUTHORIZED(
            "RN-AUTH-006","error.auth.role.delete.unauthorized",
            "No autorizado: solo roles permitidos en el sector RECURSOS_HUMANOS pueden eliminar roles",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_UNAUTHORIZED_REVOKE(
            "RN-AUTH-007","error.assignment.unauthorized.revoke",
            "No autorizado: el rol no tiene permisos para revocar asignaciones en este sector",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_UNAUTHORIZED_EXTENSION(
            "RN-AUTH-008","error.assignment.unauthorized.extension",
            "No autorizado: el rol no puede extender asignaciones en este sector",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_UNAUTHORIZED_READ(
            "RN-AUTH-009","error.assignment.unauthorized.read",
            "No autorizado: el rol no puede consultar asignaciones en este sector",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    // CORREGIDO: valores vacíos → valores reales
    ERR_AUTH_SYSTEM_ERROR(
            "RN-AUTH-010","error.auth.system.error",
            "Error interno en el módulo de autorización",
            HttpStatus.INTERNAL_SERVER_ERROR, ErrorSeverity.FATAL);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    AuthorizationError(String code, String messageKey, String defaultMessage,
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
