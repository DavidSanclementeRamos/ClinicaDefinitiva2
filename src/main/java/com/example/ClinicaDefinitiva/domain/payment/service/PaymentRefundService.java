
package com.example.ClinicaDefinitiva.domain.payment.service;


import com.example.ClinicaDefinitiva.domain.errors.catalog.payment.PaymentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.event.PaymentRefundedEvent;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGateway;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGatewayResult;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRepository;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentMethod;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Domain Service: PaymentRefundService
 * 
 * Responsabilidad:
 * - Procesar reembolsos de pagos
 * - Coordinar con gateway si es necesario
 * - Publicar eventos de reembolso
 */
@Service
public class PaymentRefundService {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentRefundService.class);
    
    private final Map<PaymentMethod, PaymentGateway> gateways;
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public PaymentRefundService(
            Map<PaymentMethod, PaymentGateway> gateways,
            PaymentRepository paymentRepository,
            ApplicationEventPublisher eventPublisher) {
        
        this.gateways = gateways;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * Reembolsa un pago (total o parcial).
     * 
     * @param paymentId ID del pago a reembolsar
     * @param refundAmount Monto a reembolsar
     * @return Payment actualizado
     */
    @Transactional
    public Payment refundPayment(PaymentId paymentId, Price refundAmount, String reason) {
        
        // 1. Buscar pago
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_NOT_FOUND,
                EntityContext.PAYMENT
            ));
        
        // 2. Procesar reembolso en gateway si aplica
        if (payment.getPaymentMethod().requiresGateway()) {
            processGatewayRefund(payment, refundAmount);
        }
        
        // 3. Registrar reembolso en dominio
        payment.refund(refundAmount,reason);
        
        // 4. Persistir
        paymentRepository.save(payment);
        
        // 5. Publicar evento
        eventPublisher.publishEvent(new PaymentRefundedEvent(
            payment.getId(), 
            payment.getInvoiceId(),
            refundAmount
        ));
        
        log.info("Payment {} refunded: {}", paymentId, refundAmount);
        
        return payment;
    }
    
    /**
     * Procesa el reembolso en el gateway externo.
     */
    private void processGatewayRefund(Payment payment, Price refundAmount) {
        PaymentGateway gateway = gateways.get(payment.getPaymentMethod());
        
        if (gateway == null || !gateway.isAvailable()) {
            log.warn("Gateway not available for refund: {}", payment.getPaymentMethod());
            return;
        }
        
        String transactionRef = payment.getTransactionReference().value();
        
        PaymentGatewayResult result = gateway.refundPayment(transactionRef, refundAmount);
        
        if (!result.success()) {
            log.error("Gateway refund failed: {}", result.errorMessage());
            throw new BusinessRuleViolationException(
                PaymentError.ERR_PAYMENT_REFUND_FAILED,
                EntityContext.PAYMENT
            );
        }
        
        log.info("Gateway refund successful: {}", result.transactionRef());
    }
}
