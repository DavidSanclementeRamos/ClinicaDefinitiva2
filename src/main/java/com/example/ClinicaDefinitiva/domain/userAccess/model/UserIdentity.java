package com.example.ClinicaDefinitiva.domain.userAccess.model;

import com.example.ClinicaDefinitiva.domain.administration.accessControl.Rol;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;

import java.time.LocalDate;
import java.util.Set;

public class UserIdentity {
// infra estructura tecnica, solo vive lo que garantiza que un usuario
// puede autenticarse y ser reconocido por el sistema.
    private String id;
    private String name;
    private String gmail;
    private String passwork;
    private LocalDate dateCreate;
    private UserStatus statusUser;
    private String imagenPerfil;
    private LocalDate ultimaFechaDeCoexion;
   // private Set<Rol> rol = new HashSet<>();
    private boolean isEnabled;
    private boolean accountNoExpired;
    private boolean accountNoLocked;
    private boolean credentialNoExpired;
    private UserStatus status;
   // private Dentist rolOdontologo;

    public UserIdentity() {
    }

    public UserIdentity(boolean accountNoExpired, boolean accountNoLocked, boolean credentialNoExpired, LocalDate dateCreate, String gmail, String id, String imagenPerfil, boolean isEnabled, String name, String passwork, Set<Rol> rol, UserStatus statusUser, LocalDate ultimaFechaDeCoexion, UserStatus status) {
        this.accountNoExpired = accountNoExpired;
        this.accountNoLocked = accountNoLocked;
        this.credentialNoExpired = credentialNoExpired;
        this.dateCreate = dateCreate;
        this.gmail = gmail;
        this.id = id;
        this.imagenPerfil = imagenPerfil;
        this.isEnabled = isEnabled;
        this.name = name;
        this.passwork = passwork;
        this.rol = rol;
        this.statusUser = statusUser;
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
        this.status = status;
    }


    // metodo delegado de UserStatus para evitar complamiento
    public boolean isActive() {
        return  status.isActive();
    }
    public  UserStatus getStatus() {
        return status;
    }

    public void inactivate() {
        this.status = UserStatus.of(UserStatus.Status.INACTIVE);
    }

    public void activate() {
        this.status = UserStatus.of(UserStatus.Status.ACTIVE);
    }




    public void setStatus(UserStatus status) {
        this.status = status;
    }

    /// No puede desactivarse si tiene citas activas en las próximas 24 horas
    /// Protege la continuidad del servicio y evita cancelaciones abruptas
    public void deactivate() {
        if (status.isInactive()) return;

        if (rolOdontologo != null && rolOdontologo.tieneCitasActivasEnLasProximas24Horas()) {
            int count = rolOdontologo.citasActivasEnLasProximas24Horas().size();
            throw new IllegalArgumentException(
                    "No se puede desactivar: tiene " + count + " cita" + (count > 1 ? "s" : "") + " en las próximas 24 horas"
            );
        }

    }


        public boolean isAccountNoExpired() {
        return accountNoExpired;
    }

    public void setAccountNoExpired(boolean accountNoExpired) {
        this.accountNoExpired = accountNoExpired;
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

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswork() {
        return passwork;
    }

    public void setPasswork(String passwork) {
        this.passwork = passwork;
    }

    public Set<Rol> getRol() {
        return rol;
    }

    public void setRol(Set<Rol> rol) {
        this.rol = rol;
    }

    public UserStatus getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(UserStatus statusUser) {
        this.statusUser = statusUser;
    }

    public LocalDate getUltimaFechaDeCoexion() {
        return ultimaFechaDeCoexion;
    }

    public void setUltimaFechaDeCoexion(LocalDate ultimaFechaDeCoexion) {
        this.ultimaFechaDeCoexion = ultimaFechaDeCoexion;
    }
}
