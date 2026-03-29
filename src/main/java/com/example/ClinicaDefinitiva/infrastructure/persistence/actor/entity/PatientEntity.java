package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity;


import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ContractEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "paciente")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_identidad", nullable = false)
    private UserIdentityEntity userIdentity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsable")
    private GuardianEntity guardian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato")
    private ContractEntity contract;

    @Embedded
    private PersonEmbeddable person;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime lastUpdate;

    public PatientEntity() {}

    public Long getId()                    { return id; }
    public UserIdentityEntity getUserIdentity() { return userIdentity; }
    public GuardianEntity getGuardian()      { return guardian; }
    public ContractEntity getContract()      { return contract; }
    public PersonEmbeddable getPerson()      { return person; }
    public LocalDateTime getLastUpdate()     { return lastUpdate; }

    public void setId(Long id) {
        this.id = id;
    }

    
    public void setUserIdentity(UserIdentityEntity userIdentity)  { this.userIdentity = userIdentity; }
    public void setGuardian(GuardianEntity guardian)              { this.guardian = guardian; }
    public void setContract(ContractEntity contract)              { this.contract = contract; }
    public void setPerson(PersonEmbeddable person)                { this.person = person; }
    public void setLastUpdate(LocalDateTime lastUpdate)           { this.lastUpdate = lastUpdate; }
}