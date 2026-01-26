package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Specialties;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.WorkingHours;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

import java.time.LocalDateTime;
/**
 * DTO para actualizar información de contacto
 */
public record DentistUpdateContactRequest(
        Long dentistId,
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber

        ) {}
