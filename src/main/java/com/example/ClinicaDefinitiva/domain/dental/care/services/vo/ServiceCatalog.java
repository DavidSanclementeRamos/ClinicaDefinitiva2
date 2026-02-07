package com.example.ClinicaDefinitiva.domain.dental.care.services.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

public final class ServiceCatalog {

    private final ServiceId id;
    private final String name;
    private final String category;

    public ServiceCatalog(ServiceId id, String name, String category) {

        if (name == null || name.isBlank()) throw new ValueObjectValidationException(ServiceVOError
                .ERR_SERVICE_NAME_NULL_OR_BLANK, VOContext.SERVICE_CATALOG);
        if (category == null || category.isBlank()) throw new ValueObjectValidationException(ServiceVOError
                .ERR_SERVICE_CATEGORY_NULL_OR_BLANK, VOContext.SERVICE_CATALOG);

        this.id = id;
        this.name = name;
        this.category = category;
    }

    public ServiceId getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return name + " (" + category + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceCatalog)) return false;
        ServiceCatalog that = (ServiceCatalog) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 🔗 Enum interno opcional como catálogo base
    public enum Defaults {
        GENERAL_CONSULTATION(new ServiceCatalog(ServiceId.GENERAL_CONSULTATION, "General Consultation", "General")),
        PROPHYLAXIS_CLEANING(new ServiceCatalog(ServiceId.PROPHYLAXIS_CLEANING, "Prophylaxis & Cleaning", "General")),
        ORTHO_METAL_BRACKETS(new ServiceCatalog(ServiceId.ORTHO_METAL_BRACKETS, "Metal Brackets", "Orthodontics")),
        ORTHO_CLEAR_ALIGNERS(new ServiceCatalog(ServiceId.ORTHO_CLEAR_ALIGNERS, "Clear Aligners", "Orthodontics")),
        SURG_WISDOM_EXTRACTION(new ServiceCatalog(ServiceId.SURG_WISDOM_EXTRACTION, "Wisdom Tooth Extraction", "Surgery")),
        SURG_SOFT_TISSUE_GRAFT(new ServiceCatalog(ServiceId.SURG_SOFT_TISSUE_GRAFT, "Soft Tissue Graft", "Surgery")),
        PED_SEALANTS_FLUORIDE(new ServiceCatalog(ServiceId.PED_SEALANTS_FLUORIDE, "Sealants & Fluoride", "Pediatrics")),
        PED_RESIN_RESTORATION(new ServiceCatalog(ServiceId.PED_RESIN_RESTORATION, "Resin Restoration", "Pediatrics")),
        AES_IN_OFFICE_WHITENING(new ServiceCatalog(ServiceId.AES_IN_OFFICE_WHITENING, "In-Office Whitening", "Aesthetics")),
        AES_PORCELAIN_VENEER(new ServiceCatalog(ServiceId.AES_PORCELAIN_VENEER, "Porcelain Veneer", "Aesthetics")),
        IMP_SINGLE_IMPLANT(new ServiceCatalog(ServiceId.IMP_SINGLE_IMPLANT, "Single Implant", "Implantology")),
        PRO_PORCELAIN_CROWN(new ServiceCatalog(ServiceId.PRO_PORCELAIN_CROWN, "Porcelain Crown", "Prosthetics"));

        private final ServiceCatalog catalog;

        Defaults(ServiceCatalog catalog) {
            this.catalog = catalog;
        }

        public ServiceCatalog get() {
            return catalog;
        }
    }


}