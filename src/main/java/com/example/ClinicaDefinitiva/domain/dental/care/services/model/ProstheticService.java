package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

public class ProstheticService extends ProvidedService {
    private final String prosthetic_type;          // Tipo de prótesis (crown, bridge, denture)
    private final String material;                 // Material (porcelain, zirconia, resin)
    private final int units;                        // Número de dientes/unidades
    private final String fixed_or_removable;        // Tipo (fixed, removable)

    public ProstheticService(ServiceId id, String name, ServiceCatalog category, ServiceCode code, Money baseRate, ServiceDuration duration, boolean requiresAuthorization, String description, ServiceStatus status, String fixed_or_removable, String material, String prosthetic_type, int units) {
        super(id, name, category, code, baseRate, duration, requiresAuthorization, description, status);
        this.fixed_or_removable = fixed_or_removable;
        this.material = material;
        this.prosthetic_type = prosthetic_type;
        this.units = units;
    }

    public String getFixed_or_removable() {return fixed_or_removable;}
    public String getMaterial() {
        return material;
    }
    public String getProsthetic_type() {
        return prosthetic_type;
    }
    public int getUnits() {
        return units;
    }

}
