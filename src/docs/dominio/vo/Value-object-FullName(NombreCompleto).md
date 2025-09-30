# Value Object: FullName

## Propósito

FullName encapsula el nombre completo de una persona clínica como una unidad semántica validada. Representa la combinación de nombre y apellido con coherencia, evitando ambigüedad, errores de formato y degeneración semántica. Este VO permite validar longitud, caracteres permitidos y generar representaciones consistentes para informes, etiquetas y trazabilidad.

## Motivación

El nombre completo es un dato clínico sensible que aparece en turnos, informes, facturación, historia clínica y trazabilidad operativa. Usarlo como dos cadenas separadas (String nombre, String apellido) genera duplicación, errores de formato y pérdida de intención semántica. Este VO permite encapsular la lógica de validación y representación, delegando la responsabilidad al dominio.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class FullName {

    private final String firstName;
    private final String lastName;

    public FullName(String firstName, String lastName) {
        if (firstName == null || lastName == null || firstName.isBlank() || lastName.isBlank()) {
            throw new ClinicalValidationException("Nombre y apellido son obligatorios");
        }
        if (firstName.length() < 2 || lastName.length() < 2) {
            throw new ClinicalValidationException("Nombre o apellido demasiado cortos");
        }
        if (!firstName.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+") || !lastName.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
            throw new ClinicalValidationException("Nombre o apellido con caracteres inválidos");
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getInitials() {
        return (firstName.charAt(0) + "" + lastName.charAt(0)).toUpperCase();
    }
}
```
## Reglas clínicas encapsuladas

- Nombre y apellido son obligatorios.
- Longitud mínima de 2 caracteres.
- Solo se permiten letras y espacios (incluye acentos y ñ).
- Se puede generar nombre completo (getFullName()) e iniciales (getInitials()).

## Uso en el modelo

- Paciente, Responsable, Odontólogo, Secretario → usan FullName como parte de su perfil clínico.
- Se utiliza en etiquetas, informes, historia clínica, trazabilidad de turnos y métricas.
- Mejora la consistencia en la representación de nombres en todo el sistema.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad clínica del sistema.
- Evita ambigüedad y errores de formato.
- Permite internacionalización y personalización.

## Proyección

Este VO será extendido con:

- Métodos para normalización (mayúsculas, capitalización).
- Soporte para nombres compuestos y apellidos múltiples.
- Integración con VO Email y Dni para perfiles clínicos.
- Posibilidad de representar alias o nombres sociales.

## Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
  