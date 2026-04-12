# ADR-51 (Arquitectura): Implementación de Authorization Helper Pattern

- **Estado**: Aprobado
- **Autor:** David Stiven Sanclemente


## Contexto

### Problema Identificado
En la implementación actual, la lógica de autorización está duplicada en cada ApplicationService:

```java
// Código duplicado en CADA servicio (10-15 líneas)
Receptionist receptionist = receptionRepository.findByUserId(requesterId)
    .orElseThrow(() -> new BusinessRuleViolationException(
        AuthorizationError.ERR_AUTH_SECTOR_REQUIRED,
        VOContext.AUTHORIZATION
    ));

SecurityContext context = SecurityContext
    .builder(Permission.create(...), requesterId)
    .withSector(receptionist.getSector().Value())
    .build();

if (!authorizationService.isAuthorized(requesterRolId, context)) {
    throw new BusinessRuleViolationException(
        AuthorizationError.ERR_AUTH_PERMISSION_DENIED,
        VOContext.AUTHORIZATION
    );
}
```

### Consecuencias del Problema
1. **Duplicación masiva**: ~15 líneas de código repetido en 19+ servicios
2. **Inconsistencias**: Diferentes servicios construyen SecurityContext de formas ligeramente distintas
3. **Mantenibilidad**: Cualquier cambio en autorización requiere modificar 19+ archivos
4. **Testing**: Imposible probar la lógica de autorización de forma aislada
5. **Auditoría**: No hay un punto central para loggear decisiones de autorización
6. **Violación de principios**:
   - DRY (Don't Repeat Yourself)
   - Single Responsibility Principle
   - Open/Closed Principle

### Políticas de Autorización Actuales
El sistema implementa múltiples políticas ABAC/RBAC:

1. **Sector-Based Policy**: Recepcionistas por sector (RRHH, Contabilidad, etc.)
2. **Ownership Policy**: Usuarios solo acceden a sus propios recursos
3. **Guardianship Policy**: Tutores acceden a recursos de pacientes bajo tutela
4. **Specialty-Based Policy**: Dentistas solo ven servicios de su especialidad
5. **Assignment-Based Policy**: Dentistas solo ven tratamientos asignados

## Decisión

### Implementar Authorization Helper Pattern
Creamos un **helper centralizado** que:

1. **Construye SecurityContext** con todos los atributos ABAC necesarios
2. **Valida autorización** contra el PolicyEngine existente
3. **Audita todas las decisiones** (permitidas y denegadas)
4. **Maneja errores** de forma consistente
5. **Proporciona API fluida** con AuthorizationContext builder

### Diseño de Solución

```java
// En ApplicationService (reducido a 1 línea)
authorizationHelper.authorize(
    requesterId,
    requesterRolId,
    ResourceCatalog.BasicResource.COMPANY,
    ActionCatalog.BasicAction.CREATE,
    AuthorizationContext.builder()
        .withSector() // opcional
        .withOwnership(...) // opcional
        .build()
);
```

### Componentes

#### 1. AuthorizationHelper (Interface)
```java
public interface AuthorizationHelper {
    void authorize(
        UserIdentityId requesterId,
        RolId requesterRolId,
        ResourceCatalog.BasicResource resource,
        ActionCatalog.BasicAction action,
        AuthorizationContext context
    );
}
```

#### 2. DefaultAuthorizationHelper (Implementation)
- Obtiene Receptionist cuando es necesario (sector)
- Construye SecurityContext con atributos ABAC
- Delega a AuthorizationService.isAuthorized()
- Audita decisión (permitida o denegada)
- Lanza BusinessRuleViolationException si denegado

#### 3. AuthorizationContext (Builder)
```java
public class AuthorizationContext {
    private Long resourceId;
    private UserIdentityId resourceOwnerId;
    private Long patientGuardianId;
    private Set<String> dentistSpecialties;
    private Map<String, Object> attributes;
    
    public static Builder builder() { ... }
}
```

#### 4. AuditLogger (Auditoría)
- Registra TODAS las decisiones de autorización
- Incluye: userId, rolId, resource, action, decision (ALLOW/DENY), duration
- Almacena en logs estructurados (JSON)
- Facilita compliance y debugging

## Consecuencias

### Positivas ✅

1. **Reducción de código**: De ~600 líneas a ~50 líneas totales
2. **Consistencia garantizada**: Misma lógica en todos los servicios
3. **Mantenibilidad**: Cambios en un solo lugar
4. **Testing**: Helper probado aisladamente con 100% coverage
5. **Auditoría completa**: Todas las decisiones registradas
6. **Performance**: Posibilidad de cachear decisiones en el helper
7. **Observabilidad**: Métricas de autorización (% denegadas, recursos más accedidos)
8. **Principios SOLID**: Separación de responsabilidades clara

### Negativas ⚠️

1. **Indirección adicional**: Una capa más entre service y AuthorizationService
   - *Mitigación*: Beneficio de abstracción supera el costo
2. **Aprendizaje**: Desarrolladores deben conocer AuthorizationContext
   - *Mitigación*: API intuitiva con ejemplos y documentación

### Neutras ℹ️

1. **No cambia el PolicyEngine**: Sigue siendo el mismo motor de políticas
2. **No cambia las políticas**: RoleBasedPolicy, OwnershipPolicy, etc. siguen igual
3. **No cambia SecurityContext**: Sigue siendo la misma estructura

## Alternativas Consideradas

### Alternativa 1: Mantener código duplicado
- ❌ **Rechazada**: Inmantenible a largo plazo, violaciones de DRY

### Alternativa 2: Aspect-Oriented Programming (AOP)
```java
@CheckAuthorization(resource = "COMPANY", action = "CREATE")
public ReadCompanyDto createCompany(...) { ... }
```
- ❌ **Rechazada**: 
  - Menos explícito (magia oculta)
  - Difícil construir contexto dinámico (ownership, guardianship)
  - Debugging complejo

### Alternativa 3: Helper por política
```java
sectorBasedAuthHelper.authorize(...)
ownershipAuthHelper.authorize(...)
```
- ❌ **Rechazada**: 
  - Muchos helpers (confusión)
  - No permite combinación de políticas
  - Cada service decide qué helper usar (error-prone)

## Ubicación de Archivos

```
src/main/java/com/example/ClinicaDefinitiva/
├── application/
│   ├── service/
│   │   └── shared/
│   │       ├── AuthorizationHelper.java              ← Interface
│   │       └── DefaultAuthorizationHelper.java       ← Implementation
│   └── dto/
│       └── shared/
│           └── AuthorizationContext.java             ← Builder
└── domain/
    └── administration/
        └── authorization/
            ├── service/
            │   └── AuditLogger.java                  ← Interface
            ├── event/
            │   └── AuthorizationAuditEvent.java      ← Event
            └── infrastructure/
                └── DefaultAuditLogger.java           ← Implementation
```


## Referencias

- [OWASP ABAC Guidelines](https://owasp.org/www-community/Access_Control)
- [Martin Fowler - Service Layer Pattern](https://martinfowler.com/eaaCatalog/serviceLayer.html)
- Clean Architecture - Robert C. Martin
- Domain-Driven Design - Eric Evans


---

**Nota**: Este ADR reemplaza el patrón actual de autorización duplicada. Todos los nuevos ApplicationServices DEBEN usar AuthorizationHelper. Los services existentes se migrarán gradualmente.
