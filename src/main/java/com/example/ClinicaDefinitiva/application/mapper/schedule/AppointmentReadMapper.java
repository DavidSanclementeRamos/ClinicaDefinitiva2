package com.example.ClinicaDefinitiva.application.mapper.schedule;

import com.example.ClinicaDefinitiva.application.dto.sheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentReadMapper {

    public  ReadAppointmentDto toReadDto(Appointment appointment) {
        return new ReadAppointmentDto(
                appointment.getId().getValue(),
                appointment.getDentistId().value(),
                appointment.getPatientId().getValue(),
                appointment.getServiceId().getId(),
                appointment.getStart(),
                appointment.getEnd(),
                appointment.getStatus().toString(),
                appointment.getReason(),
                appointment.getAppointmentType().toString(),
                appointment.getCompletion() != null ? appointment.getCompletion().getClinicalNotes() : null,
                appointment.getCompletion() != null ?
                        String.valueOf(appointment.getCompletion().getActualDuration().getMinutes()) : null,
                appointment.getCreationDate(),
                appointment.getLastUpdated()
        );
    }
}
