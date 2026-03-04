package com.example.ClinicaDefinitiva.domain.dentalService.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Objects;

/**
 * Value Object para el nombre del servicio odontológico.
 * Incluye un enum con valores estáticos y la posibilidad de ingresar otro nombre.
 */
public final class ServiceName {

    public enum DentalServiceName {
        CLEANING,
        WHITENING,
        ORTHODONTICS,
        IMPLANTOLOGY,
        PROSTHETICS,
        PEDIATRICS,
        SURGERY,
        OTHER // Permite ingresar un nombre personalizado
    }

    private final DentalServiceName predefinedName;
    private final String customName;

    private ServiceName(DentalServiceName predefinedName, String customName) {
        this.predefinedName = predefinedName;
        this.customName = customName;
    }

    public static ServiceName of(DentalServiceName predefinedName) {
        return new ServiceName(predefinedName, null);
    }

    public static ServiceName custom(String customName) {
        if (customName == null || customName.trim().length() < 3) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_NAME_CUSTOM_INVALID, VOContext.DENTAL_SERVICES);
        }
        return new ServiceName(DentalServiceName.OTHER, customName.trim());
    }

    public String getValue() {
        return predefinedName == DentalServiceName.OTHER ? customName : predefinedName.name();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.predefinedName);
        hash = 97 * hash + Objects.hashCode(this.customName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServiceName other = (ServiceName) obj;
        if (!Objects.equals(this.customName, other.customName)) {
            return false;
        }
        return this.predefinedName == other.predefinedName;
    }
    
    

}