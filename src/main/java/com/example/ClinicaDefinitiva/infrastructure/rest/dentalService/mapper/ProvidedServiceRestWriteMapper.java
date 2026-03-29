package com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.mapper;


import com.example.ClinicaDefinitiva.application.dentalService.dto.CreateServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceDetailsDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceInfoDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceRateDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto.CreateServiceRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto.UpdateServiceDetailsRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto.UpdateServiceInfoRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto.UpdateServiceRateRequest;
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
