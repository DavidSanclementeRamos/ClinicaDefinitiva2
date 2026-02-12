package com.example.ClinicaDefinitiva.application.mapper.schedule;

import com.example.ClinicaDefinitiva.application.dto.sheduled.AppointmentCompletionDTO;
import com.example.ClinicaDefinitiva.application.dto.sheduled.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.service.AppointmentSchedulingService;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentCompletion;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;



public class AppointmentWriteMapper {

    public Appointment fromCreateDto(CreateAppointmentDto dto, AppointmentSchedulingService service) {
         return service.scheduleAppointment(
                 DentistId.of(dto.dentistId()),
                 PatientId.of( dto.patientId()),
                 dto.start(),
                 dto.end(),
                 AppointmentType.valueOf( dto.type()),
                 dto.reason(),
                 ServiceId.of(dto.serviceId())
         );



    }

    public  void updateFromDto(Appointment appointment ,AppointmentSchedulingService service, UpdateAppointmentDto dto) {

         service.rescheduleAppointment(
                appointment,
                DentistId.of(dto.dentistId()),
                PatientId.of( dto.patientId()),
                dto.newStart(),
                dto.newEnd()
        );

    }

    public AppointmentCompletion toCompletion(AppointmentCompletionDTO dto) {
        return new AppointmentCompletion(
                 ServiceDuration.of(dto.actualDurationMinutes()),
                 dto.clinicalNotes()
        );
    }
}

