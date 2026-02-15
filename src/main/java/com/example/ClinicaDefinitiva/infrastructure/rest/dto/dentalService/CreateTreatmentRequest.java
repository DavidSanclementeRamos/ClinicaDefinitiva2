package com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService;


import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentPhaseDto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * Record para la creación de un tratamiento clínico.
 * Incluye validaciones de negocio en cada campo.
 */
public record CreateTreatmentRequest(

        Long patientId,
        Long dentistId,
        Long serviceId,
        LocalDate startDate,
        LocalDate expectedEndDate,
        List<TreatmentPhaseRest> phases,
        String notes,
        Long rateId
) {}

