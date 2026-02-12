package com.example.ClinicaDefinitiva.domain.dental.care.service.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

public final class ServiceCatalog {

    private final ServiceId id;
    private final String name;
    private final String category;

    private ServiceCatalog(ServiceId id, String name, String category) {

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



    public enum Defaults {
        GENERAL_CONSULTATION(new ServiceCatalog(
                ServiceId.of(1L),
                "General Consultation",
                "General"
        )),
        PROPHYLAXIS_CLEANING(new ServiceCatalog(
                ServiceId.of(2L),
                "Prophylaxis & Cleaning",
                "General"
        )),
        ORTHO_METAL_BRACKETS(new ServiceCatalog(
                ServiceId.of(3L),
                "Metal Brackets",
                "Orthodontics"
        )),
        ORTHO_CLEAR_ALIGNERS(new ServiceCatalog(
                ServiceId.of(4L),
                "Clear Aligners",
                "Orthodontics"
        )),
        SURG_WISDOM_EXTRACTION(new ServiceCatalog(
                ServiceId.of(5L),
                "Wisdom Tooth Extraction",
                "Surgery"
        )),
        SURG_SOFT_TISSUE_GRAFT(new ServiceCatalog(
                ServiceId.of(6L),
                "Soft Tissue Graft",
                "Surgery"
        )),
        PED_SEALANTS_FLUORIDE(new ServiceCatalog(
                ServiceId.of(7L),
                "Sealants & Fluoride",
                "Pediatrics"
        )),
        PED_RESIN_RESTORATION(new ServiceCatalog(
                ServiceId.of(8L),
                "Resin Restoration",
                "Pediatrics"
        )),
        AES_IN_OFFICE_WHITENING(new ServiceCatalog(
                ServiceId.of(9L),
                "In-Office Whitening",
                "Aesthetics"
        )),
        AES_PORCELAIN_VENEER(new ServiceCatalog(
                ServiceId.of(10L),
                "Porcelain Veneer",
                "Aesthetics"
        )),
        IMP_SINGLE_IMPLANT(new ServiceCatalog(
                ServiceId.of(11L),
                "Single Implant",
                "Implantology"
        )),
        PRO_PORCELAIN_CROWN(new ServiceCatalog(
                ServiceId.of(12L),
                "Porcelain Crown",
                "Prosthetics"
        ));

        private final ServiceCatalog catalog;

        Defaults(ServiceCatalog catalog) {
            this.catalog = catalog;
        }

        public ServiceCatalog get() {
            return catalog;
        }
    }



}