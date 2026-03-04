
package com.example.ClinicaDefinitiva.domain.administration.authorization.event;


import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Evento de auditoría para decisiones de autorización.
 * 
 * Registra TODAS las decisiones (permitidas y denegadas) para:
 * - Compliance (SOC2, ISO 27001, GDPR)
 * - Debugging de permisos
 * - Análisis de seguridad
 * - Detección de ataques
 * 
 * Los logs se almacenan en formato JSON estructurado.
 */
public class AuthorizationAuditEvent {
    
    private final UserIdentityId userId;
    private final Long rolId;
    private final ResourceCatalog.BasicResource resource;
    private final ActionCatalog.BasicAction action;
    private final Decision decision;
    private final Instant timestamp;
    private final long durationMs;
    private final Map<String, Object> contextAttributes;
    private final String denyReason;
    
    private AuthorizationAuditEvent(Builder builder) {
        this.userId = builder.userId;
        this.rolId = builder.rolId;
        this.resource = builder.resource;
        this.action = builder.action;
        this.decision = builder.decision;
        this.timestamp = builder.timestamp;
        this.durationMs = builder.durationMs;
        this.contextAttributes = builder.contextAttributes;
        this.denyReason = builder.denyReason;
    }
    
    public enum Decision {
        ALLOW,
        DENY
    }
    
    // Getters
    public UserIdentityId getUserId() { return userId; }
    public Long getRolId() { return rolId; }
    public ResourceCatalog.BasicResource getResource() { return resource; }
    public ActionCatalog.BasicAction getAction() { return action; }
    public Decision getDecision() { return decision; }
    public Instant getTimestamp() { return timestamp; }
    public long getDurationMs() { return durationMs; }
    public Map<String, Object> getContextAttributes() { return contextAttributes; }
    public String getDenyReason() { return denyReason; }
    
    /**
     * Serializa el evento a JSON para almacenamiento.
     * Formato estructurado facilita análisis con herramientas como ELK, Splunk, etc.
     */
    public String toJson() {
        return String.format(
            "{\"timestamp\":\"%s\",\"userId\":%d,\"rolId\":%d,\"resource\":\"%s\",\"action\":\"%s\",\"decision\":\"%s\",\"durationMs\":%d%s%s}",
            timestamp,
            userId.value(),
            rolId,
            resource.name(),
            action.name(),
            decision.name(),
            durationMs,
            contextAttributes.isEmpty() ? "" : ",\"context\":" + serializeContext(),
            denyReason != null ? ",\"denyReason\":\"" + denyReason + "\"" : ""
        );
    }
    
    private String serializeContext() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : contextAttributes.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                sb.append("\"").append(entry.getValue()).append("\"");
            } else {
                sb.append(entry.getValue());
            }
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private UserIdentityId userId;
        private Long rolId;
        private ResourceCatalog.BasicResource resource;
        private ActionCatalog.BasicAction action;
        private Decision decision;
        private Instant timestamp = Instant.now();
        private long durationMs;
        private Map<String, Object> contextAttributes = new HashMap<>();
        private String denyReason;
        
        public Builder userId(UserIdentityId userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder rolId(Long rolId) {
            this.rolId = rolId;
            return this;
        }
        
        public Builder resource(ResourceCatalog.BasicResource resource) {
            this.resource = resource;
            return this;
        }
        
        public Builder action(ActionCatalog.BasicAction action) {
            this.action = action;
            return this;
        }
        
        public Builder decision(Decision decision) {
            this.decision = decision;
            return this;
        }
        
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder durationMs(long durationMs) {
            this.durationMs = durationMs;
            return this;
        }
        
        public Builder contextAttribute(String key, Object value) {
            this.contextAttributes.put(key, value);
            return this;
        }
        
        public Builder denyReason(String denyReason) {
            this.denyReason = denyReason;
            return this;
        }
        
        public AuthorizationAuditEvent build() {
            if (userId == null || rolId == null || resource == null || action == null || decision == null) {
                throw new IllegalStateException("userId, rolId, resource, action, and decision are required");
            }
            return new AuthorizationAuditEvent(this);
        }
    }
}
