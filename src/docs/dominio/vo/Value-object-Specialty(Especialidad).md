# Value Object: Specialty

## Propósito

Specialty encapsula una especialidad clínica como unidad semántica validada. A diferencia de Specialties (que puede representar una colección o clasificación), este VO representa una *instancia única* de especialidad que puede ser asignada a un odontólogo, una cita, un servicio o una atención específica.

Este VO permite validar si una especialidad es reconocida por el sistema, encapsular lógica de clasificación clínica y delegar responsabilidades semánticas al dominio.

## Motivación

La especialidad clínica es un dato sensible que afecta la asignación de turnos, la atención permitida, la facturación y la trazabilidad profesional. Usar este dato como texto plano o enum técnico genera ambigüedad, errores de asignación y degeneración semántica. Este VO permite representar especialidades como entidades semánticas, con validación, formato y proyección.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public final class Specialty {
  private static final Set<String> VALID_SPECIALTIES = Set.of(
          "Orthodontics",
          "Endodontics",
          "Periodontics",
          "Prosthodontics",
          "Pediatric Dentistry",
          "Oral Surgery",
          "General Dentistry"
  );

  private final String value;

  public  Specialty(String value) {
       /* if (isBlank(value)) {
            throw new IllegalArgumentException("Specialty must not be empty.");
        }*/
    String normalized = value.trim();
    if (!VALID_SPECIALTIES.contains(normalized)) {
      throw new InvalidSpecialtyValueException(ContextoEntidad.DENTIST,"Invalid specialty: " + value);
    }
    this.value = normalized;
  }

  // public Specialty(String value) {
  //   this.value = value;
  // }

  // methods semantic
  public boolean is(String expected) {
    return value.equalsIgnoreCase(expected.trim());
  }

  public String asText() {
    return value;
  }

  private boolean isBlank(String input) {
    return input == null || input.trim().isEmpty();
  }

  // methods access
  public String Value() {
    return value;
  }

  // methods utility
  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Specialty)) return false;
    Specialty that = (Specialty) o;
    return value.equalsIgnoreCase(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value.toLowerCase());
  }





}

```

## Reglas clínicas encapsuladas

- La especialidad debe ser reconocida por el sistema.
- Permite validar si una atención puede ser asignada a un odontólogo.
- Permite filtrar servicios, turnos y métricas por especialidad.
- Evita ambigüedad en flujos clínicos y operativos.

## Uso en el modelo

- Odontólogo → usa Specialty como parte de su perfil clínico.
- Cita, Servicio, Turno → pueden referenciar Specialty para trazabilidad.
- AgendaService, TurnoService, CitaService → filtran operaciones por especialidad.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad clínica del sistema.
- Evita ambigüedad y errores de asignación.
- Permite expansión futura sin romper contratos.

## Proyección

Este VO será extendido con:

- Métodos para justificar cambios de especialidad.
- Soporte para especialidades múltiples o jerárquicas.
- Integración con permisos, roles y tipos de cita.
- Posibilidad de representar especialidades personalizadas por clínica.

## Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
  `