package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.dentalService;


import com.example.ClinicaDefinitiva.application.dto.dentalService.PageServiceDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.ReadServiceDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service.PageServiceResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service.ReadServiceResponse;
import org.springframework.stereotype.Component;


@Component
public class ProvidedServiceRestReadMapper {

    public ReadServiceResponse toRest(ReadServiceDto dto) {
        if (dto == null) return null;

        return new ReadServiceResponse(
                dto.id(),
                dto.code(),
                dto.name(),
                dto.description(),
                dto.baseRate(),
                dto.durationMinutes(),
                dto.category(),
                dto.serviceType(),
                dto.detailsJson(),
                dto.requiresAuthorization(),
                dto.category(),
                dto.status(),
                dto.baseRate()

        );
    }

    public PageServiceResponse toPageRest(PageServiceDto dto) {
        if (dto == null) return null;

        return new PageServiceResponse(
                dto.id(),
                dto.name(),
                dto.category(),
                dto.code(),
                dto.baseRate(),
                dto.currency(),
                dto.durationMinutes(),
                dto.status(),
                dto.serviceType()
        );
    }
}



