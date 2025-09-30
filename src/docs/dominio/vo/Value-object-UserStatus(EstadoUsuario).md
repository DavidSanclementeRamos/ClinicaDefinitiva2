# Value Object: UserStatus

## Propósito

UserStatus encapsula el estado operativo de un usuario dentro del sistema clínico. Representa una unidad semántica que permite validar si un usuario está habilitado para autenticarse, operar, recibir notificaciones o ser auditado. Este VO evita el uso de banderas booleanas dispersas y permite delegar la lógica de habilitación y trazabilidad al dominio.

## Motivación

El estado de un usuario afecta directamente su acceso, visibilidad, permisos y trazabilidad. Usar múltiples booleanos (isEnabled, accountNonExpired, accountNonLocked, credentialsNonExpired) sin encapsulación genera ambigüedad, duplicación y degeneración semántica. Este VO permite representar el estado como una entidad ética, coherente y validable.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class UserStatus {

    private final boolean isEnabled;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;

    public UserStatus(boolean isEnabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired) {
        this.isEnabled = isEnabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isActive() {
        return isEnabled && accountNonExpired && accountNonLocked && credentialsNonExpired;
    }

    public boolean isSuspended() {
        return !isEnabled || !accountNonLocked;
    }

    public boolean isExpired() {
        return !accountNonExpired || !credentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
}
```
## Reglas clínicas encapsuladas

- El usuario está activo solo si todas las condiciones son verdaderas.
- Se puede detectar suspensión por bloqueo o deshabilitación.
- Se puede detectar expiración por cuenta o credenciales.
- Permite validar acceso, trazabilidad y visibilidad operativa.

## Uso en el modelo

- Usuario → usa UserStatus como parte de su perfil de seguridad.
- SecurityService, LoginService, AuditService → consultan UserStatus para validar operaciones.
- Se utiliza en autenticación, autorización, auditoría y notificaciones.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad operativa del sistema.
- Evita ambigüedad en flujos de seguridad.
- Permite trazabilidad ética del estado de usuario.

## Proyección

Este VO será extendido con:

- Métodos para justificar cambios de estado (motivo, timestamp, actor).
- Integración con eventos de seguridad (intentos fallidos, bloqueos automáticos).
- Soporte para estados temporales o condicionales.
- Posibilidad de representar estados personalizados por clínica o rol.

 ## Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
  `