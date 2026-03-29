package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.jpaRepository;


import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.DentalServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DentalServiceJpaRepository  extends JpaRepository<DentalServiceEntity, Long> {
   // El campo en DentalServiceEntity es String serviceType → parámetro String
    Page<DentalServiceEntity> findByServiceType(String serviceType, Pageable pageable);

    // JPQL usa el nombre de la clase Java: DentalServiceEntity, no ProvidedServiceEntity
    // El campo en la entidad es "category", no "catalogCategory"
    @Query("SELECT d FROM DentalServiceEntity d WHERE d.category = :category")
    Page<DentalServiceEntity> findByCatalogCategory(@Param("category") String category, Pageable pageable);


   /** @Query("SELECT p FROM ProvidedServiceEntity p JOIN p.orthodonticDetails o WHERE o.treatmentDurationMonths = :months")
    Page<DentalServiceEntity> findByOrthodonticTreatmentDurationMonths(@Param("months") Integer months, Pageable pageable);*/

    boolean existsByCode(String code);
    
    // Añadir en DentalServiceJpaRepository.java

@Query("SELECT d FROM DentalServiceEntity d " +
       "JOIN d.orthodonticDetail o " +
       "WHERE o.durationMonths = :months")
Page<DentalServiceEntity> findByOrthodonticDurationMonths(
        @Param("months") Integer months, 
        Pageable pageable);

@Query("SELECT d FROM DentalServiceEntity d " +
       "JOIN d.prostheticDetail p " +
       "WHERE p.units = :units")
Page<DentalServiceEntity> findByProstheticUnits(
        @Param("units") Integer units, 
        Pageable pageable);

@Query("SELECT d FROM DentalServiceEntity d " +
       "JOIN d.implantologyDetail i " +
       "WHERE i.healingMonths = :months")
Page<DentalServiceEntity> findByImplantHealingMonths(
        @Param("months") Integer months, 
        Pageable pageable);

@Query("SELECT d FROM DentalServiceEntity d WHERE d.status = :status")
Page<DentalServiceEntity> findByStatus(
        @Param("status") String status, 
        Pageable pageable);

}
