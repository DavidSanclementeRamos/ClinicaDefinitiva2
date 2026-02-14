package com.example.ClinicaDefinitiva.application.mapper.dentalService;

import com.example.ClinicaDefinitiva.application.dto.dentalService.CreateServiceDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.UpdateServiceDetailsDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.UpdateServiceInfoDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.UpdateServiceRateDto;
import com.example.ClinicaDefinitiva.domain.dental.care.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dental.care.service.ServiceDetailsFactory;
import com.example.ClinicaDefinitiva.domain.dental.care.service.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.*;

import java.util.Currency;

public class ProvidedServiceWriteMapper {


    public ProvidedService fromCreateDto(CreateServiceDto dto) {

        // Parse service type
        ServiceType serviceType = ServiceType.valueOf(dto.serviceType().toUpperCase());

        // Create service-specific details using factory
        ServiceDetails details = ServiceDetailsFactory.fromMap(
                serviceType,
                dto.details()
        );

        // Build service using builder pattern
        return ProvidedService.builder()
                //.id(ServiceId.generate())
                .name(ServiceName.custom(dto.name()))
                .category(ServiceCatalog.of(ServiceId.of(dto.categoryId()), dto.categoryName(), dto.categoryType()))
                .code(ServiceCode.of(dto.code()))
                .baseRate(Price.of(dto.baseRateAmount(), Currency.getInstance(dto.currency())))
                .duration(ServiceDuration.of(dto.durationMinutes()))
                .requiresAuthorization(dto.requiresAuthorization())
                .description(ServiceDescription.of(dto.description()))
                .details(details)
                .status(ServiceStatus.of(ServiceStatus.State.ACTIVE))
                .build();
    }


    public void updateInformationFromDto(UpdateServiceInfoDto dto, ProvidedService service) {
        service.updateInformation(
                dto.name() != null ? ServiceName.of(ServiceName.DentalServiceName.valueOf(dto.name())) : null,
                dto.categoryId() != null ?
                        ServiceCatalog.of(ServiceId.of( dto.categoryId()), dto.categoryName(), dto.categoryType()) : null,
                dto.durationMinutes() != null ?
                        ServiceDuration.of(dto.durationMinutes()) : null,
                dto.requiresAuthorization(),
                dto.description() != null ?
                        ServiceDescription.of(dto.description()) : null
        );
    }


    public Price mapRateFromDto(UpdateServiceRateDto dto) {
        return Price.of(dto.newRateAmount(), Currency.getInstance(dto.currency()));
    }

    public ServiceDetails updateDetailsFromDto(UpdateServiceDetailsDto dto) {
        ServiceType serviceType = ServiceType.valueOf(dto.serviceType().toUpperCase());

        return ServiceDetailsFactory.fromMap(
                serviceType,
                dto.details()
        );
    }




}
