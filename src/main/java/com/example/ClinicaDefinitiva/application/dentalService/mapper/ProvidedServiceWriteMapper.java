package com.example.ClinicaDefinitiva.application.dentalService.mapper;

import com.example.ClinicaDefinitiva.application.dentalService.dto.CreateServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceDetailsDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceInfoDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceRateDto;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetailsFactory;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceCatalog;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceCode;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDescription;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceName;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceStatus;
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
