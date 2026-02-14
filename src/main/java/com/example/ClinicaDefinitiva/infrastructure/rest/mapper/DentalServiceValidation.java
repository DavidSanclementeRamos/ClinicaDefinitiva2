package com.example.ClinicaDefinitiva.infrastructure.rest.mapper;


import com.example.ClinicaDefinitiva.application.dto.dentalService.CreateProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.UpdateProvidedServiceDto;

public final class DentalServiceValidation {

    private DentalServiceValidation() {}

    /**public static void validateCreateDto(CreateProvidedServiceDto dto) {
        //String type = dto.serviceType == null ? (dto.catalog.category == null ? null : dto.catalog.category).toUpperCase())dto.serviceType.toUpperCase();

        switch (type) {
            case "ORTHODONTIC" -> {
                if (dto.orthodontic == null) throw new IllegalArgumentException("orthodontic details required for serviceType ORTHODONTIC");
                if (dto.orthodontic.applianceType == null || dto.orthodontic.applianceType.isBlank()) throw new IllegalArgumentException("applianceType is required");
            }
            case "PROSTHETIC" -> {
                if (dto.prosthetic == null) throw new IllegalArgumentException("prosthetic details required for serviceType PROSTHETIC");
            }
            case "IMPLANTOLOGY" -> {
                if (dto.implantology == null) throw new IllegalArgumentException("implantology details required for serviceType IMPLANTOLOGY");
            }
            case "AESTHETIC" -> {
                if (dto.aesthetic == null) throw new IllegalArgumentException("aesthetic details required for serviceType AESTHETIC");
            }
            case "PEDIATRIC" -> {
                if (dto.pediatric == null) throw new IllegalArgumentException("pediatric details required for serviceType PEDIATRIC");
            }
            case "SURGICAL" -> {
                if (dto.surgical == null) throw new IllegalArgumentException("surgical details required for serviceType SURGICAL");
            }
            default -> {
                // no-op: allow generic services with no details
            }
        }
    }*/

    public static void validateUpdateDto(UpdateProvidedServiceDto dto) {
        // Si trae detail parcial, valida su coherencia mínima
        if (dto == null) return;
        if (dto.orthodontic != null) {
            if (dto.orthodontic.applianceType == null || dto.orthodontic.applianceType.isBlank())
                throw new IllegalArgumentException("applianceType is required in orthodontic details");
        }
        // similar checks pueden añadirse para otros detalles según reglas
    }
}
