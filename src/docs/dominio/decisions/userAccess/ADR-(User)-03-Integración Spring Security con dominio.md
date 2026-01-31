# ADR-03: Integración Spring Security con dominio

- **Fecha**: 2026-01-21
- **Estado**: Aprobado
- **Categoría**: Arquitectura
- **Dividido desde**: ADR-002

---

## Problema

Spring Security maneja aspectos técnicos de autenticación/autorización (hashing, JWT, sesiones, filtros).

El dominio (UserIdentity) maneja reglas de negocio (bloqueo por intentos, verificación, estado).

No está claro:
- ¿Quién tiene la fuente de verdad?
- ¿Cómo evitar que Spring Security bypasee validaciones de dominio?
- ¿Dónde se genera el hash de contraseña?
- ¿Dónde se valida la contraseña?
- ¿Cómo se integran los filtros JWT con los use cases?

## Decisión

**Separación clara de responsabilidades:**

### Spring Security → Técnico
- `PasswordEncoder`: Hashear y validar contraseñas
- `JwtTokenProvider`: Crear y validar tokens JWT
- Filtros de seguridad: `JwtAuthenticationFilter`
- Configuración de endpoints públicos/privados
- Manejo de sesiones (stateless con JWT)

### Dominio → Reglas de negocio
- `UserIdentity`: Estado, bloqueo, verificación
- `canPerformSensitiveAction()`: Validación de elegibilidad
- `recordFailedLogin()`, `recordSuccessfulLogin()`: Lógica de intentos

### Application Service → Orquestación
- Coordina Spring Security + Dominio
- Delega hashing a `PasswordEncoder`
- Delega validación de contraseña a `PasswordEncoder`
- Invoca métodos del agregado para registrar eventos
- Emite eventos de dominio

## Arquitectura de integración

```
┌──────────────────────────────────────────────────────────────┐
│  CAPA DE PRESENTACIÓN                                        │
│                                                              │
│  POST /auth/login                                            │
│       │                                                      │
│       └─→ JwtAuthenticationFilter (Spring Security)         │
│                │                                             │
│                └─→ AuthenticationManager                    │
│                         │                                    │
└────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌──────────────────────────────────────────────────────────────┐
│  INTEGRACIÓN (Custom AuthenticationProvider)                │
│                                                              │
│  DomainAuthenticationProvider                               │
│       │                                                      │
│       └─→ AuthenticateUserUseCase (Aplicación)             │
│                │                                             │
└────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌──────────────────────────────────────────────────────────────┐
│  CAPA DE APLICACIÓN                                          │
│                                                              │
│  AuthenticationService                                       │
│       │                                                      │
│       ├─→ UserRepository.findByEmail()                      │
│       │                                                      │
│       ├─→ PasswordEncoder.matches() (Spring Security)       │
│       │        │                                             │
│       │        └─→ BCrypt hash validation                   │
│       │                                                      │
│       ├─→ user.recordFailedLogin() (Dominio)                │
│       │   or user.recordSuccessfulLogin()                   │
│       │                                                      │
│       └─→ JwtTokenProvider.createToken() (Spring Security)  │
│                                                              │
└────────────────────────────────────────────────────────────┘
```

## Implementación

### 1. Custom AuthenticationProvider

```java
@Component
public class DomainAuthenticationProvider implements AuthenticationProvider {
    
    private final AuthenticateUserUseCase authenticateUser;
    
    @Override
    public Authentication authenticate(Authentication authentication) 
            throws AuthenticationException {
        
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        try {
            // Delegar a use case del dominio
            AuthenticationResult result = authenticateUser.execute(
                new AuthenticateCommand(new Email(email), password)
            );
            
            // Convertir a formato Spring Security
            return new UsernamePasswordAuthenticationToken(
                result.userId(),
                null,
                result.authorities()
            );
            
        } catch (InvalidCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        } catch (UserNotEligibleException e) {
            throw new DisabledException(e.getMessage());
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
            .isAssignableFrom(authentication);
    }
}
```

### 2. Application Service con PasswordEncoder

```java
@Service
@Transactional
public class AuthenticationService implements AuthenticateUserUseCase {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;  // Spring Security bean
    private final TokenServicePort tokenService;
    
    @Override
    public AuthenticationResult execute(AuthenticateCommand command) {
        Instant now = Instant.now();
        
        // 1. Obtener usuario
        UserIdentity user = userRepository.findByEmail(command.email())
            .orElseThrow(() -> new InvalidCredentialsException());
        
        // 2. Validar contraseña con Spring Security
        boolean passwordMatches = passwordEncoder.matches(
            command.password(), 
            user.getHashedPassword().value()
        );
        
        if (!passwordMatches) {
            // 3. Registrar fallo (lógica de dominio)
            user.recordFailedLogin(now);
            userRepository.save(user);
            throw new InvalidCredentialsException();
        }
        
        // 4. Validar elegibilidad (lógica de dominio)
        Outcome<Void> eligibility = user.canPerformSensitiveAction(now);
        if (!eligibility.isSuccess()) {
            throw new UserNotEligibleException(eligibility.getDetails());
        }
        
        // 5. Registrar éxito (lógica de dominio)
        user.recordSuccessfulLogin(now);
        userRepository.save(user);
        
        // 6. Crear token con Spring Security
        String token = tokenService.createToken(user.getId(), user.getRoles());
        
        return new AuthenticationResult(
            user.getId(),
            token,
            now.plus(1, ChronoUnit.HOURS)
        );
    }
}
```

### 3. Registro de usuario con hashing

```java
@Service
@Transactional
public class RegistrationService implements RegisterUserUseCase {
    
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;  // Spring Security
    private final EmailSenderPort emailSender;
    
    @Override
    public UserDto execute(RegisterUserCommand command) {
        Email email = new Email(command.email());
        
        // 1. Validar unicidad (Application Service)
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        
        // 2. Hashear contraseña con Spring Security
        String rawPassword = command.password();
        String hashed = passwordEncoder.encode(rawPassword);
        
        // 3. Crear agregado (Dominio)
        UserIdentity user = UserIdentity.create(
            email,
            new HashedPassword(hashed),
            Set.of(Role.PATIENT)  // rol por defecto
        );
        
        // 4. Persistir
        UserIdentity saved = userRepository.save(user);
        
        // 5. Enviar email de verificación
        VerificationToken token = VerificationToken.generate();
        emailSender.sendVerificationEmail(email, token);
        
        return mapper.toDto(saved);
    }
}
```

### 4. JWT Filter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final TokenServicePort tokenService;
    
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        String token = extractToken(request);
        
        if (token != null) {
            Optional<UserId> userId = tokenService.validateToken(token);
            
            if (userId.isPresent()) {
                // Autenticar en contexto Spring Security
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    userId.get(),
                    null,
                    Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
```

### 5. Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final DomainAuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                .requestMatchers("/auth/login", "/auth/register").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## Eventos de dominio

Emitir eventos tras cambios importantes para desacoplar side-effects:

```java
@Service
@Transactional
public class AuthenticationService implements AuthenticateUserUseCase {
    
    private final ApplicationEventPublisher eventPublisher;
    
    @Override
    public AuthenticationResult execute(AuthenticateCommand command) {
        // ... autenticación ...
        
        user.recordSuccessfulLogin(now);
        userRepository.save(user);
        
        // Emitir evento
        eventPublisher.publishEvent(new UserLoggedInEvent(
            user.getId(),
            now
        ));
        
        return result;
    }
}

// Listener en infraestructura
@Component
public class UserEventListener {
    
    private final AuditLogRepository auditRepo;
    
    @EventListener
    public void onUserLoggedIn(UserLoggedInEvent event) {
        auditRepo.save(new AuditLog(
            event.userId(),
            "USER_LOGIN",
            event.timestamp()
        ));
    }
}
```

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Spring Security maneja todo | Pierde control sobre reglas de negocio (bloqueo, verificación) |
| Dominio hashea contraseñas | El dominio no debe conocer algoritmos de hashing (BCrypt, Argon2) |
| Duplicar validaciones en filtro + dominio | Mantenimiento doble, riesgo de desincronización |

## Consecuencias

### Ganamos
- **Separación clara:** Técnico vs negocio bien diferenciado
- **Flexibilidad:** Cambiar algoritmo de hashing sin tocar dominio
- **Testabilidad:** Dominio probable sin Spring Security
- **Eventos desacoplados:** Side-effects manejados por listeners

### Perdemos
- **Más configuración:** Custom AuthenticationProvider, filtros
- **Coordinación necesaria:** Application Service debe orquestar correctamente
- **Curva de aprendizaje:** Entender integración Spring Security + DDD

## Consideraciones operativas

### Throttling distribuido

Para múltiples instancias, usar Redis:

```java
@Service
public class DistributedThrottlingService {
    
    private final RedisTemplate<String, Integer> redis;
    
    public void recordFailedLogin(Email email) {
        String key = "login_attempts:" + email.value();
        Integer attempts = redis.opsForValue().increment(key);
        
        if (attempts == 1) {
            redis.expire(key, 15, TimeUnit.MINUTES);
        }
        
        if (attempts >= 5) {
            throw new AccountLockedException();
        }
    }
}
```

### Rotación de claves JWT

```java
@Scheduled(cron = "0 0 2 * * *")  // 2 AM diario
public void rotateJwtSigningKey() {
    String newKey = KeyGenerator.generateSecureKey();
    keyVault.storeKey("jwt_signing_key_new", newKey);
    
    // Período de gracia: ambas claves válidas por 24h
    // Después, eliminar clave antigua
}
```

### Revocación de tokens

```java
@Service
public class TokenRevocationService {
    
    private final RedisTemplate<String, String> redis;
    
    public void revokeToken(String token) {
        String jti = extractJti(token);
        Instant expiresAt = extractExpiration(token);
        
        redis.opsForValue().set(
            "revoked_token:" + jti,
            "true",
            Duration.between(Instant.now(), expiresAt)
        );
    }
    
    public boolean isRevoked(String token) {
        String jti = extractJti(token);
        return redis.hasKey("revoked_token:" + jti);
    }
}
```

## Relación con otros ADRs

- **Parte de:**
    - ADR-020: Arquitectura hexagonal para módulo de acceso

- **Usa:**
    - ADR-021: UserIdentity como agregado rico

- **Complementa:**
    - ADR-023: UserAccessValidator (Application Service orquesta ambos)