package com.example.ClinicaDefinitiva.application.service.dentalService;


import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentDto;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.actor.DentistNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actor.PatientNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.clinicalTreatments.TreatmentNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.clinicalTreatments.TreatmentReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.clinicalTreatments.TreatmentWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.dentalService.TreatmentUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.model.Treatment;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.enu.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.output.TreatmentRepository;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
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
    private final TreatmentReadMapper readMapper;
    private final TreatmentWriteMapper writeMapper;
    private final PatientRepository patientRepository;
    private final AuthorizationHelper authorizationHelper; 

    public TreatmentApplicationService(TreatmentRepository treatmentRepository, DentistRepository dentistRepository, TreatmentReadMapper readMapper, TreatmentWriteMapper writeMapper, PatientRepository patientRepository, AuthorizationHelper authorizationHelper) {
        this.treatmentRepository = treatmentRepository;
        this.dentistRepository = dentistRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.patientRepository = patientRepository;
        this.authorizationHelper = authorizationHelper;
    }


    

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.TREATMENT,
            action = ActionCatalog.BasicAction.READ)
    public TreatmentDto findById(TreatmentId id,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {

        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new TreatmentNotFoundException(""));


        Dentist destist = dentistRepository.findById(treatment.getDentistId())
                .orElseThrow(() -> new DentistNotFoundException(""));

        // Obtener paciente para ownership/guardianship
        Patient patient = patientRepository.findById(treatment.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException(""));

        // POLÍTICAS COMBINADAS: Ownership + Guardianship + Assignment
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.TREATMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .withOwnership(patient.getUser()) // ← OwnershipPolicy: Paciente ve sus tratamientos
                        .withPatientGuardianId(patient.getGuardianId() != null ?
                                patient.getGuardianId().value() : null) // ← GuardianshipPolicy: Tutor ve tratamientos de tutelados
                        .withAssignedDentist(destist.getUserId()) // ← AssignmentPolicy: Dentista ve tratamientos asignados
                        .build()
        );
        
        return readMapper.toDto(treatment);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.TREATMENT,
            action = ActionCatalog.BasicAction.READ)
    public Page<TreatmentDto> findAll(Pageable pageable,
                                      UserIdentityId requesterId,
                                      RolId requesterRolId) {

        // Simple authorization (sector-based para receptionist)
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.TREATMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

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

        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.TREATMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

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

        // Solo receptionist puede crear (sector-based)
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.TREATMENT,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );


         Treatment treatment = Treatment.createNew(
            writeMapper.toPatientId(dto),
            writeMapper.toDentistId(dto),
            writeMapper.toServiceId(dto),
            writeMapper.toStartDate(dto),
            writeMapper.toExpectedEndDate(dto),
            writeMapper.toPhases(dto),
            writeMapper.toNotes(dto),
            writeMapper.toRateId(dto)
        );

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
        
          Dentist dentist = dentistRepository.findById(treatment.getDentistId())
                .orElseThrow(() -> new PatientNotFoundException(""));


        // AssignmentPolicy: Solo dentista asignado puede completar
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.TREATMENT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .withAssignedDentist(dentist.getUserId()) // ← CRÍTICO: Solo dentista asignado
                        .build()
        );

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
        
          Dentist dentist = dentistRepository.findById(treatment.getDentistId())
                .orElseThrow(() -> new PatientNotFoundException(""));


        // AssignmentPolicy: Solo dentista asignado o receptionist puede cancelar
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.TREATMENT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                       
                        .withAssignedDentist(dentist.getUserId())
                        .build()
        );



        treatment.cancel(reason);
        Treatment cancelled = treatmentRepository.save(treatment);

        return readMapper.toDto(cancelled);
    }
}
