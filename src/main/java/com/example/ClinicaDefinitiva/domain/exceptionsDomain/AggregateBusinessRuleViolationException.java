package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.DomainContext;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;

import java.util.List;

public class AggregateBusinessRuleViolationException extends ModelException {
    private final List<OutcomeDetail> detalles;

    public AggregateBusinessRuleViolationException(List<OutcomeDetail> detalles) {
        super(
                detalles != null && !detalles.isEmpty() ? detalles.get(0).getCode() : null,
                detalles != null && !detalles.isEmpty() ? detalles.get(0).getContext() : null
        );
        this.detalles = detalles != null ? List.copyOf(detalles) : List.of();
    }

    public List<OutcomeDetail> getDetalles() {
        return detalles;
    }
}

