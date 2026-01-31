package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.schedule;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.portsOutput.Administration.ScheduleRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.Availability;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
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
    public Schedule findByDentistId(DentistId dentistId) {
        List<Appointment> appointments = appointmentJpaRepository.findByDentistId(dentistId);
        Availability availability = availabilityJpaRepository.findByDentistId(dentistId);
        return new Schedule(appointments, availability);
    }

    @Override
    public void save(Schedule schedule) {

    }
}



