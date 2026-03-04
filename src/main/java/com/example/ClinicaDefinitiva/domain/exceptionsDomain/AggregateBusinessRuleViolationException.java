package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.catalog.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.errors.context.DomainContext;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;

import java.util.List;

public class AggregateBusinessRuleViolationException extends DomainAggregateException {

    private final List<OutcomeDetail> detalles;

    public AggregateBusinessRuleViolationException(List<OutcomeDetail> detalles) {
        super(
            primerCatalogo(detalles),   // ← validación ocurre aquí, dentro del argumento
            primerContexto(detalles),
            Map.of("totalViolaciones", detalles.size())
        );
        this.detalles = List.copyOf(detalles);
    }

    private static ErrorCatalog primerCatalogo(List<OutcomeDetail> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                "AggregateBusinessRuleViolationException requiere al menos un OutcomeDetail"
            );
        }
        return detalles.get(0).getCode();
    }

    private static DomainContext primerContexto(List<OutcomeDetail> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                "AggregateBusinessRuleViolationException requiere al menos un OutcomeDetail"
            );
        }
        return detalles.get(0).getContext();
    }

    public List<OutcomeDetail> getDetalles() {
        return detalles;
    }
}

