
package com.example.ClinicaDefinitiva.application.administration.operations.mapper;

import com.example.ClinicaDefinitiva.application.administration.operations.dto.ExcludedBlockDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.PageShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.ReadShiftDto;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import org.springframework.stereotype.Component;

@Component
public class ShiftReadMapper {

    // Dominio → DTO de lectura detallada
    public ReadShiftDto toReadDto(Shift shift) {
        return new ReadShiftDto(
                shift.getId().value(),
                shift.getDentistId().value(),
                shift.getDate(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getType().toString(),
                shift.getStatus().toString(),
                shift.getCancellationReason(),
                shift.getExcludedBlocks().stream()
                        .map(b -> new ExcludedBlockDto(
                               
                                b.getStart(),
                                b.getEnd(),
                                b.getReason()))
                        .toList(),
                shift.getVersion()
        );
    }

    // Dominio → DTO de página (resumen)
    public PageShiftDto toPageDto(Shift shift) {
        return new PageShiftDto(
                shift.getId().value(),
                shift.getDentistId().value(),
                shift.getDate(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getType().toString(),
                shift.getStatus().toString()
        );
    }
}
