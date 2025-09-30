

# Value Object: Specialties

## Propósito

Specialties encapsula la especialidad clínica de un Odontólogo como una unidad semántica validada. Representa el área profesional en la que el odontólogo está habilitado para atender, y permite filtrar turnos, asignaciones, métricas y validaciones clínicas según su especialidad.

Este VO evita el uso de enums técnicos o cadenas planas que no expresan intención clínica, y permite delegar la lógica de clasificación y validación al dominio.

## Motivación

La especialidad define el tipo de atención que puede brindar un odontólogo: ortodoncia, cirugía, estética, endodoncia, etc. Usar este dato como texto plano o enum técnico genera ambigüedad, errores de asignación y degeneración semántica. Este VO permite encapsular la lógica de validación y representación, delegando la responsabilidad al dominio.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class Specialties {

    private final String value;

    public Specialties(String value) {
        if (value == null || value.isBlank()) {
            throw new ClinicalValidationException("La especialidad es obligatoria");
        }

        List<String> especialidadesValidas = List.of(
            "ORTODONCIA", "ENDODONCIA", "CIRUGIA", "ESTETICA", "PERIODONCIA", "GENERAL"
        );

        if (!especialidadesValidas.contains(value.toUpperCase())) {
            throw new ClinicalValidationException("Especialidad inválida: " + value);
        }

        this.value = value.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public boolean isOrthodontics() {
        return "ORTODONCIA".equals(value);
    }

    public boolean isSurgery() {
        return "CIRUGIA".equals(value);
    }

    public boolean isGeneral() {
        return "GENERAL".equals(value);
    }

    // Otros métodos específicos según especialidad
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
 