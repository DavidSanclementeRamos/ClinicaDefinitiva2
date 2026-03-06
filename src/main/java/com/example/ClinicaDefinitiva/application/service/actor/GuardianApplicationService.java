package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.GuardianNoFoundException;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper.GuardianReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.guardianMapper.GuardianWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.GuardianUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.output.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * GuardianApplicationService refactorizado.
 * 
 * POLÍTICAS:
 * - OwnershipPolicy: Tutor solo ve sus propios datos
 * - SectorBasedPolicy: Receptionist por sector
 */
@Service
@Transactional
public class GuardianApplicationService implements GuardianUseCase {

    private final GuardianRepository guardianRepository;
    private final ReceptionRepository receptionRepository;
    private final GuardianReadMapper readMapper;
    private final GuardianWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public GuardianApplicationService(GuardianRepository guardianRepository,
                                      ReceptionRepository receptionRepository,
                                      GuardianReadMapper readMapper,
                                      GuardianWriteMapper writeMapper,
                                      AuthorizationHelper authorizationHelper) {
        this.guardianRepository = guardianRepository;
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.READ)
    public ReadGuardianDto findById(GuardianId id,
                                    UserIdentityId requesterId,
                                    RolId requesterRolId) {

       
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.GUARDIAN,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .build()
        );
         Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNoFoundException("Not fount"));


        return readMapper.toReadDto(guardian);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageGuardianDto> findAll(Pageable pageable,
                                         UserIdentityId requesterId,
                                         RolId requesterRolId) {

       // SectorBasedPolicy: Receptionist ve todos
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.GUARDIAN,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder().build()
        );

        return guardianRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageGuardianDto> findByPatientId(PatientId patientId,
                                                 Pageable pageable,
                                                 UserIdentityId requesterId,
                                                 RolId requesterRolId) {

       
        

         authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.GUARDIAN,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder()
                .withResourceId(patientId.value())
                .build()
        );

        return guardianRepository.findByPatientId(patientId, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadGuardianDto save(CreateGuardianDto dto,
                                UserIdentityId requesterId,
                                RolId requesterRolId) {

         authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.GUARDIAN,
            ActionCatalog.BasicAction.CREATE,
            AuthorizationContext.builder().build()
        );

          Guardian guardian = Guardian.registerGuardian(
            writeMapper.toPerson(dto),
            writeMapper.toUserIdentityId(dto),
            writeMapper.toTypeGuardian(dto)
        );

        Guardian saved = guardianRepository.save(guardian);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadGuardianDto updateContactData(UpdateGuardianContactDto dto,
                                             GuardianId id,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNoFoundException("Not found"));

        // OwnershipPolicy
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.GUARDIAN,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .withOwnership(guardian.getUserId())
                .build()
        );

         guardian.updateContactData(
            writeMapper.toAddress(dto),
            writeMapper.toPhoneNumber(dto)
        );

        Guardian updated = guardianRepository.save(guardian);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadGuardianDto updateSensitiveData(UpdateGuardianSensitiveDto dto,
                                               GuardianId id,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {

        
          authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.GUARDIAN,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .build()
        );
          
          Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNoFoundException("Not found"));


          guardian.updateSensitiveData(
            writeMapper.toAge(dto),
            writeMapper.toBloodType(dto),
            writeMapper.toDateOfBirth(dto),
            writeMapper.toDocument(dto),
            writeMapper.toDocumentEPS(dto),
            writeMapper.toFullName(dto),
            writeMapper.toTypeGuardian(dto)
        );


          Guardian updated = guardianRepository.save(guardian);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.GUARDIAN,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteById(GuardianId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

       

          authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.GUARDIAN,
            ActionCatalog.BasicAction.DELETE,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .build()
        );
          
           Guardian guardian = guardianRepository.findById(id)
                .orElseThrow(() -> new GuardianNoFoundException("Not found"));

        guardianRepository.deleteById(guardian.getGuardianId());
    }
}
