
# ADR-011: Separación de Value Objects para el estado de `User` y `Dentist`

## Estado
Aceptado

## Contexto
En el modelo clínico-administrativo, tanto `User` como `Dentist` son actores relevantes, pero representan dimensiones distintas del sistema. `User` modela la identidad digital del sistema (credenciales, acceso, roles), mientras que `Dentist` representa una entidad del dominio clínico con reglas operativas propias.

Inicialmente, se consideró que el estado de `Dentist` podría derivarse del estado de `User`. Sin embargo, esto generaba ambigüedad semántica y acoplamiento innecesario entre el modelo técnico y el modelo de negocio.

## Decisión
Se decidió modelar el estado de `User` y `Dentist` como Value Objects separados:

- `UserStatus` encapsula estados como `ACTIVE`, `INACTIVE`, `SUSPENDED`, `PENDING_VERIFICATION`, relacionados con el acceso al sistema.
- `AvailabilityStatus` (en el contexto de `Dentist`) encapsula estados como `AVAILABLE`, `UNAVAILABLE`, `ON_LEAVE`, `RETIRED`, relacionados con la disponibilidad clínica.

Cada VO vive en su respectivo agregado (`User` y `Dentist`) y expone métodos semánticos (`isActive()`, `isAvailable()`, etc.) que permiten expresar reglas de negocio de forma clara y trazable.

## Consecuencias
- Se mejora la cohesión semántica del modelo.
- Se evita acoplamiento entre capas técnicas y de dominio.
- Se permite que un `User` esté activo en el sistema mientras su rol clínico (`Dentist`) esté inactivo o retirado.
- Se facilita la extensión futura de cada VO sin afectar al otro.

## Ejemplo de colaboración

```java
public class Dentist {
    private final User user;
    private final AvailabilityStatus status;

    public boolean isOperational() {
        return user.isActive() && status.isAvailable();
    }
}