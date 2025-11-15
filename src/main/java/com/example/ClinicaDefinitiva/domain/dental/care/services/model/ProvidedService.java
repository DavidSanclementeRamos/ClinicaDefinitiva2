package com.example.ClinicaDefinitiva.domain.dental.care.services.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.*;

import java.util.Objects;
import java.util.Optional;

public class ProvidedService {
    private final ServiceId id;                             // Identificador único del servicio
    private  String name;                         // Nombre del servicio
    private  ServiceCatalog category;      //Categoría (ej. "Orthodontics", "Surgery", "Pediatrics")
    private  ServiceCode code;               // Código estandarizado (ej. CUPS en Colombia)
    private  Money baseRate;                // Tarifa base del servicio
    private  ServiceDuration duration;           // Duración estimada en minutos
    private  boolean requiresAuthorization;      // Indica si requiere autorización (EPS/aseguradora)
    private  String description;                // Descripción general del servicio
    private  ServiceStatus status;        // Estado del servicio (activo/inactivo)
    private ServiceDetails details; // NUEVO: composición



    public ProvidedService(ServiceId id, String name, ServiceCatalog category, ServiceCode code,
                           Money baseRate, ServiceDuration duration, boolean requiresAuthorization,
                           String description, ServiceStatus status, ServiceDetails details) {

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
        this.details = details;
    }

    public void deactivate() {
        if (!status.isActive()) {
            throw new IllegalStateException("Service is already inactive");
        }
        this.status = new ServiceStatus("Inactive");
    }

    public Optional<ServiceDetails> getDetails() { return Optional.ofNullable(details); }



  //  public ServiceDetails getDetails() {
      //  return details;
    //}

    public void setDetails(ServiceDetails details) {
        this.details = details;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
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

    public void updateCommon(String name, ServiceCatalog serviceCatalog, Money money, ServiceDuration serviceDuration, Boolean requiresAuthorization, String description, String status) {
    }

    // updateCommon
    public void updateCommon(String name, ServiceCatalog category, Money baseRate, ServiceDuration duration, Boolean requiresAuthorization, String description, ServiceStatus status) {
        if (name != null && name.isBlank()) throw new IllegalArgumentException("name cannot be blank");
        if (description != null && description.isBlank()) throw new IllegalArgumentException("description cannot be blank");
        if (name != null) this.name = name;
        if (category != null) this.category = category;
        if (baseRate != null) this.baseRate = baseRate;
        if (duration != null) this.duration = duration;
        if (requiresAuthorization != null) this.requiresAuthorization = requiresAuthorization;
        if (description != null) this.description = description;
        if (status != null) this.status = status;
    }

    // replaceDetails
    public void replaceDetails(ServiceDetails newDetails) {
        this.details = newDetails;
    }

    // helpers tipo-específicos
    public void updateOrthodonticDetails(String applianceType, Integer months, Boolean requiresFollowup) {
        OrthodonticDetails od = new OrthodonticDetails(applianceType, months, requiresFollowup);
        replaceDetails(od);
    }
    public void updateProstheticDetails(String fixedOrRemovable, String material, String prostheticType, Integer units) {
        ProstheticDetails pd = new ProstheticDetails(fixedOrRemovable, material, prostheticType, units);
        replaceDetails(pd);
    }
    public void updateImplantologyDetails(Integer healingMonths, String implantType, String placementSite, Boolean requiresBoneGraft) {
        ImplantologyDetails id = new ImplantologyDetails(healingMonths, implantType, placementSite, requiresBoneGraft);
        replaceDetails(id);
    }
    public void updateAestheticDetails(String aestheticType, String materialUsed, String expectedResult) {
        AestheticDetails ad = new AestheticDetails(aestheticType, materialUsed, expectedResult);
        replaceDetails(ad);
    }


    public void updatePediatricDetails(String ageRange, String behaviorManagement, String pediatricMaterials) {
        PediatricDetails pd = new PediatricDetails(ageRange, behaviorManagement, pediatricMaterials);
        replaceDetails(pd);
    }
    public void updateSurgicalDetails(String surgeryType, String complexityLevel, Boolean requiresAnesthesia, Boolean operatingRoomNeeded) {
        SurgicalDetails sd = new SurgicalDetails(surgeryType, complexityLevel, requiresAnesthesia, operatingRoomNeeded);
        replaceDetails(sd);
    }

    // equals/hashCode por id
    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof ProvidedService)) return false; ProvidedService that = (ProvidedService)o; return id.equals(that.id); }
    @Override public int hashCode() { return Objects.hash(id); }


}
