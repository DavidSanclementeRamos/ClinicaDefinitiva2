package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

public final class ActionCatalog {

    public enum BasicAction {
        CREATE, READ, UPDATE, DELETE,
        APPROVE, CANCEL, CONFIRM, COMPLETE,
        DEACTIVATE, ASSIGN, SCHEDULE,
        RESCHEDULE, POST, REVERSE,SUSPEND,
        MARK_AS_NO_SHOW, ACTIVATE,
        APPLY_INCAPACITY,
        APPLY_VACATION,

        AUTHENTICATE,

        CREATE_CUSTOM,
        CLONE,
        ADD,
        REMOVE,
        SET_PERMISSIONS,
        CHECK,
         MARK_DELETED,

        CREATE_TEMPORARY,
        CREATE_PERMANENT,
        REVOKE_ALL,
        REVOKE,
        
        UPDATE_PRIMARY,
        EXTEND,
        IS_ACTIVE_AT,
        IS_CURRENTLY_ACTIVE,

    }

    private final String code;

    private ActionCatalog(String code) {
        this.code = code.toUpperCase();
    }

    // Factory para valores básicos
    public static ActionCatalog of(BasicAction action) {
        return new ActionCatalog(action.name());
    }

    // Factory para valores dinámicos (ej. cargados desde BD)
    public static ActionCatalog custom(String action) {
        return new ActionCatalog(action);
    }

    public String getCode() { return code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionCatalog)) return false;
        ActionCatalog that = (ActionCatalog) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(code);
    }
}

