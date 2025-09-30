# Value Object: DentistAvailabilityStatus

## Propósito

DentistAvailabilityStatus encapsula el estado de disponibilidad de un odontólogo en un horario clínico específico. Representa una unidad semántica que permite validar si un odontólogo está habilitado para recibir turnos, si su disponibilidad ha sido bloqueada, agotada o modificada por eventos operativos.

Este VO evita el uso de cadenas o enums dispersos que no expresan intención clínica, y permite delegar la lógica de habilitación y trazabilidad al dominio.

## Motivación

La disponibilidad de un odontólogo es un dato clínico operativo que afecta directamente la asignación de turnos, la generación de slots, la planificación de jornadas y la trazabilidad de bloqueos. Validar disponibilidad como texto plano o enum técnico genera ambigüedad, errores operativos y degeneración semántica.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class DentistAvailabilityStatus {

    private final String value;

    public DentistAvailabilityStatus(String value) {
        if (value == null || value.isBlank()) {
            throw new ClinicalValidationException("El estado de disponibilidad no puede estar vacío");
        }

        List<String> estadosValidos = List.of("HABILITADO", "BLOQUEADO", "AGOTADO", "INACTIVO");

        if (!estadosValidos.contains(value.toUpperCase())) {
            throw new ClinicalValidationException("Estado de disponibilidad inválido: " + value);
        }

        this.value = value.toUpperCase();
    }

    public boolean isEnabled() {
        return "HABILITADO".equals(value);
    }

    public boolean isBlocked() {
        return "BLOQUEADO".equals(value);
    }

    public boolean isExhausted() {
        return "AGOTADO".equals(value);
    }

    public boolean isInactive() {
        return "INACTIVO".equals(value);
    }

    public String getValue() {
        return value;
    }
}
```
## Reglas clínicas encapsuladas

- El estado debe ser uno de los valores válidos: HABILITADO, BLOQUEADO, AGOTADO, INACTIVO.
- Permite validar si el odontólogo puede recibir turnos (isEnabled()).
- Permite detectar bloqueos operativos (isBlocked()).
- Permite identificar jornadas agotadas (isExhausted()).
- Permite excluir horarios inactivos (isInactive()).

## Uso en el modelo

- Horario → usa DentistAvailabilityStatus para determinar si puede generar disponibilidades.
- Disponibilidad → hereda el estado del horario y lo puede modificar por eventos operativos.
- TurnoService → consulta el estado para validar asignación de turnos.
- AgendaService → filtra horarios según estado para planificación clínica.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Trazabilidad de bloqueos y agotamientos.
- Mejora la integridad operativa del sistema.
- Evita ambigüedad en flujos de asignación.

## Proyección

Este VO será extendido con:

- Métodos para justificar cambios de estado (motivo, usuario, timestamp).
- Integración con eventos operativos (ausencias, feriados, urgencias).
- Soporte para estados temporales o condicionales.
- Posibilidad de representar estados personalizados por clínica.

Relación con ADR

- ADR-02: Implementación estratégica de VO
- ADR-01: Migración a arquitectura hexagonal
  `