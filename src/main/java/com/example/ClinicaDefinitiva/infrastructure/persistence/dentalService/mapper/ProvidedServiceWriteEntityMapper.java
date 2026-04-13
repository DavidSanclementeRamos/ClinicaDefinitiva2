package com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.mapper;

import com.example.ClinicaDefinitiva.domain.dentalService.model.*;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ProvidedServiceWriteEntityMapper {

   

    public DentalServiceEntity toEntity(ProvidedService service) {
        if (service == null) return null;

        DentalServiceEntity entity = new DentalServiceEntity();

        if (service.getId() != null && service.getId().getId() != null) {
            entity.setId(service.getId().getId());
        }

        entity.setName(service.getName().getValue());
        entity.setCategory(service.getCategory().getCategory());
        entity.setCode(service.getCode().getValue());
        entity.setBaseRate(service.getBaseRate().asBigDecimal());
        entity.setBaseRateCurrency(service.getBaseRate().getCurrency().getCurrencyCode());
        entity.setDurationMinutes(service.getDuration().getMinutes());
        entity.setRequiresAuthorization(service.isRequiresAuthorization());
        entity.setDescription(service.getDescription().getValue());
        entity.setStatus(service.getStatus().getValue().name());

        // Mapear detalles según el tipo (crea nuevas entidades hijas)
        service.getDetails().ifPresent(details -> {
            entity.setServiceType(details.serviceType().name());
            mapDetailsToEntity(entity, details);
        });

        entity.setServiceType(service.getServiceType().name());

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

   
    /**
     * Actualiza una entidad JPA ya cargada en la sesión con los datos del agregado.
     * No crea nuevas entidades detalle, sino que actualiza las existentes.
     */
    public void updateEntity(DentalServiceEntity entity, ProvidedService service) {
        if (entity == null || service == null) return;

        // Campos simples
        entity.setName(service.getName().getValue());
        entity.setCategory(service.getCategory().getCategory());
        entity.setCode(service.getCode().getValue());
        entity.setBaseRate(service.getBaseRate().asBigDecimal());
        entity.setBaseRateCurrency(service.getBaseRate().getCurrency().getCurrencyCode());
        entity.setDurationMinutes(service.getDuration().getMinutes());
        entity.setRequiresAuthorization(service.isRequiresAuthorization());
        entity.setDescription(service.getDescription().getValue());
        entity.setStatus(service.getStatus().getValue().name());
        entity.setServiceType(service.getServiceType().name());

        // Actualizar detalles (sin crear nuevas instancias)
        service.getDetails().ifPresent(details -> {
            entity.setServiceType(details.serviceType().name());
            updateDetailsEntity(entity, details);
        });
    }

    private void updateDetailsEntity(DentalServiceEntity entity, ServiceDetails details) {
        switch (details.serviceType()) {
            case ORTHODONTIC -> updateOrthodonticDetails(entity, (OrthodonticDetails) details);
            case SURGERY -> updateSurgicalDetails(entity, (SurgicalDetails) details);
            case AESTHETICS -> updateAestheticDetails(entity, (AestheticDetails) details);
            case IMPLANTOLOGY -> updateImplantologyDetails(entity, (ImplantologyDetails) details);
            case PEDIATRICS -> updatePediatricDetails(entity, (PediatricDetails) details);
            case PROSTHETICS -> updateProstheticDetails(entity, (ProstheticDetails) details);
        }
    }

    private void updateOrthodonticDetails(DentalServiceEntity entity, OrthodonticDetails details) {
        OrthodonticDetailEntity detail = entity.getOrthodonticDetail();
        if (detail == null) {
            detail = new OrthodonticDetailEntity();
            detail.setDentalService(entity);
            entity.setOrthodonticDetail(detail);
        }
        detail.setApplianceType(details.getApplianceType());
        detail.setDurationMonths(details.getTreatmentDurationMonths());
        detail.setRequiresFollowUp(details.getRequiresFollowup());
    }

    private void updateSurgicalDetails(DentalServiceEntity entity, SurgicalDetails details) {
        SurgeryDetailEntity detail = entity.getSurgeryDetail();
        if (detail == null) {
            detail = new SurgeryDetailEntity();
            detail.setDentalService(entity);
            entity.setSurgeryDetail(detail);
        }
        detail.setSurgeryType(details.getSurgeryType());
        detail.setComplexityLevel(details.getComplexityLevel());
        detail.setRequiresAnesthesia(details.getRequiresAnesthesia());
        detail.setRequiresOperatingRoom(details.getOperatingRoomNeeded());
    }

    private void updateAestheticDetails(DentalServiceEntity entity, AestheticDetails details) {
        AestheticDetailsEntity detail = entity.getAestheticDetail();
        if (detail == null) {
            detail = new AestheticDetailsEntity();
            detail.setDentalService(entity);
            entity.setAestheticDetail(detail);
        }
        detail.setAestheticType(details.getAestheticType());
        detail.setMaterialUsed(details.getMaterialUsed());
        detail.setExpectedResult(details.getExpectedResult());
    }

    private void updateImplantologyDetails(DentalServiceEntity entity, ImplantologyDetails details) {
        ImplantologyDetailEntity detail = entity.getImplantologyDetail();
        if (detail == null) {
            detail = new ImplantologyDetailEntity();
            detail.setDentalService(entity);
            entity.setImplantologyDetail(detail);
        }
        detail.setHealingMonths(details.getHealingTimeMonths());
        detail.setImplantType(details.getImplantType());
        detail.setPlacementSite(details.getPlacementSite());
        detail.setRequiresBoneGraft(details.getRequiresBoneGraft());
    }

    private void updatePediatricDetails(DentalServiceEntity entity, PediatricDetails details) {
        PediatricDetailEntity detail = entity.getPediatricDetail();
        if (detail == null) {
            detail = new PediatricDetailEntity();
            detail.setDentalService(entity);
            entity.setPediatricDetail(detail);
        }
        detail.setMinAgeRange(details.getAgeRange().getMinAge());
        detail.setMaxAgeRange(details.getAgeRange().getMaxAge());
        detail.setBehaviorManagement(details.getBehaviorManagement());
        detail.setPediatricMaterials(details.getPediatricMaterials());
    }

    private void updateProstheticDetails(DentalServiceEntity entity, ProstheticDetails details) {
        ProstheticDetailEntity detail = entity.getProstheticDetail();
        if (detail == null) {
            detail = new ProstheticDetailEntity();
            detail.setDentalService(entity);
            entity.setProstheticDetail(detail);
        }
        detail.setFixedOrRemovable(details.getFixedOrRemovable());
        detail.setMaterial(details.getMaterial());
        detail.setProstheticType(details.getProstheticType());
        detail.setUnits(details.getUnits());
    }
}
