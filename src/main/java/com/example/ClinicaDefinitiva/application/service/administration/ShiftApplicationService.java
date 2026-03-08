
package com.example.ClinicaDefinitiva.application.service.administration;

import com.example.ClinicaDefinitiva.application.dto.administration.operations.AssignShiftDto;
import com.example.ClinicaDefinitiva.application.dto.administration.operations.CanAccommodateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.operations.ExcludedBlockDto;
import com.example.ClinicaDefinitiva.application.dto.administration.operations.PageShiftDto;
import com.example.ClinicaDefinitiva.application.dto.administration.operations.ReadShiftDto;
import com.example.ClinicaDefinitiva.application.dto.administration.operations.RescheduleShiftDto;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.administration.operations.ShiftNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.operations.ShiftReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.operations.ShiftWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.ShiftUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.service.ShiftAssignmentService;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ShiftApplicationService implements ShiftUseCase {

    private final ShiftReadMapper readMapper;
    private final ShiftWriteMapper writeMapper;
    private final ShiftRepository repository;
    private final AuthorizationHelper authorization;
    private final ShiftAssignmentService shiftAssignmentService;

    public ShiftApplicationService(ShiftReadMapper readMapper, ShiftWriteMapper writeMapper, ShiftRepository repository, AuthorizationHelper authorization, ShiftAssignmentService shiftAssignmentService) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.repository = repository;
        this.authorization = authorization;
        this.shiftAssignmentService = shiftAssignmentService;
    }

   



    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.READ)
    public ReadShiftDto findById(ShiftId shiftId,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {

                authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(shiftId.value())
                        .build()
        );
                
                Shift shift = repository.findById(ShiftId.from(shiftId.value()))
                .orElseThrow(() -> new ShiftNotFoundException("Not found"));



        return readMapper.toReadDto(shift);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageShiftDto> findAll(UserIdentityId requesterId,
                                      RolId requesterRolId) {

        authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findAll(Pageable.unpaged())
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageShiftDto> findByDentist(DentistId dentistId,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(dentistId.value())
                        .build()
        );

        return repository.findActiveByDentist(dentistId, Pageable.unpaged())
                .map(readMapper::toPageDto);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadShiftDto assignShift(AssignShiftDto dto,
                                    UserIdentityId requesterId,
                                    RolId requesterRolId) {

        authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        // ShiftAssignmentService valida horario del dentista y persiste el turno
        Shift shift =  shiftAssignmentService.assignShift(
               DentistId.of( dto.dentistId()),
                dto.date(),
                dto.startTime(),
                dto.endTime(),
               ShiftType.valueOf( dto.type())
        );

        return readMapper.toReadDto(shift);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadShiftDto excludeBlock(ShiftId id,ExcludedBlockDto dto,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

       
        authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .build()
        );

         Shift shift = repository.findById(id)
                .orElseThrow(() -> new ShiftNotFoundException("Not found"));

        shift.excludeBlock(
    writeMapper.toStart(dto),
    writeMapper.toEnd(dto),
    writeMapper.toReason(dto)
);

        Shift updated = repository.save(shift);

        return readMapper.toReadDto(updated);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.READ)
    public boolean canAccommodateAppointment(ShiftId shiftId,
                                             CanAccommodateAppointmentDto dto,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

       
        authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(shiftId.value())
                        .build()
        );
         Shift shift = repository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException("Not found"));

        boolean canAccommodate = shift.canAccommodateAppointment(
    writeMapper.toAppointmentStart(dto),
    writeMapper.toAppointmentEnd(dto)
);
       return  canAccommodate;

    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadShiftDto reschedule(ShiftId shiftId,
                                   RescheduleShiftDto dto,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

       
        authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(shiftId.value())
                       .build()
        );
         Shift shift = repository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException("Not found"));



shift.reschedule(
    writeMapper.toNewDate(dto),
    writeMapper.toNewStart(dto),
    writeMapper.toNewEnd(dto),
    writeMapper.toAuthorization(dto)
); 

Shift updated = repository.save(shift);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadShiftDto cancel(ShiftId shiftId,
                               String reason,
                               UserIdentityId requesterId,
                               RolId requesterRolId) {

               authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(shiftId.value())
                        .build()
        );
                Shift shift = repository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException("Not found"));



        shift.cancel(reason);
        Shift updated = repository.save(shift);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.SHIFT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadShiftDto complete(ShiftId shiftId,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {

       
        authorization.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.SHIFT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(shiftId.value())
                        .build()
        );
         Shift shift = repository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException("Not found"));



        shift.complete();
        Shift updated = repository.save(shift);

        return readMapper.toReadDto(updated);
    }
}