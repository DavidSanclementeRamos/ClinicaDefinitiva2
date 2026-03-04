
package com.example.ClinicaDefinitiva.domain.payment.output;


import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;

import java.util.List;
import java.util.Optional;

/**
 * Port: PaymentRepository
 * 
 * Repositorio para el agregado Payment.
 */
public interface PaymentRepository {
    
    Payment save(Payment payment);
    
    Optional<Payment> findById(PaymentId id);
    
    List<Payment> findByInvoiceId(InvoiceId invoiceId);
    
    List<Payment> findAll();
    
    void delete(PaymentId id);
}

