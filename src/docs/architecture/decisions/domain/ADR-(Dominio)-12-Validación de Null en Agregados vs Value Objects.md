

# ADR-12(Dominio): Validación de Null en Agregados vs Value Objects

- **Fecha**: 2026-02-19
- **Estado**: Aprobado
- **Categoría**: Dominio
- **Autor:** David Stiven Sanclemente

---

## Problema

En el sistema existen múltiples agregados (ej. `AdministrativeReport`, `Invoice`, `Appointment`, `Treatment`) que interactúan con diversos Value Objects (`Indicator`, `InvoiceItem`, `DentistId`, etc.).

Los Value Objects ya validan sus invariantes internas en el constructor: no permiten valores nulos, vacíos o inconsistentes. Sin embargo, algunos agregados han replicado estas validaciones, lanzando excepciones con catálogos propios. Esto genera **duplicación de reglas**, **explosión de catálogos redundantes** y confusión sobre qué capa es responsable de cada validación.

La pregunta crítica es: ¿deben los agregados validar nuevamente que sus VOs no sean nulos, o deben confiar en que los VOs garantizan su propia invarianza? Resolverlo es clave para mantener coherencia arquitectónica y evitar sobre‑ingeniería en un sistema que busca reflejar prácticas empresariales reales.

---

## Decisión

**Los agregados no deben validar null de Value Objects.**  
Se confía en que los VOs garantizan su invarianza desde la construcción.

### Reglas:
1. **VOs validan su construcción**
    - Ejemplo: `Indicator` valida nombre, valor y unidad en su constructor.
2. **Agregados NO re‑validan VOs**
    - Si llega un null inesperado, se considera un bug en la capa de aplicación o infraestructura.
    - Se permite `fail fast` (NullPointerException) o validación defensiva ligera (`Objects.requireNonNull`).
3. **Agregados validan SUS reglas de negocio**
    - Ejemplo: máximo de indicadores, estado editable, consistencia de fechas.
    - Estas reglas sí deben usar catálogos de error propios del agregado.

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Validar null en agregados con catálogo | Duplica responsabilidades del VO y genera catálogos redundantes |
| Fusionar catálogos VO y agregado | Mezcla invariancias internas con reglas contextuales, confunde trazabilidad |
| Usar solo NPE sin política clara | Falta de consistencia, difícil de explicar en documentación y entrevistas |

---

## Consecuencias

### Ganamos
- **Claridad de responsabilidades**: VO = invariancia, Agregado = reglas de negocio.
- **Catálogos más limpios**: sin duplicación de errores entre VO y agregado.
- **Consistencia DDD**: los agregados confían en sus VOs, reforzando el principio de “Trust the Value Object”.
- **Portafolio profesional**: demuestra comprensión de separación de responsabilidades y evita sobre‑ingeniería.

### Perdemos
- **Menor trazabilidad en errores de null inesperados**: un NPE puede ser menos descriptivo que un catálogo.
- **Dependencia en la correcta implementación de VOs**: si un VO está mal diseñado, el agregado no lo detectará.
- **Mayor disciplina en capa de aplicación**: se debe garantizar que nunca se pase null al agregado.

---

## Implementación

```java
// En Indicator (VO)
private Indicator(String name, BigDecimal value, String unit) {
    if (name == null || name.isBlank()) {
        throw new ValueObjectValidationException(
            VoAccountingError.ERR_INDICATOR_NAME_NULL,
            VOContext.ACCOUNTING
        );
    }
    if (value == null) {
        throw new ValueObjectValidationException(
            VoAccountingError.ERR_INDICATOR_VALUE_NULL,
            VOContext.ACCOUNTING
        );
    }
    if (unit == null || unit.isBlank()) {
        throw new ValueObjectValidationException(
            VoAccountingError.ERR_INDICATOR_UNIT_INVALID,
            VOContext.ACCOUNTING
        );
    }
    this.name = name.trim();
    this.value = value;
    this.unit = unit.trim();
}

// En AdministrativeReport (Agregado)
public void addIndicator(Indicator indicator) {
    ensureEditable();  // regla del agregado
    if (indicators.size() >= MAX_INDICATORS) {
        throw new DomainAggregateException(
            AdministrativeReportError.ERR_REPORT_TOO_MANY_INDICATORS,
            EntityContext.ADMINISTRATIVEREPORT
        );
    }
    indicators.add(indicator);  // confiar en el VO
    lastUpdate = LocalDateTime.now();
}
```

---

## Relación con otros ADR

- [ADR-(Arquitectura)-51-Implementación de Authorization Helper Pattern.md](../arch/ADR-%28Arquitectura%29-51-Implementaci%C3%B3n%20de%20Authorization%20Helper%20Pattern.md)
- [ADR-(Arquitectura)-21-Catálogos de errores por agregado con interfaz común.md](../arch/ADR-%28Arquitectura%29-21-Cat%C3%A1logos%20de%20errores%20por%20agregado%20con%20interfaz%20com%C3%BAn.md)