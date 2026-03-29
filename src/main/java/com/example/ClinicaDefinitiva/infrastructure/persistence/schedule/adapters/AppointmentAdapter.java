package com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.adapters;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.DentistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.PatientJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.jpaRepository.DentalServiceJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.entity.AppointmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.jpaRepository.AppointmentJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.mapper.AppointmentReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.mapper.AppointmentWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
public class AppointmentAdapter implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final DentistJpaRepository dentistJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final DentalServiceJpaRepository dentalServiceJpaRepository;
    private final AppointmentReadEntityMapper readMapper;
    private final AppointmentWriteEntityMapper writeMapper;

    public AppointmentAdapter(
            AppointmentJpaRepository appointmentJpaRepository,
            DentistJpaRepository dentistJpaRepository,
            PatientJpaRepository patientJpaRepository,
            DentalServiceJpaRepository dentalServiceJpaRepository,
            AppointmentReadEntityMapper readMapper,
            AppointmentWriteEntityMapper writeMapper) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.dentistJpaRepository = dentistJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.dentalServiceJpaRepository = dentalServiceJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public Appointment save(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentEntity entity = writeMapper.toEntity(appointment);

        // Establecer relaciones
        if (appointment.getDentistId() != null && appointment.getDentistId().value() != null) {
            dentistJpaRepository.findById(appointment.getDentistId().value())
                    .ifPresent(entity::setDentist);
        }

        if (appointment.getPatientId() != null && appointment.getPatientId().value() != null) {
            patientJpaRepository.findById(appointment.getPatientId().value())
                    .ifPresent(entity::setPatient);
        }

        if (appointment.getServiceId() != null && appointment.getServiceId().getId() != null) {
            dentalServiceJpaRepository.findById(appointment.getServiceId().getId())
                    .ifPresent(entity::setDentalService);
        }

        AppointmentEntity savedEntity = appointmentJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> findById(AppointmentId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return appointmentJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findAll(Pageable pageable) {
        return appointmentJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findConflictingForDentist(DentistId dentistId, 
                                                        LocalDateTime start, 
                                                        LocalDateTime end, 
                                                        boolean withLock) {
        if (withLock) {
            return appointmentJpaRepository.findConflictingForDentistWithLock(
                    dentistId.value(), start, end
            ).stream()
             .map(readMapper::toDomain)
             .collect(Collectors.toList());
        } else {
            return appointmentJpaRepository.findConflictingForDentist(
                    dentistId.value(), start, end
            ).stream()
             .map(readMapper::toDomain)
             .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findConflictingForPatient(PatientId patientId, 
                                                        LocalDateTime start, 
                                                        LocalDateTime end, 
                                                        boolean withLock) {
        if (withLock) {
            return appointmentJpaRepository.findConflictingForPatientWithLock(
                    patientId.value(), start, end
            ).stream()
             .map(readMapper::toDomain)
             .collect(Collectors.toList());
        } else {
            return appointmentJpaRepository.findConflictingForPatient(
                    patientId.value(), start, end
            ).stream()
             .map(readMapper::toDomain)
             .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findUpcomingForDentist(DentistId dentistId, 
                                                     LocalDateTime now, 
                                                     LocalDateTime limit, 
                                                     Pageable pageable) {
        return appointmentJpaRepository.findUpcomingForDentist(
                dentistId.value(), now, limit, pageable
        ).map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findFutureForPatient(PatientId patientId, 
                                                    LocalDateTime now, 
                                                    Pageable pageable) {
        return appointmentJpaRepository.findFutureForPatient(
                patientId.value(), now, pageable
        ).map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByDateRange(LocalDateTime startOfDay, 
                                              LocalDateTime endOfDay, 
                                              Pageable pageable) {
        return appointmentJpaRepository.findByDateRange(startOfDay, endOfDay, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByPatientBetween(PatientId patientId, 
                                                    LocalDateTime start, 
                                                    LocalDateTime end, 
                                                    Pageable pageable) {
        return appointmentJpaRepository.findByPatientBetween(
                patientId.value(), start, end, pageable
        ).map(readMapper::toDomain);
    }

    @Override
    public void delete(AppointmentId id) {
        if (id != null && id.getValue() != null) {
            appointmentJpaRepository.deleteById(id.getValue());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByDentistBetween(DentistId dentistId, 
                                                    LocalDateTime start, 
                                                    LocalDateTime end, 
                                                    Pageable pageable) {
        return appointmentJpaRepository.findByDentistBetween(
                dentistId.value(), start, end, pageable
        ).map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByDentistAndDate(DentistId dentistId, 
                                                    LocalDate date, 
                                                    Pageable pageable) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return findByDentistBetween(dentistId, startOfDay, endOfDay, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByDentist(DentistId dentistId, Pageable pageable) {
        return appointmentJpaRepository.findByDentistId(dentistId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByPatientId(PatientId patientId, Pageable pageable) {
        return appointmentJpaRepository.findByPatientId(patientId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByDentistId(DentistId dentistId, Pageable pageable) {
        return findByDentist(dentistId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByServiceId(ServiceId serviceId, Pageable pageable) {
        return appointmentJpaRepository.findByServiceId(serviceId.getId(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable) {
        return appointmentJpaRepository.findByStatus(status.getValue().name(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findByPatientAndDentist(PatientId patientId, 
                                                       DentistId dentistId, 
                                                       LocalDate start, 
                                                       LocalDate end, 
                                                       Pageable pageable) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        
        return appointmentJpaRepository.findByPatientAndDentist(
                patientId.value(), 
                dentistId.value(), 
                startDateTime, 
                endDateTime, 
                pageable
        ).map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByServiceId(ServiceId serviceId) {
        return appointmentJpaRepository.existsByServiceId(serviceId.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public long countScheduledByDentist(DentistId dentistId) {
        return appointmentJpaRepository.countScheduledByDentist(
                dentistId.value(), 
                AppointmentStatus.Status.SCHEDULED.name()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsScheduledByDentistBetween(DentistId dentistId, 
                                                     LocalDateTime start, 
                                                     LocalDateTime end) {
        return appointmentJpaRepository.existsScheduledByDentistBetween(
                dentistId.value(), 
                start, 
                end, 
                AppointmentStatus.Status.SCHEDULED.name()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsScheduledByPatientBetween(PatientId patientId, 
                                                     LocalDateTime start, 
                                                     LocalDateTime end) {
        return appointmentJpaRepository.existsScheduledByPatientBetween(
                patientId.value(), 
                start, 
                end, 
                AppointmentStatus.Status.SCHEDULED.name()
        );
    }
}