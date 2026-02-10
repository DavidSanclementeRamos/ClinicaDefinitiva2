package com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authentication;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "usuarios_identidad")
public class UserIdentityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;


    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "contrasena_hash", nullable = false, length = 255)
    private String hashedPassword;

    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Instant createdAt;


    @Column(name = "ultimo_login")
    private Instant lastLoginAt;

    @Column(name = "intentos_fallidos", nullable = false)
    private int failedLoginAttempts;

    @Column(name = "bloqueado_hasta")
    private Instant lockedUntil;

    @Column(name = "verificado", nullable = false)
    private boolean verified;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Version
    @Column(name = "version")
    private long version;

    protected UserIdentityEntity() {}

    public UserIdentityEntity(String email, String hashedPassword, String name, Instant createdAt,
                              boolean verified, String status) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.createdAt = createdAt;
        this.verified = verified;
        this.status = status;
        this.failedLoginAttempts = 0;
    }

    // Getters y setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(Instant lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(int failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }

    public Instant getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(Instant lockedUntil) { this.lockedUntil = lockedUntil; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getVersion() { return version; }
}

