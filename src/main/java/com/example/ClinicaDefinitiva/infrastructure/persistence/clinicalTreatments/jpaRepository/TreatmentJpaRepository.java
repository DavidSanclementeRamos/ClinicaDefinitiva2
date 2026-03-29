package com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.clinicalTreatments.entity.TreatmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentJpaRepository extends JpaRepository<TreatmentEntity, Long> {

    @Query("SELECT t FROM TreatmentEntity t WHERE t.dentist.id = :dentistId")
    Page<TreatmentEntity> findByDentistId(
            @Param("dentistId") Long dentistId,
            Pageable pageable);

    @Query("SELECT t FROM TreatmentEntity t WHERE t.dentist.id = :dentistId AND t.status = :status")
    Page<TreatmentEntity> findByDentistIdAndStatus(
            @Param("dentistId") Long dentistId,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT t FROM TreatmentEntity t WHERE t.status = :status")
    Page<TreatmentEntity> findByStatus(
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT COUNT(t) > 0 FROM TreatmentEntity t WHERE t.patient.id = :patientId AND t.status = 'ACTIVE'")
    boolean hasActiveTreatments(@Param("patientId") Long patientId);
}
