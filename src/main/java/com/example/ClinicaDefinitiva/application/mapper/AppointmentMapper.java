package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.application.dto.sheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentMapper {
    public ReadAppointmentDto toAppointmentDto(Appointment appointment){
        ReadAppointmentDto dto = new ReadAppointmentDto();

        dto.setAppointmentId( appointment.getId() == null ? null : appointment.getId().toString());
        dto.setDentist( appointment.getDentist() == null ? null : appointment.getDentist().toString());
        dto.setPatient(appointment.getPatientId() == null ? null : appointment.getPatient().toString());
        dto.setStart(appointment.getStart() == null ? null : appointment.getStart());
        dto.setEnd(appointment.getEnd() == null ? null : appointment.getEnd());
        dto.setStatus(appointment.getStatus()  == null ? null : appointment.getStatus());
        dto.setReason(appointment.getReason()  == null ? null : appointment.getReason());
        dto.setAppointmentType(appointment.getAppointmentType()  == null ? null : appointment.getAppointmentType());
        dto.setClinicalNotes(appointment.getClinicalNotes()  == null ? null : appointment.getClinicalNotes());
        dto.setActualDuration(appointment.getActualDuration()  == null ? null : appointment.getActualDuration());
        dto.setAttendedBy(appointment.getAttendedBy()  == null ? null : appointment.getAttendedBy());
        dto.setCreationDate(appointment.getCreationDate()  == null ? null : appointment.getCreationDate());
        dto.setLastUpdated(appointment.getLastUpdated()  == null ? null : appointment.getLastUpdated());


      return dto;
    }
}
