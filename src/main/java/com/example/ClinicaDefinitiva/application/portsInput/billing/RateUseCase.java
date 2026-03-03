package com.example.ClinicaDefinitiva.application.portsInput.billing;


import com.example.ClinicaDefinitiva.application.dto.billing.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.PageRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.ReadRateDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * Puerto de entrada para casos de uso de tarifas (Rates).
 *
 * Define todas las operaciones disponibles para gestionar tarifas de servicios odontológicos.
 *
 * Las tarifas varían según:
 * - Tipo de pagador (EPS, PARTICULAR, INSURANCE, ARL, SOAT, PREPAID)
 * - Vigencia temporal (validFrom - validTo)
 * - Contrato asociado (obligatorio para EPS)
 *
 * Seguridad: Todos los métodos requieren requesterId y requesterRolId
 * para autorización explícita, según ADR-48.
 */
public interface RateUseCase {


    ReadRateDto findById(
            RateId id,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageRateDto> findAll(
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageRateDto> findByService(
            Long serviceId,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    Page<PageRateDto> findByPayerType(
            String payerType,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageRateDto> findByContract(
            Long contractId,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadRateDto findActiveRateForService(
            Long serviceId,
            String payerType,
            Long contractId,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    Page<PageRateDto> findCurrentlyValid(
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    ReadRateDto create(
            CreateRateDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

   
    ReadRateDto endValidityAt(
            RateId id,
            LocalDateTime endDate,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    void deactivate(
            RateId id,
            UserIdentityId requesterId,
            RolId requesterRolId
    );
    void markAsReplaced(
    RateId id,
            UserIdentityId requesterId,
            RolId requesterRolId);
}
