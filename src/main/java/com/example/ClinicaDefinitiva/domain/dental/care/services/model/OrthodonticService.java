package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

public class OrthodonticService extends ProvidedService {
    private final String appliance_type;              // Tipo de aparato (brackets metálicos, cerámicos, alineadores)
    private final int treatment_duration_months;     // Duración estimada del tratamiento en meses
    private final boolean requires_followup;        //Indica si requiere controles periódicos

    public OrthodonticService(ServiceId id, String name, ServiceCatalog category, ServiceCode code, Money baseRate, ServiceDuration duration, boolean requiresAuthorization, String description, ServiceStatus status, String appliance_type, boolean requires_followup, int treatment_duration_months) {
        super(id, name, category, code, baseRate, duration, requiresAuthorization, description, status);
        this.appliance_type = appliance_type;
        this.requires_followup = requires_followup;
        this.treatment_duration_months = treatment_duration_months;
    }

    public String getAppliance_type() {
        return appliance_type;
    }
    public boolean isRequires_followup() {return requires_followup;}
    public int getTreatment_duration_months() {
        return treatment_duration_months;
    }


}
