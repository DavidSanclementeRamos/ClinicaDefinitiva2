package com.example.ClinicaDefinitiva.domain.portsOutput;

import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProvidedServiceRepository {
    Optional<ProvidedService> findById(ServiceId id);
    void save(ProvidedService providedService);
    void deleteById(ServiceId id);
    boolean existsByCode(String code);

    Page<ProvidedService> findAll(Pageable pageable);
    Page<ProvidedService> findByServiceType(ServiceType type, Pageable pageable);
    Page<ProvidedService> findByCatalogCategory(String category, Pageable pageable);

    // Consultas por campos específicos (ejemplos cubiertos por tus subclases)
    Page<ProvidedService> findByOrthodonticTreatmentDurationMonths(Integer months, Pageable pageable);
    Page<ProvidedService> findByProstheticUnits(Integer units, Pageable pageable);
    Page<ProvidedService> findByImplantHealingTimeMonths(Integer months, Pageable pageable);
    // Puedes añadir más métodos específicos según futuros requisitos


}
