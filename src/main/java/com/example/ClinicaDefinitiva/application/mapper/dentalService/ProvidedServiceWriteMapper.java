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
import com.example.ClinicaDefinitiva.domain.vo.Price;

import java.util.Currency;
import org.springframework.stereotype.Component;


@Component
public class ProvidedServiceWriteMapper {

    public ServiceName toServiceName(CreateServiceDto dto) {
        return ServiceName.custom(dto.name());
    }

    public ServiceCatalog toServiceCategory(CreateServiceDto dto) {
        return ServiceCatalog.of(
            ServiceId.of(dto.categoryId()),
            ServiceName.custom(dto.categoryName()),
            dto.categoryType()
        );
    }

    public ServiceCode toServiceCode(CreateServiceDto dto) {
        return ServiceCode.of(dto.code());
    }

    public Price toBaseRate(CreateServiceDto dto) {
        return Price.of(dto.baseRateAmount(), Currency.getInstance(dto.currency()));
    }

    public ServiceDuration toDuration(CreateServiceDto dto) {
        return ServiceDuration.of(dto.durationMinutes());
    }

    public boolean toRequiresAuthorization(CreateServiceDto dto) {
        return dto.requiresAuthorization();
    }

    public ServiceDescription toDescription(CreateServiceDto dto) {
        return ServiceDescription.of(dto.description());
    }

    public ServiceDetails toDetails(CreateServiceDto dto) {
        ServiceType serviceType = ServiceType.valueOf(dto.serviceType().toUpperCase());
        return ServiceDetailsFactory.fromMap(serviceType, dto.details());
    }

    public ServiceStatus toStatus(CreateServiceDto dto) {
        return ServiceStatus.of(ServiceStatus.State.ACTIVE);
    }

    // Métodos para Update DTOs
    public ServiceName toServiceName(UpdateServiceInfoDto dto) {
        return dto.name() != null ? ServiceName.of(ServiceName.DentalServiceName.valueOf(dto.name())) : null;
    }

    public ServiceCatalog toServiceCategory(UpdateServiceInfoDto dto) {
        return dto.categoryId() != null
                ? ServiceCatalog.of(ServiceId.of(dto.categoryId()), ServiceName.custom(dto.categoryName()), dto.categoryType())
                : null;
    }

    public ServiceDuration toDuration(UpdateServiceInfoDto dto) {
        return dto.durationMinutes() != null ? ServiceDuration.of(dto.durationMinutes()) : null;
    }

    public boolean toRequiresAuthorization(UpdateServiceInfoDto dto) {
        return dto.requiresAuthorization();
    }

    public ServiceDescription toDescription(UpdateServiceInfoDto dto) {
        return dto.description() != null ? ServiceDescription.of(dto.description()) : null;
    }

    public Price toRate(UpdateServiceRateDto dto) {
        return Price.of(dto.newRateAmount(), Currency.getInstance(dto.currency()));
    }
    

    public ServiceDetails toDetails(UpdateServiceDetailsDto dto) {
        ServiceType serviceType = ServiceType.valueOf(dto.serviceType().toUpperCase());
        return ServiceDetailsFactory.fromMap(serviceType, dto.details());
    }
}
