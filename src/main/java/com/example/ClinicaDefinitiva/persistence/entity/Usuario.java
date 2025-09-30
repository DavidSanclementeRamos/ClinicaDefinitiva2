package com.example.ClinicaDefinitiva.persistence.entity;


import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombreUsuario;
    private String correoEletronico;
    private String contrasena;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate fechaDeCreacion;

    private Estado estado;
    private String imagenPerfil;
    private LocalDate ultimaFechaDeCoexion;

    @OneToMany(cascade = {  CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<RolesEntity> roles = new HashSet<>();

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_No_Expired")
    private boolean accountNoExpired;

    @Column(name = "account_No_Locked")
    private boolean accountNoLocked;

    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired;

    public boolean isAccountNoExpired() {
        return accountNoExpired;
    }

    public void setAccountNoExpired(boolean accountNoExpired) {
        this.accountNoExpired = accountNoExpired;
    }

    public Usuario(){

    }

    public Usuario(boolean accountNoExpired, boolean accountNoLocked, String contrasena, String correoEletronico, boolean credentialNoExpired, Estado estado, LocalDate fechaDeCreacion, long id, String imagenPerfil, boolean isEnabled, String nombreUsuario, Set<RolesEntity> roles, LocalDate ultimaFechaDeCoexion) {
        this.accountNoExpired = accountNoExpired;
        this.accountNoLocked = accountNoLocked;
        this.contrasena = contrasena;
        this.correoEletronico = correoEletronico;
        this.credentialNoExpired = credentialNoExpired;
        this.estado = estado;
        this.fechaDeCreacion = fechaDeCreacion;
        this.id = id;
        this.imagenPerfil = imagenPerfil;
        this.isEnabled = isEnabled;
        this.nombreUsuario = nombreUsuario;
        this.roles = roles;
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }

    public boolean isAccountNoLocked() {
        return accountNoLocked;
    }

    public void setAccountNoLocked(boolean accountNoLocked) {
        this.accountNoLocked = accountNoLocked;
    }

    public boolean isCredentialNoExpired() {
        return credentialNoExpired;
    }

    public void setCredentialNoExpired(boolean credentialNoExpired) {
        this.credentialNoExpired = credentialNoExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Set<RolesEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesEntity> roles) {
        this.roles = roles;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreoEletronico() {
        return correoEletronico;
    }

    public void setCorreoEletronico(String correoEletronico) {
        this.correoEletronico = correoEletronico;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDate getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(LocalDate fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }



    public LocalDate getUltimaFechaDeCoexion() {
        return ultimaFechaDeCoexion;
    }

    public void setUltimaFechaDeCoexion(LocalDate ultimaFechaDeCoexion) {
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }
}
