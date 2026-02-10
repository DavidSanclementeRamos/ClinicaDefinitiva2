package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters;

import com.example.ClinicaDefinitiva.domain.dental.care.services.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.Price;
import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.*;
import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.*;
import com.example.ClinicaDefinitiva.domain.portsOutput.ProvidedServiceRepository;


import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.providedService.*;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.SpringDataProvidedServiceJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProvidedServiceRepositoryJpaAdapter implements ProvidedServiceRepository {

    private final SpringDataProvidedServiceJpaRepository repo;
    private final ProvidedServiceMapper providedServiceMapper;

    public ProvidedServiceRepositoryJpaAdapter(SpringDataProvidedServiceJpaRepository repo, ProvidedServiceMapper providedServiceMapper) {
        this.repo = repo;
        this.providedServiceMapper = providedServiceMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProvidedService> findById(ServiceId id) {
        return repo.findById(id.toString()).map(this::toDomain);
    }

    @Override
    @Transactional
    public void save(ProvidedService providedService) {
        ProvidedServiceEntity e = toEntity (providedService);
        LocalDateTime now = LocalDateTime.now();
        if (e.getCreatedAt() == null) e.setCreatedAt(now);
        e.setUpdatedAt(now);
        // ensure bidirectional linking for orthodontic details
        if (e.getOrthodonticDetails() != null) {
            e.getOrthodonticDetails().setProvidedService(e);
        }
        repo.save(e);
    }

    @Override
    @Transactional
    public void deleteById(ServiceId id) {
        repo.deleteById(id.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return repo.existsByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByServiceType(ServiceType serviceType, Pageable pageable) {
        return repo.findByServiceType(serviceType, pageable).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByCatalogCategory(String category, Pageable pageable) {
        return repo.findByCatalogCategory(category, pageable).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProvidedService> findByOrthodonticTreatmentDurationMonths(Integer months, Pageable pageable) {
        return repo.findByOrthodonticTreatmentDurationMonths(months, pageable).map(this::toDomain);
    }

    @Override
    public Page<ProvidedService> findByProstheticUnits(Integer units, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ProvidedService> findByImplantHealingTimeMonths(Integer months, Pageable pageable) {
        return null;
    }

    // Mapping helpers: Entity -> Domain
    private ProvidedService toDomain(ProvidedServiceEntity e) {
        ServiceId id = ServiceId.fromString(e.getId());
        ServiceCatalog catalog = new ServiceCatalog(ServiceId.fromString(e.getCatalogId()), e.getCatalogName(), e.getCatalogCategory());
        ServiceCode code = new ServiceCode(e.getCode());
        Price money = e.getBaseRateAmount() == null ? null : new Price(e.getBaseRateAmount(), e.getBaseRateCurrency());
        ServiceDuration duration = e.getDurationMinutes() == null ? null : new ServiceDuration(e.getDurationMinutes());
        ServiceStatus status = e.getStatus() == null ? new ServiceStatus("Active") : new ServiceStatus(e.getStatus());

        // details mapping: currently only orthodontic table implemented; extend for others
        ServiceDetails details = null;

        if (e.getOrthodonticDetails() != null) {
            OrthodonticDetailsEntity od = e.getOrthodonticDetails();
            details = new OrthodonticDetails(od.getApplianceType(), od.getTreatmentDurationMonths(), od.getRequiresFollowup());
        } else if (e.getProstheticDetailsEntity() != null) {
            ProstheticDetailsEntity pd = e.getProstheticDetailsEntity();
            details = new ProstheticDetails(pd.getFixedOrRemovable(), pd.getMaterial(), pd.getProstheticType(), pd.getUnits());
        } else if (e.getImplantologyDetailsEntity() != null) {
            ImplantologyDetailsEntity ide = e.getImplantologyDetailsEntity();
            details = new ImplantologyDetails(ide.getHealingTimeMonths(), ide.getImplantType(), ide.getPlacementSite(), ide.getRequiresBoneGraft());
        } else if (e.getAestheticDetailsEntity() != null) {
            AestheticDetailsEntity ade = e.getAestheticDetailsEntity();
            details = new AestheticDetails(ade.getAestheticType(), ade.getMaterialUsed(), ade.getExpectedResult());
        } else if (e.getPediatricDetailsEntity() != null) {
            PediatricDetailsEntity pde = e.getPediatricDetailsEntity();
            details = new PediatricDetails(pde.getAgeRange(), pde.getBehaviorManagement(), pde.getPediatricMaterials());
        } else if (e.getSurgicalDetailsEntity() != null) {
            SurgicalDetailsEntity sde = e.getSurgicalDetailsEntity();
            details = new SurgicalDetails(sde.getSurgeryType(), sde.getComplexityLevel(), sde.getRequiresAnesthesia(), sde.getOperatingRoomNeeded());
        }

        ProvidedService domain = new ProvidedService(id, e.getName(), catalog, code, money, duration, Boolean.TRUE.equals(e.getRequiresAuthorization()), e.getDescription(), status, details);
        return domain;
    }

    // Mapping helpers: Domain -> Entity
    private ProvidedServiceEntity toEntity(ProvidedService domain) {
        ProvidedServiceEntity e = new ProvidedServiceEntity();
        e.setId(domain.getId().toString());
        e.setName(domain.getName());
        e.setServiceType(String.valueOf(domain.getDetails().map(ServiceDetails::serviceType).orElse(null)));
        e.setCatalogId(domain.getCategory() == null ? null : domain.getCategory().getId().toString());
        e.setCatalogName(domain.getCategory() == null ? null : domain.getCategory().getName());
        e.setCatalogCategory(domain.getCategory() == null ? null : domain.getCategory().getCategory());
        e.setCode(domain.getCode() == null ? null : domain.getCode().getValue());
        e.setBaseRateAmount(domain.getBaseRate() == null ? null : domain.getBaseRate().asBigDecimal());
        e.setBaseRateCurrency(domain.getBaseRate() == null ? null : domain.getBaseRate().getCurrency());
        e.setDurationMinutes(domain.getDuration() == null ? null : domain.getDuration().getMinutes());
        e.setRequiresAuthorization(domain.isRequiresAuthorization());
        e.setDescription(domain.getDescription());
        e.setStatus(domain.getStatus() == null ? null : domain.getStatus().getValue());
        // map details
        // añadir mapeos para otros tipos cuando se implementen sus tables/entities
        domain.getDetails().ifPresent(details -> {
            switch (details.serviceType()) {
                case ORTHODONTIC -> {
                    OrthodonticDetails od = (OrthodonticDetails) details;
                    OrthodonticDetailsEntity ode = new OrthodonticDetailsEntity(
                            od.getApplianceType(),
                            od.getTreatmentDurationMonths(),
                            od.getRequiresFollowup()
                    );
                    ode.setProvidedService(e);
                    e.setOrthodonticDetails(ode);
                }
                case PROSTHETICS -> {
                    ProstheticDetails pd = (ProstheticDetails) details;
                    ProstheticDetailsEntity pde = new ProstheticDetailsEntity(
                            pd.getFixedOrRemovable(),
                            pd.getMaterial(),
                            pd.getProstheticType(),
                            pd.getUnits()
                    );
                    pde.setProvidedService(e);
                    e.setProstheticDetailsEntity(pde);
                }
                case IMPLANTOLOGY -> {
                    ImplantologyDetails id = (ImplantologyDetails) details;
                    ImplantologyDetailsEntity ide = new ImplantologyDetailsEntity(
                            id.getHealingTimeMonths(),
                            id.getImplantType(),
                            id.getPlacementSite(),
                            id.getRequiresBoneGraft()
                    );
                    ide.setProvidedService(e);
                    e.setImplantologyDetailsEntity(ide);
                }
                case AESTHETICS -> {
                    AestheticDetails ad = (AestheticDetails) details;
                    AestheticDetailsEntity ade = new AestheticDetailsEntity(
                            ad.getAestheticType(),
                            ad.getMaterialUsed(),
                            ad.getExpectedResult()
                    );
                    ade.setProvidedService(e);
                    e.setAestheticDetailsEntity(ade);
                }
                case PEDIATRICS -> {
                    PediatricDetails pd = (PediatricDetails) details;
                    PediatricDetailsEntity pde = new PediatricDetailsEntity(
                            pd.getAgeRange(),
                            pd.getBehaviorManagement(),
                            pd.getPediatricMaterials()
                    );
                    pde.setProvidedService(e);
                    e.setPediatricDetailsEntity(pde);
                }
                case SURGICAL -> {
                    SurgicalDetails sd = (SurgicalDetails) details;
                    SurgicalDetailsEntity sde = new SurgicalDetailsEntity(
                            sd.getSurgeryType(),
                            sd.getComplexityLevel(),
                            sd.getRequiresAnesthesia(),
                            sd.getOperatingRoomNeeded()
                    );
                    sde.setProvidedService(e);
                    e.setSurgicalDetailsEntity(sde);
                }
                default -> throw new IllegalArgumentException("Unsupported serviceType: " + details.serviceType());
            }
        });
        return e;
    }
}