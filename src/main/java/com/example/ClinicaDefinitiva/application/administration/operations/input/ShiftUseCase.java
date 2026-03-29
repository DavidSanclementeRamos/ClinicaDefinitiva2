
package com.example.ClinicaDefinitiva.application.administration.operations.input;

import com.example.ClinicaDefinitiva.application.administration.operations.dto.AssignShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.CanAccommodateAppointmentDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.ExcludedBlockDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.PageShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.ReadShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.RescheduleShiftDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;

/**
 * Puerto de entrada para casos de uso de turnos operativos (Shift).
 *
 * Define todas las operaciones disponibles para gestionar turnos de dentistas.
 * Siguiendo la arquitectura hexagonal, esta interfaz pertenece a la capa de aplicación
 * y será implementada por servicios de aplicación.
 *
 * Seguridad: Todos los métodos requieren requesterId y requesterRolId
 * para autorización explícita, según ADR-48.
 *
 * Diferencia clave:
 * - WorkingHours: Contrato laboral recurrente (ej. "Lunes 8-17h")
 * - Shift: Presencia operativa específica (ej. "15-Feb-2026 9-18h")
 */
public interface ShiftUseCase {

    ReadShiftDto findById(
            ShiftId shiftId ,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageShiftDto> findAll(
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageShiftDto> findByDentist(
            DentistId dentistId,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto assignShift(
            AssignShiftDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto excludeBlock(
            ShiftId id,
            ExcludedBlockDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    boolean canAccommodateAppointment(
            ShiftId shiftId,
            CanAccommodateAppointmentDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto reschedule(
            ShiftId shiftId,
            RescheduleShiftDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto cancel(
            ShiftId shiftId,
            String reason,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto complete(
            ShiftId shiftId,
            UserIdentityId requesterId,
            RolId requesterRolId
    );
}
