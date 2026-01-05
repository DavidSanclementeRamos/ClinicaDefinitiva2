package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

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
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_NAME_CUSTOM_INVALID, VOContext.SERVICE_NAME);
        }
        return new ServiceName(DentalServiceName.OTHER, customName.trim());
    }

    public String getValue() {
        return predefinedName == DentalServiceName.OTHER ? customName : predefinedName.name();
    }

}