package com.example.ClinicaDefinitiva.application.mapper.actor.dentist;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import org.springframework.stereotype.Component;



@Component
public class DentistReadMapper {

    // dominio → DTO de lectura
    public ReadDentistDto toReadDto(Dentist dentist) {
        return new ReadDentistDto(
                dentist.getDentistId().value(),
                dentist.getSpecialties().toString(),
                dentist.getAvailabilityStatus().toString(),
                 new WorkingHoursDto(dentist.getWorkingHours().getStart(),
                 dentist.getWorkingHours().getEnd(), dentist.getWorkingHours().getDayOfWeek(), dentist.getWorkingHours().getDeclaredHoursPerWeek()),

                dentist.getPersonData().getDni().toString(),
                dentist.getPersonData().getFullname().FirstName(),
                dentist.getPersonData().getFullname().LastName(),
                dentist.getPersonData().getAge().toString(),
                dentist.getPersonData().getPhoneNumber().toString(),
                dentist.getPersonData().getDateOfBirth().asDate(),
                dentist.getPersonData().getBloodType().getValue(),
                dentist.getPersonData().getDocumentoEPS(),
                dentist.getUserId().value().toString(),
                dentist.getLastUpdate(),
                dentist.getPersonData().getAddress().Street(),
                dentist.getPersonData().getAddress().City(),
                dentist.getPersonData().getAddress().State(),
                dentist.getPersonData().getAddress().Country(),
                dentist.getPersonData().getAddress().PostalCode());

    }

    // dominio → DTO de lectura
    public PageDentistDto toPageDto(Dentist dentist) {
        return new PageDentistDto(
                dentist.getDentistId().value(),
                dentist.getSpecialties().toString(),
                dentist.getPersonData().getDni().toString(),
                dentist.getPersonData().getFullname().FirstName(),
                dentist.getPersonData().getFullname().LastName(),
                dentist.getPersonData().getPhoneNumber().Value(),
                dentist.getAvailabilityStatus().toString()

                );
    }


}


