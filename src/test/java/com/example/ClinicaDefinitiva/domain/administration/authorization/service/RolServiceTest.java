
package com.example.ClinicaDefinitiva.domain.administration.authorization.service;

import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.RolService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

class RolServiceTest {

    @Test
    void shouldCreateCustomRoleSuccessfully() {
        RolRepository repo = mock(RolRepository.class);
        when(repo.existsByDescription("Custom Role")).thenReturn(false);
        when(repo.save(any(Rol.class))).thenAnswer(inv -> inv.getArgument(0));

        RolService service = new RolService(repo);

        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)));

        Rol rol = service.createCustom(RolEnum.RECEPTIONIST, "Custom Role");

        assertEquals("Custom Role", rol.getDescription());
        assertTrue(rol.isEditable());
        assertTrue(rol.isDeletable());
        assertEquals(RolStatus.ACTIVE, rol.getStatusRol());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsDuplicateOnCreate() {
        RolRepository repo = mock(RolRepository.class);
        when(repo.existsByDescription("Duplicate")).thenReturn(true);

        RolService service = new RolService(repo);

        assertThrows(BusinessRuleViolationException.class, () ->
                service.createCustom(RolEnum.DENTIST, "Duplicate"));
    }

    @Test
    void shouldCloneRoleSuccessfully() {
        RolRepository repo = mock(RolRepository.class);
        when(repo.existsByDescription("Cloned")).thenReturn(false);
        when(repo.save(any(Rol.class))).thenAnswer(inv -> inv.getArgument(0));

        Rol source =  Rol.createDefault(RolEnum.DENTIST, "Source");
        source.addPermission(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));

        RolService service = new RolService(repo);

        Rol cloned = service.cloneRole(source, "Cloned");

        assertEquals("Cloned", cloned.getDescription());
        assertEquals(source.getRolEnum(), cloned.getRolEnum());
        assertTrue(cloned.isEditable());
        assertTrue(cloned.isDeletable());
        assertEquals(RolStatus.ACTIVE, cloned.getStatusRol());
        assertEquals(source.getPermissions().size(), cloned.getPermissions().size());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsDuplicateOnClone() {
        RolRepository repo = mock(RolRepository.class);
        when(repo.existsByDescription("Duplicate")).thenReturn(true);

        Rol source =  Rol.createDefault(RolEnum.DENTIST, "Source");

        RolService service = new RolService(repo);

        assertThrows(BusinessRuleViolationException.class, () ->
                service.cloneRole(source, "Duplicate"));
    }
}

