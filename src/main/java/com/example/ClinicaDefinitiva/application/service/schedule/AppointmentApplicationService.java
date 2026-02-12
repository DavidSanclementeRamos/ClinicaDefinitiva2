package com.example.ClinicaDefinitiva.application.service.schedule;

import com.example.ClinicaDefinitiva.application.dto.sheduled.AppointmentCompletionDTO;
import com.example.ClinicaDefinitiva.application.dto.sheduled.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.application.exceptions.AppointmentNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.ReceptionistNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.schedule.AppointmentReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.schedule.AppointmentWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.AppointmentUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
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

@Service
@Transactional
public class AppointmentApplicationService implements AppointmentUseCase {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentSchedulingService schedulingService;
    private final AppointmentReadMapper readMapper;
    private final AppointmentWriteMapper writeMapper;
    private final AuthorizationService authorizationService;
    private final ReceptionRepository receptionRepository;

    public AppointmentApplicationService(AppointmentRepository appointmentRepository,
                                         AppointmentSchedulingService schedulingService,
                                         AppointmentReadMapper readMapper,
                                         AppointmentWriteMapper writeMapper,
                                         AuthorizationService authorizationService,
                                         ReceptionRepository receptionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.schedulingService = schedulingService;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationService = authorizationService;
        this.receptionRepository = receptionRepository;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public ReadAppointmentDto findById(AppointmentId appointmentId,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(appointmentId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(""));

        return readMapper.toReadDto(appointment);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<ReadAppointmentDto> findAll(Pageable pageable,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(patientId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(dentistId.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(serviceId.getId())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return appointmentRepository.findByPatientAndDentist(patientId, dentistId, start, end, pageable)
                .map(readMapper::toReadDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadAppointmentDto save(CreateAppointmentDto dto,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Appointment appointment = writeMapper.fromCreateDto(dto,schedulingService);
        Appointment saved = appointmentRepository.save(appointment);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.APPOINTMENT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAppointmentDto update(UpdateAppointmentDto dto,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(dto.appointmentId())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Appointment appointment = appointmentRepository.findById(AppointmentId.of(dto.appointmentId()))
                .orElseThrow(() -> new AppointmentNotFoundException(""));

        writeMapper.updateFromDto(appointment,schedulingService, dto);
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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT),
                        ActionCatalog.of(ActionCatalog.BasicAction.CANCEL)
                ), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(id.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(""));

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT),
                        ActionCatalog.of(ActionCatalog.BasicAction.COMPLETE)
                ), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(id.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(""));

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT),
                        ActionCatalog.of(ActionCatalog.BasicAction.MARK_AS_NO_SHOW)
                ), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(id.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(""));

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

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new ReceptionistNotFoundException(""));

        SecurityContext context = SecurityContext
                .builder(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .withResourceId(id.getValue())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(""));


        ReadAppointmentDto dto = readMapper.toReadDto(appointment);
        appointmentRepository.delete(appointment.getId());

        return dto;
    }
}





