package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

public class PediatricService  extends ProvidedService {
    private final String age_range;                  // Rango de edad del paciente (ej. "3-12 years")
    private final String behavior_management;       // Técnica de manejo infantil (ej. "tell-show-do", sedación ligera)
    private final String pediatric_materials;      // Materiales específicos usados en odontopediatría

    public PediatricService(ServiceId id, String name, ServiceCatalog category, ServiceCode code, Money baseRate, ServiceDuration duration, boolean requiresAuthorization, String description, ServiceStatus status, String age_range, String behavior_management, String pediatric_materials) {
        super(id, name, category, code, baseRate, duration, requiresAuthorization, description, status);
        this.age_range = age_range;
        this.behavior_management = behavior_management;
        this.pediatric_materials = pediatric_materials;
    }

    public String getAge_range() {return age_range;}
    public String getBehavior_management() {
        return behavior_management;
    }
    public String getPediatric_materials() {
        return pediatric_materials;
    }

}
