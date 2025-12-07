package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.ReadDentistDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import org.springframework.stereotype.Component;

@Component
public class DentistMapper {
    public ReadDentistDto toDentist(Dentist dentist){
        ReadDentistDto dto = new ReadDentistDto();
        dto.setDentistId( dentist.getDentistId() == null ? null : dentist.getDentistId().toString());
        dto.setUser(dentist.getUser()  == null ? null : String.valueOf(dentist.getUser()));
        dto.setAvailabilityList(dentist.getAvailabilityList() == null ? null : dentist.getAvailabilityList());
        dto.setAvailabilityStatus(dentist.getAvailabilityStatus() == null ? null : dentist.getAvailabilityStatus());
        dto.setSchedule(dentist.getSchedule()== null ? null : dentist.getSchedule());
        dto.setSpecialties(dentist.getSpecialties() == null ? null : dentist.getSpecialties());
        dto.setLastUpdate(dentist.getLastUpdate() == null ? null : dentist.getLastUpdate());
        dto.setWorkingHours(dentist.getWorkingHours() == null ? null : dentist.getWorkingHours());
        dto.setPersonData(dentist.getPersonData() == null ? null : dentist.getPersonData());
        dto.setTimeSlotList(dentist.getTimeSlotList()== null ? null : dentist.getTimeSlotList());
        return dto;
    }
}
