package com.example.ClinicaDefinitiva.infrastructure.rest.schedule.mapper;

import com.example.ClinicaDefinitiva.application.schedule.dto.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto.ReadAppointmentResponse;
import org.springframework.stereotype.Component;

@Component
public class AppointmentRestReadMapper {

    public  ReadAppointmentResponse toRest(ReadAppointmentDto dto) {
        return new ReadAppointmentResponse(
                dto.appointmentId(),
                dto.dentistId(),
                dto.patientId(),
                dto.ServiceId(),
                dto.start(),
                dto.end(),
                dto.status(),
                dto.reason(),
                dto.appointmentType(),
                dto.clinicalNotes(),
                dto.actualDuration(),
                dto.creationDate(),
                dto.lastUpdated()
        );
    }
}
