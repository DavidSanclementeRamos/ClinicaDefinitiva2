
package com.example.ClinicaDefinitiva.application.payment.service;

import com.example.ClinicaDefinitiva.application.payment.dto.CreatePaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.PaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.RefundPaymentDto;
import com.example.ClinicaDefinitiva.application.payment.input.PaymentUseCase;
import com.example.ClinicaDefinitiva.application.payment.mapper.PaymentAppWriteMapper;
import com.example.ClinicaDefinitiva.application.payment.mapper.PaymentReadMapper;
import com.example.ClinicaDefinitiva.application.shared.dto.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.shared.service.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRepository;
import com.example.ClinicaDefinitiva.domain.payment.service.PaymentProcessingService;
import com.example.ClinicaDefinitiva.domain.payment.service.PaymentRefundService;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 
 * POLÍTICAS:
 * - SectorBasedPolicy: Solo RECEPTIONIST puede procesar/gestionar pagos
 * - (Futuro) OwnershipPolicy: Paciente puede ver sus propios pagos
 * - (Futuro) AssignmentPolicy: Dentista puede ver pagos de sus tratamientos
 * 
 * REGLAS DE NEGOCIO:
 * - Solo receptionist sector CONTABILIDAD puede procesar pagos
 * - Refunds requieren autorización especial (UPDATE permission)
 * - Pagos confirmados no pueden ser modificados (validado en dominio)
 */
@Service
@Transactional
public class PaymentApplicationService implements PaymentUseCase {

    private final PaymentProcessingService paymentProcessingService;
    private final PaymentRefundService paymentRefundService;
    private final PaymentRepository paymentRepository;
    private final PaymentReadMapper readMapper;
    private final PaymentAppWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public PaymentApplicationService(
            PaymentProcessingService paymentProcessingService,
            PaymentRefundService paymentRefundService,
            PaymentRepository paymentRepository,
            PaymentReadMapper readMapper,
            PaymentAppWriteMapper writeMapper,
            AuthorizationHelper authorizationHelper) {
        this.paymentProcessingService = paymentProcessingService;
        this.paymentRefundService = paymentRefundService;
        this.paymentRepository = paymentRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }

    /**
     * Procesa un nuevo pago.
     * 
     * Autorización:
     * - Requiere permiso CREATE en PAYMENT
     * - Solo receptionist sector CONTABILIDAD puede procesar pagos
     * 
     * Flujo:
     * 1. Validar autorización
     * 2. Delegar a PaymentProcessingService (dominio)
     * 3. Retornar DTO
     */
    @Override
    public PaymentDto processPayment(
            CreatePaymentDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId) {

        
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PAYMENT,     
                ActionCatalog.BasicAction.CREATE,          
                AuthorizationContext.builder().build()    
        );

        
        Payment payment = paymentProcessingService.processPayment(
            writeMapper.toInvoiceId(dto),
            writeMapper.toAmount(dto),
            writeMapper.toPaymentMethod(dto),
            dto.customerEmail(),
            dto.customerName(),
            writeMapper.toPayer(dto)
        );

        return readMapper.toDto(payment);
    }

    /**
     * Reembolsa un pago existente.
     * 
     * Autorización:
     * - Requiere permiso UPDATE en PAYMENT
     * - Solo receptionist sector CONTABILIDAD puede hacer refunds
     * - ResourceId = paymentId (para policies futuras de ownership)
     * 
     * Reglas de negocio (validadas en dominio):
     * - Solo pagos CONFIRMED pueden ser reembolsados
     * - Monto del refund <= monto del pago original
     * - Razón de refund es obligatoria
     */
    @Override
    public PaymentDto refundPayment(
            RefundPaymentDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId) {

                authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PAYMENT,    
                ActionCatalog.BasicAction.UPDATE,          
                AuthorizationContext.builder()
                        .withResourceId(dto.payment())     
                        .build()
        );

        // Delegar a servicio de dominio
        Payment payment = paymentRefundService.refundPayment(
            PaymentId.of(dto.payment()),
            writeMapper.toAmount(dto),
            dto.reason()
        );

        return readMapper.toDto(payment);
    }

    /**
     * Busca un pago por ID.
     * 
     * Autorización:
     * - Requiere permiso READ en PAYMENT
     * - ResourceId = paymentId
     * 
     * Futuras policies:
     * - OwnershipPolicy: Paciente puede ver sus propios pagos
     * - AssignmentPolicy: Dentista puede ver pagos de sus tratamientos
     */
    @Override
    public PaymentDto findById(
            PaymentId paymentId,
            UserIdentityId requesterId,
            RolId requesterRolId) {

        
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PAYMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(paymentId.value())
                        .withOwnership(requesterId)
                        .build()
        );

        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_NOT_FOUND,
                EntityContext.PAYMENT
            ));

        return readMapper.toDto(payment);
    }

    /**
     * Busca todos los pagos de una factura.
     * 
     * Autorización:
     * - Requiere permiso READ en PAYMENT
     * - ResourceId = invoiceId (para validar ownership de la invoice)
     * 
     * Futuras policies:
     * - OwnershipPolicy: Solo si la invoice pertenece al paciente
     * - AssignmentPolicy: Solo si la invoice está asignada al dentista
     */
    @Override
    public List<PaymentDto> findByInvoiceId(
            InvoiceId invoiceId,
            UserIdentityId requesterId,
            RolId requesterRolId) {

        // ⭐ IMPLEMENTADO: Autorización para leer pagos de una invoice
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PAYMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(invoiceId.getValue())
                        .withOwnership(requesterId)
                        .build()
        );

        return paymentRepository.findByInvoiceId(invoiceId)
            .stream()
            .map(readMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca solo pagos confirmados de una factura.
     * 
     * Autorización:
     * - Requiere permiso READ en PAYMENT
     * - ResourceId = invoiceId
     * 
     * Uso típico:
     * - Calcular total pagado de una factura
     * - Verificar si factura está completamente pagada
     */
    @Override
    public List<PaymentDto> findConfirmedPaymentsByInvoice(
            InvoiceId invoiceId,
            UserIdentityId requesterId,
            RolId requesterRolId) {

        // ⭐ IMPLEMENTADO: Autorización (mismo que findByInvoiceId)
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PAYMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(invoiceId.getValue())
                        .build()
        );

        
        return paymentRepository.findConfirmedByInvoiceId(invoiceId)
            .stream()
            .map(readMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Busca pagos por estado.
     * 
     * Autorización:
     * - Requiere permiso READ en PAYMENT
     * - Sin resourceId (sector-based)
     * 
     * Uso típico:
     * - Reportes de pagos pendientes
     * - Dashboard de pagos confirmados/rechazados
     * - Solo receptionist puede ejecutar esta consulta
     */
    @Override
    public List<PaymentDto> findByStatus(
            String status,
            UserIdentityId requesterId,
            RolId requesterRolId) {

        
        authorizationHelper.authorize(
                requesterId,
                requesterRolId,
                ResourceCatalog.BasicResource.PAYMENT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()  
        );

        //PaymentStatus.Status statusEnum = PaymentStatus.Status.valueOf(status);

        
        return paymentRepository.findByStatus(status)
            .stream()
            .map(readMapper::toDto)
            .collect(Collectors.toList());
    }
}