package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

public class SurgicalService extends ProvidedService {
    private final String surgery_type;            // Tipo de cirugía (extracción, injerto, frenillo)
    private final String complexity_level;        // Nivel de complejidad (low, medium, high)
    private final boolean requires_anesthesia;    // Indica si requiere anestesia
    private final boolean operating_room_needed;   // Indica si requiere quirófano/equipo especial

    public SurgicalService(ServiceId id, String name, ServiceCatalog category, ServiceCode code, Money baseRate, ServiceDuration duration, boolean requiresAuthorization, String description, ServiceStatus status, String complexity_level, boolean operating_room_needed, boolean requires_anesthesia, String surgery_type) {
        super(id, name, category, code, baseRate, duration, requiresAuthorization, description, status);
        this.complexity_level = complexity_level;
        this.operating_room_needed = operating_room_needed;
        this.requires_anesthesia = requires_anesthesia;
        this.surgery_type = surgery_type;
    }

    public String getComplexity_level() {return complexity_level;}
    public boolean isOperating_room_needed() {
        return operating_room_needed;
    }
    public boolean isRequires_anesthesia() {
        return requires_anesthesia;
    }
    public String getSurgery_type() {
        return surgery_type;
    }

}
