package com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum UserRolAssignmentError implements ErrorCatalog {

    ERR_ASSIGNMENT_DUPLICATE_ACTIVE(
            "RN-ASSIGNMENT-001","error.assignment.duplicate.active",
            "Un usuario no puede tener dos asignaciones del mismo rol activas",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_VALID_FROM_REQUIRED(
            "RN-ASSIGNMENT-002","error.assignment.valid.from.required",
            "La fecha de inicio (validFrom) no puede ser nula",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ASSIGNMENT_TEMPORARY_CANNOT_BE_PRIMARY(
            "RN-ASSIGNMENT-003","error.assignment.temporary.primary",
            "Los roles temporales no pueden ser primarios",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_INVALID_DATE_RANGE(
            "RN-ASSIGNMENT-004","error.assignment.date.range",
            "validFrom no puede ser posterior a validTo",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    // CORREGIDO: "RN-ASSIGNMENT-0O5" (letra O) → "RN-ASSIGNMENT-005"
    ERR_ASSIGNMENT_INVALID_EXTENSION_DATE(
            "RN-ASSIGNMENT-005","error.assignment.extension.date",
            "La nueva fecha fin debe ser posterior a la actual",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ASSIGNMENT_CANNOT_REVOKE_LAST_INDIVIDUAL(
            "RN-ASSIGNMENT-006","error.assignment.revoke.last.individual",
            "No puede revocar el último rol activo de un usuario de forma individual; use la opción de revocación total",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_UNAUTHORIZED(
            "RN-ASSIGNMENT-007","error.assignment.unauthorized",
            "Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede asignar roles",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_UNAUTHORIZED_REVOKE(
            "RN-ASSIGNMENT-008","error.assignment.revoke.unauthorized",
            "Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede revocar roles",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_INACTIVE_ROLE(
            "RN-ASSIGNMENT-009","error.assignment.inactive.role",
            "No puede asignar rol INACTIVE",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_ASSIGNMENT_CANNOT_EXTEND_PERMANENT(
            "RN-ASSIGNMENT-010","error.assignment.extend.permanent",
            "La extensión de vigencia solo aplica a roles temporales",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    UserRolAssignmentError(String code, String messageKey, String defaultMessage,
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