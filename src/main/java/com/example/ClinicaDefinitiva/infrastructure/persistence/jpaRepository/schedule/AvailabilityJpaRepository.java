package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.schedule;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;

public interface AvailabilityJpaRepository {
    Availability findByDentistId(DentistId dentistId);
}
