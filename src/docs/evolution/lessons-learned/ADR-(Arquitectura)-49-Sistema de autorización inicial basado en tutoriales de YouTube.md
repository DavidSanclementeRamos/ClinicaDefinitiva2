# ADR-49 (Arquitectura): Sistema de autorización inicial basado en tutoriales de YouTube

- **Estado:** Lección aprendida ([ADR-(Arquitectura)-14](../../architecture/decisions/arch/ADR-%28Arquitectura%29-14-Separaci%C3%B3n%20identidad%20del%20usuario%20con%20roles%20y%20permisos.md))
- **Fecha:** 2026-02-09
- **Autor:** David Stiven Sanclemente

## Por qué se documenta esto ahora

Este ADR se crea **retrospectivamente** durante la limpieza de código legacy y la implementación de tests unitarios. Al eliminar clases obsoletas (`RolesEntity`, `PermissionEntityxd`, `RolesFactory`, `CustomUserDetailsService`), me di cuenta de que la narrativa arquitectónica estaba incompleta: los ADRs existentes (ADR-01, ADR-14) documentan mejoras, pero nunca explicaron **desde dónde partimos**.

Sin este contexto, las decisiones arquitectónicas actuales parecen cambios radicales sin justificación clara. Este documento completa la historia mostrando las limitaciones del diseño original y por qué fue necesario evolucionar.

---

## Contexto del diseño inicial (marzo-agosto 2025)

Al iniciar el proyecto odontológico tras varios cursos de Spring Boot, necesitaba implementar autenticación y autorización rápidamente para avanzar con las funcionalidades del negocio. Mi principal fuente de conocimiento eran **tutoriales de YouTube** sobre Spring Security.

En ese momento, consideraba que estos tutoriales representaban "buenas prácticas de la industria". No tenía experiencia real para discernir entre soluciones didácticas y arquitecturas escalables.

---

## Decisión arquitectónica inicial

Se implementó un sistema de autorización simple siguiendo el patrón más común de tutoriales:

### Estructura de clases

```java
@Entity
public class Usuario {
    @Id
    private long id;
    private String nombreUsuario;
    private String contrasena;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_roles")
    private Set<RolesEntity> roles = new HashSet<>();
    
    // Campos técnicos de Spring Security
    private boolean isEnabled;
    private boolean accountNoExpired;
    private boolean accountNoLocked;
    private boolean credentialNoExpired;
}

@Entity
public class RolesEntity {
    @Id
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private Roles roleEnum; // ADMINISTRADOR, ODONTOLOGO, PACIENTE, etc.
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Permisos> permissionList; // GET_USUARIO_ID, PUT_USUARIOS, etc.
    
    private String description;
    private boolean isDefault;
    private boolean isEditable;
    private boolean isDeletable;
    private Estado status;
}

// Factory con permisos hardcodeados
public final class RolesFactory {
    private static final Map<Roles, RolesEntity> roles = new HashMap<>();
    
    static {
        roles.put(Roles.ADMINISTRADOR, new RolesEntity(
            "Administrador con acceso completo",
            true, true, true,
            List.of(
                Permisos.GET_HORARIO_POR_FECHA,
                Permisos.GET_HORARIOS_DISPONIBLE,
                Permisos.POST_HORARIOS,
                Permisos.PUT_HORARIOS,
                Permisos.DELETE_HORARIO,
                Permisos.GET_USUARIO_ID,
                Permisos.POST_USUARIOS,
                // ... 50+ permisos más hardcodeados
            ),
            Roles.ADMINISTRADOR,
            Estado.ACTIVO
        ));
        // Repetir para cada rol...
    }
}

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario u = userRepo.findByNombreUsuarioIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no existe"));
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (RolesEntity role : u.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()));
            role.getPermissionList().forEach(perm ->
                authorities.add(new SimpleGrantedAuthority(perm.name()))
            );
        }
        
        return new org.springframework.security.core.userdetails.User(
            u.getNombreUsuario(),
            u.getContrasena(),
            u.isEnabled(),
            u.isAccountNoExpired(),
            u.isCredentialNoExpired(),
            u.isAccountNoLocked(),
            authorities
        );
    }
}

@RestController
@RequestMapping("/api/v1/usuarios")
@PreAuthorize("denyAll()")
public class UsuarioController {
    
    @PreAuthorize("hasAuthority('GET_USUARIO_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> findId(@PathVariable long id) {
        return ResponseEntity.ok(usuarioService.findId(id));
    }
    
    @PreAuthorize("hasAuthority('PUT_USUARIOS')")
    @PutMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> update(
        @PathVariable long id,
        @Valid @RequestBody UpdateUsuarioDto updateUsuarioDto
    ) {
        return ResponseEntity.ok(usuarioService.update(id, updateUsuarioDto));
    }
}
```

### Características del diseño

1. **Usuario como entidad única**: mezclaba identidad técnica (credenciales, estados de cuenta) con roles de negocio
2. **RolesFactory estática**: roles y permisos hardcodeados en código Java
3. **Permisos como enums**: `Permisos.GET_USUARIO_ID`, `PUT_USUARIOS`, etc.
4. **Autorización declarativa**: `@PreAuthorize("hasAuthority('PERMISO')")` en cada endpoint
5. **Sin contexto de negocio**: validación binaria (tiene permiso = sí/no), sin considerar ownership, sector, relaciones

---

## Justificación en ese momento

### Por qué parecía correcto

- **Simplicidad aparente**: "funciona en 30 minutos"
- **Confianza en tutoriales**: "si está en YouTube, debe ser la forma correcta"
- **Suficiencia inicial**: cubría autenticación HTTP Basic y control básico de acceso
- **Familiaridad**: Spring Security manejaba todo "mágicamente"
- **Avance rápido**: permitía enfocarse en lógica de negocio (turnos, pacientes, etc.)

### Falta de contexto técnico

No sabía que:
- Los tutoriales enseñan patrones didácticos, no arquitecturas escalables
- RBAC puro es insuficiente para sistemas con reglas de negocio complejas
- Mezclar identidad y autorización viola Single Responsibility
- Hardcodear permisos en factories dificulta auditoría y evolución
- `@PreAuthorize` sin contexto de negocio es una validación incompleta

---

## Limitaciones detectadas (que llevaron a ADR-14 y arquitectura actual)

### 1. Alto acoplamiento identidad-autorización

```java
// Usuario contenía TODO
Usuario {
    - nombreUsuario, contrasena           // Identidad
    - isEnabled, accountNoExpired         // Estados técnicos
    - Set<RolesEntity> roles              // Autorización de negocio
    - Estado estado                       // Estado clínico
}
```

**Problema**: cambiar reglas de autorización afectaba la entidad de identidad.

### 2. Rigidez extrema en permisos

```java
// RolesFactory.java - 200+ líneas de permisos hardcodeados
roles.put(Roles.ADMINISTRADOR, new RolesEntity(
    "Administrador...",
    List.of(
        Permisos.GET_HORARIO_POR_FECHA,
        Permisos.GET_HORARIOS_DISPONIBLE,
        Permisos.GET_HORARIO_POR_ODONTOLOGO_ID,
        // ... 50+ permisos más
    )
));
```

**Problema**: agregar un nuevo permiso requería modificar código Java, recompilar y redesplegar.

### 3. Ausencia total de contexto de negocio

```java
@PreAuthorize("hasAuthority('PUT_PACIENTES')")
@PutMapping("/{id}")
public ResponseEntity<ReadPacienteDto> update(...) {
    // ¿Puede CUALQUIER usuario con PUT_PACIENTES editar CUALQUIER paciente?
    // ¿No importa si el paciente es suyo o de otro odontólogo?
    // ¿No importa el sector del recepcionista?
}
```

**Problema**: no había validación de:
- Ownership (solo el dueño puede editar)
- Sector (recepcionistas solo ven su área)
- Relaciones (guardianes solo gestionan sus pacientes)

### 4. Imposibilidad de auditar decisiones

No se registraba:
- ¿Quién autorizó la acción?
- ¿Con qué rol?
- ¿Por qué se permitió o denegó?
- ¿Cuál fue el contexto (recurso, ownership, sector)?

### 5. No reflejaba complejidad real del dominio

El sistema parecía un CRUD simple cuando en realidad tenía reglas como:
- "Un odontólogo solo ve turnos de su agenda"
- "Un recepcionista de ORTODONCIA no puede agendar en ENDODONCIA"
- "Un guardián solo puede cancelar turnos de pacientes bajo su responsabilidad"

Estas reglas **no existían en el código**.

---

## Por qué funcionó inicialmente

- Era suficiente para **validar la idea** del proyecto
- Permitía **aprender Spring Security** en un contexto real
- Cubría el **caso base**: autenticación + permisos binarios
- No había usuarios reales ni reglas de negocio complejas implementadas

---

## Por qué dejó de funcionar

Al avanzar el proyecto, surgieron necesidades reales:

1. **Caso Recepcionista**: "Solo puede ver turnos de su sector (RECEPCIÓN, ORTODONCIA, ENDODONCIA)"
    - Imposible con `@PreAuthorize("hasAuthority('GET_TURNOS')")`

2. **Caso Paciente**: "Solo puede editar su propio perfil, no perfiles de otros pacientes"
    - Imposible sin validar ownership

3. **Caso Guardián**: "Solo puede gestionar pacientes bajo su responsabilidad"
    - Imposible sin validar relación guardián-paciente

4. **Auditoría**: "¿Quién cambió el estado de este turno y por qué se permitió?"
    - Imposible sin registrar contexto de autorización

---

## Aprendizaje clave

> **Los tutoriales de YouTube enseñan Spring Security, no diseñan sistemas de autorización para dominios complejos.**

Este proyecto es evidencia de esa evolución:
- **Nació** de un tutorial de YouTube (curso Java EE)
- **Se refactorizó** con patrones de Spring Boot (también de tutoriales)
- **Se transformó** en una arquitectura hexagonal con ABAC contextual (aprendizaje autodidacta + documentación oficial)

La diferencia entre un desarrollador junior y uno con criterio no es "saber Spring Security", es **saber cuándo el patrón del tutorial es insuficiente y diseñar una solución propia**.

---

## Relación con otros ADR

- **Superado por:** [ADR-14: Separación identidad del usuario con roles y permisos](ADR-14-Separación-identidad-del-usuario-con-roles-y-permisos.md)
- **Estrategia actual:** [Guía: Estrategia de autorización con SecurityContext](guia-estrategia-autorización-security_context.md)
- **Contexto general:** [ADR-01: Migración progresiva a arquitectura hexagonal](ADR-01-Migración-progresiva-a-arquitectura-hexagonal.md)
- **Historia del proyecto:** [STORY.md](STORY.md)

---

## Evidencia de código eliminado

Durante la limpieza de código (febrero 2026), se eliminaron:
- `RolesEntity.java` (entidad persistence con permisos)
- `PermissionEntityxd.java` (entidad no utilizada)
- `RolesFactory.java` (factory estática con permisos hardcodeados)
- `CustomUserDetailsService.java` (convertía roles a GrantedAuthority)
- `@PreAuthorize("hasAuthority('...')")` en 40+ endpoints

Estas clases impedían la ejecución de tests unitarios y representaban deuda técnica acumulada.

---

## Reflexión final

Documentar este diseño inicial **no es admitir un error**, es **mostrar crecimiento profesional**.

Cualquier desarrollador que trabaje con Spring Security empezará con este patrón porque es lo que enseñan los tutoriales. La diferencia está en:
1. Identificar sus limitaciones cuando el dominio crece
2. Diseñar una solución propia basada en necesidades reales
3. Documentar la evolución para que otros aprendan del proceso

