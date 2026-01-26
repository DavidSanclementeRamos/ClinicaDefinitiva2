package com.example.ClinicaDefinitiva.domain.portsOutput.ScheduleRepository;


import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Availability;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AvailabilityId;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto: Interface de repositorio en capa de dominio
 * ✅ SIN dependencias de JPA, Hibernate, Spring Data, etc.
 */
public interface AvailabilityRepository {

    Availability save(Availability availability);

    Optional<Availability> findById(AvailabilityId id);

    List<Availability> findAll();

    /**
     * Encuentra disponibilidades activas para un dentista en un día específico
     * Usado en: AppointmentSchedulingService.ensureAvailabilityCoverage()
     */
    List<Availability> findByDentistAndDay(DentistId dentistId, DayOfWeek dayOfWeek);

    /**
     * Encuentra todas las disponibilidades activas de un dentista
     */
    List<Availability> findActiveByDentist(DentistId dentistId);

    /**
     * Encuentra disponibilidades que se solapan con un rango horario
     * @param withLock true para garantizar thread-safety (pessimistic lock)
     */
    List<Availability> findOverlapping(
            DentistId dentistId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            boolean withLock
    );

    void delete(AvailabilityId id);
}
