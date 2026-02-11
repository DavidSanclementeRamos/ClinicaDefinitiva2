package com.example.ClinicaDefinitiva.domain.schedule.output;

import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;

import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.Optional;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Puerto: Interface de repositorio en capa de dominio
 *
 */
public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(AppointmentId id);

    Page<Appointment> findAll(Pageable pageable);

    /**
     *  El contrato especifica que esta query debe ser thread-safe
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

    Page<Appointment> findUpcomingForDentist(
            DentistId dentistId,
            LocalDateTime now,
            LocalDateTime limit,Pageable pageable
    );

    Page<Appointment> findFutureForPatient(
            PatientId patientId,
            LocalDateTime now,Pageable pageable
    );

    Page<Appointment> findByDateRange(
            LocalDateTime startOfDay,
            LocalDateTime endOfDay, Pageable pageable
    );

    void delete(AppointmentId id);

    Page<Appointment> findByDentistBetween(DentistId dentistId, LocalDateTime start, LocalDateTime end,Pageable pageable);

    Page<Appointment> findByDentistAndDate(DentistId dentistId, LocalDate date,Pageable pageable);

    Page<Appointment> findByDentist(DentistId dentistId,Pageable pageable);

    Page<Appointment> findByPatientId(PatientId patientId, Pageable pageable);

    Page<Appointment> findByDentistId(DentistId dentistId, Pageable pageable);

    Page<Appointment> findByServiceId(ServiceId serviceId, Pageable pageable);

    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);

    Page<Appointment> findByPatientAndDentist(PatientId patientId, DentistId dentistId, LocalDate start, LocalDate end, Pageable pageable);
}