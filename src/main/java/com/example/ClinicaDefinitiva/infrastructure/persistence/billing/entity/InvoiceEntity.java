package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity;


import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.DentistEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PatientEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ContractEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factura")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Factura particular: id_paciente poblado, id_contrato null.
     * Factura institucional: id_contrato poblado, id_paciente null.
     * Decisión documentada en ADR-54.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente")
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dentista", nullable = false)
    private DentistEntity dentist;

    @Column(name = "id_proveedor")
    private Long providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato")
    private ContractEntity contract;

    @Column(name = "numero_factura", unique = true, length = 30)
    private String invoiceNumber;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "moneda", nullable = false, length = 3)
    private String currency;

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 4)
    private BigDecimal subtotal;

    @Column(name = "impuesto", nullable = false, precision = 19, scale = 4)
    private BigDecimal tax;

    @Column(name = "total", nullable = false, precision = 19, scale = 4)
    private BigDecimal total;

    @Column(name = "total_pagado", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalPaid;

    @Column(name = "fecha_vencimiento")
    private LocalDateTime dueDate;

    @Column(name = "actualizado_en")
    private LocalDateTime updatedAt;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItemEntity> items = new ArrayList<>();

    public InvoiceEntity() {}

    public Long getId()                          { return id; }
    public PatientEntity getPatient()            { return patient; }
    public DentistEntity getDentist()            { return dentist; }
    public Long getProviderId()                  { return providerId; }
    public ContractEntity getContract()          { return contract; }
    public String getInvoiceNumber()             { return invoiceNumber; }
    public String getStatus()                    { return status; }
    public String getCurrency()                  { return currency; }
    public BigDecimal getSubtotal()              { return subtotal; }
    public BigDecimal getTax()                    { return tax; }
    public BigDecimal getTotal()                  { return total; }
    public BigDecimal getTotalPaid()              { return totalPaid; }
    public LocalDateTime getDueDate()             { return dueDate; }
    public LocalDateTime getUpdatedAt()           { return updatedAt; }
    public String getNotes()                      { return notes; }
    public List<InvoiceItemEntity> getItems()     { return items; }

    public void setPatient(PatientEntity patient)                    { this.patient = patient; }
    public void setDentist(DentistEntity dentist)                    { this.dentist = dentist; }
    public void setProviderId(Long providerId)                       { this.providerId = providerId; }
    public void setContract(ContractEntity contract)                 { this.contract = contract; }
    public void setInvoiceNumber(String invoiceNumber)               { this.invoiceNumber = invoiceNumber; }
    public void setStatus(String status)                             { this.status = status; }
    public void setCurrency(String currency)                         { this.currency = currency; }
    public void setSubtotal(BigDecimal subtotal)                     { this.subtotal = subtotal; }
    public void setTax(BigDecimal tax)                               { this.tax = tax; }
    public void setTotal(BigDecimal total)                           { this.total = total; }
    public void setTotalPaid(BigDecimal totalPaid)                   { this.totalPaid = totalPaid; }
    public void setDueDate(LocalDateTime dueDate)                    { this.dueDate = dueDate; }
    public void setUpdatedAt(LocalDateTime updatedAt)                { this.updatedAt = updatedAt; }
    public void setNotes(String notes)                               { this.notes = notes; }
    public void setItems(List<InvoiceItemEntity> items)              { this.items = items; }
}