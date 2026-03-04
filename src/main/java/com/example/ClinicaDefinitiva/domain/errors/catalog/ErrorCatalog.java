package com.example.ClinicaDefinitiva.domain.errors.catalog;

import org.springframework.http.HttpStatus;

public interface ErrorCatalog {
    String getCode();
    String getMessageKey();
    String getDefaultMessage();
 




}
