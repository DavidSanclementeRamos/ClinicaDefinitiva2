package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations;

import com.example.ClinicaDefinitiva.application.administration.operations.dto.ExcludedBlockDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.PageShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.ReadShiftDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.ExcludedBlockResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.PageShiftResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.ReadShiftResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShiftRestReadMapper {

    /**
     * Convierte ReadShiftDto (aplicación) a ReadShiftResponse (REST)
     */
    public ReadShiftResponse toRest(ReadShiftDto dto) {
        if (dto == null) return null;

        List<ExcludedBlockResponse> excludedBlocks = dto.excludedBlocks() != null ?
                dto.excludedBlocks().stream()
                        .map(this::toExcludedBlockResponse)
                        .toList() :
                List.of();

        return new ReadShiftResponse(
                dto.id(),
                dto.dentistId(),
                dto.date(),
                dto.startTime(),
                dto.endTime(),
                dto.type(),
                dto.status(),
                dto.cancellationReason(),
                excludedBlocks,
                dto.version()
        );
    }

    /**
     * Convierte ExcludedBlockDto a ExcludedBlockResponse
     */
    public ExcludedBlockResponse toExcludedBlockResponse(ExcludedBlockDto dto) {
        if (dto == null) return null;

        return new ExcludedBlockResponse(
                dto.start(),
                dto.end(),
                dto.reason()
        );
    }

    /**
     * Convierte PageShiftDto (aplicación) a PageShiftResponse (REST)
     */
    public PageShiftResponse toPageRest(PageShiftDto dto) {
        if (dto == null) return null;

        return new PageShiftResponse(
                dto.id(),
                dto.dentistId(),
                dto.date(),
                dto.startTime(),
                dto.endTime(),
                dto.type(),
                dto.status()
        );
    }
}
