package com.example.ClinicaDefinitiva.application.dentalService.service;


import com.example.ClinicaDefinitiva.application.dentalService.dto.CreateServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.PageServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.ReadServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceDetailsDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceInfoDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceRateDto;
import com.example.ClinicaDefinitiva.application.dentalService.input.ProvidedServiceUseCase;
import com.example.ClinicaDefinitiva.application.dentalService.mapper.ProvidedServiceReadMapper;
import com.example.ClinicaDefinitiva.application.dentalService.mapper.ProvidedServiceWriteMapper;
import com.example.ClinicaDefinitiva.application.exceptions.dentalService.ProvidedServiceNotFoundException;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dentalService.enu.ServiceType;
import com.example.ClinicaDefinitiva.domain.dentalService.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dentalService.output.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetails;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceDetailsFactory;
import com.example.ClinicaDefinitiva.domain.dentalService.service.ServiceRatePolicy;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import java.util.Map;
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
 *
 */
@Service
@Transactional
public class ProvidedServiceApplicationService implements ProvidedServiceUseCase {

    private final ProvidedServiceRepository serviceRepository;
    private final ProvidedServiceReadMapper readMapper;
    private final ProvidedServiceWriteMapper writeMapper;
    private final ServiceRatePolicy serviceRatePolicy;
    private final AuthorizationHelper authorizationHelper;
    private final ServiceDeactivationValidator serviceDeactivationValidator;

    public ProvidedServiceApplicationService(
            ProvidedServiceRepository serviceRepository,
            ProvidedServiceReadMapper readMapper,
            ProvidedServiceWriteMapper writeMapper,
            ServiceRatePolicy serviceRatePolicy,
            AuthorizationHelper authorizationHelper,
            ServiceDeactivationValidator serviceDeactivationValidator) {
        this.serviceRepository = serviceRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.serviceRatePolicy = serviceRatePolicy;
        this.authorizationHelper = authorizationHelper;
        this.serviceDeactivationValidator = serviceDeactivationValidator;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.READ)
    public ReadServiceDto findById(ServiceId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE, 
                ActionCatalog.BasicAction.READ,                                 
                AuthorizationContext.builder()
                        .withResourceId(id.getId())
                        .build()
        );

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ProvidedServiceNotFoundException("No found"));

        return readMapper.toReadDto(service);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageServiceDto> findAll(Pageable pageable,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

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
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

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
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

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
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return serviceRepository.findByType(serviceType, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadServiceDto create(CreateServiceDto dto,
                                 UserIdentityId requesterId,
                                 RolId requesterRolId) {
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.CREATE,              
                AuthorizationContext.builder().build()
        );

        ServiceDetails details = null;
        if (dto.serviceType() != null && dto.details()!= null) {
            // Usar factory para crear el tipo correcto de ServiceDetails
            details = ServiceDetailsFactory.fromMap(ServiceType.valueOf(dto.serviceType() ), (Map<String, Object>) dto.details());
        }

        // Crear servicio con todos los campos
        ProvidedService service = ProvidedService.create(
                writeMapper.toServiceName(dto),
                writeMapper.toServiceCategory(dto),
                writeMapper.toServiceCode(dto),
                writeMapper.toBaseRate(dto),
                writeMapper.toDuration(dto),
                writeMapper.toDescription(dto),
                details,                                        
                writeMapper.toRequiresAuthorization(dto)
        );

        ProvidedService saved = serviceRepository.save(service);
        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.UPDATE_INFORMATION)
    public ReadServiceDto updateInformation(UpdateServiceInfoDto dto,
                                            ServiceId id,
                                            UserIdentityId requesterId,
                                            RolId requesterRolId) {
        // ⭐ CORREGIDO: Recurso correcto
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE, 
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getId())
                        .build()
        );

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ProvidedServiceNotFoundException("No found"));

        service.updateInformation(
            writeMapper.toServiceName(dto),
            writeMapper.toServiceCategory(dto),
            writeMapper.toDuration(dto),
            writeMapper.toRequiresAuthorization(dto),
            writeMapper.toDescription(dto)
        );

        ProvidedService updated = serviceRepository.save(service);
        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.UPDATE_PRICE)
    public ReadServiceDto updateRate(UpdateServiceRateDto dto,
                                     ServiceId id,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getId())
                        .build()
        );

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ProvidedServiceNotFoundException("No found"));

        Price newRate = writeMapper.toRate(dto);
        
        // Validar con la política antes de aplicar el cambio
        serviceRatePolicy.validateRateChange(service.getBaseRate(), newRate);

        service.updateRate(newRate, dto.justification());

        ProvidedService updated = serviceRepository.save(service);
        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.UPDATE_DETAILS)
    public ReadServiceDto updateDetails(UpdateServiceDetailsDto dto,
                                        ServiceId id,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {
        // ⭐ CORREGIDO
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getId())
                        .build()
        );

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ProvidedServiceNotFoundException("No found"));

        ServiceDetails newDetails = null;
        if (dto.serviceType() != null && dto.details()!= null) {
            newDetails = ServiceDetailsFactory.fromMap(ServiceType.valueOf(dto.serviceType())   , (Map<String, Object>) dto.details());
        } else if (dto.details()!= null && service.getDetails().isPresent()) {
            // Si no viene serviceType pero sí detailsMap, usar el tipo actual del servicio
            newDetails = ServiceDetailsFactory.fromMap(service.getDetails().get().serviceType(), (Map<String, Object>) dto.details());
        }

        if (newDetails != null) {
            service.updateDetails(newDetails);
        }

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

        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.DEACTIVATE,          
                AuthorizationContext.builder()
                        .withResourceId(id.getId())
                        .build()
        );

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ProvidedServiceNotFoundException("No found"));

        // Validar que no haya citas pendientes
        serviceDeactivationValidator.validateNoAppointments(id);

        service.deactivate(reason);
        serviceRepository.save(service);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.PROVIDED_SERVICE,
            action = ActionCatalog.BasicAction.ACTIVATE)
    public ReadServiceDto reactivate(ServiceId id,
                                     UserIdentityId requesterId,
                                     RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PROVIDED_SERVICE,
                ActionCatalog.BasicAction.ACTIVATE,                          AuthorizationContext.builder()
                        .withResourceId(id.getId())
                        .build()
        );

        ProvidedService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ProvidedServiceNotFoundException("No found"));

        service.reactivate();
        ProvidedService reactivated = serviceRepository.save(service);

        return readMapper.toReadDto(reactivated);
    }

   }
