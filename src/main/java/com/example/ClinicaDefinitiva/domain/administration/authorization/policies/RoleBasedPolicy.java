package com.example.ClinicaDefinitiva.domain.administration.authorization.policies;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Política RBAC centralizada — define permisos base por rol.
 *
 * RESPONSABILIDAD: responder "¿puede el rol X hacer la acción Y sobre el recurso Z?"
 * sin conocer contexto (sector, ownership, especialidad).
 *
 * Esta es la ÚNICA clase que debes modificar para cambios RBAC.
 *
 * MATRIZ DE PERMISOS:
 * ┌─────────────────────┬─────────────┬──────────┬─────────┬──────────┬──────────────┐
 * │ Recurso             │ ADMINISTRATOR│RECEPTIONIST│ DENTIST │ PATIENT  │  GUARDIAN    │
 * ├─────────────────────┼─────────────┼──────────┼─────────┼──────────┼──────────────┤
 * │ ROLE                │ CRUD + admin │   -      │   -     │    -     │      -       │
 * │ ASSIGNMENT          │ CRUD completo│   -      │   -     │    -     │      -       │
 * │ USER_IDENTITY       │ CRUD + admin │   -      │   -     │    -     │      -       │
 * │ PATIENT             │ R           │ CRU      │  R(*)   │  RU(*)   │   RU(*)      │
 * │ DENTIST             │ RD          │ CRUD(*) │   -     │    -     │      -       │
 * │ GUARDIAN            │ R           │ CRU      │   -     │    -     │   RU(*)      │
 * │ RECEPTIONIST        │ R           │ R(self)  │   -     │    -     │      -       │
 * │ APPOINTMENT         │ R           │ CRUD+ops │ R+COMPL │   R      │    CR        │
 * │ AVAILABILITY        │ R           │   -      │ CRUD    │    -     │      -       │
 * │ SHIFT               │ R           │ CRU      │  RU     │    -     │      -       │
 * │ PROVIDED_SERVICE    │ R           │ RU       │ CRU(*) │   R      │      R       │
 * │ INVOICE             │ CRUD        │ CRU      │  R      │   R      │      R       │
 * │ PAYMENT             │ CRUD        │ R        │  R      │   R      │      R       │
 * │ RATE                │ CRUD        │  -       │  R      │    -     │      -       │
 * │ CONTRACT            │ CRUD        │  -       │   -     │    -     │      -       │
 * │ JOURNAL_ENTRY       │ CRUD        │  -       │   -     │    -     │      -       │
 * │ COMPANY             │ CRUD        │  -       │   -     │    -     │      -       │
 * │ ADMINISTRATIVE_REPORT│ CRUD       │ R        │  R      │    -     │      -       │
 * └─────────────────────┴─────────────┴──────────┴─────────┴──────────┴──────────────┘
 *
 * (*) Restricciones ABAC adicionales evaluadas por DefaultAuthorizationHelper:
 *   - DENTIST READ PATIENT: solo pacientes asignados (SpecialtyBasedPolicy)
 *   - PATIENT/GUARDIAN RU PATIENT: solo sus propios datos (OwnershipPolicy)
 *   - GUARDIAN RU GUARDIAN: solo sus propios datos (OwnershipPolicy)
 *   - RECEPTIONIST DELETE DENTIST: solo si es del sector RRHH (SectorBasedPolicy)
 *   - DENTIST CRU PROVIDED_SERVICE: solo servicios de su especialidad (SpecialtyBasedPolicy)
 */
public class RoleBasedPolicy implements PermissionPolicy {

    private static final Map<RolEnum, Set<Permission>> ROLE_PERMISSIONS = new HashMap<>();

    static {
        configureAdministratorPermissions();
        configureReceptionistPermissions();
        configureDentistPermissions();
        configurePatientPermissions();
        configureGuardianPermissions();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ADMINISTRATOR: gestiona usuarios, roles y asignaciones; puede ver todo
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureAdministratorPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // ── Seguridad / gestión de acceso ──────────────────────────────────
        // Roles
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.VIEW_ROLE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CREATE_CUSTOM_ROLE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CLONE_ROLE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.ADD_PERMISSION)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.REMOVE_PERMISSION)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.SET_PERMISSIONS)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CHECK_PERMISSION)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.DELETE_ROLE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.ACTIVATE_ROLE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.DEACTIVATE_ROLE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.SUSPEND_ROLE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.MARK_DELETED_ROLE)));

        // Asignaciones
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.ASSIGNMENT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.ASSIGNMENT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.ASSIGNMENT)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.ASSIGNMENT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.CREATE_TEMPORARY)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.REVOKE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.REVOKE_ALL)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.EXTEND_ASSIGNMENT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.VIEW_ASSIGNMENT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.IS_ACTIVE_AT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.UPDATE_PRIMARY)));

        // Identidades de usuario
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.USER_IDENTITY)));
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.USER_IDENTITY)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.USER_IDENTITY)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.DEACTIVATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.SUSPEND)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.REACTIVATE)));

        // ── Actores (solo lectura; los crea el flujo de registro) ──────────
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.DENTIST)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.DENTIST)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.GUARDIAN)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.RECEPTIONIST)));

        // ── Facturación y contabilidad (gestión completa) ──────────────────
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.APPROVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.REVERSE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.POST)));

        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.PAYMENT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PAYMENT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.PAYMENT)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.PAYMENT)));

        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.RATE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.RATE)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.RATE)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.RATE)));

        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.CONTRACT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.CONTRACT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.CONTRACT)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.CONTRACT)));

        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.REVERSE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.POST)));

        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.COMPANY)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.COMPANY)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.COMPANY)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.COMPANY)));

        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT)));

        // ── Agenda (solo lectura; la opera RECEPTIONIST) ───────────────────
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.SHIFT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.AVAILABILITY)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));

        ROLE_PERMISSIONS.put(RolEnum.ADMINISTRATOR, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RECEPTIONIST: opera agenda, gestiona actores del sector, facturación básica
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureReceptionistPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Gestión de actores
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.DENTIST)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.DENTIST)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.DENTIST)));
        // DELETE DENTIST: tiene restricción ABAC → solo sector RRHH (SectorBasedPolicy)
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.DENTIST)));
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.GUARDIAN)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.GUARDIAN)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.GUARDIAN)));

        // Gestión de agenda
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CANCEL)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.RESCHEDULE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.SCHEDULE)));

        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.SHIFT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.SHIFT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.SHIFT)));

        // Servicios (lectura y actualización administrativa)
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));

        // Facturación básica
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PAYMENT)));

        // Reportes administrativos (solo lectura)
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT)));

        ROLE_PERMISSIONS.put(RolEnum.RECEPTIONIST, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DENTIST: gestiona sus pacientes asignados, disponibilidad y servicios
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureDentistPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Lectura de pacientes asignados (ABAC: SpecialtyBasedPolicy filtrará por asignación)
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PATIENT)));

        // Servicios clínicos propios (ABAC: SpecialtyBasedPolicy valida especialidad)
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));

        // Gestión de citas y disponibilidad propia
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.COMPLETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.MARK_AS_NO_SHOW)));
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.AVAILABILITY)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.AVAILABILITY)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.AVAILABILITY)));
        permissions.add(Permission.delete(r(ResourceCatalog.BasicResource.AVAILABILITY)));

        // Turnos propios
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.SHIFT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.SHIFT)));

        // Consulta de facturación
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.RATE)));

        // Reportes
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT)));

        ROLE_PERMISSIONS.put(RolEnum.DENTIST, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PATIENT: auto-gestión con restricciones ABAC de ownership
    // ─────────────────────────────────────────────────────────────────────────
    private static void configurePatientPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Auto-gestión (ABAC: OwnershipPolicy valida que sea su propio registro)
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.PATIENT)));

        // Citas propias
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.APPOINTMENT)));

        // Consulta de servicios y facturación propios
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PAYMENT)));

        ROLE_PERMISSIONS.put(RolEnum.PATIENT, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GUARDIAN: gestión de datos propios y de pacientes bajo tutela
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureGuardianPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Datos propios (ABAC: OwnershipPolicy)
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.GUARDIAN)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.GUARDIAN)));

        // Pacientes bajo tutela (ABAC: OwnershipPolicy via guardianship)
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PATIENT)));
        permissions.add(Permission.update(r(ResourceCatalog.BasicResource.PATIENT)));

        // Citas de tutelados
        permissions.add(Permission.create(r(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.APPOINTMENT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CANCEL)));

        // Consulta de servicios y facturación
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.INVOICE)));
        permissions.add(Permission.read(r(ResourceCatalog.BasicResource.PAYMENT)));

        ROLE_PERMISSIONS.put(RolEnum.GUARDIAN, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // API pública
    // ─────────────────────────────────────────────────────────────────────────

    public static Set<Permission> getPermissionsFor(RolEnum rolEnum) {
        return ROLE_PERMISSIONS.getOrDefault(rolEnum, Set.of());
    }

    @Override
    public boolean isAllowed(Rol rol, SecurityContext context) {
        Set<Permission> permissions = ROLE_PERMISSIONS.get(rol.getRolEnum());
        if (permissions == null) return false;
        return permissions.contains(context.getPermission());
    }

    @Override
    public int getPriority() {
        return 100;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers privados para reducir verbosidad
    // ─────────────────────────────────────────────────────────────────────────
    private static ResourceCatalog r(ResourceCatalog.BasicResource resource) {
        return ResourceCatalog.of(resource);
    }

    private static ActionCatalog a(ActionCatalog.BasicAction action) {
        return ActionCatalog.of(action);
    }
}