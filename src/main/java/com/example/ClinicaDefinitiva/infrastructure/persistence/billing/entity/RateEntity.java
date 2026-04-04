package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ContractEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.DentalServiceEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarifa")
public class RateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servicio", nullable = false)
    private DentalServiceEntity dentalService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato")
    private ContractEntity contract;

    @Column(name = "tipo_pagador", nullable = false, length = 30)
    private String payerType;

    @Column(name = "monto", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "moneda", nullable = false, length = 3)
    private String currency;

    @Column(name = "vigente_desde", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "vigente_hasta")
    private LocalDateTime validUntil;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    public RateEntity() {}

    public Long getId()                         { return id; }
    public DentalServiceEntity getDentalService(){ return dentalService; }
    public ContractEntity getContract()         { return contract; }
    public String getPayerType()                { return payerType; }
    public BigDecimal getAmount()               { return amount; }
    public String getCurrency()                 { return currency; }
    public LocalDateTime getValidFrom()         { return validFrom; }
    public LocalDateTime getValidUntil()        { return validUntil; }
    public String getStatus()                   { return status; }

    
     public void setId(Long id) {
        this.id = id;
    }
    public void setDentalService(DentalServiceEntity dentalService) { this.dentalService = dentalService; }
    public void setContract(ContractEntity contract)                { this.contract = contract; }
    public void setPayerType(String payerType)                      { this.payerType = payerType; }
    public void setAmount(BigDecimal amount)                        { this.amount = amount; }
    public void setCurrency(String currency)                        { this.currency = currency; }
    public void setValidFrom(LocalDateTime validFrom)               { this.validFrom = validFrom; }
    public void setValidUntil(LocalDateTime validUntil)             { this.validUntil = validUntil; }
    public void setStatus(String status)                            { this.status = status; }
}