package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

public final class ActionCatalog {

    public enum BasicAction {
        CREATE, READ, UPDATE, DELETE,
        APPROVE, CANCEL, CONFIRM, COMPLETE,
        DEACTIVATE, REACTIVATE, ASSIGN, SCHEDULE,
        RESCHEDULE, POST, REVERSE,SUSPEND,

        AUTHENTICATE,
        //ROL PERMISIONES

        VIEW_ROLE,
        CREATE_CUSTOM_ROLE,
        CLONE_ROLE,
        ADD_PERMISSION,
        REMOVE_PERMISSION,
        SET_PERMISSIONS,
        CHECK_PERMISSION,
        DELETE_ROLE,
        ACTIVATE_ROLE, DEACTIVATE_ROLE, SUSPEND_ROLE, MARK_DELETED_ROLE,

        // ASSIGNMENT
        CREATE_TEMPORARY,
        REVOKE_ALL,
        REVOKE,
        REVOKE_ASSIGNMENT,
        UPDATE_PRIMARY,
        EXTEND_ASSIGNMENT,
        IS_ACTIVE_AT,
        IS_CURRENTLY_ACTIVE,
        VIEW_ASSIGNMENT

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

