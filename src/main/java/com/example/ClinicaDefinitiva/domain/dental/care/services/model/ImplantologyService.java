package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

public class ImplantologyService extends ProvidedService {
    private final String implant_type;              // Tipo de implante (titanium, zirconia)
    private final String placement_site;            // Ubicación (ej. "upper jaw, molar region")
    private final boolean requires_bone_graft;      // Indica si requiere injerto óseo
    private final int healing_time_months;          // Tiempo estimado de osteointegración

    public ImplantologyService(ServiceId id, String name, ServiceCatalog category, ServiceCode code, Money baseRate, ServiceDuration duration, boolean requiresAuthorization, String description, ServiceStatus status, int healing_time_months, String implant_type, String placement_site, boolean requires_bone_graft) {
        super(id, name, category, code, baseRate, duration, requiresAuthorization, description, status);
        this.healing_time_months = healing_time_months;
        this.implant_type = implant_type;
        this.placement_site = placement_site;
        this.requires_bone_graft = requires_bone_graft;
    }

    public boolean isRequires_bone_graft() {return requires_bone_graft;}
    public String getPlacement_site() {
        return placement_site;
    }
    public String getImplant_type() {
        return implant_type;
    }
    public int getHealing_time_months() {
        return healing_time_months;
    }

}
