package com.example.ClinicaDefinitiva.application.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.DomainContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

public class UserIdentityNoFoundException extends RuntimeException {
    public UserIdentityNoFoundException(String message) {
        super(message);
    }
}