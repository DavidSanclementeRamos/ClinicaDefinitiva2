# ADR-42 (Arquitectura): AggregateBusinessRuleViolationException para violaciones múltiples

- **Fecha**: 2026-01-30
- **Estado**: Aprobado
- **Categoría**: Arquitectura
- **Complementa**: ADR-018 (simplificación de jerarquía de excepciones)

## Problema

ADR-018 simplificó la jerarquía de excepciones reemplazando clases específicas por excepciones parametrizadas.

Sin embargo, surgió la necesidad de manejar **múltiples violaciones de reglas** en una sola operación (ej. desactivar paciente con tratamientos activos Y citas pendientes).

Modificar `BusinessRuleViolationException` para aceptar listas de `OutcomeDetail` requeriría refactorizar todos los agregados que ya la usan.

## Decisión

Crear `AggregateBusinessRuleViolationException` para casos de acumulación de errores.

**Regla:**
- `BusinessRuleViolationException` → violación individual (se mantiene sin cambios)
- `AggregateBusinessRuleViolationException` → múltiples violaciones acumuladas

**Invariante:** La lista de detalles NO puede estar vacía.

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Modificar `BusinessRuleViolationException` para aceptar lista | Requiere refactorizar >50 agregados existentes |
| Lanzar solo el primer error | Se pierde información. El usuario merece saber TODOS los problemas |
| Lista de excepciones separadas | Complica el manejo en controladores. Una excepción con lista es más simple |

## Consecuencias

**Ganamos:**
- Compatibilidad total con código existente
- Comunicación clara: nombre indica múltiples violaciones
- Reutilizable en cualquier servicio que acumule validaciones

**Perdemos:**
- Dos excepciones para conceptos similares (posible confusión inicial)
- Requiere documentar cuándo usar cada una

## Priorización de errores

El primer `OutcomeDetail` de la lista se considera el error principal:
- Para logging
- Para respuesta HTTP (el resto va en campo `details`)

## Implementación

```java
public class AggregateBusinessRuleViolationException extends ModelException {
    private final List<OutcomeDetail> detalles;

    public AggregateBusinessRuleViolationException(List<OutcomeDetail> detalles) {
        super(
            detalles.get(0).getCode(), // primer error como principal
            detalles.get(0).getCategory().toDomainContext()
        );
        
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                "Lista de detalles no puede estar vacía"
            );
        }
        
        this.detalles = List.copyOf(detalles);
    }

    public List<OutcomeDetail> getDetalles() {
        return detalles;
    }
}
```

**Uso en Application Service:**

```java
Outcome<Void> outcome = userDeactivationPolicy.validate(user);
if (outcome.isFailure()) {
    throw new AggregateBusinessRuleViolationException(
        outcome.getDetails()
    );
}
```