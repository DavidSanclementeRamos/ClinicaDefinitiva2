
# Value Object: Age (Edad)

## Contexto

La edad es un dato clínico derivado que impacta decisiones operacionales como registro, clasificación y validación de actores. Validar edad directamente en servicios o controladores genera duplicación, acoplamiento y degeneración semántica.

Este VO encapsula la lógica de derivación y validación de edad a partir de un VO DateOfBirth [Value-object-DateOfBirth(FechaNacimiento)](ClinicaDefinitiva/src/docs/dominio/vo/Value-object-DateOfBirth(FechaNacimiento).md), evitando el uso de tipos primitivos (int, Integer) que no expresan intención ni validación. Permite delegar responsabilidades éticas al dominio y mantener coherencia semántica en todo el sistema.

## Decisión

Se define el Value Object Age como una clase inmutable que:

- Deriva la edad desde DateOfBirth usando Period.between(...).
- Validad que la edad esté en el rango clínicamente aceptable (0–130 años).
- Expone métodos semánticos para clasificación y validación ética.

## Estructura

```java
public final class Age {

    private final int value;

    public Age(DateOfBirth dateOfBirth) {
        this.value = Period.between(dateOfBirth.asDate(), LocalDate.now()).getYears();
        if (value < 0 || value > 130) {
            throw new IllegalArgumentException("Derived age is invalid.");
        }
    }

    // methods semantice
    public boolean isAdult() {
        return value >= 18;
    }

    public boolean isElderly() {
        return value >= 65;
    }

    public boolean isEligibleForRegistration() {
        return value >= 13;
    }

    public boolean isBetween(int min, int max) {
        return value >= min && value <= max;
    }

    public String ageCategory() {
        if (value < 13) return "Child";
        if (value < 18) return "Teenager";
        if (value < 65) return "Adult";
        return "Senior";
    }

    public int asInt() {
        return value;
    }

    // methods access
    public int Value() {
        return value;
    }

    // methods utility
    @Override
    public String toString() {
        return "Age: " + value + " (" + ageCategory() + ")";
    }

}
```
## Reglas clínicas encapsuladas

- Edad válida: debe estar entre 0 y 130 años.
- Clasificación semántica: Child, Teenager, Adult, Senior.
- Registro: edad mínima de 13 años (isEligibleForRegistration()).
- Validación de elegibilidad por rango (isBetween(min, max)).

## Uso en el modelo

Para evitar acoplamiento, cada agregado clínico interpreta la edad según su propia lógica:

- Paciente: determina si requiere responsable usando isEligibleForRegistration().
- Responsable: validad elegibilidad desde su agregado, usando isEligibleForRegistration().
- Odontólogo y Secretario: utilizan isBetween(min, max) desde sus propios agregados para validar edad mínima según configuración.

La lógica de decisión no está en el VO, sino en los agregados que lo consumen éticamente.

## Ventajas

- Validación derivada y centralizada.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Trazabilidad de errores clínicos.
- Evolución legítima del modelo sin romper contratos.
- Evita acoplamiento entre VO y lógica de negocio.

## Proyección

Este VO será extendido con:

- Métodos para calcular edad desde LocalDate.
- Integración con VO DateOfBirth.
- Validaciones cruzadas con Responsable y Paciente.

## Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
- ADR-02-value-objects-(Vo).md)
  `