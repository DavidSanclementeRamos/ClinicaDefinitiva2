package com.example.ClinicaDefinitiva.domain.clinicalTreatments.output;


import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.TreatmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface TreatmentRepository {

    Optional<Treatment> findById(TreatmentId id);

    Page<Treatment> findAll(Pageable pageable);
    Page<Treatment> findByDentist(DentistId id, Pageable pageable);
    Page<Treatment> findByDentistAndStatus(DentistId id, TreatmentStatus status, Pageable pageable);

    Page<Treatment> findByStatus(TreatmentStatus status, Pageable pageable);

    boolean existsById(TreatmentId id);

    Treatment save(Treatment treatment);

    void delete(Treatment treatment);
}
