

# ADR-01 (Turno): Reubicación del agregado Shift en el submódulo Administración/Operativo

- **Fecha**: 2026-01-31
- **Estado**: Aprobado
- **Categoría**: Dominio
- **Autor**: David Stiven Sanclemente 
---

## Problema

El agregado `Shift` modela los turnos de trabajo del personal (dentistas, recepcionistas). Inicialmente se consideró ubicarlo en los módulos `Actor` o `Cita`. Sin embargo, esto generaba problemas de acoplamiento:

- En `Actor` se mezclaba identidad y roles con lógica operativa de turnos.
- En `Cita` se confundía la gestión clínica con la presencia física del personal.

Esto dificultaba la escalabilidad del modelo y la claridad de responsabilidades. Si no se resolvía, el sistema corría el riesgo de tener validaciones dispersas y dependencias cruzadas entre módulos que deberían ser autónomos.

---

## Decisión

Reubicar el agregado `Shift` dentro del submódulo **Administración/Operativo**, convirtiéndolo en el responsable de gestionar turnos, horarios y presencia del personal administrativo y clínico.

**Regla:**  
Siempre que la lógica de negocio involucre turnos, horarios o disponibilidad operativa del personal, se debe consultar el agregado `Shift` desde el submódulo Administración/Operativo.

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Ubicar `Shift` en `Actor` | Mezcla identidad/roles con lógica operativa, rompiendo cohesión. |
| Ubicar `Shift` en `Cita` | Confunde gestión clínica con presencia física, acoplando dos contextos distintos. |
| Crear un módulo independiente | Aumenta complejidad innecesaria, duplicando responsabilidades ya cubiertas por Administración. |

---

## Consecuencias

### Ganamos
- Separación clara de responsabilidades entre módulos.
- Escalabilidad para extender el modelo a vacaciones, licencias y guardias.
- Consistencia: lógica operativa centralizada en un solo agregado.
- Consultas transversales más limpias: `Actor` y `Cita` referencian `ShiftId` sin acoplarse.

### Perdemos
- Mayor dependencia del módulo Administración para validaciones de disponibilidad.
- Posible sobrecarga de consultas si se requieren reportes masivos de turnos.
- Necesidad de coordinar infraestructura para consultas transversales.

---

## Implementación

```java
// Ejemplo de repositorio
public interface ShiftRepository {
    Optional<Shift> findByReceptionistId(ReceptionistId receptionistId);
    Optional<Shift> findByDentistId(DentistId dentistId);
}

// Ejemplo de validación en Domain Service
public class ReceptionistShiftDomainService {
    private final ShiftRepository shiftRepository;

    public Outcome<Void> validateReceptionistHasNoShifts(ReceptionistId receptionistId) {
        List<Shift> shifts = shiftRepository.findByReceptionistId(receptionistId);
        boolean hasActiveShifts = shifts.stream().anyMatch(Shift::isActive);
        if (hasActiveShifts) {
            return Outcome.fail(new OutcomeDetail(
                ReceptionistError.ERR_RECEPTIONIST_ASSIGNED_SHIFTS,
                Severity.INFO,
                Category.ADMINISTRACION));
        }
        return Outcome.ok();
    }
}
```

---

## Notas adicionales

- Este ADR se relaciona directamente con [ADR-(Arquitectura)-42-AggregateBusinessRuleViolationException para violaciones múltiples.md](../../arch/ADR-%28Arquitectura%29-42-AggregateBusinessRuleViolationException%20para%20violaciones%20m%C3%BAltiples.md)(manejo de múltiples violaciones de reglas de negocio).
- La decisión refuerza la separación de responsabilidades entre **Actor (identidad)**, **Cita (agendamiento clínico)** y **Administración/Operativo (turnos)**.
- Futuro: se puede extender `Shift` para manejar licencias, vacaciones y guardias especiales.

