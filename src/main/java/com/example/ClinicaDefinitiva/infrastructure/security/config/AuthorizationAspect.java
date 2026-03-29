package com.example.ClinicaDefinitiva.infrastructure.security.config;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Aspecto que intercepta métodos con @RequiresPermission.
 *
 * RESPONSABILIDAD ÚNICA: verificar RBAC base.
 * Pregunta: "¿El tipo de rol del usuario tiene este permiso en la tabla ROLE_PERMISSIONS?"
 *
 * NO hace:
 * - Validación de sector (eso es SectorBasedPolicy en DefaultAuthorizationHelper)
 * - Validación de ownership (eso es OwnershipPolicy en DefaultAuthorizationHelper)
 * - Validación de especialidad (eso es SpecialtyBasedPolicy en DefaultAuthorizationHelper)
 * - Consultas a BD de contexto (sector, owner, etc.)
 *
 * Por qué esta separación:
 * - Este aspecto corre ANTES de entrar al método → no tiene acceso al contexto del recurso
 * - El contexto ABAC (¿de quién es este paciente?, ¿en qué sector?) lo construye el
 *   ApplicationService desde los parámetros del request.
 * - Si este aspecto hiciera ABAC, necesitaría duplicar la lógica de negocio del service.
 */
@Aspect
@Component
public class AuthorizationAspect {

    private final AuthorizationService authorizationService;

    public AuthorizationAspect(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Around("@annotation(requiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint,
                                  RequiresPermission requiresPermission) throws Throwable {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // Construir permiso solicitado
        Permission permission = Permission.of(
                ResourceCatalog.of(requiresPermission.resource()),
                ActionCatalog.of(requiresPermission.action())
        );

        // Contexto mínimo — solo necesitamos el permiso y el userId para RBAC
        SecurityContext context = SecurityContext
                .builder(permission, userDetails.getId())
                .build();

        // Verificar RBAC base: al menos uno de los roles activos debe tener el permiso
        // Usa isAllowedByRole() — NO isAuthorized() — para evitar ABAC aquí
        boolean hasBasePermission = userDetails.getRols().stream()
                .anyMatch(rol -> authorizationService.isAllowedByRole(rol, context));

        if (!hasBasePermission) {
            throw new AccessDeniedException(
                    "Rol no tiene permiso base: " + permission.getCode()
            );
        }

        // RBAC pasó → el ApplicationService hará la validación ABAC con authorizationHelper
        return joinPoint.proceed();
    }
}