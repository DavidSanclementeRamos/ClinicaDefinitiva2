package com.example.ClinicaDefinitiva.infrastructure.rest.schedule.mapper;


import com.example.ClinicaDefinitiva.application.schedule.dto.AppointmentCompletionDTO;
import com.example.ClinicaDefinitiva.application.schedule.dto.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.schedule.dto.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto.AppointmentCompletionRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto.CreateAppointmentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto.UpdateAppointmentRequest;
import org.springframework.stereotype.Component;

@Component
public class AppointmentRestWriteMapper {

    public  CreateAppointmentDto toServiceCreate(CreateAppointmentRequest request) {
        return new CreateAppointmentDto(
                request.dentistId(),
                request.patientId(),
                request.serviceId(),
                request.start(),
                request.end(),
                request.reason(),
                request.type()
        );
    }

    public  UpdateAppointmentDto toServiceUpdate(UpdateAppointmentRequest request) {
        return new UpdateAppointmentDto(
                request.appointmentId(),
                request.dentistId(),
                request.patientId(),
                request.newStart(),
                request.newEnd()
        );
    }

    public AppointmentCompletionDTO toServiceCompletion(AppointmentCompletionRequest request) {
        return new AppointmentCompletionDTO(
                request.attendedBy(),
                request.actualDurationMinutes(),
                request.clinicalNotes()
        );
    }
}

