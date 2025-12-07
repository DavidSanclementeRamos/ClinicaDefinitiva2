package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.application.dto.service.*;
import com.example.ClinicaDefinitiva.domain.Money;
import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.*;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProvidedServiceMapper {
    public  ProvidedService toDomain(CreateProvidedServiceDto dto) {

        ServiceId id = ServiceId.generate(); // adapta si tu VO usa otra firma
        ServiceCatalog catalog = new ServiceCatalog(ServiceId.fromString(dto.catalog.id), dto.catalog.name, dto.catalog.category);
        ServiceCode code = new ServiceCode(dto.code);
        Money money = dto.baseRateAmount == null ? null : new Money(dto.baseRateAmount, dto.baseRateCurrency == null ? "COP" : dto.baseRateCurrency);
        ServiceDuration duration = dto.durationMinutes == null ? null : new ServiceDuration(dto.durationMinutes);
        ServiceStatus status = dto.status == null ? new ServiceStatus("Active") : new ServiceStatus(dto.status);

        ServiceDetails details = null;
        String type = Optional.ofNullable(dto.serviceType).orElseGet(() -> catalog.getCategory().toUpperCase());
        switch (type.toUpperCase()) {
            case "ORTHODONTIC":
                if (dto.orthodontic != null) {
                    details = new OrthodonticDetails(dto.orthodontic.applianceType, dto.orthodontic.treatmentDurationMonths, dto.orthodontic.requiresFollowup);
                }
                break;
            case "PROSTHETIC":
                if (dto.prosthetic != null) {
                    details = new ProstheticDetails(dto.prosthetic.fixedOrRemovable, dto.prosthetic.material, dto.prosthetic.prostheticType, dto.prosthetic.units);
                }
                break;
            case "IMPLANTOLOGY":
                if (dto.implantology != null) {
                    details = new ImplantologyDetails(dto.implantology.healingTimeMonths, dto.implantology.implantType, dto.implantology.placementSite, dto.implantology.requiresBoneGraft);
                }
                break;
            case "AESTHETIC":
                if (dto.aesthetic != null) {
                    details = new AestheticDetails(dto.aesthetic.aestheticType, dto.aesthetic.materialUsed, dto.aesthetic.expectedResult);
                }
                break;
            case "PEDIATRIC":
                if (dto.pediatric != null) {
                    details = new PediatricDetails(dto.pediatric.ageRange, dto.pediatric.behaviorManagement, dto.pediatric.pediatricMaterials);
                }
                break;
            case "SURGICAL":
                if (dto.surgical != null) {
                    details = new SurgicalDetails(dto.surgical.surgeryType, dto.surgical.complexityLevel, dto.surgical.requiresAnesthesia, dto.surgical.operatingRoomNeeded);
                }
                break;
            default:
                details = null;
        }

        return new ProvidedService(id, dto.name, catalog, code, money, duration, Boolean.TRUE.equals(dto.requiresAuthorization), dto.description, status, details);
    }

    public  ReadProvidedServiceDto toReadDto(ProvidedService s) {
        ReadProvidedServiceDto out = new ReadProvidedServiceDto();
        out.id = s.getId().toString();
        out.name = s.getName();
        out.catalogId = s.getCategory() == null ? null : String.valueOf(s.getCategory().getId());
        out.catalogName = s.getCategory() == null ? null : s.getCategory().getName();
        out.catalogCategory = s.getCategory() == null ? null : s.getCategory().getCategory();
        out.code = s.getCode() == null ? null : s.getCode().getValue();
        out.baseRateAmount = s.getBaseRate() == null ? null : s.getBaseRate().asBigDecimal();
        out.baseRateCurrency = s.getBaseRate() == null ? null : s.getBaseRate().getCurrency();
        out.durationMinutes = s.getDuration() == null ? null : s.getDuration().getMinutes();
        out.requiresAuthorization = s.isRequiresAuthorization();
        out.description = s.getDescription();
        out.status = s.getStatus() == null ? null : s.getStatus().getValue();
        out.serviceType = String.valueOf(s.getDetails().map(ServiceDetails::serviceType).orElse(null));

        s.getDetails().ifPresent(d -> {
            String st = String.valueOf(d.serviceType());
            switch (st) {
                case "ORTHODONTIC":
                    OrthodonticDetails od = (OrthodonticDetails) d;
                    CreateProvidedServiceDto.OrthodonticDto odv = new CreateProvidedServiceDto.OrthodonticDto();
                    odv.applianceType = od.getApplianceType();
                    odv.treatmentDurationMonths = od.getTreatmentDurationMonths();
                    odv.requiresFollowup = od.getRequiresFollowup();
                    out.orthodontic = odv;
                    break;
                case "PROSTHETIC":
                    ProstheticDetails pd = (ProstheticDetails) d;
                    CreateProvidedServiceDto.ProstheticDto pdto = new CreateProvidedServiceDto.ProstheticDto();
                    pdto.fixedOrRemovable = pd.getFixedOrRemovable();
                    pdto.material = pd.getMaterial();
                    pdto.prostheticType = pd.getProstheticType();
                    pdto.units = pd.getUnits();
                    out.prosthetic = pdto;
                    break;
                case "IMPLANTOLOGY":
                    ImplantologyDetails idet = (ImplantologyDetails) d;
                    CreateProvidedServiceDto.ImplantologyDto idto = new CreateProvidedServiceDto.ImplantologyDto();
                    idto.healingTimeMonths = idet.getHealingTimeMonths();
                    idto.implantType = idet.getImplantType();
                    idto.placementSite = idet.getPlacementSite();
                    idto.requiresBoneGraft = idet.getRequiresBoneGraft();
                    out.implantology = idto;
                    break;
                case "AESTHETIC":
                    AestheticDetails ad = (AestheticDetails) d;
                    CreateProvidedServiceDto.AestheticDto adto = new CreateProvidedServiceDto.AestheticDto();
                    adto.aestheticType = ad.getAestheticType();
                    adto.materialUsed = ad.getMaterialUsed();
                    adto.expectedResult = ad.getExpectedResult();
                    out.aesthetic = adto;
                    break;
                case "PEDIATRIC":
                    PediatricDetails ped = (PediatricDetails) d;
                    CreateProvidedServiceDto.PediatricDto pedto = new CreateProvidedServiceDto.PediatricDto();
                    pedto.ageRange = ped.getAgeRange();
                    pedto.behaviorManagement = ped.getBehaviorManagement();
                    pedto.pediatricMaterials = ped.getPediatricMaterials();
                    out.pediatric = pedto;
                    break;
                case "SURGICAL":
                    SurgicalDetails sd = (SurgicalDetails) d;
                    CreateProvidedServiceDto.SurgicalDto sdto = new CreateProvidedServiceDto.SurgicalDto();
                    sdto.surgeryType = sd.getSurgeryType();
                    sdto.complexityLevel = sd.getComplexityLevel();
                    sdto.requiresAnesthesia = sd.getRequiresAnesthesia();
                    sdto.operatingRoomNeeded = sd.getOperatingRoomNeeded();
                    out.surgical = sdto;
                    break;
            }
        });
        // createdAt/updatedAt left null if not present in domain; adapter may populate via entity mapping
        return out;
    }
}


