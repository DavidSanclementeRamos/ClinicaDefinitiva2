package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authorization;

import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "asignacion_rol_usuario")   // tabla en español
public class UserRolAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")       // columna en español
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long userId;                  // atributo en inglés

    @Column(name = "id_rol", nullable = false)
    private Long rolId;                   // atributo en inglés

    @Column(name = "vigente_desde")
    private LocalDate validFrom;          // atributo en inglés

    @Column(name = "vigente_hasta")
    private LocalDate validTo;            // atributo en inglés

    @Column(name = "es_principal", nullable = false)
    private boolean isPrimary;            // atributo en inglés

    public UserRolAssignmentEntity() {}

    public UserRolAssignmentEntity(Long id, Long userId, Long rolId,
                                   LocalDate validFrom, LocalDate validTo, boolean isPrimary) {
        this.id = id;
        this.userId = userId;
        this.rolId = rolId;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.isPrimary = isPrimary;
    }

    // Getters y Setters en inglés
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }

    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }

    public LocalDate getValidTo() { return validTo; }
    public void setValidTo(LocalDate validTo) { this.validTo = validTo; }

    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }
}
