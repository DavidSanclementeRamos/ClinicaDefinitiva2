package com.example.ClinicaDefinitiva.infrastructure.rest.billing.mapper.rate;

import com.example.ClinicaDefinitiva.application.billing.dto.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.rate.CreateRateRequest;
import org.springframework.stereotype.Component;

@Component
public class RateRestWriteMapper {
    public CreateRateDto toServiceCreate(CreateRateRequest request) {

        return new CreateRateDto(
                request.serviceId(),
                request.payerType(),
                request.contractId(),
                request.amount(),
                request.currency()
                
        );
    }
}
