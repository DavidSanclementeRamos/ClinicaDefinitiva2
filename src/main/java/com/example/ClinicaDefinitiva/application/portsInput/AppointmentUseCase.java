package com.example.ClinicaDefinitiva.application.portsInput;

import com.example.ClinicaDefinitiva.application.dto.sheduled.AppointmentCompletionDTO;
import com.example.ClinicaDefinitiva.application.dto.sheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AppointmentUseCase {
    ReadAppointmentDto findById(AppointmentId appointmentId, UserIdentityId requesterId, RolId requesterRolId);
    Page<ReadAppointmentDto> findAll(Pageable pageable,UserIdentityId requesterId, RolId requesterRolId);
    Page<ReadAppointmentDto> findByPatientId(PatientId patientId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<ReadAppointmentDto> findByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<ReadAppointmentDto> findByDentistId(DentistId dentistId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<ReadAppointmentDto> findByServiceId(ServiceId serviceId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<ReadAppointmentDto> findByStatus(AppointmentStatus status, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<ReadAppointmentDto> findByPatientAndDentist(PatientId patientId, DentistId dentistId, LocalDate start, LocalDate end, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    ReadAppointmentDto save(CreateAppointmentDto dto,UserIdentityId requesterId, RolId requesterRolId);
    ReadAppointmentDto update(UpdateAppointmentDto dto,UserIdentityId requesterId, RolId requesterRolId);

    ReadAppointmentDto cancel(AppointmentId id, String reason, UserIdentityId requesterId, RolId requesterRolId);
    ReadAppointmentDto complete(AppointmentId id, AppointmentCompletionDTO completionDTO  , UserIdentityId requesterId, RolId requesterRolId);
    ReadAppointmentDto markAsNoShow(  AppointmentId id, String reason,UserIdentityId requesterId, RolId requesterRolId);

    ReadAppointmentDto daleById(AppointmentId id,UserIdentityId requesterId, RolId requesterRolId );

}
