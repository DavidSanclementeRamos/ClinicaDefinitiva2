package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

public class ProvidedService {
    private final ServiceId id;                             // Identificador único del servicio
    private final String name;                         // Nombre del servicio
    private final ServiceCatalog category;      //Categoría (ej. "Orthodontics", "Surgery", "Pediatrics")
    private final ServiceCode code;               // Código estandarizado (ej. CUPS en Colombia)
    private final Money baseRate;                // Tarifa base del servicio
    private final ServiceDuration duration;           // Duración estimada en minutos
    private final boolean requiresAuthorization;      // Indica si requiere autorización (EPS/aseguradora)
    private final String description;                // Descripción general del servicio
    private  ServiceStatus status;        // Estado del servicio (activo/inactivo)

    public ProvidedService(ServiceId id, String name, ServiceCatalog category, ServiceCode code,
                           Money baseRate, ServiceDuration duration, boolean requiresAuthorization,
                           String description, ServiceStatus status) {

        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be null or blank");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("description cannot be null or blank");

        this.id = id;
        this.name = name;
        this.category = category;
        this.code = code;
        this.baseRate = baseRate;
        this.duration = duration;
        this.requiresAuthorization = requiresAuthorization;
        this.description = description;
        this.status = status;
    }

    public void deactivate() {
        if (!status.isActive()) {
            throw new IllegalStateException("Service is already inactive");
        }
        this.status = new ServiceStatus("Inactive");
    }


    public Money getBaseRate() {
        return baseRate;
    }

    public ServiceCatalog getCategory() {
        return category;
    }

    public ServiceCode getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public ServiceDuration getDuration() {
        return duration;
    }

    public ServiceId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isRequiresAuthorization() {
        return requiresAuthorization;
    }

    public ServiceStatus getStatus() {
        return status;
    }
}
