package com.example.ClinicaDefinitiva.application.service.schedule;

import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.dto.sheduled.AppointmentCompletionDTO;
import com.example.ClinicaDefinitiva.application.dto.sheduled.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.application.exceptions.AppointmentNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.schedule.AppointmentReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.schedule.AppointmentWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.schedule.AppointmentUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.service.AppointmentSchedulingService;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
* POLÍTICAS:
 * - SectorBasedPolicy: Solo RECEPTIONIST del sector correcto puede gestionar citas
 * - (Futuro) AssignmentPolicy: Dentista solo ve sus citas asignadas
 */
@Service
@Transactional
public class AppointmentApplicationService implements AppointmentUseCase {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentSchedulingService schedulingService;
    private final AppointmentReadMapper readMapper;
    private final AppointmentWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public AppointmentApplicationService(
            AppointmentRepository appointmentRepository,
            AppointmentSchedulingService schedulingService,
            AppointmentReadMapper readMapper,
            AppointmentWriteMapper writeMapper,
            AuthorizationHelper authorizationHelper) {
        this.appointmentRepository = appointmentRepository;
        this.schedulingService = schedulingService;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public ReadAppointmentDto findById(AppointmentId appointmentId,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {
        // ⭐ CORREGIDO: Usar AuthorizationHelper
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(appointmentId.getValue())
                        .build()
        );

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        return readMapper.toReadDto(appointment);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<ReadAppointmentDto> findAll(Pageable pageable,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return appointmentRepository.findAll(pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<ReadAppointmentDto> findByPatientId(PatientId patientId,
                                                    Pageable pageable,
                                                    UserIdentityId requesterId,
                                                    RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(patientId.value())
                        .build()
        );

        return appointmentRepository.findByPatientId(patientId, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<ReadAppointmentDto> findByDateRange(LocalDateTime start,
                                                    LocalDateTime end,
                                                    Pageable pageable,
                                                    UserIdentityId requesterId,
                                                    RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return appointmentRepository.findByDateRange(start, end, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<ReadAppointmentDto> findByDentistId(DentistId dentistId,
                                                    Pageable pageable,
                                                    UserIdentityId requesterId,
                                                    RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(dentistId.value())
                        .build()
        );

        return appointmentRepository.findByDentistId(dentistId, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<ReadAppointmentDto> findByServiceId(ServiceId serviceId,
                                                    Pageable pageable,
                                                    UserIdentityId requesterId,
                                                    RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(serviceId.getId())
                        .build()
        );

        return appointmentRepository.findByServiceId(serviceId, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<ReadAppointmentDto> findByStatus(AppointmentStatus status,
                                                 Pageable pageable,
                                                 UserIdentityId requesterId,
                                                 RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return appointmentRepository.findByStatus(status, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<ReadAppointmentDto> findByPatientAndDentist(PatientId patientId,
                                                            DentistId dentistId,
                                                            LocalDate start,
                                                            LocalDate end,
                                                            Pageable pageable,
                                                            UserIdentityId requesterId,
                                                            RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return appointmentRepository.findByPatientAndDentist(patientId, dentistId, start, end, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadAppointmentDto save(CreateAppointmentDto dto,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        Appointment appointment = schedulingService.scheduleAppointment(
            writeMapper.toDentistId(dto),
            writeMapper.toPatientId(dto),
            writeMapper.toStart(dto),
            writeMapper.toEnd(dto),
            writeMapper.toType(dto),
            writeMapper.toReason(dto),
            writeMapper.toServiceId(dto)
        );

        Appointment saved = appointmentRepository.save(appointment);
        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAppointmentDto update(UpdateAppointmentDto dto,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(dto.appointmentId())
                        .build()
        );

        Appointment appointment = appointmentRepository.findById(AppointmentId.of(dto.appointmentId()))
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        schedulingService.rescheduleAppointment(
            appointment,
            writeMapper.toDentistId(dto),
            writeMapper.toPatientId(dto),
            writeMapper.toNewStart(dto),
            writeMapper.toNewEnd(dto)
        );

        Appointment updated = appointmentRepository.save(appointment);
        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.CANCEL)
    public ReadAppointmentDto cancel(AppointmentId id,
                                     String reason,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.CANCEL,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        appointment.cancel(reason);
        Appointment cancelled = appointmentRepository.save(appointment);

        return readMapper.toReadDto(cancelled);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.COMPLETE)
    public ReadAppointmentDto complete(AppointmentId id,
                                       AppointmentCompletionDTO completionDTO,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.COMPLETE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        writeMapper.toCompletion(completionDTO);
        Appointment completed = appointmentRepository.save(appointment);

        return readMapper.toReadDto(completed);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.MARK_AS_NO_SHOW)
    public ReadAppointmentDto markAsNoShow(AppointmentId id,
                                           String reason,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.MARK_AS_NO_SHOW,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        appointment.markAsNoShow(reason);
        Appointment updated = appointmentRepository.save(appointment);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.DELETE)
    public ReadAppointmentDto daleById(AppointmentId id,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.APPOINTMENT,
                ActionCatalog.BasicAction.DELETE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        ReadAppointmentDto dto = readMapper.toReadDto(appointment);
        appointmentRepository.delete(appointment.getId());

        return dto;
    }
}























































































