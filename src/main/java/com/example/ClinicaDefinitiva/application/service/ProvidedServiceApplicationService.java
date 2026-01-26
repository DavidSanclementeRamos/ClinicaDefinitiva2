package com.example.ClinicaDefinitiva.application.service;

import com.example.ClinicaDefinitiva.application.dto.service.CreateProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.dto.service.ReadProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.dto.service.UpdateProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.mapper.ProvidedServiceMapper;
import com.example.ClinicaDefinitiva.application.portsInput.ProvidedServiceUseCase;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Price;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;
import com.example.ClinicaDefinitiva.domain.portsOutput.ProvidedServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public class ProvidedServiceApplicationService  implements ProvidedServiceUseCase {
    private final ProvidedServiceRepository repository;
    private final ProvidedServiceMapper providedServiceMapper;

    public ProvidedServiceApplicationService(ProvidedServiceRepository repository, ProvidedServiceMapper providedServiceMapper) {
        this.repository = repository;
        this.providedServiceMapper = providedServiceMapper;
    }

    @Override
    @Transactional
    public ReadProvidedServiceDto create(CreateProvidedServiceDto dto) {
        if (dto == null) throw new IllegalArgumentException("dto no puede ser null");
        // validaciones de presencia ya deberían ejecutarse con Bean Validation en el adapter web
        ProvidedService domain = providedServiceMapper.toDomain(dto);
        if (repository.existsByCode(domain.getCode().getValue())) {
            throw new IllegalArgumentException("Service code already exists: " + domain.getCode().getValue());
        }
        repository.save(domain);
        return providedServiceMapper.toReadDto(domain);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadProvidedServiceDto findById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        ServiceId sid = ServiceId.fromString(id);
        ProvidedService s = repository.findById(sid).orElseThrow(() -> new IllegalArgumentException("Not found: " + id));
        return providedServiceMapper.toReadDto(s);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadProvidedServiceDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(providedServiceMapper::toReadDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadProvidedServiceDto> findByCategory(String category, Pageable pageable) {
        return repository.findByCatalogCategory(category, pageable).map(providedServiceMapper::toReadDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadProvidedServiceDto> findByServiceType(String serviceType, Pageable pageable) {
        // map serviceType string to domain.ServiceType enum if you have one; here we call jpaRepository by service type via enum
        try {
            ServiceType st = ServiceType.valueOf(serviceType.toUpperCase());
            return repository.findByServiceType(st, pageable).map(providedServiceMapper::toReadDto);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid serviceType: " + serviceType);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadProvidedServiceDto> findOrthodonticByTreatmentDuration(Integer months, Pageable pageable) {
        return repository.findByOrthodonticTreatmentDurationMonths(months, pageable).map(providedServiceMapper::toReadDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadProvidedServiceDto> findProstheticByUnits(Integer units, Pageable pageable) {
        return repository.findByProstheticUnits(units, pageable).map(providedServiceMapper::toReadDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadProvidedServiceDto> findImplantologyByHealingTime(Integer months, Pageable pageable) {
        return repository.findByImplantHealingTimeMonths(months, pageable).map(providedServiceMapper::toReadDto);
    }

    @Override
    @Transactional
    public ReadProvidedServiceDto update(String id, UpdateProvidedServiceDto dto) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        ServiceId sid = ServiceId.fromString(id);
        ProvidedService existing = repository.findById(sid).orElseThrow(() -> new IllegalArgumentException("Not found: " + id));
        // aplica cambios parciales en el dominio usando métodos públicos que deberías añadir (ej. updateCommon, attach...).
        existing.updateCommon(
                dto.name,
                dto.catalog == null ? null : new ServiceCatalog(ServiceId.fromString(dto.catalog.id), dto.catalog.name, dto.catalog.category),
                dto.baseRateAmount == null ? null : new Price(dto.baseRateAmount, dto.baseRateCurrency),
                dto.durationMinutes == null ? null : new ServiceDuration(dto.durationMinutes),
                dto.requiresAuthorization,
                dto.description,
                dto.status);

        // update details when present (orthodontic example)
        if (dto.orthodontic != null) {
            existing.updateOrthodonticDetails(dto.orthodontic.applianceType, dto.orthodontic.treatmentDurationMonths, dto.orthodontic.requiresFollowup);
        }
        if (dto.prosthetic != null) {
            existing.updateProstheticDetails(dto.prosthetic.fixedOrRemovable, dto.prosthetic.material, dto.prosthetic.prostheticType, dto.prosthetic.units);
        }
        if (dto.implantology != null) {
            existing.updateImplantologyDetails(dto.implantology.healingTimeMonths, dto.implantology.implantType, dto.implantology.placementSite, dto.implantology.requiresBoneGraft);
        }
        if (dto.aesthetic != null) {
            existing.updateAestheticDetails(dto.aesthetic.aestheticType, dto.aesthetic.materialUsed, dto.aesthetic.expectedResult);
        }
        if (dto.pediatric != null) {
            existing.updatePediatricDetails(dto.pediatric.ageRange, dto.pediatric.behaviorManagement, dto.pediatric.pediatricMaterials);
        }
        if (dto.surgical != null) {
            existing.updateSurgicalDetails(dto.surgical.surgeryType, dto.surgical.complexityLevel, dto.surgical.requiresAnesthesia, dto.surgical.operatingRoomNeeded);
        }

        repository.save(existing);
        return providedServiceMapper.toReadDto(existing);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        repository.deleteById(ServiceId.fromString(id));
    }






}
