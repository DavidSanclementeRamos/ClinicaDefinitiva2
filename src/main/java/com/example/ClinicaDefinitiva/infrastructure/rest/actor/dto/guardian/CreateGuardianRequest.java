package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian;

import com.example.ClinicaDefinitiva.application.actor.dto.patient.PagePatientDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateGuardianRequest(

        //TypeGuardian
        String code,
        String description,

        // Person
       String dni,
             String first,
             String lastName,
             String age,
             String phoneNumber,

             LocalDate dateOfBirth,
             String bloodType,
             String documentoEPS,
             Long user,
             LocalDateTime lastUpdate,

             // Address
             String street,
             String city,
             String state,
             String country,
             String postalCode
){
}

