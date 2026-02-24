package com.example.ClinicaDefinitiva.application.service.actor;


import com.example.ClinicaDefinitiva.application.dto.actor.Patient.*;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.PatientNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper.PatientReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.patientMapper.PatientWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.PatientUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;

import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientApplicationService implements PatientUseCase {

    private final PatientRepository patientRepository;
    private final ReceptionRepository receptionRepository;
    private final PatientReadMapper readMapper;
    private final PatientWriteMapper writeMapper;
    private final AuthorizationService authorizationService;

    public PatientApplicationService(PatientRepository patientRepository,
                                     ReceptionRepository receptionRepository,
                                     PatientReadMapper readMapper,
                                     PatientWriteMapper writeMapper,
                                     AuthorizationService authorizationService) {
        this.patientRepository = patientRepository;
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.READ)
    public ReadPatientDto findById(PatientId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Not fount"));

        // Construir contexto con ownership y guardianship
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), requesterId)
                .withResourceId(id.value())
                .withResourceOwnerId(patient.getUser());

        // Si el paciente tiene guardian, agregar al contexto para GuardianshipPolicy
        if (patient.getGuardianId() != null) {
            contextBuilder.withPatientGuardianId(patient.getGuardianId().value());
        }

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

        return readMapper.toReadDto(patient);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PagePatientDto> findAll(Pageable pageable,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), requesterId);

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

        return patientRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PagePatientDto> findByContractId(ContractId contractId,
                                                 Pageable pageable,
                                                 UserIdentityId requesterId,
                                                 RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), requesterId)
                .withResourceId(contractId.getValue());

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

        return patientRepository.findByContractId(contractId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PagePatientDto> findByGuardianId(GuardianId guardianId,
                                                 Pageable pageable,
                                                 UserIdentityId requesterId,
                                                 RolId requesterRolId) {

        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), requesterId)
                .withPatientGuardianId(guardianId.value()); // Para GuardianshipPolicy

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

        return patientRepository.findByGuardianId(guardianId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadPatientDto save(CreatePatientDto createPatientDto,
                               UserIdentityId requesterId,
                               RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        Patient patient = writeMapper.fromCreateDto(createPatientDto);
        Patient saved = patientRepository.save(patient);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadPatientDto updateContactData(UpdatePatientContactDto updatePatientDto,
                                            PatientId id,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(" Not found"));

        // Construir contexto con ownership y guardianship
        SecurityContext.Builder contextBuilder = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), requesterId)
                .withResourceId(id.value())
                .withResourceOwnerId(patient.getUser());

        // Si tiene guardian, agregarlo para GuardianshipPolicy
        if (patient.getGuardianId() != null) {
            contextBuilder.withPatientGuardianId(patient.getGuardianId().value());
        }

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

        writeMapper.updateContactFromDto(updatePatientDto, patient);
        Patient updated = patientRepository.save(patient);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadPatientDto updateSensitiveData(UpdatePatientSensitiveDto updatePatientDto,
                                              PatientId id,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Not found"));

        // Para datos sensibles, requiere sector (receptionist)
        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), requesterId)
                .withResourceId(id.value())
                .withSector(receptionist.getSector().Value())
                .withResourceOwnerId(patient.getUser())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        writeMapper.updateSensitiveFromDto(updatePatientDto, patient);
        Patient updated = patientRepository.save(patient);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteById(PatientId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PathElementException("No found"));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), requesterId)
                .withResourceId(id.value())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        patientRepository.deleteById(patient.getPatientId());
    }
}