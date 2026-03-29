
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "saldo_inicial")
public class OpeningBalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cuenta_contable", nullable = false)
    private LedgerAccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tercero")
    private ThirdPartyEntity thirdParty;

    @Column(name = "monto", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "moneda", nullable = false, length = 3)
    private String currency;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    public OpeningBalanceEntity() {}

    public Long getId()                       { return id; }
    public CompanyEntity getCompany()          { return company; }
    public LedgerAccountEntity getAccount()          { return account; }
    public ThirdPartyEntity getThirdParty()    { return thirdParty; }
    public BigDecimal getAmount()              { return amount; }
    public String getCurrency()                { return currency; }
    public LocalDate getDate()                 { return date; }

    public void setCompany(CompanyEntity company)                { this.company = company; }
    public void setAccount(LedgerAccountEntity account)                { this.account = account; }
    public void setThirdParty(ThirdPartyEntity thirdParty)       { this.thirdParty = thirdParty; }
    public void setAmount(BigDecimal amount)                     { this.amount = amount; }
    public void setCurrency(String currency)                     { this.currency = currency; }
    public void setDate(LocalDate date)                          { this.date = date; }
}
