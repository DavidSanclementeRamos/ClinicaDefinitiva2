package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PermissionTest {

    @Test
    @DisplayName("Crear permiso CREATE")
    void createPermission() {
        Permission perm = Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        assertThat(perm.getAction().getCode()).isEqualTo("CREATE");
        assertThat(perm.getResource().getCode()).isEqualTo("PATIENT");
    }

    @Test
    @DisplayName("Crear permiso READ")
    void readPermission() {
        Permission perm = Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        assertThat(perm.getAction().getCode()).isEqualTo("READ");
    }

    @Test
    @DisplayName("Crear permiso UPDATE")
    void updatePermission() {
        Permission perm = Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        assertThat(perm.getAction().getCode()).isEqualTo("UPDATE");
    }

    @Test
    @DisplayName("Crear permiso DELETE")
    void deletePermission() {
        Permission perm = Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT));
        assertThat(perm.getAction().getCode()).isEqualTo("DELETE");
    }

    @Test
    @DisplayName("Crear permiso personalizado")
    void customPermission() {
        Permission perm = Permission.of(
                ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT),
                ActionCatalog.of(ActionCatalog.BasicAction.CANCEL)
        );
        assertThat(perm.getCode()).isEqualTo("CANCEL_APPOINTMENT");
    }
}
