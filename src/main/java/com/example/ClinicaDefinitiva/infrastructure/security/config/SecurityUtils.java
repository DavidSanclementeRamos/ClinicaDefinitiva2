package com.example.ClinicaDefinitiva.infrastructure.security.config;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Utilidades de contexto de seguridad para acceso estático al usuario autenticado.
 *
 * IMPORTANTE: getCurrentUserSector() solo debe llamarse cuando el usuario
 * en sesión es RECEPTIONIST. Llamarlo para otros roles lanzará una excepción
 * porque no existirán en la tabla de recepcionistas.
 *
 * Ejemplo de uso correcto:
 *   if (SecurityUtils.hasRole(RolEnum.RECEPTIONIST)) {
 *       String sector = SecurityUtils.getCurrentUserSector();
 *   }
 */
public final class SecurityUtils {

    private static ReceptionRepository receptionistRepository;

    public static void setReceptionistRepository(ReceptionRepository repository) {
        receptionistRepository = repository;
    }

    private SecurityUtils() {}

    public static UserIdentityId getCurrentUserId() {
        return getDetails().getId();
    }

    public static RolId getCurrentUserRolId() {
        return getDetails().getActiveRolId();
    }

    /**
     * Verifica si el usuario en sesión tiene un rol específico.
     * Úsalo ANTES de llamar getCurrentUserSector() para evitar excepciones.
     *
     * @param rolEnum Rol a verificar
     * @return true si el usuario tiene ese rol activo
     */
    public static boolean hasRole(RolEnum rolEnum) {
        return getDetails().getRols().stream()
                .anyMatch(r -> r.getRolEnum() == rolEnum);
    }

    /**
     * Obtiene el sector del usuario en sesión.
     *
     * PRECONDICIÓN: el usuario debe ser RECEPTIONIST.
     * Si no lo es, lanza IllegalStateException para evitar comportamiento silencioso erróneo.
     *
     * @return sector del receptionist
     * @throws IllegalStateException si el usuario no es RECEPTIONIST o no existe en la tabla
     */
    public static String getCurrentUserSector() {
        // CORREGIDO: verificar que el usuario es RECEPTIONIST antes de buscar en BD
        if (!hasRole(RolEnum.RECEPTIONIST)) {
            throw new IllegalStateException(
                    "getCurrentUserSector() solo puede llamarse para usuarios con rol RECEPTIONIST. " +
                    "Verifica con hasRole(RolEnum.RECEPTIONIST) antes de llamar este método."
            );
        }

        UserIdentityId userIdentityId = getCurrentUserId();

        Receptionist receptionist = receptionistRepository
                .findByUserId(userIdentityId)
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró el Receptionist para el usuario con ID: " + userIdentityId.value() +
                        ". El usuario tiene rol RECEPTIONIST pero no existe en la tabla de recepcionistas."
                ));

        return receptionist.getSector().toString();
    }

    /**
     * Obtiene el sector del usuario si es RECEPTIONIST, o empty si no lo es.
     * Versión segura que no lanza excepción — preferible en lógica condicional.
     *
     * @return Optional con el sector, o empty si el usuario no es RECEPTIONIST
     */
    public static Optional<String> getCurrentUserSectorIfReceptionist() {
        if (!hasRole(RolEnum.RECEPTIONIST)) {
            return Optional.empty();
        }
        return receptionistRepository
                .findByUserId(getCurrentUserId())
                .map(r -> r.getSector().toString());
    }

    private static CustomUserDetails getDetails() {
        return (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}