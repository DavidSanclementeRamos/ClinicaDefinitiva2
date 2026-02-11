package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.shedule;


import com.example.ClinicaDefinitiva.application.dto.sheduled.AppointmentCompletionDTO;
import com.example.ClinicaDefinitiva.application.dto.sheduled.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;


import com.example.ClinicaDefinitiva.infrastructure.rest.dto.schedule.AppointmentCompletionRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.schedule.CreateAppointmentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.schedule.UpdateAppointmentRequest;
import org.springframework.stereotype.Component;

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

