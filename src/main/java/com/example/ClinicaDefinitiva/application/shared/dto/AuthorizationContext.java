package com.example.ClinicaDefinitiva.application.shared.dto;

import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Contexto de autorización con builder fluido para atributos ABAC.
 * 
 * Este objeto encapsula los atributos contextuales necesarios para
 * evaluar políticas de autorización complejas (ABAC).
 * 
 * Ejemplos de uso:
 * 
 * 1. Operación simple (solo sector):
 * AuthorizationContext.builder().build()  // No atributos adicionales
 * 
 * 2. Operación con ownership:
 * AuthorizationContext.builder()
 *     .withResourceId(companyId)
 *     .withOwnership(companyOwnerId)
 *     .build()
 * 
 * 3. Operación compleja (guardianship + sector):
 * AuthorizationContext.builder()
 *     .withResourceId(patientId)
 *     .withPatientGuardianId(guardianId)
 *     .build()
 * 
 * 4. Operación por especialidad:
 * AuthorizationContext.builder()
 *     .withDentistSpecialties(Set.of("ORTODONCIA", "ENDODONCIA"))
 *     .build()
 */
public class AuthorizationContext {
    
    private final Long resourceId;
    private final UserIdentityId resourceOwnerId;
    private final Long patientGuardianId;
    private final UserIdentityId assignedDentistUserId;
    private final Set<String> dentistSpecialties;
    private final Map<String, Object> additionalAttributes;
    
    private AuthorizationContext(Builder builder) {
        this.resourceId = builder.resourceId;
        this.resourceOwnerId = builder.resourceOwnerId;
        this.patientGuardianId = builder.patientGuardianId;
        this.assignedDentistUserId = builder.assignedDentistUserId;
        this.dentistSpecialties = builder.dentistSpecialties;
        this.additionalAttributes = builder.additionalAttributes;
    }
    
    // Getters
    public Long getResourceId() { return resourceId; }
    public UserIdentityId getResourceOwnerId() { return resourceOwnerId; }
    public Long getPatientGuardianId() { return patientGuardianId; }
    public UserIdentityId getAssignedDentistUserId() { return assignedDentistUserId; }
    public Set<String> getDentistSpecialties() { return dentistSpecialties; }
    public Map<String, Object> getAdditionalAttributes() { return additionalAttributes; }
    
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder fluido para construir AuthorizationContext de forma legible.
     */
    public static class Builder {
        private Long resourceId;
        private UserIdentityId resourceOwnerId;
        private Long patientGuardianId;
        private UserIdentityId assignedDentistUserId;
        private Set<String> dentistSpecialties;
        private Map<String, Object> additionalAttributes = new HashMap<>();
        
        /**
         * ID del recurso objetivo (para operaciones sobre recursos específicos).
         * Ejemplo: companyId, patientId, invoiceId
         */
        public Builder withResourceId(Long resourceId) {
            this.resourceId = resourceId;
            return this;
        }
        
        /**
         * Dueño del recurso (para OwnershipPolicy).
         * Ejemplo: userId del paciente que posee su propio registro
         */
        public Builder withOwnership(UserIdentityId resourceOwnerId) {
            this.resourceOwnerId = resourceOwnerId;
            return this;
        }
        
        /**
         * Guardian del paciente (para GuardianshipPolicy).
         * Ejemplo: guardianId del tutor de un paciente menor de edad
         */
        public Builder withPatientGuardianId(Long patientGuardianId) {
            this.patientGuardianId = patientGuardianId;
            return this;
        }
        
        /**
         * Dentista asignado a un recurso (para AssignmentPolicy).
         * Ejemplo: dentista asignado a un tratamiento o cita
         */
        public Builder withAssignedDentist(UserIdentityId assignedDentistUserId) {
            this.assignedDentistUserId = assignedDentistUserId;
            return this;
        }
        
        /**
         * Especialidades del dentista (para SpecialtyBasedPolicy).
         * Ejemplo: Set.of("ORTODONCIA", "ENDODONCIA")
         */
        public Builder withDentistSpecialties(Set<String> dentistSpecialties) {
            this.dentistSpecialties = dentistSpecialties;
            return this;
        }
        
        /**
         * Atributo adicional genérico (para casos no cubiertos).
         * Evitar usar si hay un método específico disponible.
         */
        public Builder withAttribute(String key, Object value) {
            this.additionalAttributes.put(key, value);
            return this;
        }
        
        public AuthorizationContext build() {
            return new AuthorizationContext(this);
        }
    }

    
}
