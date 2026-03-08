package com.example.ClinicaDefinitiva.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_items")
public class InvoiceItemEntity {
    @Id
    private String id;
    private String serviceCode;
    private String description;
    private int quantity;
    private double unitPrice;
    private String currency;
    private String rateId;
    private LocalDateTime performedAt;
    private String providerId;
    // getters/setters
}


