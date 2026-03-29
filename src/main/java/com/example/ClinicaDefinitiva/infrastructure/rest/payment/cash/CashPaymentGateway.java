
package com.example.ClinicaDefinitiva.infrastructure.rest.payment.cash;


import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGateway;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentGatewayResult;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRequest;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentStatus;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adapter: CashPaymentGateway
 * 
 * Gateway para pagos en efectivo (sin API externa).
 * 
 * Los pagos en efectivo se confirman inmediatamente porque:
 * - No requieren procesamiento externo
 * - Se registran directamente en el sistema
 * - La transacción es instantánea
 */
@Component
public class CashPaymentGateway implements PaymentGateway {
    
    private static final Logger log = LoggerFactory.getLogger(CashPaymentGateway.class);
    
    @Override
    public PaymentGatewayResult processPayment(PaymentRequest request) {
        log.info("Processing cash payment: amount={}, invoice={}", 
                request.amount(), request.invoiceNumber());
        
        // Generar referencia interna para el recibo
        String transactionRef = generateCashReceiptNumber();
        
        log.info("✅ Cash payment processed: receipt={}", transactionRef);
        
        return PaymentGatewayResult.success(transactionRef, transactionRef);
    }
    
    @Override
    public PaymentGatewayResult refundPayment(String transactionRef, Price amount) {
        log.info("Processing cash refund: receipt={}, amount={}", 
                transactionRef, amount);
        
        // Generar referencia de reembolso
        String refundRef = "REFUND-" + transactionRef;
        
        log.info("✅ Cash refund processed: refundRef={}", refundRef);
        
        return PaymentGatewayResult.success(refundRef, transactionRef);
    }
    
    @Override
    public PaymentStatus getPaymentStatus(String transactionRef) {
        // Pagos en efectivo siempre están confirmados
        return PaymentStatus.confirmed();
    }
    
    @Override
    public boolean isAvailable() {
        // Efectivo siempre disponible
        return true;
    }
    
    /**
     * Genera un número de recibo único para pagos en efectivo.
     */
    private String generateCashReceiptNumber() {
        return "CASH-" + System.currentTimeMillis();
    }
}
