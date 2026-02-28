
package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.event.AuthorizationAuditEvent;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementación por defecto de AuditLogger usando SLF4J.
 * 
 * Los logs se escriben en formato JSON estructurado para facilitar:
 * - Parsing con herramientas como ELK (Elasticsearch, Logstash, Kibana)
 * - Análisis con Splunk o Datadog
 * - Exportación para compliance
 * 
 * Configuración recomendada en logback-spring.xml:
 * 
 * <logger name="AUTHORIZATION_AUDIT" level="INFO" additivity="false">
 *     <appender-ref ref="AUTHORIZATION_AUDIT_FILE"/>
 * </logger>
 * 
 * Esto separa logs de auditoría de logs de aplicación normales.
 */
@Service
public class DefaultAuditLogger implements AuditLogger {
    
    // Logger dedicado para auditoría (separado de logs de aplicación)
    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger("AUTHORIZATION_AUDIT");
    
    // Logger normal para errores
    private static final Logger ERROR_LOGGER = LoggerFactory.getLogger(DefaultAuditLogger.class);
    
    @Override
    public void logAuthorizationDecision(AuthorizationAuditEvent event) {
        if (event.getDecision() == AuthorizationAuditEvent.Decision.ALLOW) {
            // Decisiones permitidas: INFO level
            AUDIT_LOGGER.info("Authorization decision: {}", event.toJson());
        } else {
            // Decisiones denegadas: WARN level (más visibles para seguridad)
            AUDIT_LOGGER.warn("Authorization DENIED: {}", event.toJson());
        }
        
        
    }
    
    @Override
    public void logAuthorizationError(Long userId, String resource, String action, Exception error) {
        ERROR_LOGGER.error(
            "Authorization error - userId: {}, resource: {}, action: {}, error: {}",
            userId,
            resource,
            action,
            error.getMessage(),
            error
        );
        

    }
}

