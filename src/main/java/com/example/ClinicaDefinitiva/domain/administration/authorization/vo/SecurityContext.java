package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;


import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Contexto de seguridad que contiene toda la información necesaria
 * para evaluar permisos RBAC y ABAC
 */
public class SecurityContext {
    private final Permission permission;
    private final UserId requestingUserId;
    private final Map<String, Object> attributes;

    private SecurityContext(Builder builder) {
        this.permission = builder.permission;
        this.requestingUserId = builder.requestingUserId;
        this.attributes = builder.attributes;
    }

    public Permission getPermission() { return permission; }
    public UserId getRequestingUserId() { return requestingUserId; }

    public <T> Optional<T> getAttribute(String key, Class<T> type) {
        Object value = attributes.get(key);
        if (value != null && type.isInstance(value)) {
            return Optional.of(type.cast(value));
        }
        return Optional.empty();
    }

    public static Builder builder(Permission permission, UserId userId) {
        return new Builder(permission, userId);
    }

    public static class Builder {
        private final Permission permission;
        private final UserId requestingUserId;
        private final Map<String, Object> attributes = new HashMap<>();

        public Builder(Permission permission, UserId userId) {
            this.permission = permission;
            this.requestingUserId = userId;
        }

        public Builder withAttribute(String key, Object value) {
            attributes.put(key, value);
            return this;
        }

        // Atributos comunes para ABAC
        public Builder withResourceOwnerId(UserId ownerId) {
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