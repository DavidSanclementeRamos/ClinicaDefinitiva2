 
package com.example.ClinicaDefinitiva.infrastructure.persistence.payment.jpaRepository;
 
import com.example.ClinicaDefinitiva.infrastructure.persistence.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
/**
 * Spring Data JPA Repository para PaymentEntity.
 */
@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    
    /**
     * Busca todos los pagos de una factura.
     */
    List<PaymentEntity> findByInvoiceId(Long invoiceId);
    
    /**
     * Busca pagos por estado.
     */
    List<PaymentEntity> findByStatus(String status);
    
    /**
     * Busca pagos por método de pago.
     */
    List<PaymentEntity> findByPaymentMethod(String paymentMethod);
    
    /**
     * Busca pagos confirmados de una factura.
     */
    @Query("SELECT p FROM PaymentEntity p WHERE p.invoice.id = :invoiceId AND p.status = 'CONFIRMED'")
    List<PaymentEntity> findConfirmedPaymentsByInvoice(@Param("invoiceId") Long invoiceId);
}

