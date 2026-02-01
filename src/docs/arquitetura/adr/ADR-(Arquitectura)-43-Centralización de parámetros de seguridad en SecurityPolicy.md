Aquí tienes el ADR redactado siguiendo **exactamente la plantilla que me compartiste**, aplicado al caso de **centralizar parámetros de seguridad en una `SecurityPolicy` inyectada**:

---

# ADR-002: Centralización de parámetros de seguridad en SecurityPolicy

- **Fecha**: 2026-01-31
- **Estado**: Aprobado
- **Categoría**: Arquitectura

---

## Problema

En la capa de aplicación, el método `recordFailedLogin` necesita parámetros de seguridad como el número máximo de intentos fallidos y la duración del bloqueo. Inicialmente, estos valores se pasaban como parámetros sueltos o constantes dentro del código, lo que generaba acoplamiento y dificultaba su modificación.

Esto plantea varios problemas:
- Los valores de seguridad no estaban centralizados ni configurables.
- Cambiar la política requería modificar código en múltiples lugares.
- No existía una forma clara de inyectar estos parámetros desde configuración externa.

---

## Decisión

Centralizar los parámetros de seguridad en un componente `SecurityPolicy` gestionado por Spring.  
Este componente expone los valores `maxAttempts` y `lockDuration` como propiedades configurables, inyectadas desde `application.properties` o `application.yml`.

**Regla:**
- Los valores de seguridad (`maxAttempts`, `lockDuration`) nunca se pasan como parámetros sueltos en la interfaz de caso de uso.
- Siempre se obtienen desde el bean `SecurityPolicy` inyectado en el Application Service.
- La configuración externa define los valores, permitiendo cambios sin modificar código.

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Definir constantes en la clase Application Service | Duplica lógica y obliga a recompilar para cambiar valores. |
| Pasar los valores como parámetros en la interfaz de caso de uso | Expone detalles técnicos en el contrato de negocio y rompe la separación de capas. |
| Configuración dispersa en múltiples clases | Genera inconsistencias y dificulta el mantenimiento. |

---

## Consecuencias

### Ganamos
- Centralización de parámetros de seguridad en un único componente.
- Configuración externa y flexible sin necesidad de recompilar.
- Separación clara entre lógica de negocio y detalles técnicos.

### Perdemos
- Una capa adicional de indirección (hay que inyectar `SecurityPolicy`).
- Dependencia explícita de Spring para la inyección de valores.

---

## Implementación

```java
@Component
public class SecurityPolicy {
    private final int maxAttempts;
    private final Duration lockDuration;

    public SecurityPolicy(
        @Value("${security.maxAttempts}") int maxAttempts,
        @Value("${security.lockDurationMinutes}") long lockDurationMinutes
    ) {
        this.maxAttempts = maxAttempts;
        this.lockDuration = Duration.ofMinutes(lockDurationMinutes);
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public Duration getLockDuration() {
        return lockDuration;
    }
}
```

Uso en el Application Service:

```java
Outcome<UserIdentity> outcome = user.recordFailedLogin(
    Instant.now(),
    securityPolicy.getMaxAttempts(),
    securityPolicy.getLockDuration()
);
```

---

## Notas adicionales

- Esta decisión aplica de forma general a todos los casos donde se requieran parámetros de seguridad.
- Se mantiene consistencia con otras políticas (ej. `UserDeactivationPolicy`) que también se inyectan en el Application Service.
- Documentar esta decisión asegura que futuros cambios en la política de seguridad se hagan en un solo lugar.



