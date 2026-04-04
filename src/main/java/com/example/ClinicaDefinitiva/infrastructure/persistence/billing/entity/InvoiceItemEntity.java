package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity;


import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.entity.DentalServiceEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_factura")
public class InvoiceItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_factura", nullable = false)
    private InvoiceEntity invoice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servicio", nullable = false)
    private DentalServiceEntity dentalService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarifa")
    private RateEntity rate;

    @Column(name = "codigo_servicio", nullable = false, length = 20)
    private String serviceCode;

    @Column(name = "descripcion_servicio", columnDefinition = "TEXT")
    private String serviceDescription;

    @Column(name = "precio_unitario", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "moneda", nullable = false, length = 3)
    private String currency;

    @Column(name = "cantidad", nullable = false)
    private int quantity;

    @Column(name = "fecha_realizacion")
    private LocalDateTime performedDate;

    public InvoiceItemEntity() {}

    public Long getId()                           { return id; }
    public InvoiceEntity getInvoice()              { return invoice; }
    public DentalServiceEntity getDentalService()  { return dentalService; }
    public RateEntity getRate()                    { return rate; }
    public String getServiceCode()                 { return serviceCode; }
    public String getServiceDescription()          { return serviceDescription; }
    public BigDecimal getUnitPrice()               { return unitPrice; }
    public String getCurrency()                    { return currency; }
    public int getQuantity()                       { return quantity; }
    public LocalDateTime getPerformedDate()        { return performedDate; }

     public void setId(Long id) {
        this.id = id;
    }
    public void setInvoice(InvoiceEntity invoice)                    { this.invoice = invoice; }
    public void setDentalService(DentalServiceEntity dentalService)  { this.dentalService = dentalService; }
    public void setRate(RateEntity rate)                             { this.rate = rate; }
    public void setServiceCode(String serviceCode)                   { this.serviceCode = serviceCode; }
    public void setServiceDescription(String serviceDescription)     { this.serviceDescription = serviceDescription; }
    public void setUnitPrice(BigDecimal unitPrice)                   { this.unitPrice = unitPrice; }
    public void setCurrency(String currency)                         { this.currency = currency; }
    public void setQuantity(int quantity)                            { this.quantity = quantity; }
    public void setPerformedDate(LocalDateTime performedDate)        { this.performedDate = performedDate; }
}