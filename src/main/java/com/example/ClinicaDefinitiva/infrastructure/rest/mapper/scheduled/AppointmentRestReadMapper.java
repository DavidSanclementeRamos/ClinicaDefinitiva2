package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.scheduled;

import com.example.ClinicaDefinitiva.application.dto.scheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.schedule.ReadAppointmentResponse;

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
