package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;


import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Contexto de seguridad que contiene toda la información necesaria
 * para evaluar permisos RBAC y ABAC
 */
public class SecurityContext {
    private final Permission permission;
    private final UserIdentityId requestingUserIdentityId;
    private final Map<String, Object> attributes;

    private SecurityContext(Builder builder) {
        this.permission = builder.permission;
        this.requestingUserIdentityId = builder.requestingUserIdentityId;
        this.attributes = builder.attributes;
    }

    public Permission getPermission() { return permission; }
    public UserIdentityId getRequestingUserId() { return requestingUserIdentityId; }

    public <T> Optional<T> getAttribute(String key, Class<T> type) {
        Object value = attributes.get(key);
        if (value != null && type.isInstance(value)) {
            return Optional.of(type.cast(value));
        }
        return Optional.empty();
    }

    public static Builder builder(Permission permission, UserIdentityId userIdentityId) {
        return new Builder(permission, userIdentityId);
    }

    public static class Builder {
        private final Permission permission;
        private final UserIdentityId requestingUserIdentityId;
        private final Map<String, Object> attributes = new HashMap<>();

        public Builder(Permission permission, UserIdentityId userIdentityId) {
            this.permission = permission;
            this.requestingUserIdentityId = userIdentityId;
        }

        public Builder withAttribute(String key, Object value) {
            attributes.put(key, value);
            return this;
        }

        // Atributos comunes para ABAC
        public Builder withResourceOwnerId(UserIdentityId ownerId) {
            return withAttribute("resourceOwnerId", ownerId);
        }

        public Builder withResourceId(Long resourceId) {
            return withAttribute("resourceId", resourceId);
        }

        public Builder withSector(String sector) {
            return withAttribute("sector", sector);
        }

        public Builder withSpecialty(String specialty) {
            return withAttribute("specialty", specialty);
        }

        public Builder withPatientGuardianId(Long guardianId) {
            return withAttribute("patientGuardianId", guardianId);
        }

        public Builder withDentistSpecialties(java.util.Set<String> specialties) {
            return withAttribute("dentistSpecialties", specialties);
        }

        public SecurityContext build() {
            return new SecurityContext(this);
        }
    }
}