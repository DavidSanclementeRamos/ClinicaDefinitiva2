# ADR-37 (Arquitectura): Arquitectura hexagonal para módulo de acceso

- **Fecha**: 2026-01-21
- **Estado**: Aprobado
- **Categoría**: Arquitectura
- **Dividido desde**: ADR-002

---

## Problema

El módulo de acceso (autenticación, autorización, gestión de usuarios) estaba implementado con arquitectura MVC tradicional, donde:

- Controladores contenían lógica de negocio
- Servicios mezclaban reglas de negocio con acceso a datos
- Spring Security manejaba todo directamente sin capa de dominio
- Difícil de testear unitariamente
- Reglas de negocio dispersas entre múltiples capas

**Riesgo principal:** Inconsistencias si Spring Security queda como única fuente de verdad, saltándose validaciones de negocio.

## Decisión

Migrar el módulo de acceso a **arquitectura hexagonal** con clara separación de capas:

### Estructura de capas

```
┌─────────────────────────────────────────────────────────────┐
│  ADAPTADORES DE ENTRADA                                     │
│  (REST Controllers, Security Filters)                       │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  PUERTOS DE ENTRADA (Use Cases)                             │
│  - AuthenticateUserUseCase                                  │
│  - RegisterUserUseCase                                      │
│  - EditUserUseCase                                          │
│  - GetUserByIdUseCase                                       │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  CAPA DE APLICACIÓN (Application Services)                 │
│  - Orquesta casos de uso                                    │
│  - Coordina repositorios y domain services                  │
│  - Emite eventos de dominio                                 │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  DOMINIO                                                    │
│  - Agregado: UserIdentity (ver ADR-021)                    │
│  - Domain Services: UserAccessValidator, etc.              │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  PUERTOS DE SALIDA                                          │
│  - UserRepositoryPort                                       │
│  - TokenServicePort                                         │
│  - EmailSenderPort                                          │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  ADAPTADORES DE SALIDA                                      │
│  (JPA Repository, JWT Provider, SMTP Client)                │
└─────────────────────────────────────────────────────────────┘
```

### Definición de puertos

**Puertos de Entrada:**
```java
public interface AuthenticateUserUseCase {
    AuthenticationResult execute(AuthenticateCommand command);
}

public interface RegisterUserUseCase {
    UserDto execute(RegisterUserCommand command);
}

public interface EditUserUseCase {
    UserDto execute(EditUserCommand command);
}

public interface GetUserByIdUseCase {
    Optional<UserDto> execute(UserId userIdentityId);
}
```

**Puertos de Salida:**
```java
public interface UserRepositoryPort {
    Optional<UserIdentity> findById(UserId id);
    Optional<UserIdentity> findByEmail(Email email);
    UserIdentity save(UserIdentity user);
    boolean existsByEmail(Email email);
}

public interface TokenServicePort {
    String createToken(UserId userIdentityId, Set<Role> roles);
    Optional<UserId> validateToken(String token);
    void revokeToken(String token);
}

public interface EmailSenderPort {
    void sendVerificationEmail(Email to, VerificationToken token);
    void sendPasswordResetEmail(Email to, ResetToken token);
}
```

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Mantener MVC con servicios gruesos | Lógica de negocio dispersa, difícil de testear |
| CQRS completo | Overkill para módulo de acceso, complejidad innecesaria |
| Delegar todo a Spring Security | Pierde control sobre reglas de negocio, difícil auditoría |

## Consecuencias

### Ganamos
- **Separación clara de responsabilidades:** Cada capa tiene propósito único
- **Testabilidad:** Use cases probables sin infraestructura
- **Independencia de frameworks:** Dominio no depende de Spring
- **Trazabilidad:** Flujo explícito desde controlador hasta repositorio
- **Flexibilidad:** Cambiar JPA por otro ORM sin tocar dominio

### Perdemos
- **Más código de orquestación:** Application Services requieren coordinar múltiples puertos
- **Curva de aprendizaje:** Desarrolladores deben entender hexagonal
- **Boilerplate inicial:** Definir puertos, adaptadores, mappers

## Implementación

### Flujo de autenticación completo

```java
// 1. Controlador REST (Adaptador de entrada)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticateUserUseCase authenticateUser;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        var command = new AuthenticateCommand(
            new Email(request.email()),
            request.password()
        );
        
        AuthenticationResult result = authenticateUser.execute(command);
        
        return ResponseEntity.ok(new AuthResponse(
            result.token(),
            result.expiresAt()
        ));
    }
}

// 2. Application Service (Capa de aplicación)
@Service
@Transactional
public class AuthenticationService implements AuthenticateUserUseCase {
    
    private final UserRepositoryPort userIdentityRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenServicePort tokenService;
    
    @Override
    public AuthenticationResult execute(AuthenticateCommand command) {
        // Obtener usuario
        UserIdentity user = userIdentityRepository.findByEmail(command.email())
            .orElseThrow(() -> new InvalidCredentialsException());
        
        // Validar contraseña
        if (!passwordEncoder.matches(command.password(), user.getHashedPassword())) {
            user.recordFailedLogin(Instant.now());
            userIdentityRepository.save(user);
            throw new InvalidCredentialsException();
        }
        
        // Registrar login exitoso
        user.recordSuccessfulLogin(Instant.now());
        userIdentityRepository.save(user);
        
        // Crear token
        String token = tokenService.createToken(user.getId(), user.getRoles());
        
        return new AuthenticationResult(token, Instant.now().plus(1, ChronoUnit.HOURS));
    }
}

// 3. Adaptador JPA (Adaptador de salida)
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    
    private final JpaUserRepository jpaRepo;
    private final UserMapper mapper;
    
    @Override
    public Optional<UserIdentity> findByEmail(Email email) {
        return jpaRepo.findByEmail(email.value())
            .map(mapper::toDomain);
    }
    
    @Override
    public UserIdentity save(UserIdentity user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaRepo.save(entity);
        return mapper.toDomain(saved);
    }
}
```

## Fases de migración

### Fase 1: Definir contratos
- Crear interfaces de puertos de entrada (use cases)
- Crear interfaces de puertos de salida (repositories, services)
- Definir DTOs y Commands

### Fase 2: Implementar capa de aplicación
- Application Services que implementan use cases
- Orquestación entre repositorios y domain services
- Manejo de transacciones

### Fase 3: Adaptadores de infraestructura
- Adaptar repositorios JPA existentes a puertos de salida
- Implementar TokenServicePort con JWT
- Implementar EmailSenderPort con SMTP

### Fase 4: Integrar con Spring Security
- Ver ADR-022 para detalles de integración
- Delegar autenticación a AuthenticateUserUseCase
- Mantener filtros JWT en infraestructura

### Fase 5: Migración gradual
- Canary deployment
- Validar métricas (latencia, errores)
- Rollback plan si falla

## Relación con otros ADRs

- **Complementado por:**
    - ADR-021: UserIdentity como agregado rico (dominio)
    - ADR-022: Integración Spring Security con dominio (infraestructura)
    - ADR-023: UserAccessValidator como domain service

- **Motiva:**
    - Clara separación entre reglas técnicas y reglas de negocio
    - Testabilidad mejorada del dominio