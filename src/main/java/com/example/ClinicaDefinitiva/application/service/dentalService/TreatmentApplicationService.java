package com.example.ClinicaDefinitiva.application.service.dentalService;


import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentDto;
import com.example.ClinicaDefinitiva.application.exceptions.TreatmentNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.dentalService.treatment.TreatmentReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.dentalService.treatment.TreatmentWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.dentalService.TreatmentUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.output.TreatmentRepository;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Servicio de aplicación para Treatment (Tratamientos clínicos).
 *
 * Implementa políticas de autorización:
 * - Los pacientes solo pueden ver sus propios tratamientos (Ownership).
 * - Los odontólogos pueden ver los tratamientos en los que están asignados.
 * - Los recepcionistas pueden gestionar todos los tratamientos (sujetos a su sector).
 *
 */
@Service
@Transactional
public class TreatmentApplicationService implements TreatmentUseCase {

    private final TreatmentRepository treatmentRepository;
    private final DentistRepository dentistRepository;
    private final ReceptionRepository receptionRepository;
    private final TreatmentReadMapper readMapper;
    private final TreatmentWriteMapper writeMapper;
    private final AuthorizationService authorizationService;
    private final PatientRepository patientRepository;

    public TreatmentApplicationService(
            TreatmentRepository treatmentRepository,
            DentistRepository dentistRepository,
            ReceptionRepository receptionRepository,
            TreatmentReadMapper readMapper,
            TreatmentWriteMapper writeMapper,
            AuthorizationService authorizationService, PatientRepository patientRepository) {
        this.treatmentRepository = treatmentRepository;
        this.dentistRepository = dentistRepository;
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationService = authorizationService;
        this.patientRepository = patientRepository;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.TREATMENT,
            action = ActionCatalog.BasicAction.READ)
    public TreatmentDto findById(TreatmentId id,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new TreatmentNotFoundException(""));

        // Construir contexto con ownership y dentist assignment
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.TREATMENT)), requesterId)
                .withResourceId(id.getValue());


        // Si tiene dentista asignado, agregar al contexto
        if (treatment.getDentistId() != null) {
            contextBuilder.withAttribute("assignedDentistUserId",
                    treatment.getDentistId());
        }

        // Si es receptionist, agregar sector
        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );


         Patient patient = patientRepository.findByUserId(requesterId);

         contextBuilder.withPatientGuardianId(patient.getGuardianId().getValue());


        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        return readMapper.toDto(treatment);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.TREATMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<TreatmentDto> findAll(Pageable pageable,
                                      UserIdentityId requesterId,
                                      RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.TREATMENT)), requesterId);

        // Si es receptionist, agregar sector
        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // Si es dentist, filtrar por tratamientos asignados
        return dentistRepository.findByUserId(requesterId)
                .map(dentist -> treatmentRepository.findByDentist(dentist.getDentistId(), pageable)
                        .map(readMapper::toDto))
                .orElseGet(() -> treatmentRepository.findAll(pageable)
                        .map(readMapper::toDto));
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.TREATMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<TreatmentDto> findByStatus(TreatmentStatus status,
                                           Pageable pageable,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.TREATMENT)), requesterId);

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // Si es dentist, filtrar por sus tratamientos
        return dentistRepository.findByUserId(requesterId)
                .map(dentist -> treatmentRepository.findByDentistAndStatus(
                                dentist.getDentistId(), status, pageable)
                        .map(readMapper::toDto))
                .orElseGet(() -> treatmentRepository.findByStatus(status, pageable)
                        .map(readMapper::toDto));
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.TREATMENT,
            action = ActionCatalog.BasicAction.CREATE)
    public TreatmentDto create(CreateTreatmentDto dto,
                               UserIdentityId requesterId,
                               RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.TREATMENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Treatment treatment = writeMapper.fromCreateDto(dto);

        Treatment saved = treatmentRepository.save(treatment);
        return readMapper.toDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.TREATMENT,
            action = ActionCatalog.BasicAction.UPDATE)
    public TreatmentDto complete(TreatmentId id,
                                 LocalDate actualEndDate,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new TreatmentNotFoundException("Not found"));

        // Validar autorización (dentista asignado o receptionist)
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.TREATMENT)), requesterId)
                .withResourceId(id.getValue());

        if (treatment.getDentistId() != null) {
            contextBuilder.withAttribute("assignedDentistUserId",
                    treatment.getDentistId());
        }

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        treatment.complete(actualEndDate);
        Treatment completed = treatmentRepository.save(treatment);

        return readMapper.toDto(completed);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.TREATMENT,
            action = ActionCatalog.BasicAction.UPDATE)
    public TreatmentDto cancel(TreatmentId id,
                               String reason,
                               UserIdentityId requesterId,
                               RolId requesterRolId) {

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new TreatmentNotFoundException("Not found"));

        // Validar autorización (dentista asignado o receptionist)
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.TREATMENT)), requesterId)
                .withResourceId(id.getValue());

        if (treatment.getDentistId() != null) {
            contextBuilder.withAttribute("assignedDentistUserId",
                    treatment.getDentistId());
        }

        receptionRepository.findByUserId(requesterId).ifPresent(receptionist ->
                contextBuilder.withSector(receptionist.getSector().Value())
        );

        SecurityContext context = contextBuilder.build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        treatment.cancel(reason);
        Treatment cancelled = treatmentRepository.save(treatment);

        return readMapper.toDto(cancelled);
    }
}
