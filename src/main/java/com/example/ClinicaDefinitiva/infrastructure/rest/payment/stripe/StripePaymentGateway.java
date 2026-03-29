
package com.example.ClinicaDefinitiva.infrastructure.rest.payment.stripe;



import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGateway;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGatewayResult;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRequest;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentStatus;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Adapter: StripePaymentGateway
 * 
 * Implementación del gateway de pagos usando Stripe API.
 * 
 * Responsabilidades:
 * - Traducir llamadas del dominio a Stripe API
 * - Traducir respuestas de Stripe a tipos del dominio
 * - Manejar errores de Stripe
 * 
 * Configuración requerida:
 * - stripe.enabled=true
 * - stripe.api.key=sk_test_xxxxx
 */
@Component
public class StripePaymentGateway implements PaymentGateway {
    
    private static final Logger log = LoggerFactory.getLogger(StripePaymentGateway.class);
    
    @Value("${stripe.api.key:}")
    private String stripeApiKey;
    
    @Value("${stripe.enabled:false}")
    private boolean enabled;
    
    @PostConstruct
    public void init() {
        if (enabled && stripeApiKey != null && !stripeApiKey.isBlank()) {
            Stripe.apiKey = stripeApiKey;
            log.info("✅ Stripe payment gateway initialized");
        } else {
            log.warn("⚠️ Stripe payment gateway disabled (check configuration)");
        }
    }
    
    @Override
    public PaymentGatewayResult processPayment(PaymentRequest request) {
        if (!isAvailable()) {
            return PaymentGatewayResult.failure("Stripe gateway not available");
        }
        
        try {
            log.info("Processing Stripe payment: amount={}, currency={}", 
                    request.amount(), request.currency());
            
            // Convertir a centavos (Stripe usa la unidad mínima)
            long amountInCents = convertToCents(request.amount());
            
            // Crear Payment Intent en Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(request.currency().toLowerCase())
                .setReceiptEmail(request.customerEmail())
                .setDescription(request.description())
                .putMetadata("invoice_number", request.invoiceNumber())
                .putMetadata("customer_name", request.customerName())
                .setConfirm(true) // Confirmar inmediatamente
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();
            
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            
            log.info("✅ Stripe payment intent created: id={}, status={}", 
                    paymentIntent.getId(), paymentIntent.getStatus());
            
            // Mapear respuesta a tipos del dominio
            PaymentStatus status = mapStripeStatusToDomain(paymentIntent.getStatus());
            boolean success = "succeeded".equals(paymentIntent.getStatus());
            
            return new PaymentGatewayResult(
                success,
                paymentIntent.getId(),
                paymentIntent.getId(),
                status,
                null
            );
            
        } catch (StripeException e) {
            log.error("❌ Stripe payment failed: code={}, message={}", 
                    e.getCode(), e.getMessage(), e);
            return PaymentGatewayResult.failure(e.getMessage());
        }
    }
    
    @Override
    public PaymentGatewayResult refundPayment(String transactionRef, Price amount) {
        if (!isAvailable()) {
            return PaymentGatewayResult.failure("Stripe gateway not available");
        }
        
        try {
            log.info("Processing Stripe refund: transactionRef={}, amount={}", 
                    transactionRef, amount);
            
            long amountInCents = convertToCents(amount);
            
            RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(transactionRef)
                .setAmount(amountInCents)
                .build();
            
            Refund refund = Refund.create(params);
            
            log.info("✅ Stripe refund created: id={}, status={}", 
                    refund.getId(), refund.getStatus());
            
            return PaymentGatewayResult.success(refund.getId(), transactionRef);
            
        } catch (StripeException e) {
            log.error("❌ Stripe refund failed: code={}, message={}", 
                    e.getCode(), e.getMessage(), e);
            return PaymentGatewayResult.failure(e.getMessage());
        }
    }
    
    @Override
    public PaymentStatus getPaymentStatus(String transactionRef) {
        if (!isAvailable()) {
            return PaymentStatus.failed();
        }
        
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(transactionRef);
            return mapStripeStatusToDomain(paymentIntent.getStatus());
            
        } catch (StripeException e) {
            log.error("Failed to retrieve payment status from Stripe: {}", 
                    e.getMessage());
            return PaymentStatus.failed();
        }
    }
    
    @Override
    public boolean isAvailable() {
        return enabled && stripeApiKey != null && !stripeApiKey.isBlank();
    }
    
    // ============ Métodos auxiliares ============
    
    /**
     * Convierte Price a centavos (Stripe usa unidad mínima de moneda).
     */
    private long convertToCents(Price amount) {
        return (long) (amount.asBigDecimal().doubleValue() * 100);
    }
    
    /**
     * Mapea estado de Stripe a estado del dominio.
     */
    private PaymentStatus mapStripeStatusToDomain(String stripeStatus) {
        return switch (stripeStatus) {
            case "succeeded" -> PaymentStatus.confirmed();
            case "processing" -> PaymentStatus.pending();
            case "requires_payment_method", "canceled" -> PaymentStatus.failed();
            case "requires_action", "requires_confirmation" -> PaymentStatus.pending();
            default -> {
                log.warn("Unknown Stripe status: {}", stripeStatus);
                yield PaymentStatus.pending();
            }
        };
    }
}