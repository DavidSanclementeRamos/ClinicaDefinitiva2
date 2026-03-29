
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "linea_asiento_contable")
public class JournalEntryLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_asiento_contable", nullable = false)
    private JournalEntryEntity accountingEntry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cuenta_contable", nullable = false)
    private LedgerAccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tercero")
    private ThirdPartyEntity thirdParty;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "monto", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "moneda", nullable = false, length = 3)
    private String currency;

    @Column(name = "es_debito", nullable = false)
    private boolean isDebit;

    @Column(name = "referencia_documento", length = 100)
    private String documentReference;

    public JournalEntryLineEntity() {}

    public Long getId()                           { return id; }
    public JournalEntryEntity getAccountingEntry() { return accountingEntry; }
    public LedgerAccountEntity getAccount()              { return account; }
    public ThirdPartyEntity getThirdParty()        { return thirdParty; }
    public String getDescription()                 { return description; }
    public BigDecimal getAmount()                  { return amount; }
    public String getCurrency()                    { return currency; }
    public boolean isDebit()                       { return isDebit; }
    public String getDocumentReference()           { return documentReference; }

    public void setAccountingEntry(JournalEntryEntity accountingEntry) { this.accountingEntry = accountingEntry; }
    public void setAccount(LedgerAccountEntity account)                         { this.account = account; }
    public void setThirdParty(ThirdPartyEntity thirdParty)                { this.thirdParty = thirdParty; }
    public void setDescription(String description)                        { this.description = description; }
    public void setAmount(BigDecimal amount)                              { this.amount = amount; }
    public void setCurrency(String currency)                              { this.currency = currency; }
    public void setDebit(boolean isDebit)                                 { this.isDebit = isDebit; }
    public void setDocumentReference(String documentReference)            { this.documentReference = documentReference; }
}
