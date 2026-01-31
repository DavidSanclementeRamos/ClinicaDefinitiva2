package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository;

import com.example.ClinicaDefinitiva.domain.dental.care.services.Enum.ServiceType;
import com.example.ClinicaDefinitiva.infrastructure.persistence.providedService.ProvidedServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataProvidedServiceJpaRepository  extends JpaRepository<ProvidedServiceEntity, String> {
    Page<ProvidedServiceEntity> findByServiceType(ServiceType serviceType, Pageable pageable);

    @Query("SELECT p FROM ProvidedServiceEntity p WHERE p.catalogCategory = :category")
    Page<ProvidedServiceEntity> findByCatalogCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM ProvidedServiceEntity p JOIN p.orthodonticDetails o WHERE o.treatmentDurationMonths = :months")
    Page<ProvidedServiceEntity> findByOrthodonticTreatmentDurationMonths(@Param("months") Integer months, Pageable pageable);

    boolean existsByCode(String code);

}
