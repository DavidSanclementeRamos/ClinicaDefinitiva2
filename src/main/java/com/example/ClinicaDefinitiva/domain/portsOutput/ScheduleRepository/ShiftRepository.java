package com.example.ClinicaDefinitiva.domain.portsOutput.ScheduleRepository;



import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Shift;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.ShiftId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto: Interface de repositorio en capa de dominio
 * ✅ SIN dependencias de JPA, Hibernate, Spring Data, etc.
 */
public interface ShiftRepository {

    Shift save(Shift shift);

    Optional<Shift> findById(ShiftId id);

    List<Shift> findAll();

    /**
     * Encuentra turnos activos de un dentista en una fecha específica
     * Usado en: AppointmentSchedulingService.ensureShiftCoverage()
     */
    List<Shift> findActiveByDentistAndDate(DentistId dentistId, LocalDate date);

    /**
     * Encuentra turnos activos de un dentista en un rango de fechas
     */
    List<Shift> findActiveByDentistAndDateRange(
            DentistId dentistId,
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * Encuentra turnos que se solapan con un horario específico
     * @param withLock true para garantizar thread-safety (pessimistic lock)
     */
    List<Shift> findOverlapping(
            DentistId dentistId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            boolean withLock
    );

    /**
     * Encuentra todos los turnos activos de un dentista
     */
    List<Shift> findActiveByDentist(DentistId dentistId);

    void delete(ShiftId id);
}
