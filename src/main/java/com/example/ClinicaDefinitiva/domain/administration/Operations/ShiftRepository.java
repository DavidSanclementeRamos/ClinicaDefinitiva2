package com.example.ClinicaDefinitiva.domain.administration.operations;



import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Puerto: Interface de repositorio en capa de dominio
 * ✅ SIN dependencias de JPA, Hibernate, Spring Data, etc.
 */
public interface ShiftRepository {

    Shift save(Shift shift);

    Optional<Shift> findById(ShiftId id);

    Page<Shift> findAll(Pageable pageable);

    /**
     * Encuentra turnos activos de un dentista en una fecha específica
     * Usado en: AppointmentSchedulingService.ensureShiftCoverage()
     */
    Page<Shift> findActiveByDentistAndDate(DentistId dentistId, LocalDate date,Pageable pageable);

    /**
     * Encuentra turnos activos de un dentista en un rango de fechas
     */
    Page<Shift> findActiveByDentistAndDateRange(
            DentistId dentistId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    /**
     * Encuentra turnos que se solapan con un horario específico
     * @param withLock true para garantizar thread-safety (pessimistic lock)
     */
    Page<Shift> findOverlapping(
            DentistId dentistId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            boolean withLock,
            Pageable pageable
    );

    /**
     * Encuentra todos los turnos activos de un dentista
     */
    Page<Shift> findActiveByDentist(DentistId dentistId, Pageable pageable);

    void delete(ShiftId id);

    public List<Shift> findByReceptionistId(ReceptionId receptionId);

    public List<Shift> findActiveByDentistAndDate(DentistId dentistId, LocalDate toLocalDate);


}
