
package com.example.ClinicaDefinitiva.application.mapper.Administration.operations;

import com.example.ClinicaDefinitiva.application.dto.administration.operations.AssignShiftDto;
import com.example.ClinicaDefinitiva.application.dto.administration.operations.CanAccommodateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.operations.ExcludedBlockDto;
import com.example.ClinicaDefinitiva.application.dto.administration.operations.RescheduleShiftDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.service.ShiftAssignmentService;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class ShiftWriteMapper {

    
    


    // Actualización: excluir bloque dentro de turno
    public void excludeBlockFromDto(ExcludedBlockDto dto, Shift shift) {
        shift.excludeBlock(
                dto.start(),
                dto.end(),
                dto.reason()
        );
    }

    // Actualización: reagendar turno
    public void rescheduleFromDto(RescheduleShiftDto dto, Shift shift) {
        shift.reschedule(
                dto.newDate(),
                dto.newStart(),
                dto.newEnd(),
                dto.hasAuthorization()
        );
    }

    

    // Verificación: puede acomodar cita
    public boolean canAccommodateFromDto(CanAccommodateAppointmentDto dto, Shift shift) {
        return shift.canAccommodateAppointment(
                dto.appointmentStart(),
                dto.appointmentEnd()
        );
    }
}
