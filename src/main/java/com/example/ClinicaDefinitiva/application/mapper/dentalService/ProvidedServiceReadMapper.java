package com.example.ClinicaDefinitiva.application.mapper.dentalService;

import com.example.ClinicaDefinitiva.application.dto.dentalService.PageServiceDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.ReadServiceDto;
import com.example.ClinicaDefinitiva.domain.dental.care.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.service.model.ProvidedService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProvidedServiceReadMapper {


    private final ObjectMapper objectMapper;

    public ProvidedServiceReadMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public ReadServiceDto toReadDto(ProvidedService service) {
        return new ReadServiceDto(
                service.getId().getId(),
                service.getName().getValue(),
                service.getCategory().getCategory(),
                service.getCode().getValue(),
                service.getBaseRate().asBigDecimal(),
                service.getBaseRate().getCurrency().getCurrencyCode(),
                service.getDuration().getMinutes(),
                service.isRequiresAuthorization(),
                service.getDescription().getValue(),
                service.getStatus().getValue().name(),
                service.getDetails()
                        .map(d -> d.serviceType().name())
                        .orElse(null) ,
                serializeDetails(service.getDetails().orElse(null))


        );
    }

    public PageServiceDto toPageDto(ProvidedService service) {
        return new PageServiceDto(
                service.getId().getId(),
                service.getName().getValue(),
                service.getCategory().getCategory(),
                service.getCode().getValue(),
                service.getBaseRate().asBigDecimal(),
                service.getBaseRate().getCurrency().getCurrencyCode(),
                service.getDuration().getMinutes(),
                service.getStatus().getValue().name(),
                service.getDetails()
                        .map(d -> d.serviceType().name())
                        .orElse(null)
        );
    }


    private String serializeDetails(ServiceDetails details) {
        if (details == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(details);
        } catch (JsonProcessingException e) {
            // Log error and return null rather than failing the entire operation
            // In production, use proper logging framework
            System.err.println("Error serializing service details: " + e.getMessage());
            return null;
        }
    }

}
