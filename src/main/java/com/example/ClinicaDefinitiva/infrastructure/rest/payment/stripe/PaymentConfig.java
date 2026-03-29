package com.example.ClinicaDefinitiva.infrastructure.rest.payment.stripe;

import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGateway;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentMethod;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.cash.CashPaymentGateway;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.eps.EPSPaymentGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

/**
 * Configuración de gateways de pago.
 * 
 * Configura múltiples gateways y los mapea según método de pago.
 */
@Configuration
public class PaymentConfig {
    
    /**
     * Mapa de gateways por método de pago.
     * 
     * Esto permite que PaymentProcessingService seleccione
     * automáticamente el gateway correcto según el método.
     */
    @Bean
    public Map<PaymentMethod, PaymentGateway> paymentGateways(
            @Qualifier("stripeGateway") PaymentGateway stripeGateway,
            @Qualifier("cashGateway") PaymentGateway cashGateway,
            @Qualifier("epsGateway") PaymentGateway epsGateway) {
        
        return Map.of(
            // Stripe para pagos online
            PaymentMethod.STRIPE, stripeGateway,
            PaymentMethod.CARD, stripeGateway, // Tarjetas también por Stripe
            
            // Efectivo
            PaymentMethod.CASH, cashGateway,
            
            // EPS
            PaymentMethod.EPS, epsGateway,
            
            // Otros métodos sin gateway (se confirman manualmente)
            PaymentMethod.BANK_TRANSFER, cashGateway,
            PaymentMethod.CONTRACT, cashGateway
        );
    }
    
    @Bean
    @Qualifier("stripeGateway")
    public PaymentGateway stripeGateway(StripePaymentGateway gateway) {
        return gateway;
    }
    
    @Bean
    @Qualifier("cashGateway")
    public PaymentGateway cashGateway(CashPaymentGateway gateway) {
        return gateway;
    }
    
    @Bean
    @Qualifier("epsGateway")
    public PaymentGateway epsGateway(EPSPaymentGateway gateway) {
        return (PaymentGateway) gateway;
    }
    
    /**
     * Gateway por defecto (Stripe).
     */
    @Bean
    @Primary
    public PaymentGateway defaultPaymentGateway(StripePaymentGateway gateway) {
        return gateway;
    }
}
