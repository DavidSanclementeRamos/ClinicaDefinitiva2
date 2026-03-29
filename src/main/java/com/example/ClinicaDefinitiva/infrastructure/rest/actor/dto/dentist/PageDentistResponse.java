package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist;

/**
 * DTO simplificado para listados
 */
public record PageDentistResponse(
   Long dentistId,
   String specialties,
   String dni,
   String first,
   String lastName,
   String phoneNumber,
   String availabilityStatus

){ }
