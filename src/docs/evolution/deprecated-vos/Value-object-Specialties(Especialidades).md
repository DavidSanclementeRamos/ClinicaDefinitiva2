

# Value Object: Specialties

## Propósito

Specialties encapsula la especialidad clínica de un Odontólogo como una unidad semántica validada. Representa el área profesional en la que el odontólogo está habilitado para atender, y permite filtrar turnos, asignaciones, métricas y validaciones clínicas según su especialidad.

Este VO evita el uso de enums técnicos o cadenas planas que no expresan intención clínica, y permite delegar la lógica de clasificación y validación al dominio.

## Motivación

La especialidad define el tipo de atención que puede brindar un odontólogo: ortodoncia, cirugía, estética, endodoncia, etc. Usar este dato como texto plano o enum técnico genera ambigüedad, errores de asignación y degeneración semántica. Este VO permite encapsular la lógica de validación y representación, delegando la responsabilidad al dominio.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public final class Specialties {
    private final Set<Specialty> values;

    public Specialties(Set<Specialty> values) {
        if (values == null ){
            throw new NullSpecialtySetException(ContextoEntidad.DENTIST, "At least one specialty must be null.");
        }
        if (values.isEmpty()){
            throw new EmptySpecialtySetException(ContextoEntidad.DENTIST, "At least one specialty must be provided.");
        }
        this.values = Collections.unmodifiableSet(new HashSet<>(values));
    }

    // methods semantic
    public boolean contains(Specialty specialty) {
        return values.contains(specialty);
    }

    public boolean isMultidisciplinary() {
        return values.size() > 1;
    }

    public boolean allowsSurgicalProcedures() {
        return contains(new Specialty("Oral Surgery"));
    }

    public Set<Specialty> asSet() {
        return values;
    }

    // methods access
    public Set<Specialty> Values() {
        return values;
    }

    // methods utility
    @Override
    public String toString() {
        return "Specialties: " + values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specialties)) return false;
        Specialties that = (Specialties) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }




}
```

## Reglas clínicas encapsuladas

- La especialidad debe ser una de las válidas: ORTODONCIA, ENDODONCIA, CIRUGIA, ESTETICA, PERIODONCIA, GENERAL.
- Permite validar si el odontólogo está habilitado para un tipo de atención.
- Permite filtrar turnos, asignaciones y métricas por especialidad.
- Evita ambigüedad en flujos clínicos y operativos.

## Uso en el modelo

- Odontólogo → usa Specialties como parte de su perfil clínico.
- TurnoService, AgendaService, CitaService → pueden filtrar operaciones por especialidad.
- Se utiliza en informes, métricas, asignaciones y validaciones clínicas.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad clínica del sistema.
- Evita ambigüedad y errores de asignación.
- Permite expansión futura sin romper contratos.

## Proyección

Este VO será extendido con:

- Soporte para especialidades personalizadas por clínica.
- Métodos para justificar cambios de especialidad.
- Integración con permisos, roles y tipos de cita.
- Posibilidad de representar especialidades múltiples o jerárquicas.

## Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
 