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
 * ┌─────────────────────┬─────────────┬──────────┬─────────┬──────────┬──────────────┐--------------
 * │ Recurso             │ ADMINISTRATOR│RECEPTIONIST│ DENTIST │ PATIENT  │  GUARDIAN    │
 * ├─────────────────────┼─────────────┼──────────┼─────────┼──────────┼──────────────┤_______________
 * │ ROLE                │ CRUD + admin │   -      │   -     │    -     │      -       │
 * │ ASSIGNMENT          │ CRUD completo│   -      │   -     │    -     │      -       │
 * │ USER_IDENTITY       │ CRUD + admin │   -      │   -     │    -     │      -       │
 * │ PATIENT             │ R           │ CRU      │  R(*)   │  RU(*)   │   RU(*)      │
 * │ DENTIST             │ R          │ CRUD(*) │   -     │    -     │      -       │
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
 * └─────────────────────┴─────────────┴──────────┴─────────┴──────────┴──────────────┘____________
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
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CREATE_CUSTOM)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CLONE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.ADD)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.REMOVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.SET_PERMISSIONS)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.CHECK)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.ACTIVATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.DEACTIVATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.SUSPEND)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ROLE), a(ActionCatalog.BasicAction.MARK_DELETED)));

        // Asignaciones
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.CREATE_TEMPORARY)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.REVOKE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.REVOKE_ALL)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.EXTEND)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.IS_ACTIVE_AT)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.IS_CURRENTLY_ACTIVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.UPDATE_PRIMARY)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ASSIGNMENT), a(ActionCatalog.BasicAction.DELETE)));

        // Identidades de usuario
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.DEACTIVATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.SUSPEND)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.USER_IDENTITY), a(ActionCatalog.BasicAction.ACTIVATE)));

        // ── Actores (solo lectura; los crea el flujo de registro) ──────────
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RECEPTIONIST), a(ActionCatalog.BasicAction.READ)));

        // ── Facturación y contabilidad (gestión completa) ──────────────────
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.APPROVE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.REVERSE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.POST)));

        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.DELETE)));

        // RATE
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.DELETE)));

        // CONTRACT
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.CONTRACT), a(ActionCatalog.BasicAction.DELETE)));

        // JOURNAL_ENTRY
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.REVERSE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.JOURNAL_ENTRY), a(ActionCatalog.BasicAction.POST)));

        // COMPANY
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.COMPANY), a(ActionCatalog.BasicAction.DELETE)));

        // ADMINISTRATIVE_REPORT
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.DELETE)));

        // ── Agenda (solo lectura; la opera RECEPTIONIST) ───────────────────
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));

        ROLE_PERMISSIONS.put(RolEnum.ADMINISTRATOR, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RECEPTIONIST: opera agenda, gestiona actores del sector, facturación básica
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureReceptionistPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Gestión de actores
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.UPDATE)));
        // DELETE DENTIST: tiene restricción ABAC → solo sector RRHH (SectorBasedPolicy)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.DENTIST), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.UPDATE)));

        // Gestión de agenda
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.DELETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CANCEL)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.RESCHEDULE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.SCHEDULE)));

        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.UPDATE)));

        // Servicios (lectura y actualización administrativa)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.UPDATE)));

        // Facturación básica
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.READ)));

        // Reportes administrativos (solo lectura)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.READ)));

        ROLE_PERMISSIONS.put(RolEnum.RECEPTIONIST, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DENTIST: gestiona sus pacientes asignados, disponibilidad y servicios
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureDentistPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Lectura de pacientes asignados (ABAC: SpecialtyBasedPolicy filtrará por asignación)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));

        // Servicios clínicos propios (ABAC: SpecialtyBasedPolicy valida especialidad)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.UPDATE)));

        // Gestión de citas y disponibilidad propia
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.COMPLETE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.MARK_AS_NO_SHOW)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.UPDATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.AVAILABILITY), a(ActionCatalog.BasicAction.DELETE)));

        // Turnos propios
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.SHIFT), a(ActionCatalog.BasicAction.UPDATE)));

        // Consulta de facturación
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.RATE), a(ActionCatalog.BasicAction.READ)));

        // Reportes
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT), a(ActionCatalog.BasicAction.READ)));

        ROLE_PERMISSIONS.put(RolEnum.DENTIST, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PATIENT: auto-gestión con restricciones ABAC de ownership
    // ─────────────────────────────────────────────────────────────────────────
    private static void configurePatientPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Auto-gestión (ABAC: OwnershipPolicy valida que sea su propio registro)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.UPDATE)));

        // Citas propias
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));

        // Consulta de servicios y facturación propios
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.READ)));

        ROLE_PERMISSIONS.put(RolEnum.PATIENT, permissions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GUARDIAN: gestión de datos propios y de pacientes bajo tutela
    // ─────────────────────────────────────────────────────────────────────────
    private static void configureGuardianPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // Datos propios (ABAC: OwnershipPolicy)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.GUARDIAN), a(ActionCatalog.BasicAction.UPDATE)));

        // Pacientes bajo tutela (ABAC: OwnershipPolicy via guardianship)
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PATIENT), a(ActionCatalog.BasicAction.UPDATE)));

        // Citas de tutelados
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CREATE)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.APPOINTMENT), a(ActionCatalog.BasicAction.CANCEL)));

        // Consulta de servicios y facturación
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PROVIDED_SERVICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.INVOICE), a(ActionCatalog.BasicAction.READ)));
        permissions.add(Permission.of(r(ResourceCatalog.BasicResource.PAYMENT), a(ActionCatalog.BasicAction.READ)));

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