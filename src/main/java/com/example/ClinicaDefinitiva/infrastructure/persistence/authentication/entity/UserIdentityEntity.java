package com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "usuario_identidad")
public class UserIdentityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "contrasena_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "nombre", nullable = false, length = 150)
    private String name;

    @Column(name = "estado", nullable = false, length = 30)
    private String status;

    @Column(name = "verificado", nullable = false)
    private boolean verified;

    @Column(name = "intentos_fallidos", nullable = false)
    private int failedAttempts;

    @Column(name = "bloqueado_hasta")
    private Instant lockedUntil;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "ultimo_acceso")
    private Instant lastAccess;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public UserIdentityEntity() {}

    public Long getId()              { return id; }
    public String getEmail()         { return email; }
    public String getPasswordHash()   { return passwordHash; }
    public String getName()           { return name; }
    public String getStatus()         { return status; }
    public boolean isVerified()       { return verified; }
    public int getFailedAttempts()    { return failedAttempts; }
    public Instant getLockedUntil()   { return lockedUntil; }
    public Instant getCreatedAt()     { return createdAt; }
    public Instant getLastAccess()    { return lastAccess; }
    public Long getVersion()          { return version; }

    public void setId(Long id) {
        this.id = id;
    }

    
    public void setEmail(String email)                 { this.email = email; }
    public void setPasswordHash(String passwordHash)   { this.passwordHash = passwordHash; }
    public void setName(String name)                   { this.name = name; }
    public void setStatus(String status)               { this.status = status; }
    public void setVerified(boolean verified)          { this.verified = verified; }
    public void setFailedAttempts(int failedAttempts)  { this.failedAttempts = failedAttempts; }
    public void setLockedUntil(Instant lockedUntil)    { this.lockedUntil = lockedUntil; }
    public void setCreatedAt(Instant createdAt)        { this.createdAt = createdAt; }
    public void setLastAccess(Instant lastAccess)      { this.lastAccess = lastAccess; }
    public void setVersion(long version)               { this.version = version; }

    
}