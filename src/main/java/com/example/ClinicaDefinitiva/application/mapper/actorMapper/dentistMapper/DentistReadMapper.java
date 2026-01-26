package com.example.ClinicaDefinitiva.application.mapper.actorMapper.dentistMapper;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DentistReadMapper {

    // dominio → DTO de lectura
    public ReadDentistDto toDto(Dentist dentist) {
        return new ReadDentistDto(
                dentist.getDentistId().getValue(),
                dentist.getSpecialties().toString(),
                dentist.getAvailabilityStatus().toString(),
                dentist.getWorkingHours().getStart(),
                dentist.getWorkingHours().getEnd(),
                dentist.getWorkingHours().getDayOfWeek(),
                dentist.getWorkingHours().getDeclaredHoursPerWeek(),
                dentist.getPersonData().getDni().toString(),
                dentist.getPersonData().getFullname().FirstName(),
                dentist.getPersonData().getFullname().LastName(),
                dentist.getPersonData().getAge().toString(),
                dentist.getPersonData().getPhoneNumber().toString(),
                dentist.getPersonData().getDateOfBirth().asDate(),
                dentist.getPersonData().getBloodType().getValue(),
                dentist.getPersonData().getDocumentoEPS(),
                dentist.getUserId().getValue(),
                dentist.getLastUpdate(),
                dentist.getPersonData().getAddress().Street(),
                dentist.getPersonData().getAddress().City(),
                dentist.getPersonData().getAddress().State(),
                dentist.getPersonData().getAddress().Country(),
                dentist.getPersonData().getAddress().PostalCode());

    }

    // dominio → DTO de lectura
    public PageDentistDto pageToDto(Dentist dentist) {
        return new PageDentistDto(
                dentist.getDentistId().getValue(),
                dentist.getSpecialties().toString(),
                dentist.getPersonData().getDni().toString(),
                dentist.getPersonData().getFullname().FirstName(),
                dentist.getPersonData().getFullname().LastName(),
                dentist.getPersonData().getPhoneNumber().toString(),
                dentist.getAvailabilityStatus().toString()

                );
    }

}


