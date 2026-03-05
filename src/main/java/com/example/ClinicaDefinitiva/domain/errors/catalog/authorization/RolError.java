package com.example.ClinicaDefinitiva.domain.errors.catalog.authorization;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;

/**
 * CATÁLOGO DE ERRORES - MÓDULO AUTORIZACIÓN v1.0
 * Agregado: Rol
 */
public enum RolError implements ErrorCatalog {


    // si
    ERR_ROL_DUPLICATE_DESCRIPTION(
            "RN-ROL-001",
            "error.rol.duplicate.description",
            "Los roles personalizados deben tener descripción única"
    ),

    // si
    RR_ROL_EMPTY_PERMISSIONS(
            "RN-ROL-002",
            "error.rol.empty.permissions",
            "Un rol editable debe tener al menos 1 permiso"
    ),
    // si
    ERR_ROL_SYSTEM_NOT_EDITABLE(
            "RN-ROL-003",
            "error.rol.system.not.editable",
            "No se pueden modificar los permisos de un rol no editable" ),

    
    // posponer hasta elegir entre eliminacion fisica a desactivacion

    ERR_ROL_DELETE_REASON_REQUIRED(
            "RN-ROL-004",
            "error.rol.delete.reason.required",
            "La eliminación de rol requiere motivo obligatorio (mínimo 10 caracteres)"
    ),


    // si
    ERR_ROL_SYSTEM_NOT_DELETABLE(
            "RN-ROL-005",
            "error.rol.system.not.deletable",
            "No se pueden eliminar un rol que esta marcado como no deletable "
    ),
    // SI
    ERR_ROL_NOT_FOUND(
            "RN-ROL-06",
            "error.rol.not.found",
            "El rol solicitado no existe en el sistema" ),
    
ERR_ROL_DELETE_NOT_MARKED(
        "RN-ROL-007",
        "error.rol.deleteNotMarked",
        "El rol debe estar marcado para poder eliminarse"
);    
    ;

    private final String code;
    private final String messageKey;
    private final String defaultMessage;

    RolError(String code, String messageKey, String defaultMessage) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultMessage = defaultMessage;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessageKey() { return messageKey; }
    @Override public String getDefaultMessage() { return defaultMessage; }
}

