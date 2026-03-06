package com.example.ClinicaDefinitiva.domain.dentalService.output;

import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProvidedServiceRepository {
    Optional<ProvidedService> findById(ServiceId id);
    ProvidedService save(ProvidedService providedService);
    void deleteById(ServiceId id);
    boolean existsByCode(String code);

    Page<ProvidedService> findAll(Pageable pageable);
    Page<ProvidedService> findByServiceType(ServiceType type, Pageable pageable);
    Page<ProvidedService> findByCatalogCategory(String category, Pageable pageable);

    // Consultas por campos específicos (ejemplos cubiertos por tus subclases)
    Page<ProvidedService> findByOrthodonticTreatmentDurationMonths(Integer months, Pageable pageable);
    Page<ProvidedService> findByProstheticUnits(Integer units, Pageable pageable);
    Page<ProvidedService> findByImplantHealingTimeMonths(Integer months, Pageable pageable);

    Page<ProvidedService> findByCategory(String category, Pageable pageable);

    Page<ProvidedService> findByStatus(String status, Pageable pageable);

    Page<ProvidedService> findByType(String serviceType, Pageable pageable);


}
