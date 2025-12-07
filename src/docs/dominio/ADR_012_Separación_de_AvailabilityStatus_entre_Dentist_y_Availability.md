# ADR-10 (Dominio): Separación de Value Objects para AvailabilityStatus en Dentist y Availability

- Estado: Aprobado
- Fecha: 2025-10-30
- Autor: David

## Contexto
El sistema permite modelar la disponibilidad del odontólogo a dos niveles:

1. Estado operativo del profesional (Dentist): si está disponible para atender pacientes.
2. Estado de bloques específicos (Availability): si un bloque de tiempo está activo, desactivado, en pausa, etc.

Inicialmente, se consideró que un único VO AvailabilityStatus podría cubrir ambos casos.  
Sin embargo, esto generaba confusión semántica y dificultaba la trazabilidad de reglas específicas.

## Decisión
Se decidió modelar AvailabilityStatus como dos Value Objects separados:

- En Dentist, DentistAvailabilityStatus representa el estado operativo del profesional (AVAILABLE, ON_LEAVE, RETIRED, etc.).
- En Availability, AvailabilityStatus representa el estado del bloque (ACTIVE, INACTIVE, DISABLED, etc.).

Cada VO vive en su contexto correspondiente y expone métodos semánticos que permiten validar reglas como:
- “¿El odontólogo está disponible para agendar?”
- “¿Este bloque puede recibir citas?”

## Justificación
- Claridad semántica: cada VO refleja su propio nivel de disponibilidad.
- Evita confusión: separa estados del profesional y de bloques.
- Flexibilidad: permite modelar casos como “El odontólogo está activo, pero este bloque está desactivado.”
- Evolución legítima: cada VO puede extenderse sin afectar al otro.
- Trazabilidad: reglas de negocio se mantienen claras a nivel de entidad y de bloque.

## Consecuencias
- Se mejora la claridad semántica del modelo.
- Se habilita trazabilidad clínica y operativa.
- Se facilita la extensión futura de cada VO sin colisiones semánticas.

## Plan de implementación
1. Crear DentistAvailabilityStatus en com.clinica.domain.vo.dentist.
2. Crear AvailabilityStatus en com.clinica.domain.vo.availability.
3. Definir métodos semánticos (isAvailable(), isActive()).
4. Refactorizar Dentist para usar DentistAvailabilityStatus.
5. Refactorizar Availability para usar AvailabilityStatus.
6. Añadir pruebas unitarias para cada estado.
7. Documentar reglas en docs/dominio/reglas-de-negocio/availability.md.

## Ejemplo
```java
public class DentistAvailabilityStatus {
private final Status status;

    public boolean isAvailable() {
        return status == Status.AVAILABLE;
    }
}

public class AvailabilityStatus {
private final Status status;

    public boolean isActive() {
        return status == Status.ACTIVE;
    }
}

public class Dentist {
private DentistAvailabilityStatus status;

    public boolean isAvailable() {
        return status.isAvailable();
    }
}

public class Availability {
private AvailabilityStatus status;

    public boolean isActive() {
        return status.isActive();
    }
}
```

## Relación con otros ADR
- ADR-09 (Dominio): Separación de Value Objects para el estado de User y Dentist.
- ADR-07 (Dominio): Delegación semántica para validar estado de usuario en agendamiento.
- ADR-08 (Dominio): Refactorización semántica con canScheduleAt(...).  
  