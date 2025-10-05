# Value Object: DentistAvailabilityStatus

## Propósito

DentistAvailabilityStatus encapsula el estado de disponibilidad de un odontólogo en un horario clínico específico. Representa una unidad semántica que permite validar si un odontólogo está habilitado para recibir turnos, si su disponibilidad ha sido bloqueada, agotada o modificada por eventos operativos.

Este VO evita el uso de cadenas o enums dispersos que no expresan intención clínica, y permite delegar la lógica de habilitación y trazabilidad al dominio.

## Motivación

La disponibilidad de un odontólogo es un dato clínico operativo que afecta directamente la asignación de turnos, la generación de slots, la planificación de jornadas y la trazabilidad de bloqueos. Validar disponibilidad como texto plano o enum técnico genera ambigüedad, errores operativos y degeneración semántica.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public final class DentistAvailabilityStatus {
  public enum Status
  {

    AVAILABLE,
    UNAVAILABLE,
    ON_BREAK,
    IN_CONSULTATION,
    OFF_SHIFT,
    ON_CALL,
    SICK_LEAVE,
    VACATION,
    TRAINING,
    ADMIN_TASK


  }

  private Status current;

  private static final EnumMap<Status, Set<Status>> validTransitions = new EnumMap<>(Status.class);

  static {
    validTransitions.put(Status.AVAILABLE, EnumSet.of(Status.IN_CONSULTATION, Status.ON_BREAK, Status.UNAVAILABLE, Status.OFF_SHIFT));
    validTransitions.put(Status.IN_CONSULTATION, EnumSet.of(Status.ON_BREAK, Status.AVAILABLE));
    validTransitions.put(Status.ON_BREAK, EnumSet.of(Status.AVAILABLE, Status.UNAVAILABLE));
    validTransitions.put(Status.UNAVAILABLE, EnumSet.of(Status.AVAILABLE, Status.OFF_SHIFT));
    validTransitions.put(Status.OFF_SHIFT, EnumSet.of(Status.AVAILABLE, Status.ON_CALL));
    validTransitions.put(Status.ON_CALL, EnumSet.of(Status.AVAILABLE));
    validTransitions.put(Status.SICK_LEAVE, EnumSet.of(Status.UNAVAILABLE));
    validTransitions.put(Status.VACATION, EnumSet.of(Status.UNAVAILABLE));
    validTransitions.put(Status.TRAINING, EnumSet.of(Status.AVAILABLE, Status.ADMIN_TASK));
    validTransitions.put(Status.ADMIN_TASK, EnumSet.of(Status.AVAILABLE));
  }

  public DentistAvailabilityStatus(Status initialStatus) {
    this.current = initialStatus;
  }

  public Status getCurrent() {
    return current;
  }

  public static DentistAvailabilityStatus from(DentistAvailabilityStatus.Status value) {
    if (value == null) {
      throw new IllegalArgumentException("DentistAvailabilityStatus  cannot be null.");
    }
    return new DentistAvailabilityStatus (value);
  }

  public boolean canTransitionTo(Status next) {
    return validTransitions.getOrDefault(current, EnumSet.noneOf(Status.class)).contains(next);
  }

  public boolean tryTransitionTo(Status next) {
    if (canTransitionTo(next)) {
      this.current = next;
      return true;
    }
    return false;
  }
  // Semántica avanzada
    /*
      Consultar si el profesional está operativamente disponible (isOperational)
 	  Detectar si está temporalmente no disponible (isTemporarilyUnavailable)
	  Obtener un nivel de prioridad para asignación inteligente (isPermanentlyUnavailable)
      Esto te será útil para dashboards, lógica de asignación, o motores de recomendación.
     */
  public boolean isOperational() {
    return current == Status.AVAILABLE ||  current == Status.ON_CALL || current  == Status.TRAINING ||current  == Status.ADMIN_TASK;
  }

  public boolean isTemporarilyUnavailable() {
    return current == Status.ON_BREAK ||  current == Status.IN_CONSULTATION;
  }

  public boolean isPermanentlyUnavailable() {
    return  current == Status.UNAVAILABLE || current == Status.SICK_LEAVE || current == Status.VACATION || current == Status.OFF_SHIFT;
  }

  // Nivel de prioridad para asignación (0 = no asignable, 3 = alta prioridad)
  public int getPriorityLevel() {
    return switch (current) {
      case AVAILABLE       -> 3;
      case ON_CALL         -> 2;
      case TRAINING,
           ADMIN_TASK      -> 1;
      case ON_BREAK,
           IN_CONSULTATION -> 0;
      default              -> -1; // No asignable
    };
  }



  @Override
  public String toString() {
    return current.name();
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