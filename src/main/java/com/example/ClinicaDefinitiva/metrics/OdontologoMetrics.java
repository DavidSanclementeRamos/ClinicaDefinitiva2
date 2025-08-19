package com.example.ClinicaDefinitiva.metrics;


import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;


@Component
public class OdontologoMetrics {


    private final MeterRegistry meterRegistry;

    public OdontologoMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void contarOdontologoNoEncontrado(String requestId) {
        meterRegistry.counter("clinica.odontologo.not_found",
                "entidad", "ODONTOLOGO",
                "requestId", requestId
        ).increment();
    }

    public void contarOdontologoRecuperado(String requestId) {
        meterRegistry.counter("clinica.odontologo.retrieved",
                "entidad", "ODONTOLOGO",
                "requestId", requestId
        ).increment();
    }

    public void contarOdontologoActualizado(String requestId) {
        meterRegistry.counter("clinica.odontologo.updated",
                "entidad", "ODONTOLOGO",
                "requestId", requestId
        ).increment();
    }

    public void contarOdontologoInativo(String requestId) {
        meterRegistry.counter("clinica.odontologo.status",
                "entidad", "ODONTOLOGO",
                "requestId", requestId
        ).increment();
    }

    public void contarOdontologoCreados(String requestId) {
        meterRegistry.counter("clinica.odontologo.create",
                "entidad", "ODONTOLOGO",
                "requestId", requestId
        ).increment();
    }
}


