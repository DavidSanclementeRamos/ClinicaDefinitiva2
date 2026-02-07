package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.SurgicalError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Set;

public final class SurgicalDetails implements ServiceDetails {

    private static final Set VALID_COMPLEXITY_LEVELS = Set.of(
            "LOW", "MEDIUM", "HIGH", "CRITICAL"
    );

    private final String surgeryType;
    private final String complexityLevel;
    private final boolean requiresAnesthesia;
    private final boolean operatingRoomNeeded;

    public SurgicalDetails(String surgeryType, String complexityLevel,
                           Boolean requiresAnesthesia, Boolean operatingRoomNeeded) {
        // RN-SURGICAL-006
        if (surgeryType != null && surgeryType.length() < 3) {
            throw new ValueObjectValidationException(
                    SurgicalError.ERR_SURGICAL_TYPE_TOO_SHORT, VOContext.SURGICAL
            );
        }

        // RN-SURGICAL-003
        if (complexityLevel != null && !VALID_COMPLEXITY_LEVELS.contains(complexityLevel.toUpperCase())) {
            throw new ValueObjectValidationException(
                    SurgicalError.ERR_SURGICAL_INVALID_COMPLEXITY,VOContext.SURGICAL
            );
        }

        boolean needsAnesthesia = Boolean.TRUE.equals(requiresAnesthesia);
        boolean needsOperatingRoom = Boolean.TRUE.equals(operatingRoomNeeded);

        // RN-SURGICAL-001
        if (needsAnesthesia && "LOW".equals(complexityLevel)) {
            throw new ValueObjectValidationException(
                    SurgicalError.ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH,VOContext.SURGICAL
            );
        }

        // RN-SURGICAL-004
        if ("CRITICAL".equals(complexityLevel) && (!needsAnesthesia || !needsOperatingRoom)) {
            throw new ValueObjectValidationException(
                    SurgicalError.ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS,VOContext.SURGICAL
            );
        }

        // RN-SURGICAL-007
        if (needsOperatingRoom && "LOW".equals(complexityLevel)) {
            throw new ValueObjectValidationException(
                    SurgicalError.ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH,VOContext.SURGICAL
            );
        }

        this.surgeryType = surgeryType;
        this.complexityLevel = complexityLevel == null ? null : complexityLevel.toUpperCase();
        this.requiresAnesthesia = needsAnesthesia;
        this.operatingRoomNeeded = needsOperatingRoom;
    }

    @Override
    public ServiceType serviceType() {
        return ServiceType.SURGERY;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public String getComplexityLevel() {
        return complexityLevel;
    }

    public Boolean getRequiresAnesthesia() {
        return requiresAnesthesia;
    }

    public Boolean getOperatingRoomNeeded() {
        return operatingRoomNeeded;
    }
}
