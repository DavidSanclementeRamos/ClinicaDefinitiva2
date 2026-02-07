package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.schedule;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ScheduleRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaScheduleRepository implements ScheduleRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final AvailabilityJpaRepository availabilityJpaRepository;

    public JpaScheduleRepository(AppointmentJpaRepository appointmentJpaRepository, AvailabilityJpaRepository availabilityJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.availabilityJpaRepository = availabilityJpaRepository;
    }

    @Override
    public ScheduleQueryService findByDentistId(DentistId dentistId) {
        List<Appointment> appointments = appointmentJpaRepository.findByDentistId(dentistId);
        Availability availability = availabilityJpaRepository.findByDentistId(dentistId);
        return new ScheduleQueryService(appointments, availability);
    }

    @Override
    public void save(ScheduleQueryService schedule) {

    }
}



