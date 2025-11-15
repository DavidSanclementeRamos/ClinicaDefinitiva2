package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.application.dto.Patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
   public  ReadPatientDto toReadPatientDto(Patient patient){
        ReadPatientDto dto = new ReadPatientDto();
       dto.setPatientId(patient.getPatientId());
       dto.setPerson(patient.getPerson());
       dto.setGuardianId(patient.getGuardianId());
       dto.setUser(String.valueOf(patient.getUser() != null ? patient.getUser().getId() : null));
       dto.setShift(patient.getShift());
       dto.setSchedule(patient.getSchedule());
       dto.setLastUpdate(patient.getLastUpdate());
       dto.setContractId(patient.getContractId());



       return dto;
    }
}
