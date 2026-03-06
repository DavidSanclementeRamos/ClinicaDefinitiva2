package com.example.ClinicaDefinitiva.domain.errors.catalog;

import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.http.HttpStatus;

public interface ErrorCatalog {
    String getCode();
    String getMessageKey();
    String getDefaultMessage();
    HttpStatus getSuggestedHttpStatus();   
    ErrorSeverity getSeverity(); 
 

}
