package com.example.ClinicaDefinitiva.domain.administration.authorization.model;

import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RolTest {

    private Rol defaultRol;
    private Rol customRol;

    @BeforeEach
    void setUp() {
        defaultRol = Rol.createCustom(RolEnum.PATIENT, "Rol de paciente");
        customRol = Rol.cloneFrom(defaultRol, "Rol clonado");
    }

    @Test
    @DisplayName("AUTH-UNIT-004: Crear rol por defecto")
    void createDefault() {
        assertThat(defaultRol.getRolEnum()).isEqualTo(RolEnum.PATIENT);
        assertThat(defaultRol.isDefault()).isTrue();
        assertThat(defaultRol.isEditable()).isFalse();
        assertThat(defaultRol.isDeletable()).isFalse();
        assertThat(defaultRol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
    }

    @Test
    @DisplayName("AUTH-UNIT-005: Clonar rol existente")
    void cloneRole() {
        assertThat(customRol.getRolEnum()).isEqualTo(RolEnum.PATIENT);
        assertThat(customRol.isDefault()).isFalse();
        assertThat(customRol.isEditable()).isTrue();
        assertThat(customRol.isDeletable()).isTrue();
        assertThat(customRol.getDescription()).isEqualTo("Rol clonado");
    }

    @Test
    @DisplayName("Agregar permiso a rol editable")
    void addPermission() {
        Permission perm = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        customRol.addPermission(perm);
        assertThat(customRol.hasPermission(perm)).isTrue();
    }

    @Test
    @DisplayName("Agregar permiso a rol no editable lanza excepción")
    void addPermissionToNonEditable_throws() {
        Permission perm = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        assertThatThrownBy(() -> defaultRol.addPermission(perm))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("Remover permiso existente")
    void removePermission() {
        Permission perm = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        customRol.addPermission(perm);
        customRol.removePermission(perm);
        assertThat(customRol.hasPermission(perm)).isFalse();
    }

    @Test
    @DisplayName("Remover permiso de rol sin permisos lanza excepción")
    void removePermissionFromEmpty_throws() {
        Permission perm = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        assertThatThrownBy(() -> customRol.removePermission(perm))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("Activar rol requiere razón válida")
    void activate() {
        customRol.activate("Razón válida con más de diez caracteres");
        assertThat(customRol.getStatusRol()).isEqualTo(RolStatus.ACTIVE);
        assertThat(customRol.getLastStateChangeReason()).isEqualTo("Razón válida con más de diez caracteres");
    }

    @Test
    @DisplayName("Activar rol sin razón lanza excepción")
    void activateWithoutReason_throws() {
        assertThatThrownBy(() -> customRol.activate(null))
                .isInstanceOf(BusinessRuleViolationException.class);
        assertThatThrownBy(() -> customRol.activate("Corta"))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("Desactivar rol")
    void deactivate() {
        customRol.deactivate("Desactivación temporal");
        assertThat(customRol.getStatusRol()).isEqualTo(RolStatus.INACTIVE);
    }

    @Test
    @DisplayName("Suspender rol")
    void suspend() {
        customRol.suspend("Suspensión por auditoría");
        assertThat(customRol.getStatusRol()).isEqualTo(RolStatus.SUSPENDED);
    }

    @Test
    @DisplayName("Marcar como eliminado solo si es eliminable")
    void markDeleted() {
        customRol.markDeleted("Eliminación definitiva");
        assertThat(customRol.getStatusRol()).isEqualTo(RolStatus.DELETED);
    }

    @Test
    @DisplayName("Marcar como eliminado en rol no eliminable lanza excepción")
    void markDeletedOnNonDeletable_throws() {
        assertThatThrownBy(() -> defaultRol.markDeleted("Razón"))
                .isInstanceOf(BusinessRuleViolationException.class);
    }
}