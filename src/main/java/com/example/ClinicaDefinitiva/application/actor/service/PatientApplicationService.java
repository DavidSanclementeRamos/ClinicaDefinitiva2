package com.example.ClinicaDefinitiva.application.actor.service;


import com.example.ClinicaDefinitiva.application.actor.dto.patient.CreatePatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.PagePatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.patient.UpdatePatientSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.mapper.patient.PatientReadMapper;
import com.example.ClinicaDefinitiva.application.actor.mapper.patient.PatientWriteMapper;
import com.example.ClinicaDefinitiva.application.actor.portsInput.PatientUseCase;
import com.example.ClinicaDefinitiva.application.exceptions.actor.PatientNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * PatientApplicationService refactorizado.
 * 
 * POLÍTICAS:
 * - OwnershipPolicy: Paciente solo ve sus propios datos
 * - GuardianshipPolicy: Tutor accede a datos de pacientes bajo su tutela
 * - SectorBasedPolicy: Receptionist por sector
 */
@Service
@Transactional
public class PatientApplicationService implements PatientUseCase {

    private final PatientRepository patientRepository;
    private final ReceptionRepository receptionRepository;
    private final PatientReadMapper readMapper;
    private final PatientWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public PatientApplicationService(PatientRepository patientRepository,
                                     ReceptionRepository receptionRepository,
                                     PatientReadMapper readMapper,
                                     PatientWriteMapper writeMapper,
                                     AuthorizationHelper authorizationHelper) {
        this.patientRepository = patientRepository;
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.READ)
    public ReadPatientDto findById(PatientId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Not fount"));

         // Ownership + Guardianship: Paciente ve sus datos, tutor ve datos de tutelados
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.PATIENT,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .withOwnership(patient.getUser()) // ← OwnershipPolicy
                .withPatientGuardianId(patient.getGuardianId() != null ? 
                    patient.getGuardianId().value() : null) // ← GuardianshipPolicy
                .build()
        );

        return readMapper.toReadDto(patient);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PagePatientDto> findAll(Pageable pageable,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

       
        // Solo sector (receptionist ve todos)
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.PATIENT,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder().build()
        );

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

        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.PATIENT,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder()
                .withResourceId(contractId.getValue())
                .build()
        );

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

         // GuardianshipPolicy: Tutor solo ve pacientes bajo su tutela
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.PATIENT,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder()
                .withPatientGuardianId(guardianId.value()) // ← GuardianshipPolicy
                .build()
        );

        return patientRepository.findByGuardianId(guardianId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadPatientDto save(CreatePatientDto dto,
                               UserIdentityId requesterId,
                               RolId requesterRolId) {

         // SectorBasedPolicy: Solo receptionist puede crear
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.PATIENT,
            ActionCatalog.BasicAction.CREATE,
            AuthorizationContext.builder().build()
        );

        Patient patient = Patient.registerPatient(
            writeMapper.toPerson(dto),
            writeMapper.toUserIdentityId(dto),
            writeMapper.toGuardianId(dto)
        );

        Patient saved = patientRepository.save(patient);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadPatientDto updateContactData(UpdatePatientContactDto dto,
                                            PatientId id,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(" Not found"));

         // Ownership + Guardianship: Paciente edita sus datos, tutor edita datos de tutelados
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.PATIENT,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .withOwnership(patient.getUser()) // ← Paciente puede editar
                .withPatientGuardianId(patient.getGuardianId() != null ? 
                    patient.getGuardianId().value() : null) // ← Tutor también puede editar
                .build()
        );

         patient.updatePatientContact(
            writeMapper.toAddress(dto),
            writeMapper.toPhoneNumber(dto)
        );
        Patient updated = patientRepository.save(patient);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PATIENT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadPatientDto updateSensitiveData(UpdatePatientSensitiveDto dto,
                                              PatientId id,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        

        // Datos sensibles: Solo receptionist (SectorBasedPolicy)
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.PATIENT,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .build()
        );
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Not found"));

        patient.updateSensitiveData(
            writeMapper.toBloodType(dto),
            writeMapper.toDateOfBirth(dto),
            writeMapper.toDocument(dto),
            writeMapper.toDocumentEPS(dto),
            writeMapper.toFullName(dto)
        );

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
                .orElseThrow(() -> new PatientNotFoundException("No found"));

        // Solo receptionist puede eliminar
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.PATIENT,
            ActionCatalog.BasicAction.DELETE,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .build()
        );

        patientRepository.deleteById(patient.getPatientId());
    }
}