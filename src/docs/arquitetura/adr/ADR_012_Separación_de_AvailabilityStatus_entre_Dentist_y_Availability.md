# ADR: Separación de Value Objects para `AvailabilityStatus` en `Dentist` y `Availability`

## Estado
Aceptado

## Contexto
El sistema permite modelar la disponibilidad del odontólogo a dos niveles:

1. **Estado operativo del profesional (`Dentist`)**: si está disponible para atender pacientes.
2. **Estado de bloques específicos (`Availability`)**: si un bloque de tiempo está activo, desactivado, en pausa, etc.

Inicialmente se consideró que un único VO `AvailabilityStatus` podría cubrir ambos casos. Sin embargo, esto generaba confusión semántica y dificultaba la trazabilidad de reglas específicas.

## Decisión
Se decidió modelar `AvailabilityStatus` como dos Value Objects separados:

- En `Dentist`, `DentistAvailabilityStatus` representa el estado operativo del profesional (`AVAILABLE`, `ON_LEAVE`, `RETIRED`, etc.).
- En `Availability`, `AvailabilityStatus` representa el estado del bloque (`ACTIVE`, `INACTIVE`, `DISABLED`, etc.).

Cada VO vive en su contexto correspondiente y expone métodos semánticos que permiten validar reglas como:

- “¿El odontólogo está disponible para agendar?”
- “¿Este bloque puede recibir citas?”

## Consecuencias
- Se mejora la claridad semántica del modelo.
- Se permite modelar casos como: “El odontólogo está activo, pero este bloque está desactivado.”
- Se facilita la extensión futura de cada VO sin colisiones semánticas.
- Se mantiene la trazabilidad de reglas de negocio a nivel de entidad y de bloque.

## Ejemplo de colaboración

```java
public class Dentist {
    private AvailabilityStatus status; // Estado operativo

    public boolean isAvailable() {
        return status.isAvailable();
    }
}

public class Availability {
    private AvailabilityStatus status; // Estado del bloque

    public boolean isActive() {
        return status.isActive();
    }
}