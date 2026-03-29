package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.mapper;

import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.model.*;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.*;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.AestheticDetailsEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.DentalServiceEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.ImplantologyDetailEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.OrthodonticDetailEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.PediatricDetailEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.ProstheticDetailEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.SurgeryDetailEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class ProvidedServiceReadEntityMapper {

    public ProvidedService toDomain(DentalServiceEntity entity) {
        if (entity == null) return null;

        ServiceDetails details = mapDetails(entity);

        return ProvidedService.builder()
                .id(ServiceId.of(entity.getId()))
                .name(ServiceName.custom(entity.getName()))
                .category(ServiceCatalog.of(
                        ServiceId.of(entity.getId()),
                        ServiceName.custom(entity.getCategory()),
                        entity.getCategory()))
                .code(ServiceCode.of(entity.getCode()))
                .baseRate(Price.of(entity.getBaseRate(), Currency.getInstance(entity.getBaseRateCurrency())))
                .duration(ServiceDuration.of(entity.getDurationMinutes()))
                .requiresAuthorization(entity.isRequiresAuthorization())
                .description(ServiceDescription.of(entity.getDescription()))
                .status(ServiceStatus.of(ServiceStatus.State.valueOf(entity.getStatus())))
                .details(details)
                .build();
    }

    private ServiceDetails mapDetails(DentalServiceEntity entity) {
        ServiceType type = ServiceType.valueOf(entity.getServiceType());

        return switch (type) {
            case ORTHODONTIC -> mapOrthodonticDetails(entity.getOrthodonticDetail());
            case SURGERY -> mapSurgicalDetails(entity.getSurgeryDetail());
            case AESTHETICS -> mapAestheticDetails(entity.getAestheticDetail());
            case IMPLANTOLOGY -> mapImplantologyDetails(entity.getImplantologyDetail());
            case PEDIATRICS -> mapPediatricDetails(entity.getPediatricDetail());
            case PROSTHETICS -> mapProstheticDetails(entity.getProstheticDetail());
            default -> null;
        };
    }

    private OrthodonticDetails mapOrthodonticDetails(OrthodonticDetailEntity entity) {
        if (entity == null) return null;
        return new OrthodonticDetails(
                entity.getApplianceType(),
                entity.getDurationMonths(),
                entity.isRequiresFollowUp()
        );
    }

    private SurgicalDetails mapSurgicalDetails(SurgeryDetailEntity entity) {
        if (entity == null) return null;
        return new SurgicalDetails(
                entity.getSurgeryType(),
                entity.getComplexityLevel(),
                entity.isRequiresAnesthesia(),
                entity.isRequiresOperatingRoom()
        );
    }

    private AestheticDetails mapAestheticDetails(AestheticDetailsEntity entity) {
        if (entity == null) return null;
        return new AestheticDetails(
                entity.getAestheticType(),
                entity.getMaterialUsed(),
                entity.getExpectedResult()
        );
    }

    private ImplantologyDetails mapImplantologyDetails(ImplantologyDetailEntity entity) {
        if (entity == null) return null;
        return new ImplantologyDetails(
                entity.getHealingMonths(),
                entity.getImplantType(),
                entity.getPlacementSite(),
                entity.isRequiresBoneGraft()
        );
    }

    private PediatricDetails mapPediatricDetails(PediatricDetailEntity entity) {
        if (entity == null) return null;
        return new PediatricDetails(
                AgeRange.of(entity.getMinAgeRange(), entity.getMaxAgeRange()),
                entity.getBehaviorManagement(),
                entity.getPediatricMaterials()
        );
    }

    private ProstheticDetails mapProstheticDetails(ProstheticDetailEntity entity) {
        if (entity == null) return null;
        return new ProstheticDetails(
                entity.getFixedOrRemovable(),
                entity.getMaterial(),
                entity.getProstheticType(),
                entity.getUnits()
        );
    }
}
