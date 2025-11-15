package com.example.ClinicaDefinitiva.application.usecase;

import com.example.ClinicaDefinitiva.application.dto.service.CreateProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.dto.service.ReadProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.dto.service.UpdateProvidedServiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProvidedServiceUseCase {
    ReadProvidedServiceDto create(CreateProvidedServiceDto dto);
    ReadProvidedServiceDto findById(String id);
    Page<ReadProvidedServiceDto> findAll(Pageable pageable);
    Page<ReadProvidedServiceDto> findByCategory(String category, Pageable pageable);
    Page<ReadProvidedServiceDto> findByServiceType(String serviceType, Pageable pageable);

    // busquedas específicas
    Page<ReadProvidedServiceDto> findOrthodonticByTreatmentDuration(Integer months, Pageable pageable);
    Page<ReadProvidedServiceDto> findProstheticByUnits(Integer units, Pageable pageable);
    Page<ReadProvidedServiceDto> findImplantologyByHealingTime(Integer months, Pageable pageable);

    ReadProvidedServiceDto update(String id, UpdateProvidedServiceDto dto);
    void delete(String id);


}
