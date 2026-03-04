package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.event.AuthorizationAuditEvent;

/**
 * Interface para logging de auditoría de autorización.
 * 
 * Responsabilidades:
 * - Registrar TODAS las decisiones de autorización (ALLOW y DENY)
 * - Almacenar en formato estructurado (JSON)
 * - Facilitar compliance (SOC2, ISO 27001, GDPR)
 * - Permitir análisis de seguridad y debugging
 */
public interface AuditLogger {
    
    /**
     * Registra un evento de autorización.
     * 
     * @param event Evento con toda la información de la decisión
     */
    void logAuthorizationDecision(AuthorizationAuditEvent event);
    
    /**
     * Registra un error durante la evaluación de autorización.
     * Útil para detectar problemas de configuración o bugs.
     * 
     * @param userId Usuario que intentó la operación
     * @param resource Recurso objetivo
     * @param action Acción solicitada
     * @param error Excepción capturada
     */
    void logAuthorizationError(Long userId, String resource, String action, Exception error);
}


