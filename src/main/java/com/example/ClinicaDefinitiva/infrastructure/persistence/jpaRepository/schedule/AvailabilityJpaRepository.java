package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.schedule;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Availability;

public interface AvailabilityJpaRepository {
    Availability findByDentistId(DentistId dentistId);
}
