package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.schedule;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;

import java.util.List;

public interface AppointmentJpaRepository {
    List<Appointment> findByDentistId(DentistId dentistId);
}
