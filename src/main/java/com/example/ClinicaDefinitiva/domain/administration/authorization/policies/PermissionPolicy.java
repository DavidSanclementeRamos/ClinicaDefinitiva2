package com.example.ClinicaDefinitiva.domain.administration.authorization.policies;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;

/**
 * Política de permisos - evaluada en cadena de responsabilidad
 */
public interface PermissionPolicy {
    /**
     * Evalúa si el rol tiene permiso en el contexto dado
     * @return true si permite, false si deniega
     */
    boolean isAllowed(Rol rol, SecurityContext context);

    /**
     * Prioridad de evaluación (mayor = más prioritaria)
     * Orden: Ownership (300) > Sector/Specialty (200) > RBAC Base (100)
     */
    default int getPriority() {
        return 100;
    }

    /**
     * Indica si esta política aplica al contexto dado
     * Si no aplica, se salta en la evaluación
     */
    default boolean appliesTo(SecurityContext context) {
        return true;
    }
}
