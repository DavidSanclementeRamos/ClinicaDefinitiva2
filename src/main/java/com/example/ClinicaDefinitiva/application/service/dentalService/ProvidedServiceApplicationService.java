package com.example.ClinicaDefinitiva.application.service.dentalService;


import com.example.ClinicaDefinitiva.application.dto.dentalService.*;

import com.example.ClinicaDefinitiva.application.exceptions.DentalServiceNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.dentalService.ProvidedServiceReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.dentalService.ProvidedServiceWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.dentalService.ProvidedServiceUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.AuthorizationError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.dental.care.service.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicación para ProvidedService (Servicios odontológicos).
 *
 * Implementa la política basada en especialidad (SpecialtyBasedPolicy):
 * - Los odontólogos solo pueden visualizar servicios que coincidan con sus especialidades.
 * - Los recepcionistas pueden visualizar todos los servicios (sujetos a su sector).
 *
 * Reglas de negocio aplicadas:
 * - RN-SERVICE-003: El servicio debe estar activo para ser editado.
 * - RN-SERVICE-004: La categoría debe coincidir con el tipo de detalles del servicio.
 * - RN-SERVICE-005: No debe haber citas en las próximas 48 horas antes de la desactivación.
 * - RN-SERVICE-006: El tipo de servicio no puede cambiar.
 * - RN-SERVICE-008: La justificación es obligatoria para cambios de tarifa.
 * - RN-SERVICE-009: Validaciones de nombre y descripción.
 * - RN-SERVICE-011: El cambio de tarifa debe estar dentro de un rango razonable.
 * - RN-SERVICE-012: No debe haber facturas pendientes antes de la desactivación.
 * - RN-SERVICE-013: El código del servicio debe ser único.
 * - RN-SERVICE-015: La desactivación requiere un motivo detallado (mínimo 10 caracteres).
 */
@Service
@Transactional
public class ProvidedServiceApplicationService implements ProvidedServiceUseCase {

    private final ProvidedServiceRepository serviceRepository;
    private final DentistRepository dentistRepository;
    private final ReceptionRepository receptionRepository;
    private final ProvidedServiceReadMapper readMapper;
    private final ProvidedServiceWriteMapper writeMapper;
    private final AuthorizationService authorizationService;

    public ProvidedServiceApplicationService(
            ProvidedServiceRepository serviceRepository,
            DentistRepository dentistRepository,
            ReceptionRepository receptionRepository,
            ProvidedServiceReadMapper readMapper,
            ProvidedServiceWriteMapper writeMapper,
            AuthorizationService authorizationService) {
        this.serviceRepository = serviceRepository;
        this.dentistRepository = dentistRepository;
        this.receptionRepository = receptionRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationService = authorizationService;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.READ)
    public ReadServiceDto findById(ServiceId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new DentalServiceNotFoundException("No found"));


        return readMapper.toReadDto(service);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageServiceDto> findAll(Pageable pageable,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        return serviceRepository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageServiceDto> findByStatus(String status,
                                             Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {



        return serviceRepository.findByStatus(status, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageServiceDto> findByCategory(String category,
                                               Pageable pageable,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {



        return serviceRepository.findByCategory(category, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageServiceDto> findByType(String serviceType,
                                           Pageable pageable,
                                           UserIdentityId requesterId,
                                           RolId requesterRolId) {



        return serviceRepository.findByType(serviceType, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadServiceDto create(CreateServiceDto dto,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)), requesterId)
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        // RN-SERVICE-013: Service code must be unique
       /** if (serviceRepository.existsByCode(dto.code())) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_CODE_ALREADY_EXISTS,
                    EntityContext.PROVIDED_SERVICE
            );
        }*/

        ProvidedService service = writeMapper.fromCreateDto(dto);

        ProvidedService saved = serviceRepository.save(service);
        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadServiceDto updateInformation(UpdateServiceInfoDto dto,
                                            ServiceId id,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new DentalServiceNotFoundException("No fount"));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)), requesterId)
                .withResourceId(id.getId())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        writeMapper.updateInformationFromDto(dto, service);
        ProvidedService updated = serviceRepository.save(service);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadServiceDto updateRate(UpdateServiceRateDto dto,
                                     ServiceId id,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new DentalServiceNotFoundException("No fount"));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)), requesterId)
                .withResourceId(id.getId())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        writeMapper.mapRateFromDto(dto);
        ProvidedService updated = serviceRepository.save(service);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadServiceDto updateDetails(UpdateServiceDetailsDto dto,
                                        ServiceId id,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new DentalServiceNotFoundException("No found"));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)), requesterId)
                .withResourceId(id.getId())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }



        writeMapper.updateDetailsFromDto(dto);

        ProvidedService updated = serviceRepository.save(service);
        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.DEACTIVATE)
    public void deactivate(ServiceId id,
                           String reason,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new DentalServiceNotFoundException("No found "));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE),
                        ActionCatalog.of(ActionCatalog.BasicAction.DEACTIVATE)
                ), requesterId)
                .withResourceId(id.getId())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }


        service.deactivate(reason);
        serviceRepository.save(service);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.REACTIVATE)
    public ReadServiceDto reactivate(ServiceId id,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new DentalServiceNotFoundException("No found"));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.of(
                        ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE),
                        ActionCatalog.of(ActionCatalog.BasicAction.REACTIVATE)
                ), requesterId)
                .withResourceId(id.getId())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }


        service.reactivate();
        ProvidedService reactivated = serviceRepository.save(service);

        return readMapper.toReadDto(reactivated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.DELETE)
    public void deleteById(ServiceId id,
                           UserIdentityId requesterId,
                           RolId requesterRolId) {

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new DentalServiceNotFoundException("Not found"));

        Receptionist receptionist = receptionRepository.findByUserId(requesterId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
                        VOContext.AUTHORIZATION
                ));

        SecurityContext context = SecurityContext
                .builder(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)), requesterId)
                .withResourceId(id.getId())
                .withSector(receptionist.getSector().Value())
                .build();

        if (!authorizationService.isAuthorized(requesterRolId, context)) {
            throw new BusinessRuleViolationException(
                    AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
                    VOContext.AUTHORIZATION
            );
        }

        serviceRepository.deleteById(service.getId());
    }
}
