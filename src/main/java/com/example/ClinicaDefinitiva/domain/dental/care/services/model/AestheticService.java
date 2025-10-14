package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

public class AestheticService extends ProvidedService {
    private final String aesthetic_type;      // Tipo de procedimiento (whitening, veneers, contouring)
    private final String material_used;       // Material principal (ej. resina, porcelana)
    private final String expected_result;     // Resultado esperado (ej. "shade improvement", "alignment correction")

    public AestheticService(ServiceId id, String name, ServiceCatalog category, ServiceCode code, Money baseRate, ServiceDuration duration, boolean requiresAuthorization, String description, ServiceStatus status, String aesthetic_type, String expected_result, String material_used) {
        super(id, name, category, code, baseRate, duration, requiresAuthorization, description, status);
        this.aesthetic_type = aesthetic_type;
        this.expected_result = expected_result;
        this.material_used = material_used;
    }

    public String getAesthetic_type() {return aesthetic_type;}
    public String getExpected_result() {
        return expected_result;
    }
    public String getMaterial_used() {
        return material_used;
    }

}
