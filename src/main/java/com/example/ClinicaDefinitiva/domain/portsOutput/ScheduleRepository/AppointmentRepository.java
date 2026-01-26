package com.example.ClinicaDefinitiva.domain.portsOutput.ScheduleRepository;

import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentId;
import java.util.Optional;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Puerto: Interface de repositorio en capa de dominio
 * ✅ SIN dependencias de JPA, Hibernate, Spring Data, etc.
 */
public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(AppointmentId id);

    List<Appointment> findAll();

    /**
     * ✅ El contrato especifica que esta query debe ser thread-safe
     * La implementación decidirá cómo (pessimistic lock, optimistic, etc.)
     */
    List<Appointment> findConflictingForDentist(
            DentistId dentistId,
            LocalDateTime start,
            LocalDateTime end,
            boolean withLock  // ← Flag explícito para concurrencia
    );

    List<Appointment> findConflictingForPatient(
            PatientId patientId,
            LocalDateTime start,
            LocalDateTime end,
            boolean withLock
    );

    List<Appointment> findUpcomingForDentist(
            DentistId dentistId,
            LocalDateTime now,
            LocalDateTime limit
    );

    List<Appointment> findFutureForPatient(
            PatientId patientId,
            LocalDateTime now
    );

    List<Appointment> findByDateRange(
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    void delete(AppointmentId id);
}