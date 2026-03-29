package com.example.ClinicaDefinitiva.application.dentalService.input;

import com.example.ClinicaDefinitiva.application.dentalService.dto.CreateServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.PageServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.ReadServiceDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceDetailsDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceInfoDto;
import com.example.ClinicaDefinitiva.application.dentalService.dto.UpdateServiceRateDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Puerto de entrada para casos de uso de servicios odontológicos.
 *
 * Define todas las operaciones disponibles para gestionar servicios.
 * Siguiendo la arquitectura hexagonal, esta interfaz pertenece a la capa de aplicación
 * y será implementada por servicios de aplicación.
 *
 * Seguridad: Todos los métodos requieren requesterId y requesterRolId
 * para autorización explícita, según ADR-48.
 */
public interface ProvidedServiceUseCase {


    ReadServiceDto findById(ServiceId id, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageServiceDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageServiceDto> findByStatus(String status, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageServiceDto> findByCategory(String category, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);
    Page<PageServiceDto> findByType(String serviceType, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);



    ReadServiceDto create(CreateServiceDto dto,  UserIdentityId requesterId, RolId requesterRolId);
    ReadServiceDto updateInformation(UpdateServiceInfoDto dto, ServiceId id, UserIdentityId requesterId, RolId requesterRolId);
    ReadServiceDto updateRate(UpdateServiceRateDto dto, ServiceId id, UserIdentityId requesterId, RolId requesterRolId);
    ReadServiceDto updateDetails(UpdateServiceDetailsDto dto, ServiceId id, UserIdentityId requesterId, RolId requesterRolId);

    void deactivate(ServiceId id, String reason, UserIdentityId requesterId, RolId requesterRolId);
    ReadServiceDto reactivate(ServiceId id, UserIdentityId requesterId, RolId requesterRolId);
    void deleteById(ServiceId id, UserIdentityId requesterId, RolId requesterRolId);
}
