package com.example.ClinicaDefinitiva.infrastructure.security.config;

import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Aspecto que intercepta métodos anotados con @RequiresPermission
 * y valida usando el AuthorizationService del dominio
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

        // Obtener usuario autenticado
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // Construir contexto de seguridad
        Permission permission = Permission.of(
               ResourceCatalog.of(requiresPermission.resource()),
               ActionCatalog.of(  requiresPermission.action())
        );

        SecurityContext context = SecurityContext
                .builder(permission, userDetails.getId())
                .build();

        // Validar con cada rol activo (al menos uno debe permitir)
        boolean hasPermission = userDetails.getRols().stream()
                .anyMatch(rol -> authorizationService.isAuthorized(rol.getId(), context));

        if (!hasPermission) {
            throw new AccessDeniedException(
                    "User does not have permission: " + permission.getCode()
            );
        }

        // Proceder con la ejecución
        return joinPoint.proceed();
    }
}
