package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

public final class Permission {

    private final ResourceCatalog resource;
    private final ActionCatalog action;

    private Permission(ResourceCatalog resource, ActionCatalog action) {
        this.resource = resource;
        this.action = action;
    }

 

    // Factory genérico
    public static Permission of(ResourceCatalog resource, ActionCatalog action) {
        return new Permission(resource, action);
    }

    public ResourceCatalog getResource() { return resource; }
    public ActionCatalog getAction() { return action; }

    public String getCode() {
        return action.getCode() + "_" + resource.getCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return resource.equals(that.resource) && action.equals(that.action);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(resource, action);
    }
}
