
# ADR-11 (Actores): Separación de Value Objects para el estado de User y Dentist

- Estado: Aprobado
- Fecha: 2025-10-25
- Autor: David Stiven Sanclemente


## Contexto
En el modelo clínico-administrativo, tanto User como Dentist son actores relevantes, pero representan dimensiones distintas del sistema:
- User modela la identidad digital (credenciales, acceso, roles).
- Dentist representa una entidad del dominio clínico con reglas operativas propias.

Inicialmente, se consideró que el estado de Dentist podía derivarse del estado de User.  
Sin embargo, esto generaba ambigüedad semántica y acoplamiento innecesario entre el modelo técnico y el modelo de negocio.

## Decisión
Se decidió modelar el estado de User y Dentist como Value Objects separados:

- UserStatus encapsula estados como ACTIVE, INACTIVE, SUSPENDED, PENDING_VERIFICATION, relacionados con acceso al sistema.
- AvailabilityStatus (en el contexto de Dentist) encapsula estados como AVAILABLE, UNAVAILABLE, ON_LEAVE, RETIRED, relacionados con disponibilidad clínica.

Cada VO vive en su respectivo agregado (User y Dentist) y expone métodos semánticos (isActive(), isAvailable()) que permiten expresar reglas de negocio de forma clara y trazable.

## Justificación
- Cohesión semántica: cada VO refleja su propia dimensión del sistema.
- Evita acoplamiento: separa identidad técnica de disponibilidad clínica.
- Flexibilidad: permite que un User esté activo mientras su rol clínico (Dentist) esté inactivo o retirado.
- Evolución legítima: cada VO puede extenderse sin afectar al otro.

## Consecuencias
- Se mejora la claridad y expresividad del modelo clínico-administrativo.
- Se habilita trazabilidad semántica para auditorías y validaciones.
- Se facilita la extensión futura de estados en cada VO.

## Plan de implementación
1. Crear UserStatus en com.clinica.domain.vo.user.
2. Crear AvailabilityStatus en com.clinica.domain.vo.dentist.
3. Definir métodos semánticos (isActive(), isAvailable()).
4. Refactorizar UserModel para usar UserStatus.
5. Refactorizar Dentist para usar AvailabilityStatus.
6. Añadir pruebas unitarias para cada estado.
7. Documentar reglas en docs/dominio/reglas-de-negocio/status.md.

## Ejemplo
```java
public class UserStatus {
private final Status status;

    public boolean isActive() {
        return status == Status.ACTIVE;
    }
}

public class AvailabilityStatus {
private final Status status;

    public boolean isAvailable() {
        return status == Status.AVAILABLE;
    }
}

public class Dentist {
private final User user;
private final AvailabilityStatus status;

    public boolean isOperational() {
        return user.isActive() && status.isAvailable();
    }
}
```

