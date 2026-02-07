package com.example.ClinicaDefinitiva.domain.administration.authorization.policies;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.num.RolEnum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Política RBAC centralizada - define permisos base por rol
 * Esta es la ÚNICA clase que necesitas modificar para cambios RBAC
 */
public class RoleBasedPolicy implements PermissionPolicy {

    private static final Map<RolEnum, Set<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        configureReceptionistPermissions();
        configureDentistPermissions();
        configurePatientPermissions();
        configureGuardianPermissions();
    }

    private static void configureReceptionistPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Gestión de actores
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST)));
        permissions.add(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST))); // Con restricción ABAC por sector
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)));

        // Gestión de agenda
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT),ActionCatalog.of(ActionCatalog.BasicAction.CANCEL)));
        permissions.add(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT), ActionCatalog.of(ActionCatalog.BasicAction.RESCHEDULE)));
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.SHIFT)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.SHIFT)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.SHIFT)));

        // Servicios (solo lectura y actualización administrativa)
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));

        // Facturación básica
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PAYMENT)));

        ROLE_PERMISSIONS.put(RolEnum.RECEPTIONIST, permissions);
    }

    private static void configureDentistPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Lectura de pacientes asignados
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)));

        // Gestión de servicios clínicos
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));

        // Gestión de citas y disponibilidad
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.of(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT), ActionCatalog.of(ActionCatalog.BasicAction.COMPLETE)));
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.AVAILABILITY)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.AVAILABILITY)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.AVAILABILITY)));
        permissions.add(Permission.delete(ResourceCatalog.of(ResourceCatalog.BasicResource.AVAILABILITY)));

        // Turnos propios
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.SHIFT)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.SHIFT)));

        // Consulta de facturación
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.RATE)));

        ROLE_PERMISSIONS.put(RolEnum.DENTIST, permissions);
    }

    private static void configurePatientPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Auto-gestión
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT))); // Con restricción ownership
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT))); // Con restricción ownership

        // Consulta de citas propias
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));

        // Consulta de servicios y facturación
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)));

        ROLE_PERMISSIONS.put(RolEnum.PATIENT, permissions);
    }

    private static void configureGuardianPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Gestión de datos propios
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)));
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.GUARDIAN)));

        // Gestión de pacientes bajo tutela
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT))); // Con restricción ownership
        permissions.add(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT))); // Con restricción ownership

        // Gestión de citas de tutelados
        permissions.add(Permission.create(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.APPOINTMENT)));

        // Consulta de servicios y facturación
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.read(ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE)));




        ROLE_PERMISSIONS.put(RolEnum.GUARDIAN, permissions);
    }

    public static Set<Permission> getPermissionsFor(RolEnum rolEnum) {
        return ROLE_PERMISSIONS.getOrDefault(rolEnum, Set.of());
    }


    @Override
    public boolean isAllowed(Rol rol, SecurityContext context) {
        Set<Permission> permissions = ROLE_PERMISSIONS.get(rol.getRolEnum());
        if (permissions == null) {
            return false;
        }
        return permissions.contains(context.getPermission());
    }

    @Override
    public int getPriority() {
        return 100; // Prioridad base
    }
}