package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

import java.time.LocalDate;

/**
 * DTO para extender contrato
 */
public record ExtendContractRequest(
        LocalDate newEndDate
) {}
