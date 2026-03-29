
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asiento_contable")
public class JournalEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private CompanyEntity company;

    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "numero_documento", nullable = false, length = 50)
    private String documentNumber;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "balanceado", nullable = false)
    private boolean balanced;

    @Column(name = "contabilizado", nullable = false)
    private boolean posted;

    @OneToMany(mappedBy = "accountingEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntryLineEntity> lines = new ArrayList<>();

    public JournalEntryEntity() {}

    // ── Getters ──────────────────────────────────────────
    public Long getId()                      { return id; }
    public CompanyEntity getCompany()         { return company; }
    public LocalDate getDate()                { return date; }
    public String getDocumentNumber()         { return documentNumber; }
    public String getDescription()            { return description; }
    public boolean isBalanced()               { return balanced; }
    public boolean isPosted()                 { return posted; }
    public List<JournalEntryLineEntity> getLines() { return lines; }

    public void setCompany(CompanyEntity company)             { this.company = company; }
    public void setDate(LocalDate date)                       { this.date = date; }
    public void setDocumentNumber(String documentNumber)      { this.documentNumber = documentNumber; }
    public void setDescription(String description)            { this.description = description; }
    public void setBalanced(boolean balanced)                 { this.balanced = balanced; }
    public void setPosted(boolean posted)                     { this.posted = posted; }
    public void setLines(List<JournalEntryLineEntity> lines)    { this.lines = lines; }
}
