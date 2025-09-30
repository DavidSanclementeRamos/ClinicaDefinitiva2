# Value Object: DateOfBirth

## Propósito

DateOfBirth encapsula la fecha de nacimiento de un actor clínico como una unidad semántica validada. Permite calcular la edad, validar reglas clínicas por operación y evitar ambigüedad en el tratamiento de fechas sensibles. Este VO es fundamental para determinar si un paciente requiere responsable, si un actor puede ser registrado, y para generar métricas clínicas confiables.

## Motivación

La fecha de nacimiento es un dato clínico crítico. Usarla como LocalDate sin validación ni semántica genera errores operativos, ambigüedad en cálculos de edad y degeneración semántica. Este VO permite encapsular la lógica de edad, delegar responsabilidades éticas al dominio y construir un modelo trazable.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-031](../adr/ADR-031.md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class DateOfBirth {

    private final LocalDate value;

    public DateOfBirth(LocalDate value) {
        if (value == null) {
            throw new ClinicalValidationException("La fecha de nacimiento es obligatoria");
        }
        if (value.isAfter(LocalDate.now())) {
            throw new ClinicalValidationException("La fecha de nacimiento no puede ser futura");
        }
        this.value = value;
    }

    public int calculateAge() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    public boolean isMinor(int edadLegal) {
        return calculateAge() < edadLegal;
    }

    public boolean isEligible(int edadMinima) {
        return calculateAge() >= edadMinima;
    }

    public LocalDate getValue() {
        return value;
    }
}
```
Reglas clínicas encapsuladas

- La fecha no puede ser nula ni futura.
- Se puede calcular la edad exacta en años.
- Determina si el actor es menor de edad (isMinor()).
- Valida elegibilidad para registro clínico (isEligible()).

Uso en el modelo

- Paciente → DateOfBirth determina si requiere responsable.
- Responsable, Odontólogo, Secretario → validan edad mínima para registro.
- Se integra con Age para encapsular lógica dual (fecha + edad).
- Se usa en informes, métricas, trazabilidad y validaciones clínicas.

Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Cálculo de edad confiable y trazable.
- Facilidad para test unitarios.
- Evita ambigüedad en flujos operativos.
- Mejora la integridad clínica del sistema.

Proyección

Este VO será extendido con:

- Métodos para calcular edad en meses o días.
- Integración con Age para validaciones cruzadas.
- Soporte para fechas estimadas o aproximadas (en contextos pediátricos o migratorios).
- Posibilidad de representar fecha de nacimiento desconocida con justificación clínica.

Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
  `