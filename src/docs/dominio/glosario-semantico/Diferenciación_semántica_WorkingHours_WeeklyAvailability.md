

# ADR: Diferencia semántica entre WeeklyAvailability y WorkingHours
---

- Fecha: 2025-10-6
---
## Contexto
En el dominio clínico, necesitamos modelar tanto la disponibilidad práctica de agenda como la jornada laboral formal declarada por el profesional.  
Inicialmente, existía confusión entre los objetos WeeklyAvailability y WorkingHours, ya que ambos parecían representar “horas de trabajo”.  
Además, algunos métodos como cumpleMinimoHoras() estaban ubicados en WeeklyAvailability, lo que generaba ambigüedad semántica.

## Decisión
1. Separación semántica clara:
    - WeeklyAvailability
        - Representa los slots de tiempo disponibles para agendar citas.
        - Pertenece al contexto de la agenda.
        - Es declarativo y estructural, no contiene reglas de negocio.
        - Mantiene el método totalHoras(), que calcula la suma de horas disponibles en los slots.
            - Este método es útil como métrica neutra, sin implicar cumplimiento contractual.

    - WorkingHours
        - Representa la jornada laboral formal declarada por el profesional (ej. 40 horas/semana).
        - Pertenece al contexto del profesional (Dentist).
        - Se le agrega el atributo declaredHoursPerWeek, que refleja el compromiso contractual/ético.
        - Contiene el método isCompliantWith(WeeklyAvailability availability), que compara lo declarado con lo disponible.
            - Este método valida si la agenda cumple con la jornada oficial.

2. Reubicación de métodos:
    - totalHoras() → se mantiene en WeeklyAvailability (métrica neutra).
    - isCompliantWithWorkingHours() → se traslada a WorkingHours, porque la validación de cumplimiento es responsabilidad del VO que representa la jornada declarada, no de la disponibilidad.

## Consecuencias
- Se evita la duplicación semántica entre WeeklyAvailability y WorkingHours.
- WeeklyAvailability queda como objeto declarativo y exhibible.
- WorkingHours se convierte en el punto legítimo de validación contractual.
- El agregado Dentist puede integrar ambos para validar automáticamente el cumplimiento de la jornada.

---

## Ejemplo de Código

WeeklyAvailability
```java
public final class WeeklyAvailability {
private final List<TimeSlot> slots;

    public WeeklyAvailability(List<TimeSlot> slots) {
        this.slots = List.copyOf(slots == null ? List.of() : slots);
    }

    public List<TimeSlot> getSlots() { return slots; }

    public int totalHoras() {
        return slots.stream().mapToInt(TimeSlot::duracionHoras).sum();
    }
}
```

WorkingHours
```java
public record WorkingHours(int declaredHoursPerWeek) {
public WorkingHours {
if (declaredHoursPerWeek <= 0) {
throw new IllegalArgumentException("Las horas declaradas deben ser positivas");
}
}

    public boolean isCompliantWith(WeeklyAvailability availability) {
        return availability.totalHoras() >= declaredHoursPerWeek;
    }
}
```

 Integración en Dentist
```java
public final class Dentist {
private final WorkingHours workingHours;
private final Schedule schedule;

    public Dentist(WorkingHours workingHours, Schedule schedule) {
        this.workingHours = workingHours;
        this.schedule = schedule;
    }

    public boolean isCompliantWithDeclaredWorkingHours() {
        return workingHours.isCompliantWith(schedule.getWeeklyAvailability());
    }
}
```

---

## Importancia
- totalHoras(): permite medir la disponibilidad neta de la agenda.
- isCompliantWith(): asegura la trazabilidad ética y contractual, validando que la agenda respete la jornada declarada.
- declaredHoursPerWeek: introduce un dato explícito y exhibible que diferencia lo prometido de lo ejecutado.

---

## Resumen
- WeeklyAvailability = disponibilidad práctica de agenda.
- WorkingHours = jornada laboral declarada.
- La comparación entre ambos asegura legitimidad ética y trazabilidad contractual.
  `

---

