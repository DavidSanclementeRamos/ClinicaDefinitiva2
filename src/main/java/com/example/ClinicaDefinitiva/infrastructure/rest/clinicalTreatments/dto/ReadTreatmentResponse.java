package com.example.ClinicaDefinitiva.infrastructure.rest.clinicalTreatments.dto;



import java.time.LocalDate;
import java.util.List;

/**
 * DTO de response con información completa del tratamiento
 */

public record ReadTreatmentResponse (

     Long id,
     Long patientId,
     Long dentistId,
     Long serviceId,
     String status,
     LocalDate startDate,
     LocalDate expectedEndDate,
     LocalDate actualEndDate,
     List<TreatmentPhaseRest> phases,
     String notes,
     Long rateId























){}

