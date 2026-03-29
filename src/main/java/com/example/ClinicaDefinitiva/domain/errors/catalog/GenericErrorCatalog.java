package com.example.ClinicaDefinitiva.domain.errors.catalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

/**
 * Catálogo genérico para errores que no tienen un enum específico
 * Útil para errores temporales o de infraestructura
 */
public class GenericErrorCatalog implements ErrorCatalog {
    
    private final String code;
    private final String defaultMessage;
    private final HttpStatus suggestedHttpStatus;
    private final ErrorSeverity severity;
    
    public GenericErrorCatalog(String code, String defaultMessage, 
                               HttpStatus suggestedHttpStatus, ErrorSeverity severity) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.suggestedHttpStatus = suggestedHttpStatus;
        this.severity = severity;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getMessageKey() {
        return code.toLowerCase();
    }
    
    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
    
    @Override
    public HttpStatus getSuggestedHttpStatus() {
        return suggestedHttpStatus;
    }
    
    @Override
    public ErrorSeverity getSeverity() {
        return severity;
    }
}