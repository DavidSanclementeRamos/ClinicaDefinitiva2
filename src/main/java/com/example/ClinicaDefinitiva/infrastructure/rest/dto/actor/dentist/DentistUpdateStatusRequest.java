package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist;
/**
 * DTO para actualizar estado
 */
public record DentistUpdateStatusRequest(
        //Long dentistId,
        String availabilityStatus) { }
