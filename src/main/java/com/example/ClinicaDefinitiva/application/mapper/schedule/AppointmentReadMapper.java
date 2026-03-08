package com.example.ClinicaDefinitiva.application.mapper.schedule;

import com.example.ClinicaDefinitiva.application.dto.scheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import org.springframework.stereotype.Component;


@Component
public class AppointmentReadMapper {

    public ReadAppointmentDto toReadDto(Appointment appointment) {
        return new ReadAppointmentDto(
            appointment.getId().getValue(),
            appointment.getDentistId().value(),
            appointment.getPatientId().value(),
            appointment.getServiceId().getId(),
            appointment.getStart(),
            appointment.getEnd(),
            appointment.getStatus().toString(),
            appointment.getReason(),
            appointment.getAppointmentType().toString(),
            extractClinicalNotes(appointment),
            extractActualDuration(appointment),
            appointment.getCreationDate(),
            appointment.getLastUpdated()
        );
    }

    private String extractClinicalNotes(Appointment appointment) {
        return appointment.getCompletion().getClinicalNotes();
    }

    private String extractActualDuration(Appointment appointment) {
        return String.valueOf(appointment.getCompletion().getActualDuration().getMinutes());
    }
}
