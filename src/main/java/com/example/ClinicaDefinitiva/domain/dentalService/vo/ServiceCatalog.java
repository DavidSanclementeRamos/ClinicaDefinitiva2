package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;

import java.util.Objects;

public final class ServiceCatalog {

    private final ServiceId id;
    private final ServiceName name;       private final String category;

    private ServiceCatalog(ServiceId id, ServiceName name, String category) {
        
        if (category == null || category.isBlank()) {
            throw new ValueObjectValidationException(
                ServiceVOError.ERR_SERVICE_CATEGORY_NULL_OR_BLANK,
                VOContext.DENTAL_SERVICES
            );
        }

        this.id = id;
        this.name = name;
        this.category = category;
    }

    public static ServiceCatalog of(ServiceId id, ServiceName name, String category) {
        return new ServiceCatalog(id, name, category);
    }

    public ServiceId getId() { return id; }
    public ServiceName getName() { return name; }
    public String getCategory() { return category; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceCatalog)) return false;
        ServiceCatalog that = (ServiceCatalog) o;
        return id.equals(that.id) &&
               name.equals(that.name) &&
               category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category);
    }

    @Override
    public String toString() {
        return "ServiceCatalog{id=" + id + ", name=" + name + ", category='" + category + "'}";
    }

    public enum Defaults {
        GENERAL_CONSULTATION(ServiceCatalog.of(ServiceId.of(1L), ServiceName.custom("General Consultation"), "General")),
        PROPHYLAXIS_CLEANING(ServiceCatalog.of(ServiceId.of(2L), ServiceName.custom("Prophylaxis & Cleaning"), "General")),
        ORTHO_METAL_BRACKETS(ServiceCatalog.of(ServiceId.of(3L), ServiceName.custom("Metal Brackets"), "Orthodontics")),
        ORTHO_CLEAR_ALIGNERS(ServiceCatalog.of(ServiceId.of(4L), ServiceName.custom("Clear Aligners"), "Orthodontics")),
        SURG_WISDOM_EXTRACTION(ServiceCatalog.of(ServiceId.of(5L), ServiceName.custom("Wisdom Tooth Extraction"), "Surgery")),
        SURG_SOFT_TISSUE_GRAFT(ServiceCatalog.of(ServiceId.of(6L), ServiceName.custom("Soft Tissue Graft"), "Surgery")),
        PED_SEALANTS_FLUORIDE(ServiceCatalog.of(ServiceId.of(7L), ServiceName.custom("Sealants & Fluoride"), "Pediatrics")),
        PED_RESIN_RESTORATION(ServiceCatalog.of(ServiceId.of(8L), ServiceName.custom("Resin Restoration"), "Pediatrics")),
        AES_IN_OFFICE_WHITENING(ServiceCatalog.of(ServiceId.of(9L), ServiceName.custom("In-Office Whitening"), "Aesthetics")),
        AES_PORCELAIN_VENEER(ServiceCatalog.of(ServiceId.of(10L), ServiceName.custom("Porcelain Veneer"), "Aesthetics")),
        IMP_SINGLE_IMPLANT(ServiceCatalog.of(ServiceId.of(11L), ServiceName.custom("Single Implant"), "Implantology")),
        PRO_PORCELAIN_CROWN(ServiceCatalog.of(ServiceId.of(12L), ServiceName.custom("Porcelain Crown"), "Prosthetics"));

        private final ServiceCatalog catalog;

        Defaults(ServiceCatalog catalog) {
            this.catalog = catalog;
        }

        public ServiceCatalog get() {
            return catalog;
        }
    }
}