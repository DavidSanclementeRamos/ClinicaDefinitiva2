package com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.mapper;


import com.example.ClinicaDefinitiva.application.dentalService.dto.PageServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.ReadServiceDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto.PageServiceResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto.ReadServiceResponse;
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



