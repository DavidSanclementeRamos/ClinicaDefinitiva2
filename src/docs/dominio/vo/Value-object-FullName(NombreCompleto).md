# Value Object: FullName

## Propósito

FullName encapsula el nombre completo de una persona clínica como una unidad semántica validada. Representa la combinación de nombre y apellido con coherencia, evitando ambigüedad, errores de formato y degeneración semántica. Este VO permite validar longitud, caracteres permitidos y generar representaciones consistentes para informes, etiquetas y trazabilidad.

## Motivación

El nombre completo es un dato clínico sensible que aparece en turnos, informes, facturación, historia clínica y trazabilidad operativa. Usarlo como dos cadenas separadas (String nombre, String apellido) genera duplicación, errores de formato y pérdida de intención semántica. Este VO permite encapsular la lógica de validación y representación, delegando la responsabilidad al dominio.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public final class FullName {
    private final String firstName;
    private final String lastName;

    public FullName(String firstName, String lastName) {
        if(firstName == null || lastName == null){
            throw new NullFullNameException(ContextoEntidad.FULL_NAME,"First name and last name must not be null");
        }
        if (isBlank(firstName) || isBlank(lastName)) {
            throw new BlankFullNameException(ContextoEntidad.FULL_NAME, "First name and last name must not be empty.");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    // methods semantic
    public String asText() {
        return firstName + " " + lastName;
    }

    public boolean matches(String fullNameCandidate) {
        return asText().equalsIgnoreCase(fullNameCandidate.trim());
    }

    public boolean startsWith(String prefix) {
        return asText().toLowerCase().startsWith(prefix.toLowerCase().trim());
    }

    public String initials() {
        return (firstName.charAt(0) + "" + lastName.charAt(0)).toUpperCase();
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // methods access
    public String LastName() {
        return lastName;
    }

    public String FirstName() {
        return firstName;
    }

    // methods utility
    @Override
    public String toString() {
        return asText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullName)) return false;
        FullName fullName = (FullName) o;
        return firstName.equalsIgnoreCase(fullName.firstName) &&
                lastName.equalsIgnoreCase(fullName.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName.toLowerCase(), lastName.toLowerCase());
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
  