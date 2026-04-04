package com.example.ClinicaDefinitiva.infrastructure.persistence.payment.entity;

import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.InvoiceEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_factura", nullable = false)
    private InvoiceEntity invoice;

    @Column(name = "monto", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "moneda", nullable = false, length = 3)
    private String currency;

    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String paymentMethod;

    /**
     * Payer VO polimórfico descompuesto en tipo + referencia.
     * payerType: PATIENT | INSTITUTION
     * payerReferenceId: UUID del paciente o del contrato según tipo.
     */
    @Column(name = "tipo_pagador", nullable = false, length = 30)
    private String payerType;

    @Column(name = "id_referencia_pagador")
    private String payerReferenceId;
    
    @Column(name = "nombre_pagador", nullable = false, length = 30)
    private String payerName;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "referencia_transaccion", length = 100)
    private String transactionReference;

    @Column(name = "id_pago_gateway", length = 100)
    private String paymentGatewayId;

    @Column(name = "mensaje_error", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "monto_reembolsado", nullable = false, precision = 19, scale = 4)
    private BigDecimal refundedAmount;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "actualizado_en")
    private LocalDateTime updatedAt;
    
    @Column(name = "razon_reembolso")
    private String refundReason;

    public PaymentEntity() {}

    public Long getId()                        { return id; }
    public InvoiceEntity getInvoice()           { return invoice; }
    public BigDecimal getAmount()               { return amount; }
    public String getCurrency()                 { return currency; }
    public String getPaymentMethod()            { return paymentMethod; }
    public String getPayerType()                { return payerType; }
    public String getPayerReferenceId()           { return payerReferenceId; }
    public String getStatus()                   { return status; }
    public String getTransactionReference()     { return transactionReference; }
    public String getPaymentGatewayId()         { return paymentGatewayId; }
    public String getErrorMessage()             { return errorMessage; }
    public BigDecimal getRefundedAmount()       { return refundedAmount; }
    public LocalDateTime getPaymentDate()       { return paymentDate; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
    public LocalDateTime getUpdatedAt()         { return updatedAt; }

    public String getPayerName() {
        return payerName;
    }

    public String getRefundReason() {
        return refundReason;
    }


    
    
    
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    
    
    public void setInvoice(InvoiceEntity invoice)                 { this.invoice = invoice; }
    public void setAmount(BigDecimal amount)                      { this.amount = amount; }
    public void setCurrency(String currency)                      { this.currency = currency; }
    public void setPaymentMethod(String paymentMethod)            { this.paymentMethod = paymentMethod; }
    public void setPayerType(String payerType)                    { this.payerType = payerType; }
    public void setPayerReferenceId(String payerReferenceId)        { this.payerReferenceId = payerReferenceId; }
    public void setStatus(String status)                          { this.status = status; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public void setPaymentGatewayId(String paymentGatewayId)      { this.paymentGatewayId = paymentGatewayId; }
    public void setErrorMessage(String errorMessage)              { this.errorMessage = errorMessage; }
    public void setRefundedAmount(BigDecimal refundedAmount)      { this.refundedAmount = refundedAmount; }
    public void setPaymentDate(LocalDateTime paymentDate)         { this.paymentDate = paymentDate; }
    public void setCreatedAt(LocalDateTime createdAt)             { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)             { this.updatedAt = updatedAt; }
}