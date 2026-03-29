
---

# ADR-003: Ubicación de SecurityPolicy (interfaz en aplicación, implementación en infraestructura)

- **Fecha**: 2026-01-31
- **Estado**: Aprobado
- **Categoría**: Arquitectura

---

## Problema

El Application Service necesita acceder a parámetros de seguridad como el número máximo de intentos fallidos y la duración del bloqueo. Inicialmente se consideró ubicar la clase `SecurityPolicy` en el paquete `util` del dominio, pero esto genera problemas:

- El dominio no debe depender de configuraciones externas ni de detalles técnicos como `@Value` o `application.properties`.
- Si `SecurityPolicy` se coloca en dominio, se rompe la separación de responsabilidades y se mezcla lógica de negocio con infraestructura.
- Se requiere una forma clara de inyectar estos valores desde configuración externa sin acoplar el dominio.

---

## Decisión

Definir `SecurityPolicy` como **interfaz en la capa de aplicación** y ubicar su implementación (`ConfigurableSecurityPolicy`) en la capa de infraestructura.

**Regla:**
- La interfaz `SecurityPolicy` vive en la capa de aplicación, como contrato que el Application Service consume.
- La implementación concreta (`ConfigurableSecurityPolicy`) se ubica en infraestructura y obtiene los valores desde configuración externa (`application.properties` o `application.yml`).
- El Application Service depende únicamente de la interfaz, nunca de la implementación.

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Ubicar `SecurityPolicy` en dominio | Mezcla reglas de negocio con configuración externa, rompe la pureza del dominio. |
| Crear un paquete `util` en aplicación para la clase concreta | No separa contrato de implementación, dificulta reemplazo futuro. |
| Definir constantes directamente en Application Service | Duplica lógica y obliga a recompilar para cambiar valores. |

---

## Consecuencias

### Ganamos
- Separación clara de responsabilidades: contrato en aplicación, implementación en infraestructura.
- Flexibilidad para cambiar la política de seguridad sin tocar el Application Service.
- Configuración externa centralizada y fácil de modificar.

### Perdemos
- Una capa adicional de indirección (interfaz + implementación).
- Dependencia explícita de Spring en la implementación (`@Component`, `@Value`).

---

## Implementación

Interfaz en aplicación:

```java
public interface SecurityPolicy {
    int getMaxAttempts();
    Duration getLockDuration();
}
```

Implementación en infraestructura:

```java
@Component
public class ConfigurableSecurityPolicy implements SecurityPolicy {
    private final int maxAttempts;
    private final Duration lockDuration;

    public ConfigurableSecurityPolicy(
        @Value("${security.maxAttempts}") int maxAttempts,
        @Value("${security.lockDurationMinutes}") long lockDurationMinutes
    ) {
        this.maxAttempts = maxAttempts;
        this.lockDuration = Duration.ofMinutes(lockDurationMinutes);
    }

    @Override
    public int getMaxAttempts() { return maxAttempts; }

    @Override
    public Duration getLockDuration() { return lockDuration; }
}
```

Uso en Application Service:

```java
Outcome<UserIdentity> outcome = user.recordFailedLogin(
    Instant.now(),
    securityPolicy.getMaxAttempts(),
    securityPolicy.getLockDuration()
);
```

---

## Notas adicionales

- Esta decisión asegura que el dominio se mantiene limpio y libre de dependencias externas.
- El Application Service solo conoce la interfaz, lo que permite reemplazar la implementación si se requiere otra fuente de configuración (ej. base de datos, servicio remoto).
- Se mantiene consistencia con otras políticas como `UserDeactivationPolicy`, que también se inyectan en el Application Service.



