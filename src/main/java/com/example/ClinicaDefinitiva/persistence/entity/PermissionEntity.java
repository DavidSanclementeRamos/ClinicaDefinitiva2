package com.example.ClinicaDefinitiva.persistence.entity;

import com.example.ClinicaDefinitiva.Enum.Permisos;
import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Permisos permisos;

    public PermissionEntity(Long id, String name, Permisos permisos ){

        this.permisos = permisos;
        this.id = id;
        this.name = name;
    }

    public Permisos getPermisos() {
        return permisos;
    }

    public void setPermisos(Permisos permisos) {
        this.permisos = permisos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
