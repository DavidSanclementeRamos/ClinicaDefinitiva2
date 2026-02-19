package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.billing;

import com.example.ClinicaDefinitiva.application.dto.billing.rate.PageRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.ReadRateDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.PageRateResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.ReadRateResponse;
import org.springframework.stereotype.Component;

@Component
public class RateRestReadMapper {
    public ReadRateResponse toRest(ReadRateDto dto) {
        if (dto == null) return null;

        return new ReadRateResponse(
                dto.id(),
                dto.serviceId(),
                dto.payerType(),
                dto.contractId(),
                dto.amount(),
                dto.currency(),
                dto.validFrom(),
                dto.validTo(),
                dto.active(),
                dto.currentlyValid()

        );
    }

    public PageRateResponse toPageRest(PageRateDto dto) {
        if (dto == null) return null;

        return new PageRateResponse(
                dto.id(),
                dto.serviceId(),
                dto.payerType(),
                dto.amount(),
                dto.currency(),
                dto.validFrom(),
                dto.validTo(),
                dto.active()
        );

    }
}
