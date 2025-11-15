package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

import java.util.Objects;

public final class ProstheticDetails implements ServiceDetails {
    private final String fixedOrRemovable;
    private final String material;
    private final String prostheticType;
    private final Integer units;

    public ProstheticDetails(String fixedOrRemovable, String material, String prostheticType, Integer units) {
        if (fixedOrRemovable == null || fixedOrRemovable.isBlank()) throw new IllegalArgumentException("fixedOrRemovable required");
        this.fixedOrRemovable = fixedOrRemovable;
        this.material = material;
        this.prostheticType = prostheticType;
        this.units = units == null ? 0 : units;
    }

    @Override public ServiceType serviceType() { return ServiceType.PROSTHETICS; }
    public String getFixedOrRemovable() { return fixedOrRemovable; }
    public String getMaterial() { return material; }
    public String getProstheticType() { return prostheticType; }
    public Integer getUnits() { return units; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof ProstheticDetails)) return false; ProstheticDetails that = (ProstheticDetails)o; return Objects.equals(fixedOrRemovable, that.fixedOrRemovable) && Objects.equals(material, that.material) && Objects.equals(prostheticType, that.prostheticType) && Objects.equals(units, that.units); }
    @Override public int hashCode() { return Objects.hash(fixedOrRemovable, material, prostheticType, units); }


}
