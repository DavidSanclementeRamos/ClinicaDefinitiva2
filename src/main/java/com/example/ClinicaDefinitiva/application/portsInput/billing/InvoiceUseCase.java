package com.example.ClinicaDefinitiva.application.portsInput.billing;

import com.example.ClinicaDefinitiva.application.dto.billing.invoice.*;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * Puerto de entrada para casos de uso de facturación.
 *
 * Define todas las operaciones disponibles para gestionar facturas odontológicas.
 * Siguiendo la arquitectura hexagonal, esta interfaz pertenece a la capa de aplicación
 * y será implementada por servicios de aplicación.
 *
 * Seguridad: Todos los métodos requieren requesterId y requesterRolId
 * para autorización explícita, según ADR-48.
 *
 * Tipos de facturas:
 * - PARTICULAR: Paciente paga directamente
 * - INSTITUCIONAL: Pagador institucional (EPS, Aseguradora, Prepagada, ARL, SOAT)
 */
public interface InvoiceUseCase {

    ReadInvoiceDto findById(
            InvoiceId id,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageInvoiceDto> findAll(
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageInvoiceDto> findByPatient(
            Long patientId,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    Page<PageInvoiceDto> findByDentist(
            Long dentistId,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    Page<PageInvoiceDto> findByStatus(
            InvoiceStatus.Status status,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadInvoiceDto findByNumber(
            String invoiceNumber,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageInvoiceDto> findByDateRange(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId
    );



    ReadInvoiceDto createParticular(
            CreateParticularInvoiceDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );
    ReadInvoiceDto createInstitutional(
            CreateInstitutionalInvoiceDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadInvoiceDto addItem(
            InvoiceId invoiceId,
            AddInvoiceItemDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    ReadInvoiceDto emit(
            InvoiceId id,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    ReadInvoiceDto cancel(
            InvoiceId id,
            String reason,
            UserIdentityId requesterId,
            RolId requesterRolId
    );


    ReadInvoiceDto markAsPaid(
            InvoiceId id,
            LocalDate paymentDate,
            UserIdentityId requesterId,
            RolId requesterRolId
    );
}