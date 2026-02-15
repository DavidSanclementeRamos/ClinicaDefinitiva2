package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.dentalService;


import com.example.ClinicaDefinitiva.application.dto.dentalService.CreateServiceDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.UpdateServiceDetailsDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.UpdateServiceInfoDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.UpdateServiceRateDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service.CreateServiceRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service.UpdateServiceDetailsRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service.UpdateServiceInfoRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service.UpdateServiceRateRequest;
import org.springframework.stereotype.Component;

/**
 * Mapper REST para conversión de DTOs de ProvidedService (escritura)
 */
@Component
public class ProvidedServiceRestWriteMapper {

    public CreateServiceDto toServiceCreate(CreateServiceRequest request) {
        if (request == null) return null;

        return new CreateServiceDto(

                request.name(),
                request.categoryId(),
                request.categoryName(),
                request.categoryType(),
                request.serviceType(),
                request.baseRateAmount(),
                request.currency(),
                request.durationMinutes(),
                request.requiresAuthorization(),
                request.description(),
                request.serviceType(),
                request.details()
        );
    }

    public UpdateServiceInfoDto toServiceUpdateInfo(UpdateServiceInfoRequest request) {
        if (request == null) return null;

        return new UpdateServiceInfoDto(
                request.name(),
                request.categoryId(),

                request.categoryName(),
                request.categoryType(),


                request.durationMinutes()   ,
                request.requiresAuthorization(),
                request.description()

                );
    }

    public UpdateServiceRateDto toServiceUpdateRate(UpdateServiceRateRequest request) {
        if (request == null) return null;

        return new UpdateServiceRateDto(
                request.newRate(),
                request.currency(),
                request.justification()
        );
    }

    public UpdateServiceDetailsDto toServiceUpdateDetails(UpdateServiceDetailsRequest request) {
        if (request == null) return null;

        return new UpdateServiceDetailsDto(
                request.serviceDetails(),
                request.additionalDetails()
        );
    }
}
