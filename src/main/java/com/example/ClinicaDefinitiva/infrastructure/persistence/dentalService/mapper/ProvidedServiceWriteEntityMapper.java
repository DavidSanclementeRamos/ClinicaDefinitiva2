package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.mapper;

import com.example.ClinicaDefinitiva.domain.dentalService.model.*;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.AestheticDetailsEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.DentalServiceEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.ImplantologyDetailEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.OrthodonticDetailEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.PediatricDetailEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.ProstheticDetailEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.SurgeryDetailEntity;
import org.springframework.stereotype.Component;

@Component
public class ProvidedServiceWriteEntityMapper {

    public DentalServiceEntity toEntity(ProvidedService service) {
        if (service == null) return null;

        DentalServiceEntity entity = new DentalServiceEntity();

        /**if (service.getId() != null && service.getId().getId() != null) {
            entity.setId(service.getId().getId());
        }*/

        entity.setName(service.getName().getValue());
        entity.setCategory(service.getCategory().getCategory());
        entity.setCode(service.getCode().getValue());
        entity.setBaseRate(service.getBaseRate().asBigDecimal());
        entity.setBaseRateCurrency(service.getBaseRate().getCurrency().getCurrencyCode());
        entity.setDurationMinutes(service.getDuration().getMinutes());
        entity.setRequiresAuthorization(service.isRequiresAuthorization());
        entity.setDescription(service.getDescription().getValue());
        entity.setStatus(service.getStatus().getValue().name());

        // Mapear detalles según el tipo
        service.getDetails().ifPresent(details -> {
            entity.setServiceType(details.serviceType().name());
            mapDetailsToEntity(entity, details);
        });

        return entity;
    }

    private void mapDetailsToEntity(DentalServiceEntity entity, ServiceDetails details) {
        switch (details.serviceType()) {
            case ORTHODONTIC -> mapOrthodonticDetails(entity, (OrthodonticDetails) details);
            case SURGERY -> mapSurgicalDetails(entity, (SurgicalDetails) details);
            case AESTHETICS -> mapAestheticDetails(entity, (AestheticDetails) details);
            case IMPLANTOLOGY -> mapImplantologyDetails(entity, (ImplantologyDetails) details);
            case PEDIATRICS -> mapPediatricDetails(entity, (PediatricDetails) details);
            case PROSTHETICS -> mapProstheticDetails(entity, (ProstheticDetails) details);
        }
    }

    private void mapOrthodonticDetails(DentalServiceEntity entity, OrthodonticDetails details) {
        OrthodonticDetailEntity detailEntity = new OrthodonticDetailEntity();
        detailEntity.setDentalService(entity);
        detailEntity.setApplianceType(details.getApplianceType());
        detailEntity.setDurationMonths(details.getTreatmentDurationMonths());
        detailEntity.setRequiresFollowUp(details.getRequiresFollowup());
        entity.setOrthodonticDetail(detailEntity);
    }

    private void mapSurgicalDetails(DentalServiceEntity entity, SurgicalDetails details) {
        SurgeryDetailEntity detailEntity = new SurgeryDetailEntity();
        detailEntity.setDentalService(entity);
        detailEntity.setSurgeryType(details.getSurgeryType());
        detailEntity.setComplexityLevel(details.getComplexityLevel());
        detailEntity.setRequiresAnesthesia(details.getRequiresAnesthesia());
        detailEntity.setRequiresOperatingRoom(details.getOperatingRoomNeeded());
        entity.setSurgeryDetail(detailEntity);
    }

    private void mapAestheticDetails(DentalServiceEntity entity, AestheticDetails details) {
        AestheticDetailsEntity detailEntity = new AestheticDetailsEntity();
        detailEntity.setDentalService(entity);
        detailEntity.setAestheticType(details.getAestheticType());
        detailEntity.setMaterialUsed(details.getMaterialUsed());
        detailEntity.setExpectedResult(details.getExpectedResult());
        entity.setAestheticDetail(detailEntity);
    }

    private void mapImplantologyDetails(DentalServiceEntity entity, ImplantologyDetails details) {
        ImplantologyDetailEntity detailEntity = new ImplantologyDetailEntity();
        detailEntity.setDentalService(entity);
        detailEntity.setHealingMonths(details.getHealingTimeMonths());
        detailEntity.setImplantType(details.getImplantType());
        detailEntity.setPlacementSite(details.getPlacementSite());
        detailEntity.setRequiresBoneGraft(details.getRequiresBoneGraft());
        entity.setImplantologyDetail(detailEntity);
    }

    private void mapPediatricDetails(DentalServiceEntity entity, PediatricDetails details) {
        PediatricDetailEntity detailEntity = new PediatricDetailEntity();
        detailEntity.setDentalService(entity);
        detailEntity.setMinAgeRange(details.getAgeRange().getMinAge());
        detailEntity.setMaxAgeRange(details.getAgeRange().getMaxAge());
        detailEntity.setBehaviorManagement(details.getBehaviorManagement());
        detailEntity.setPediatricMaterials(details.getPediatricMaterials());
        entity.setPediatricDetail(detailEntity);
    }

    private void mapProstheticDetails(DentalServiceEntity entity, ProstheticDetails details) {
        ProstheticDetailEntity detailEntity = new ProstheticDetailEntity();
        detailEntity.setDentalService(entity);
        detailEntity.setFixedOrRemovable(details.getFixedOrRemovable());
        detailEntity.setMaterial(details.getMaterial());
        detailEntity.setProstheticType(details.getProstheticType());
        detailEntity.setUnits(details.getUnits());
        entity.setProstheticDetail(detailEntity);
    }
}
