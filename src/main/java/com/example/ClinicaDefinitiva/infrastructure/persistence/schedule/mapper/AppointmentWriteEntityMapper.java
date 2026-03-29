package com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.mapper;

import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentCompletion;
import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.entity.AppointmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.entity.AppointmentResultEntity;
import org.springframework.stereotype.Component;

@Component
public class AppointmentWriteEntityMapper {

    public AppointmentEntity toEntity(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentEntity entity = new AppointmentEntity();

       /** if (appointment.getId() != null && appointment.getId().getValue() != null) {
            entity.setId(appointment.getId().getValue());
        }/*/

        entity.setStartDateTime(appointment.getStart());
        entity.setEndDateTime(appointment.getEnd());
        entity.setStatus(appointment.getStatus().getValue().name());
        entity.setReason(appointment.getReason());
        entity.setAppointmentType(appointment.getAppointmentType().name());
        entity.setCreatedAt(appointment.getCreationDate());
        entity.setUpdatedAt(appointment.getLastUpdated());

        // Mapear resultado si existe
        if (appointment.getCompletion() != null) {
            entity.setResult(toAppointmentResultEntity(appointment.getCompletion(), entity));
        }

        // Nota: Las relaciones con DentistEntity, PatientEntity, etc.
        // se establecen en el adapter usando los repositorios

        return entity;
    }

    private AppointmentResultEntity toAppointmentResultEntity(
              AppointmentCompletion   completion, 
            AppointmentEntity appointmentEntity) {
        
        AppointmentResultEntity entity = new AppointmentResultEntity();
        entity.setAppointment(appointmentEntity);
        entity.setClinicalNotes(completion.getClinicalNotes());
        entity.setActualDurationMinutes(completion.getActualDuration().getMinutes());
        return entity;
    }
}
