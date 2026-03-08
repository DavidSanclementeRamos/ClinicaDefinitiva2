package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.billing;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {
    @Id
    private String id;
    private Long patientId;
    private String providerId;
    private String currency;
    private String payerType;
    private LocalDateTime issuedAt;
    private double total;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItemEntity> items = new ArrayList<>();

    // getters/setters
}

