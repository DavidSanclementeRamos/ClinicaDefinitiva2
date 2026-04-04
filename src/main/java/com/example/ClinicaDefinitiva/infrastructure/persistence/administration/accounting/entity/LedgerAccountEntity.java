
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "cuenta_contable")
public class LedgerAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private CompanyEntity company;

    @Column(name = "codigo", nullable = false, length = 8)
    private String code;

    @Column(name = "nombre", nullable = false, length = 200)
    private String name;

    @Column(name = "naturaleza", nullable = false, length = 20)
    private String nature;

    @Column(name = "requiere_tercero", nullable = false)
    private boolean requiresThirdParty;

    @Column(name = "requiere_documento", nullable = false)
    private boolean requiresDocument;

    @Column(name = "activo", nullable = false)
    private boolean active;

    public LedgerAccountEntity() {}

    public Long getId()                    { return id; }
    public CompanyEntity getCompany()       { return company; }
    public String getCode()                 { return code; }
    public String getName()                 { return name; }
    public String getNature()               { return nature; }
    public boolean isRequiresThirdParty()   { return requiresThirdParty; }
    public boolean isRequiresDocument()     { return requiresDocument; }
    public boolean isActive()               { return active; }

     public void setId(Long id) {
        this.id = id;
    }
    public void setCompany(CompanyEntity company)                 { this.company = company; }
    public void setCode(String code)                               { this.code = code; }
    public void setName(String name)                               { this.name = name; }
    public void setNature(String nature)                           { this.nature = nature; }
    public void setRequiresThirdParty(boolean requiresThirdParty) { this.requiresThirdParty = requiresThirdParty; }
    public void setRequiresDocument(boolean requiresDocument)     { this.requiresDocument = requiresDocument; }
    public void setActive(boolean active)                          { this.active = active; }
}
