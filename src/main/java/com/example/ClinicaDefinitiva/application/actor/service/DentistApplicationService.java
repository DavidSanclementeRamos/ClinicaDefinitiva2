package com.example.ClinicaDefinitiva.application.actor.service;


import com.example.ClinicaDefinitiva.application.actor.dto.dentist.CreateDentistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.PageDentistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.ReadDentistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.UpdateDentistContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.dentist.UpdateDentistSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.mapper.dentist.DentistReadMapper;
import com.example.ClinicaDefinitiva.application.actor.mapper.dentist.DentistWriteMapper;
import com.example.ClinicaDefinitiva.application.actor.portsInput.DentistUseCase;
import com.example.ClinicaDefinitiva.application.exceptions.actor.DentistNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.actor.vo.Specialty;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * DentistApplicationService refactorizado con AuthorizationHelper.
 * 
 * POLÍTICAS APLICADAS:
 * - SectorBasedPolicy: Solo RECEPTIONIST de RRHH puede eliminar dentistas
 * - OwnershipPolicy: Dentista solo puede modificar sus propios datos (vacaciones, incapacidad)
 * 
 * ANTES: 10-15 líneas de código de autorización por método
 * DESPUÉS: 1 línea con AuthorizationHelper
 */
@Service
@Transactional
public class DentistApplicationService implements DentistUseCase {
    
      private final DentistRepository dentistRepository;
    private final DentistReadMapper dentistReadMapper;
    private final DentistWriteMapper dentistWriteMapper;
    private final AuthorizationHelper authorizationHelper; 

    public DentistApplicationService(
            DentistRepository dentistRepository,
            DentistReadMapper dentistReadMapper,
            DentistWriteMapper dentistWriteMapper,
            AuthorizationHelper authorizationHelper) {
        this.dentistRepository = dentistRepository;
        this.dentistReadMapper = dentistReadMapper;
        this.dentistWriteMapper = dentistWriteMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.READ)
    public ReadDentistDto findById(DentistId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        
         authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .build()
        );
         
          Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        
        return dentistReadMapper.toReadDto(dentist);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageDentistDto> findAll(Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId) {

        // Autorización simple (solo sector, sin ownership)
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.DENTIST,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return dentistRepository.findAll(pageable)
                .map(dentistReadMapper::toPageDto);

    }

   

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageDentistDto> findBySpecialty(Specialty specialty,
                                                Pageable pageable,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

         authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder().build()
        ); 

        return dentistRepository.findBySpecialty(specialty, pageable)
                .map(dentistReadMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadDentistDto save(CreateDentistDto dto,
                               UserIdentityId requesterId,
                               RolId requesterRolId) {

        // SectorBasedPolicy: Requiere sector válido
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.CREATE,
            AuthorizationContext.builder().build()
        );

        Dentist dentist = Dentist.registerDentist(
    dentistWriteMapper.toPerson(dto),
    dentistWriteMapper.toSpecialties(dto.specialties()),
    dentistWriteMapper.toUserIdentityId(dto),
    dentistWriteMapper.toWorkingHours(dto.workingHoursDto())
);




        Dentist saved = dentistRepository.save(dentist);

        return dentistReadMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadDentistDto updateContactData(UpdateDentistContactDto dto,
                                            Long id,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        Dentist dentist = dentistRepository.findById(DentistId.of(id))
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

         // Sector + Ownership: Dentista puede editar sus propios datos
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withResourceId(id)
                .withOwnership(dentist.getUserId()) // ← OwnershipPolicy
                .build()
        );
        

        dentist.updateContactData(
        dentistWriteMapper.toAddress(dto),
        dentistWriteMapper.toPhoneNumber(dto)
    );
                Dentist updated = dentistRepository.save(dentist);

        return dentistReadMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadDentistDto updateSensitiveData(UpdateDentistSensitiveDto dto,
                                              Long id,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        
       // Datos sensibles: Solo RECEPTIONIST (validado por SectorBasedPolicy)
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withResourceId(id)
                .build()
        );
        
        Dentist dentist = dentistRepository.findById(DentistId.of(id))
                .orElseThrow(() -> new DentistNotFoundException("Not found"));


           // Construir FullName combinando valores parciales
    Optional<FullName> finalFullName;
    if (dto.first().isPresent() || dto.lastName().isPresent()) {
        String currentFirst = dentist.getPersonData().getFullname().getFirstName();
        String currentLast = dentist.getPersonData().getFullname().getLastName();
        String newFirst = dto.first().orElse(currentFirst);
        String newLast = dto.lastName().orElse(currentLast);
        finalFullName = Optional.of(FullName.of(newFirst, newLast));
    } else {
        finalFullName = Optional.empty();
    }
      
      dentist.updateSensitiveData(
        dentistWriteMapper.toBloodType(dto),
        dentistWriteMapper.toDateOfBirth(dto),
        dentistWriteMapper.toDocument(dto),
        dentistWriteMapper.toDocumentEPS(dto),
        finalFullName,  // ← combinado
        dentistWriteMapper.toSpecialties(dto),
        dentistWriteMapper.toWorkingHours(dto)
    );
    Dentist updated = dentistRepository.save(dentist);

        return dentistReadMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.APPLY_VACATION)
    public void applyVacation(LocalDateTime start,
                              LocalDateTime end,
                              UserIdentityId requesterId,
                              RolId requesterRolId) {

        Dentist dentist = dentistRepository.findByUserId(requesterId)
                .orElseThrow(() -> new DentistNotFoundException("No dentist profile found for the authenticated user"));
          // OwnershipPolicy: Solo el dentista puede aplicar vacaciones a sí mismo
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.APPLY_VACATION,
            AuthorizationContext.builder()
                .withOwnership(dentist.getUserId()) // ← CRÍTICO: Solo su propio recurso
                .build()
        );

        dentist.applyVacation(start, end);
        dentistRepository.save(dentist);

    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.APPLY_INCAPACITY)
    public void applyIncapacity(LocalDateTime start,
                                LocalDateTime end,
                                String note,
                                UserIdentityId requesterId,
                                RolId requesterRolId) {

        Dentist dentist = dentistRepository.findByUserId(requesterId)
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        // OwnershipPolicy: Solo el dentista puede aplicar incapacidad a sí mismo
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.APPLY_INCAPACITY,
            AuthorizationContext.builder()
                .withOwnership(dentist.getUserId())
                .build()
        );

        dentist.applyIncapacity(start,end,note);
        dentistRepository.save(dentist);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public void returnToAvailable(UserIdentityId requesterId,
                                  RolId requesterRolId) {

        Dentist dentist = dentistRepository.findByUserId(requesterId)
                .orElseThrow(() ->  new DentistNotFoundException("Not found"));

        // OwnershipPolicy
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withOwnership(dentist.getUserId())
                .build()
        );

        dentist.returnToAvailable();
        dentistRepository.save(dentist);
    }

     

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.DENTIST,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteById(DentistId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        

        // SectorBasedPolicy: Solo RECEPTIONIST de RECURSOS_HUMANOS puede eliminar
        // (Validado automáticamente por SectorBasedPolicy en el PolicyEngine)
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.DENTIST,
            ActionCatalog.BasicAction.DELETE,
            AuthorizationContext.builder()
                .withResourceId(id.value())
                .build()
        );
        
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new DentistNotFoundException("Not found"));

        dentistRepository.deleteById(dentist.getDentistId());
    }
}
