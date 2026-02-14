package com.example.ClinicaDefinitiva.application.portsInput.dentalService;

import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.num.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.TreatmentId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * Puerto de entrada para casos de uso de tratamientos clínicos.
 *
 * Define todas las operaciones disponibles para gestionar tratamientos odontológicos.
 * Siguiendo la arquitectura hexagonal, esta interfaz pertenece a la capa de aplicación
 * y será implementada por servicios de aplicación.
 *
 * Seguridad: Todos los métodos requieren requesterId y requesterRolId
 * para autorización explícita, según ADR-48.
 */
public interface TreatmentUseCase {


    TreatmentDto findById(
            TreatmentId id,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<TreatmentDto> findAll(
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    Page<TreatmentDto> findByStatus(
            TreatmentStatus status,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    TreatmentDto create(
            CreateTreatmentDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    TreatmentDto complete(
            TreatmentId id,
            LocalDate actualEndDate,
            UserIdentityId requesterId,
            RolId requesterRolId
    );
    
    TreatmentDto cancel(
            TreatmentId id,
            String reason,
            UserIdentityId requesterId,
            RolId requesterRolId
    );
}

