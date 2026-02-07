package com.example.ClinicaDefinitiva.application.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.DomainContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;

public class UserIdentityNoFoundException extends BusinessRuleViolationException {


    public UserIdentityNoFoundException(ErrorCatalog catalogo, DomainContext contexto, UserId userId) {
        super(catalogo, contexto);
    }
}