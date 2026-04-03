package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.mapper;

import com.example.ClinicaDefinitiva.application.administration.operations.dto.AssignShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.CanAccommodateAppointmentDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.ExcludedBlockDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.RescheduleShiftDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.AssignShiftRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.ExcludedBlockRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.RescheduleShiftRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShiftRestWriteMapper {

    /**
     * Convierte AssignShiftRequest (REST) a AssignShiftDto (aplicación)
     */
    public AssignShiftDto toService(AssignShiftRequest request) {
        if (request == null) return null;

        return new AssignShiftDto(
                request.dentistId(),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.type()
        );
    }

    /**
     * Convierte ExcludedBlockRequest (REST) a ExcludedBlockDto (aplicación)
     */
    public ExcludedBlockDto toExcludedBlockDto(ExcludedBlockRequest request) {
        if (request == null) return null;

        return new ExcludedBlockDto(
                request.start(),
                request.end(),
                request.reason()
        );
    }

    /**
     * Convierte RescheduleShiftRequest (REST) a RescheduleShiftDto (aplicación)
     */
    public RescheduleShiftDto toRescheduleDto(RescheduleShiftRequest request) {
        if (request == null) return null;

        return new RescheduleShiftDto(
                request.newDate(),
                request.newStart(),
                request.newEnd(),
                request.hasAuthorization()
        );
    }

    /**
     * Crea CanAccommodateAppointmentDto a partir de parámetros
     */
    public CanAccommodateAppointmentDto toCanAccommodateDto(
            LocalDateTime appointmentStart, 
            LocalDateTime appointmentEnd) {
        
        return new CanAccommodateAppointmentDto(
                appointmentStart,
                appointmentEnd
        );
    }

    /**
     * Crea ExcludedBlockDto a partir de parámetros individuales
     */
    public ExcludedBlockDto toExcludedBlockDto(
            LocalDateTime start,
            LocalDateTime end,
            String reason) {
        
        // Nota: ExcludedBlock usa LocalTime, por lo que necesitamos extraer la hora
        // Esto asume que start y end son del mismo día que el turno
        return new ExcludedBlockDto(
                start.toLocalTime(),
                end.toLocalTime(),
                reason
        );
    }
}
