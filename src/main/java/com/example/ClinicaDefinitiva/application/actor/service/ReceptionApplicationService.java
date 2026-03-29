package com.example.ClinicaDefinitiva.application.actor.service;

import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.PageReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.ReadReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.mapper.reception.ReceptionistReadMapper;
import com.example.ClinicaDefinitiva.application.actor.mapper.reception.ReceptionistWriteMapper;
import com.example.ClinicaDefinitiva.application.actor.portsInput.ReceptionUseCase;
import com.example.ClinicaDefinitiva.application.exceptions.actor.ReceptionistNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReceptionApplicationService implements ReceptionUseCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionistReadMapper readMapper;
    private final ReceptionistWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public ReceptionApplicationService(ReceptionRepository receptionRepository,
                                       ReceptionistReadMapper readMapper,
                                       ReceptionistWriteMapper writeMapper,
                                       AuthorizationHelper authorizationHelper) {
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.READ)
    public ReadReceptionistDto findById(ReceptionId id,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

       
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.RECEPTIONIST,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder()
                .withResourceId(id.getValue())
                .build()
        );
        
         Receptionist receptionist = receptionRepository.findById(id)
                .orElseThrow(() -> new ReceptionistNotFoundException("Not found"));


        return readMapper.toReadDto(receptionist);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageReceptionistDto> findAll(Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        

         // Autorización simple (solo sector, sin ownership)
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.RECEPTIONIST,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder().build() // Sin atributos adicionales
        );

        return receptionRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageReceptionistDto> findBySector(String sector,
                                                  Pageable pageable,
                                                  UserIdentityId requesterId,
                                                  RolId requesterRolId) {

         // SectorBasedPolicy: Requiere sector válido
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.RECEPTIONIST,
            ActionCatalog.BasicAction.READ,
            AuthorizationContext.builder().build()
        );

        return receptionRepository.findBySector(sector, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadReceptionistDto save(CreateReceptionistDto dto,
                                    UserIdentityId requesterId,
                                    RolId requesterRolId) {

         // SectorBasedPolicy: Requiere sector válido
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.RECEPTIONIST,
            ActionCatalog.BasicAction.CREATE,
            AuthorizationContext.builder().build()
        );

        Receptionist receptionist = Receptionist.registerReceptionist(
            writeMapper.toPerson(dto),
            writeMapper.toUserIdentityId(dto),
            writeMapper.toSector(dto)
        );
        Receptionist saved = receptionRepository.save(receptionist);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadReceptionistDto updateContact(UpdateReceptionistContactDto dto,
                                             ReceptionId id,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findById(id)
                .orElseThrow(() -> new  ReceptionistNotFoundException("Not found"));

         // Sector + Ownership
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.RECEPTIONIST,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withResourceId(id.getValue())
                .withOwnership(receptionist.getUserIdentityId()) // ← OwnershipPolicy
                .build()
        );

        receptionist.updateContactData(
            writeMapper.toAddress(dto),
            writeMapper.toPhoneNumber(dto)
        );

        Receptionist updated = receptionRepository.save(receptionist);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadReceptionistDto updateSensitive(UpdateReceptionistSensitiveDto dto,
                                               ReceptionId id,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {

        
        // Datos sensibles: Solo RECEPTIONIST (validado por SectorBasedPolicy)
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.RECEPTIONIST,
            ActionCatalog.BasicAction.UPDATE,
            AuthorizationContext.builder()
                .withResourceId(id.getValue())
                .build()
        );
        
        Receptionist receptionist = receptionRepository.findById(id)
                .orElseThrow(() -> new  ReceptionistNotFoundException("Not found"));


        receptionist.updateSensitiveData(
            writeMapper.toBloodType(dto),
            writeMapper.toDateOfBirth(dto),
            writeMapper.toDocument(dto),
            writeMapper.toDocumentEPS(dto),
            writeMapper.toFullName(dto),
            writeMapper.toSector(dto)
        );
        Receptionist updated = receptionRepository.save(receptionist);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.RECEPTIONIST,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteById(ReceptionId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

       
        // SectorBasedPolicy: Solo RECEPTIONIST de RECURSOS_HUMANOS puede eliminar
        // (Validado automáticamente por SectorBasedPolicy en el PolicyEngine)
        authorizationHelper.authorize(
            requesterId,
            requesterRolId,
            ResourceCatalog.BasicResource.RECEPTIONIST,
            ActionCatalog.BasicAction.DELETE,
            AuthorizationContext.builder()
                .withResourceId(id.getValue())
                .build()
        );
        
         Receptionist receptionist = receptionRepository.findById(id)
                .orElseThrow(() -> new ReceptionistNotFoundException("Not found"));


        receptionRepository.deleteById(receptionist.getId());
    }
}
