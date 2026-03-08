package com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.authorization;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

public enum RolError implements ErrorCatalog {

    ERR_ROL_DUPLICATE_DESCRIPTION(
            "RN-ROL-001","error.rol.duplicate.description",
            "Los roles personalizados deben tener descripción única",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR),

    RR_ROL_EMPTY_PERMISSIONS(
            "RN-ROL-002","error.rol.empty.permissions",
            "Un rol editable debe tener al menos 1 permiso",
            HttpStatus.UNPROCESSABLE_ENTITY, ErrorSeverity.ERROR),

    ERR_ROL_SYSTEM_NOT_EDITABLE(
            "RN-ROL-003","error.rol.system.not.editable",
            "No se pueden modificar los permisos de un rol no editable",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    ERR_ROL_DELETE_REASON_REQUIRED(
            "RN-ROL-004","error.rol.delete.reason.required",
            "La eliminación de rol requiere motivo obligatorio (mínimo 10 caracteres)",
            HttpStatus.BAD_REQUEST, ErrorSeverity.WARN),

    ERR_ROL_SYSTEM_NOT_DELETABLE(
            "RN-ROL-005","error.rol.system.not.deletable",
            "No se pueden eliminar un rol que esta marcado como no deletable",
            HttpStatus.FORBIDDEN, ErrorSeverity.ERROR),

    // CORREGIDO: "RN-ROL-06" → "RN-ROL-006"
    ERR_ROL_NOT_FOUND(
            "RN-ROL-006","error.rol.not.found",
            "El rol solicitado no existe en el sistema",
            HttpStatus.NOT_FOUND, ErrorSeverity.ERROR),

    ERR_ROL_DELETE_NOT_MARKED(
            "RN-ROL-007","error.rol.deleteNotMarked",
            "El rol debe estar marcado para poder eliminarse",
            HttpStatus.CONFLICT, ErrorSeverity.ERROR);

    private final String code;
    private final String messageKey;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;

    RolError(String code, String messageKey, String defaultMessage,
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