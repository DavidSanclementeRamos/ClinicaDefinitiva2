
package com.example.ClinicaDefinitiva.application.administration.operations.mapper;

import com.example.ClinicaDefinitiva.application.administration.operations.dto.CanAccommodateAppointmentDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.ExcludedBlockDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.RescheduleShiftDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class ShiftWriteMapper {

    public LocalTime toStart(ExcludedBlockDto dto) {
        return dto.start();
    }

    public LocalTime toEnd(ExcludedBlockDto dto) {
        return dto.end();
    }

    public String toReason(ExcludedBlockDto dto) {
        return dto.reason();
    }

    public LocalDate toNewDate(RescheduleShiftDto dto) {
        return dto.newDate();
    }

    public LocalTime toNewStart(RescheduleShiftDto dto) {
        return dto.newStart();
    }

    public LocalTime toNewEnd(RescheduleShiftDto dto) {
        return dto.newEnd();
    }

    public boolean toAuthorization(RescheduleShiftDto dto) {
        return dto.hasAuthorization();
    }

    public LocalDateTime toAppointmentStart(CanAccommodateAppointmentDto dto) {
        return dto.appointmentStart();
    }

    public LocalDateTime toAppointmentEnd(CanAccommodateAppointmentDto dto) {
        return dto.appointmentEnd();
    }
}