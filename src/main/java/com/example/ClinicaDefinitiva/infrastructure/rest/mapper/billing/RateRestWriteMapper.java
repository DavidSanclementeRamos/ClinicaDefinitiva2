package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.billing;

import com.example.ClinicaDefinitiva.application.dto.billing.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.CreateRateRequest;
import org.springframework.stereotype.Component;

@Component
public class RateRestWriteMapper {
    public CreateRateDto toServiceCreate(CreateRateRequest request) {
        if (request == null) return null;

        return new CreateRateDto(
                request.serviceId(),
                request.payerType(),
                request.contractId(),
                request.amount(),
                request.currency(),
                request.validFrom(),
                request.validTo()
        );
    }
}
