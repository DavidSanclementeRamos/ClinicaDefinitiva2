package com.example.ClinicaDefinitiva.domain.util;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.DomainContext;

import java.util.Objects;

/**
 * Representa un detalle específico de error o advertencia en un Outcome.
 *
 * Contiene:
 * - Código de error del catálogo
 * - Severidad (ERROR, WARNING, INFO)
 * - Categoría (CLINICO, ADMINISTRATIVO, TECNICO)
 *
 * Es inmutable y comparable por valor.
 */
public class OutcomeDetail {
    private final ErrorCatalog code;
    private final Severity severity;
    private final Category category;
    private final DomainContext context;


    public OutcomeDetail(ErrorCatalog code, Severity severity, Category category, DomainContext context) {
        this.context = context;

        Objects.requireNonNull(code, "Codigo cannot be null");
        Objects.requireNonNull(severity, "Severidad cannot be null");
        Objects.requireNonNull(category, "Categoria cannot be null");

        this.code = code;
        this.severity = severity;
        this.category = category;
    }


    public ErrorCatalog getCode() {
        return code;
    }


    public Severity getSeverity() {
        return severity;
    }

    public DomainContext getContext() {
        return context;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isError() {
        return severity == Severity.ERROR;
    }

    public boolean isWarning() {
        return severity == Severity.WARNING;
    }

    public boolean isInfo() {
        return severity == Severity.INFO;
    }

    public boolean isCategory(Category category) {
        return this.category == category;
    }

}