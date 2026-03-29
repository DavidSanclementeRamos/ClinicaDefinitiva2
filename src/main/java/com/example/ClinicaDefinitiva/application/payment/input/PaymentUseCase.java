
package com.example.ClinicaDefinitiva.application.payment.input;


import com.example.ClinicaDefinitiva.application.payment.dto.CreatePaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.PaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.RefundPaymentDto;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;

import java.util.List;

/**
 * Puerto de entrada para casos de uso de pagos.
 * 
 * Define todas las operaciones disponibles para gestionar pagos.
 * 
 * Seguridad: Todos los métodos requieren requesterId y requesterRolId
 * para autorización explícita, según ADR-48.
 */
public interface PaymentUseCase {
    
    /**
     * Procesa un pago contra una factura.
     * 
     * @param dto Datos del pago a procesar
     * @param requesterId Usuario que realiza la operación
     * @param requesterRolId Rol del usuario
     * @return Pago creado y procesado
     */
    PaymentDto processPayment(
        CreatePaymentDto dto,
        UserIdentityId requesterId,
        RolId requesterRolId
    );
    
    /**
     * Reembolsa un pago (total o parcial).
     * 
     * @param request Datos del reembolso
     * @param requesterId Usuario que realiza la operación
     * @param requesterRolId Rol del usuario
     * @return Pago actualizado con reembolso
     */
    PaymentDto refundPayment(
        RefundPaymentDto request,
        UserIdentityId requesterId,
        RolId requesterRolId
    );
    
    /**
     * Busca un pago por ID.
     */
    PaymentDto findById(
        PaymentId paymentId,
        UserIdentityId requesterId,
        RolId requesterRolId
    );
    
    /**
     * Lista todos los pagos de una factura.
     */
    List<PaymentDto> findByInvoiceId(
        InvoiceId invoiceId,
        UserIdentityId requesterId,
        RolId requesterRolId
    );
    
    /**
     * Lista todos los pagos confirmados de una factura.
     */
    List<PaymentDto> findConfirmedPaymentsByInvoice(
        InvoiceId invoiceId,
        UserIdentityId requesterId,
        RolId requesterRolId
    );
    
    /**
     * Busca pagos por estado.
     */
    List<PaymentDto> findByStatus(
        String status,
        UserIdentityId requesterId,
        RolId requesterRolId
    );
}