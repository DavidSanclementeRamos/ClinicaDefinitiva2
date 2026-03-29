package com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.adapters;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.output.TreatmentRepository;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.entity.TreatmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.DentistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.PatientJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.jpaRepository.RateJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.jpaRepository.TreatmentJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.jpaRepository.DentalServiceJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.mapper.TreatmentReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.mapper.TreatmentWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class TreatmentsAdapter implements TreatmentRepository {

    private final TreatmentJpaRepository treatmentJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final DentistJpaRepository dentistJpaRepository;
    private final DentalServiceJpaRepository dentalServiceJpaRepository;
    private final RateJpaRepository rateJpaRepository;
    private final TreatmentReadEntityMapper readMapper;
    private final TreatmentWriteEntityMapper writeMapper;

    public TreatmentsAdapter(
            TreatmentJpaRepository treatmentJpaRepository,
            PatientJpaRepository patientJpaRepository,
            DentistJpaRepository dentistJpaRepository,
            DentalServiceJpaRepository dentalServiceJpaRepository,
            RateJpaRepository rateJpaRepository,
            TreatmentReadEntityMapper readMapper,
            TreatmentWriteEntityMapper writeMapper) {
        this.treatmentJpaRepository = treatmentJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.dentistJpaRepository = dentistJpaRepository;
        this.dentalServiceJpaRepository = dentalServiceJpaRepository;
        this.rateJpaRepository = rateJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Treatment> findById(TreatmentId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return treatmentJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Treatment> findAll(Pageable pageable) {
        return treatmentJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Treatment> findByDentist(DentistId id, Pageable pageable) {
        if (id == null || id.value() == null) {
            return Page.empty();
        }
        return treatmentJpaRepository.findByDentistId(id.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Treatment> findByDentistAndStatus(DentistId id, TreatmentStatus status, Pageable pageable) {
        if (id == null || id.value() == null || status == null) {
            return Page.empty();
        }
        return treatmentJpaRepository.findByDentistIdAndStatus(id.value(), status.name(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Treatment> findByStatus(TreatmentStatus status, Pageable pageable) {
        if (status == null) {
            return Page.empty();
        }
        return treatmentJpaRepository.findByStatus(status.name(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(TreatmentId id) {
        return id != null && id.getValue() != null && 
               treatmentJpaRepository.existsById(id.getValue());
    }

    @Override
    public Treatment save(Treatment treatment) {
        if (treatment == null) return null;

        TreatmentEntity entity = writeMapper.toEntity(treatment);

        // Establecer relaciones
        if (treatment.getPatientId() != null && treatment.getPatientId().value() != null) {
            patientJpaRepository.findById(treatment.getPatientId().value())
                    .ifPresent(entity::setPatient);
        }

        if (treatment.getDentistId() != null && treatment.getDentistId().value() != null) {
            dentistJpaRepository.findById(treatment.getDentistId().value())
                    .ifPresent(entity::setDentist);
        }

        if (treatment.getServicioId() != null && treatment.getServicioId().getId() != null) {
            dentalServiceJpaRepository.findById(treatment.getServicioId().getId())
                    .ifPresent(entity::setDentalService);
        }

        if (treatment.getTarifaId() != null && treatment.getTarifaId().getValue() != null) {
            rateJpaRepository.findById(treatment.getTarifaId().getValue())
                    .ifPresent(entity::setRate);
        }

        TreatmentEntity savedEntity = treatmentJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Treatment treatment) {
        if (treatment != null && treatment.getId() != null && treatment.getId().getValue() != null) {
            treatmentJpaRepository.deleteById(treatment.getId().getValue());
        }
    }
}