package com.example.ClinicaDefinitiva.application.mapper.schedule;

import com.example.ClinicaDefinitiva.application.dto.sheduled.AppointmentCompletionDTO;
import com.example.ClinicaDefinitiva.application.dto.sheduled.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentCompletion;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;



@Component
public class AppointmentWriteMapper {

    public DentistId toDentistId(CreateAppointmentDto dto) {
        return DentistId.of(dto.dentistId());
    }

    public PatientId toPatientId(CreateAppointmentDto dto) {
        return PatientId.of(dto.patientId());
    }

    public LocalDateTime toStart(CreateAppointmentDto dto) {
        return dto.start();
    }

    public LocalDateTime toEnd(CreateAppointmentDto dto) {
        return dto.end();
    }

    public AppointmentType toType(CreateAppointmentDto dto) {
        return AppointmentType.valueOf(dto.type());
    }

    public String toReason(CreateAppointmentDto dto) {
        return dto.reason();
    }

    public ServiceId toServiceId(CreateAppointmentDto dto) {
        return ServiceId.of(dto.serviceId());
    }

    // Métodos para Update DTO
    public DentistId toDentistId(UpdateAppointmentDto dto) {
        return DentistId.of(dto.dentistId());
    }

    public PatientId toPatientId(UpdateAppointmentDto dto) {
        return PatientId.of(dto.patientId());
    }

    public LocalDateTime toNewStart(UpdateAppointmentDto dto) {
        return dto.newStart();
    }

    public LocalDateTime toNewEnd(UpdateAppointmentDto dto) {
        return dto.newEnd();
    }

    // Mapper para AppointmentCompletion
    public AppointmentCompletion toCompletion(AppointmentCompletionDTO dto) {
        return new AppointmentCompletion(
            ServiceDuration.of(dto.actualDurationMinutes()),
            dto.clinicalNotes()
        );
    }
}