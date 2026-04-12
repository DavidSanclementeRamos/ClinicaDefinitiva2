

## Guía: Manejo de intentos de login en Spring Security

### Contexto
El sistema necesita registrar intentos de login exitosos y fallidos para:
- Bloquear cuentas tras múltiples intentos fallidos.
- Resetear el contador en logins exitosos.
- Mantener trazabilidad de seguridad.

### Decisión
Separar responsabilidades:
- **CustomUserDetailsService**: solo carga usuario y roles.
- **UserIdentity**: encapsula reglas de negocio (`recordFailedLogin`, `recordSuccessfulLogin`).
- **AuthenticationSuccessHandler / AuthenticationFailureHandler**: invocan los métodos de dominio según el resultado del login.

### Ejemplo de código

#### Handler de login fallido
```java
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserIdentityRepository userIdentityRepository;

    public CustomAuthenticationFailureHandler(UserIdentityRepository userIdentityRepository) {
        this.userIdentityRepository = userIdentityRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String email = request.getParameter("username");
        userIdentityRepository.findByEmail(email).ifPresent(user -> {
            user.recordFailedLogin(Instant.now(), 3, Duration.ofMinutes(15));
            userIdentityRepository.save(user);
        });

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login fallido");
    }
}
```

#### Handler de login exitoso
```java
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserIdentityRepository userIdentityRepository;

    public CustomAuthenticationSuccessHandler(UserIdentityRepository userIdentityRepository) {
        this.userIdentityRepository = userIdentityRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserIdentity user = userIdentityRepository.findById(userDetails.getId()).orElseThrow();

        user.recordSuccessfulLogin(Instant.now());
        userIdentityRepository.save(user);

        response.sendRedirect("/home");
    }
}
```

#### Configuración en Spring Security
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationFailureHandler failureHandler,
                          CustomAuthenticationSuccessHandler successHandler) {
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout");
    }
}
```

---

### Consecuencias
- No se crean endpoints manuales para registrar intentos: el flujo de Spring Security lo maneja automáticamente.
- La lógica de negocio queda centralizada en `UserIdentity`.
- Los handlers garantizan que siempre se invoquen los métodos correctos en cada caso.



