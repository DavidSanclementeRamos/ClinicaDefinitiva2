package com.example.ClinicaDefinitiva.application.mapper.dentalService;

import com.example.ClinicaDefinitiva.application.dto.dentalService.PageServiceDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.ReadServiceDto;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
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
            service.getDetails().map(d -> d.serviceType().name()).orElse(null),
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
            service.getDetails().map(d -> d.serviceType().name()).orElse(null)
        );
    }

    private String serializeDetails(ServiceDetails details) {
        try {
            return details != null ? objectMapper.writeValueAsString(details) : null;
        } catch (JsonProcessingException e) {
            // En cumplimiento con ADR-(Aplicación)-01, el mapper no debe fallar ni contener lógica defensiva compleja.
            // Se retorna null en caso de error de serialización.
            return null;
        }
    }
}
