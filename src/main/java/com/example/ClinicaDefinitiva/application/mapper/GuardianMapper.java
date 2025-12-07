package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.application.dto.actor.guardian.ReadGuardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuardianMapper {
   public ReadGuardian toGuardian(Guardian guardian){
        ReadGuardian dto = new ReadGuardian();
        dto.setGuardianId(guardian.getGuardianId().getVauel() );
        dto.setPerson(guardian.getPerson()  == null ? null: guardian.getPerson());
       // dto.setPatientList(guardian.getPatientList()  == null ? null: guardian.getPatientList());
       List<ReadPatientDto> patients = (guardian.getPatientList() == null)
               ? Collections.emptyList()
               : guardian.getPatientList()
               .stream()
               .map(PatientMapper::toReadPatientDto)
               .collect(Collectors.toList());

       dto.setPatientList(patients);

// EL user aun no tiene un vo id, cambiar por que tiene Long
       dto.setUserId(guardian.getUser().getId());
        dto.setLastUpdate(guardian.getLastUpdate());
        dto.setTypeGuardian(guardian.getTypeGuardian());
        dto.setSchedule(guardian.getSchedule());
        return dto;
    }
}
