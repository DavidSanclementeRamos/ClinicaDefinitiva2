package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist;
/**
 * DTO para actualizar estado
 */
public record DentistUpdateStatusRequest(
        //Long dentistId,
        String availabilityStatus) { }
