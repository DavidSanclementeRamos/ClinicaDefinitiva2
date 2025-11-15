package com.example.ClinicaDefinitiva.domain.dental.care.services;

import com.example.ClinicaDefinitiva.domain.dental.care.services.model.*;

public class ProvidedServiceMigrations {
    public static ProvidedService fromOldOrthodontic(OrthodonticService old) {
        OrthodonticDetails details = new OrthodonticDetails(old.getAppliance_type(), old.getTreatment_duration_months(), old.isRequires_followup());
        return new ProvidedService(old.getId(), old.getName(), old.getCategory(), old.getCode(), old.getBaseRate(), old.getDuration(), old.isRequiresAuthorization(), old.getDescription(), old.getStatus(), details);
    }

    public static ProvidedService fromOldProsthetic(ProstheticService old) {
        ProstheticDetails details = new ProstheticDetails(old.getFixed_or_removable(), old.getMaterial(), old.getProsthetic_type(), old.getUnits());
        return new ProvidedService(old.getId(), old.getName(), old.getCategory(), old.getCode(), old.getBaseRate(), old.getDuration(), old.isRequiresAuthorization(), old.getDescription(), old.getStatus(), details);
    }

    public static ProvidedService fromOldImplantology(ImplantologyService old) {
        ImplantologyDetails details = new ImplantologyDetails(old.getHealing_time_months(), old.getImplant_type(), old.getPlacement_site(), old.isRequires_bone_graft());
        return new ProvidedService(old.getId(), old.getName(), old.getCategory(), old.getCode(), old.getBaseRate(), old.getDuration(), old.isRequiresAuthorization(), old.getDescription(), old.getStatus(), details);
    }

    public static ProvidedService fromOldAesthetic(AestheticService old) {
        AestheticDetails details = new AestheticDetails(old.getAesthetic_type(), old.getMaterial_used(), old.getExpected_result());
        return new ProvidedService(old.getId(), old.getName(), old.getCategory(), old.getCode(), old.getBaseRate(), old.getDuration(), old.isRequiresAuthorization(), old.getDescription(), old.getStatus(), details);
    }

    public static ProvidedService fromOldPediatric(PediatricService old) {
        PediatricDetails details = new PediatricDetails(old.getAge_range(), old.getBehavior_management(), old.getPediatric_materials());
        return new ProvidedService(old.getId(), old.getName(), old.getCategory(), old.getCode(), old.getBaseRate(), old.getDuration(), old.isRequiresAuthorization(), old.getDescription(), old.getStatus(), details);
    }

    public static ProvidedService fromOldSurgical(SurgicalService old) {
        SurgicalDetails details = new SurgicalDetails(old.getSurgery_type(), old.getComplexity_level(), old.isRequires_anesthesia(), old.isOperating_room_needed());
        return new ProvidedService(old.getId(), old.getName(), old.getCategory(), old.getCode(), old.getBaseRate(), old.getDuration(), old.isRequiresAuthorization(), old.getDescription(), old.getStatus(), details);
    }


}
