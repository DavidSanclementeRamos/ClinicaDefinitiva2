# ADR 0010: Cambios en el módulo de acceso y diseño del agregado UserIdentity
**Fecha:** 21/01/2026

---

## Contexto
El módulo de acceso se está migrando de una arquitectura MVC a una arquitectura hexagonal. Actualmente `UserIdentity` existe como agregado pero en la práctica se ha usado como POJO técnico; Spring Security ha manejado muchas responsabilidades (hashing, sesiones, bloqueo, verificación). Se requiere decidir cómo repartir responsabilidades entre **dominio**, **capa de aplicación** e **infraestructura** para evitar mezcla de responsabilidades, garantizar invariantes de negocio y mantener la integridad en entornos distribuidos.

### Problemas que motivan la decisión
- Reglas de negocio dispersas entre controladores, servicios y filtros de seguridad.
- Riesgo de inconsistencias y saltos de validaciones si Spring Security queda como única fuente de verdad.
- Necesidad de trazabilidad, pruebas unitarias del dominio y manejo claro de errores (p. ej. `NotFound`).
- Requisitos operativos: bloqueo por intentos, verificación obligatoria, auditoría y tokens JWT.

---

## Decisión
Adoptar el siguiente diseño y responsabilidades para el módulo de acceso:

### Dominio (agregado `UserIdentity`)
- Mantener `UserIdentity` como **agregado rico** que encapsula invariantes de negocio: bloqueo por intentos, verificación de cuenta, estado activo/inactivo, reglas de edición del agregado y roles lógicos.
- Exponer operaciones de dominio (métodos con `Outcome` o excepciones de dominio) como `recordFailedLogin`, `recordSuccessfulLogin`, `verify`, `canPerformSensitiveAction`, `editProfileAccordingToRules`.
- No incluir hashing, creación/validación de tokens ni envío de correos dentro del agregado.

### Capa de Aplicación
- Definir *use cases* explícitos como puertos de entrada: `AuthenticateUserUseCase`, `RegisterUserUseCase`, `EditUserUseCase`, `GetUserByIdUseCase`, `ListUsersUseCase` (solo si el negocio lo requiere).
- Orquestar llamadas entre repositorios (puertos de salida), `PasswordEncoder` (infra), y el agregado. La capa de aplicación decide cuándo lanzar excepciones de aplicación (`UserNotFoundException`, `BusinessRuleViolationException`).
- Emitir eventos de dominio (p. ej. `UserLockedEvent`, `UserVerifiedEvent`) tras cambios relevantes para que adaptadores infra reaccionen.

### Infraestructura
- Spring Security se encarga de lo técnico: `PasswordEncoder`, filtros JWT, `AuthenticationProvider`, gestión de sesiones y revocación de tokens.
- Repositorios JPA/DB aplican constraints físicos (unique email) y `@Version` para optimistic locking.
- Adaptadores traducen entre `UserEntity` y `UserIdentity` y entre excepciones infra y excepciones de aplicación.

### Integración
- Flujo de autenticación:  
  `Controller -> AuthenticationService (use case) -> UserRepositoryPort -> PasswordEncoder -> agregado.recordXxx() -> repo.save() -> TokenServicePort.createToken()`.
- El `AuthenticationProvider` o un listener delega la orquestación al `AuthenticateUserUseCase` en vez de implementar lógica de negocio propia.

---

## Consecuencias

### Beneficios
- Invariantes de negocio protegidas por el agregado, evitando rutas que las eludan.
- Mayor testabilidad: pruebas unitarias del dominio y pruebas de integración claras para el flujo de seguridad.
- Claridad de responsabilidades: infra técnica separada de reglas de negocio.
- Mejor trazabilidad y auditoría mediante eventos de dominio.

### Costes y riesgos
- Mayor cantidad de código de orquestación en la capa de aplicación.
- Necesidad de coordinación entre repositorios y adaptadores para evitar condiciones de carrera; mitigado con `@Version` y eventos idempotentes.
- Complejidad operativa inicial al desplegar JWT, revocación y throttling distribuido (Redis) si se requiere.

---

## Alternativas consideradas
1. **Dejar `UserIdentity` como POJO y delegar todo a Spring Security**
    - Ventaja: menos código de dominio; rápido de implementar.
    - Desventaja: reglas de negocio dispersas, riesgo de inconsistencias, difícil auditoría y pruebas de negocio.

2. **Mover todas las reglas a la capa de aplicación**
    - Ventaja: dominio simple.
    - Desventaja: pérdida de encapsulación de invariantes; mayor probabilidad de violaciones por múltiples orquestadores.

3. **Híbrido con validaciones duplicadas (dominio + infra)**
    - Ventaja: redundancia defensiva.
    - Desventaja: duplicación de lógica y riesgo de desincronización.

**Decisión adoptada:** mantener el agregado rico y delegar lo técnico a infra, por balance entre seguridad, claridad y mantenibilidad.

---

## Implementación y migración

### Fases
1. **Definición de contratos**
    - Crear puertos de entrada (`AuthenticateUserUseCase`, `RegisterUserUseCase`, `EditUserUseCase`, `GetUserByIdUseCase`) y puertos de salida (`UserRepositoryPort`, `TokenServicePort`, `EmailSenderPort`).

2. **Refactor del dominio**
    - Convertir `UserIdentity` a agregado rico: eliminar setters públicos para invariantes, añadir `recordFailedLogin`, `recordSuccessfulLogin`, `verify`, `editAccordingToRules`, `isLocked`.
    - Introducir VOs: `Email`, `HashedPassword`, `UserId`.
    - Añadir `version` para optimistic locking.

3. **Capa de aplicación**
    - Implementar `AuthenticationService` que orquesta el flujo de login usando `PasswordEncoder` y los métodos del agregado.
    - Implementar `RegistrationService` que valida unicidad (`repo.existsByEmail`), crea agregado con `HashedPassword` y emite `UserRegisteredEvent`.

4. **Adaptadores de infraestructura**
    - `UserRepositoryAdapter` mapea `UserEntity` ↔ `UserIdentity` y aplica constraints DB.
    - `JwtTokenProvider` implementa `TokenServicePort`.
    - `CustomAuthenticationProvider` delega a `AuthenticateUserUseCase` o usa el servicio de aplicación.

5. **Eventos y listeners**
    - Publicar eventos de dominio tras persistencia; listeners infra envían emails, revocan tokens o actualizan caches.

6. **Migración de datos**
    - Script para transformar contraseñas si es necesario; si ya están hasheadas con BCrypt, mantenerlas; si no, forzar rehash en primer login.
    - Población de `version` y `createdAt/lastLoginAt` con valores por defecto.

7. **Pruebas y despliegue**
    - Unit tests del dominio.
    - Integration tests del flujo de autenticación (login fallido → bloqueo; login exitoso → token y `lastLoginAt`).
    - Canary deploy para validar comportamiento en producción.

### Pruebas clave
- `recordFailedLogin` incrementa contador y bloquea tras N intentos.
- `recordSuccessfulLogin` resetea contador y actualiza `lastLoginAt`.
- `canPerformSensitiveAction` falla si no verificado o bloqueado.
- Endpoint `/auth/login` integra con JWT y persiste cambios en agregado.

### Rollback
- Mantener compatibilidad con el esquema de DB previo; si falla, revertir adaptadores y reactivar el flujo antiguo de Spring Security mientras se corrige el agregado.

---

## Notas operativas y recomendaciones
- Documentar claramente qué hace cada capa y publicar un diagrama de flujo de autenticación.
- Configurar métricas y alertas para intentos de login, bloqueos y errores de sincronización con IdP.
- Usar Redis para throttling global si la aplicación es multi‑instancia; mantener contador lógico en dominio para invariantes de negocio.
- Revisar políticas de seguridad (rotación de claves JWT, expiración, revocación) con el equipo de operaciones.  
