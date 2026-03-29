package com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.mapper;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentCompletion;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.entity.AppointmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.entity.AppointmentResultEntity;
import org.springframework.stereotype.Component;

@Component
public class AppointmentReadEntityMapper {

    public Appointment toDomain(AppointmentEntity entity) {
        if (entity == null) return null;

        Appointment.Builder builder = new Appointment.Builder()
                .withId(AppointmentId.of(entity.getId()))
                .withDentistId(DentistId.of(entity.getDentist().getId()))
                .withPatientId(PatientId.of(entity.getPatient().getId()))
                .withServiceId(ServiceId.of(entity.getDentalService().getId()))
                .withStart(entity.getStartDateTime())
                .withEnd(entity.getEndDateTime())
                .withStatus(AppointmentStatus.from(
                        AppointmentStatus.Status.valueOf(entity.getStatus())))
                .withReason(entity.getReason())
                .withAppointmentType(AppointmentType.valueOf(entity.getAppointmentType()));

        // Mapear resultado si existe
        if (entity.getResult() != null) {
            builder.withCompletion(toAppointmentCompletion(entity.getResult()));
        }

        return builder.build();
    }

    private AppointmentCompletion toAppointmentCompletion(AppointmentResultEntity entity) {
        return new AppointmentCompletion(
                ServiceDuration.of(entity.getActualDurationMinutes()),
                entity.getClinicalNotes()
        );
    }
}
