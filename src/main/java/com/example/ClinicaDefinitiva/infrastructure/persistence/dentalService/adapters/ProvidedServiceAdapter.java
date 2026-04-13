package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.adapters;

import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.DentalServiceEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.jpaRepository.DentalServiceJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.mapper.ProvidedServiceReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.mapper.ProvidedServiceWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class ProvidedServiceAdapter implements ProvidedServiceRepository {

    private final DentalServiceJpaRepository dentalServiceJpaRepository;
    private final ProvidedServiceReadEntityMapper readMapper;
    private final ProvidedServiceWriteEntityMapper writeMapper;

    public ProvidedServiceAdapter(
            DentalServiceJpaRepository dentalServiceJpaRepository,
            ProvidedServiceReadEntityMapper readMapper,
            ProvidedServiceWriteEntityMapper writeMapper) {
        this.dentalServiceJpaRepository = dentalServiceJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProvidedService> findById(ServiceId id) {
        if (id == null || id.getId() == null) {
            return Optional.empty();
        }
        return dentalServiceJpaRepository.findById(id.getId())
                .map(readMapper::toDomain);
    }

    @Override
    public ProvidedService save(ProvidedService providedService) {
        if (providedService == null) return null;

        DentalServiceEntity entity = writeMapper.toEntity(providedService);
        DentalServiceEntity savedEntity = dentalServiceJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

   

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return dentalServiceJpaRepository.existsByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findAll(Pageable pageable) {
        return dentalServiceJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByServiceType(ServiceType type, Pageable pageable) {
        return dentalServiceJpaRepository.findByServiceType(type.name(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByCatalogCategory(String category, Pageable pageable) {
        return dentalServiceJpaRepository.findByCatalogCategory(category, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByOrthodonticTreatmentDurationMonths(Integer months, Pageable pageable) {
        // Esta consulta requiere una query personalizada
        // Por simplicidad, podemos filtrar en memoria o crear una query específica
        return dentalServiceJpaRepository.findByOrthodonticDurationMonths(months,pageable)
                .map(readMapper::toDomain);
                
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByProstheticUnits(Integer units, Pageable pageable) {
        return dentalServiceJpaRepository.findByProstheticUnits(units,pageable)
                .map(readMapper::toDomain);
                    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByImplantHealingTimeMonths(Integer months, Pageable pageable) {
        return dentalServiceJpaRepository.findByImplantHealingMonths(months,pageable)
                .map(readMapper::toDomain);
                
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByCategory(String category, Pageable pageable) {
        return findByCatalogCategory(category, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByStatus(String status, Pageable pageable) {
        return dentalServiceJpaRepository.findByStatus(status,pageable)
                .map(readMapper::toDomain);
               
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByType(String serviceType, Pageable pageable) {
        return findByServiceType(ServiceType.valueOf(serviceType), pageable);
    }
}