# ADR-52 (Arquitectura): Jerarquía definitiva de excepciones de dominio y gobernanza de documentación de catálogos

**Estado**: Aprobado
**Fecha:** 2026-03-03  
**Autor:** David  
**Reemplaza parcialmente:**   [ADR-18-(Arquitectura)](ADR-%28Arquitectura%29-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md),
[ADR-19-(Arquitectura)](ADR-%28Arquitectura%29-19-Cat%C3%A1logo%20%C3%BAnico%20de%20errores%20con%20contextos%20diferenciados%20%28Entidad%20vs%20VO%29.md),
[ADR-21-(Arquitectura)](ADR-%28Arquitectura%29-21-Cat%C3%A1logos%20de%20errores%20por%20agregado%20con%20interfaz%20com%C3%BAn.md),
[ADR-42-(Arquitectura)](ADR-%28Arquitectura%29-42-AggregateBusinessRuleViolationException%20para%20violaciones%20m%C3%BAltiples.md)
---


## Contexto

A lo largo de la evolución del sistema se tomaron decisiones progresivas sobre la jerarquía de excepciones (ADR-03, ADR-04, ADR-18, ADR-19, ADR-42) y sobre la estructura del catálogo de errores (ADR-21, ADR-22, ADR-23, ADR-30). Este proceso generó tres problemas que requieren resolución formal:

**Problema 1 — Jerarquía declarada vs. implementada divergen.**  
ADR-18 establece que `BusinessRuleViolationException` es la base para violaciones de regla de negocio y que `DomainAggregateException` es la base para errores de agregado. Sin embargo, en la implementación actual ambas extienden `ModelException` como hermanas, lo que rompe el contrato de captura selectiva: un `catch (DomainAggregateException e)` no intercepta una `BusinessRuleViolationException`.

**Problema 2 — Bug de invariante en `AggregateBusinessRuleViolationException`.**  
El constructor llama a `super()` con valores derivados de `detalles` antes de validar que la lista no sea nula ni vacía. En Java, `super()` es la primera instrucción de ejecución; la validación posterior llega tarde. Una excepción cuyo constructor viola una invariante es una contradicción semántica directa.

**Problema 3 — `ErrorCatalog` incompleta frente a lo que la plantilla de catálogo promete.**  
La interfaz `ErrorCatalog` solo expone `getCode()`, `getKey()`, `getMessage()`. La plantilla de catálogo documenta HTTP sugerido y severidad sugerida, pero la interfaz no los expone. Esto obliga al `GlobalControllerAdvice` a hardcodear mappings, rompiendo la promesa de que el catálogo es la fuente de verdad.

**Problema 4 — Contradicción entre ADR-22 y ADR-23.**  
ADR-22 declara que ningún código se reutiliza jamás. ADR-23 documenta la reutilización de `RN-GUARDIAN-008`. Esta inconsistencia invalida el principio de inmutabilidad que ADR-22 establece como base de trazabilidad.

**Problema 5 — Clases sin comportamiento propio.**  
`SchedulingException` y `TemporalValidationException` extienden `ModelException` sin añadir campos, métodos ni contratos propios. No existe ningún `catch` que las trate de forma diferente a `BusinessRuleViolationException`. Son nombres sin abstracción.

**Problema 6 — Formato de documentación de catálogos no escala.**  
Los ADRs históricos (ADR-23, ADR-30) tienen un volumen que supera su utilidad operativa. Los archivos individuales por error (ej. `ERR_DENTIST_ACTIVE_APPOINTMENTS.md`) generan fragmentación: con 80 errores habría 80 archivos, dificultando la navegación y el mantenimiento.

---

## Decisión

### I. Jerarquía definitiva de excepciones

Se adopta la siguiente jerarquía como contrato inmutable. Cualquier desviación requiere un nuevo ADR.

```
ClinicaDefinitivaException          ← raíz del proyecto
└── ModelException                  ← base para errores del modelo de dominio
    ├── ValueObjectValidationException   ← validaciones de Value Objects
    └── DomainAggregateException         ← base para errores de agregados
        ├── BusinessRuleViolationException       ← violación individual de regla
        └── AggregateBusinessRuleViolationException  ← múltiples violaciones acumuladas
```

**Reglas de la jerarquía:**

- `ClinicaDefinitivaException` es la raíz. Toda excepción lanzada en el dominio desciende de ella.
- `ValueObjectValidationException` para validaciones locales de VOs (formato, null, blank, rangos). Recibe `VOContext`.
- `DomainAggregateException` como base abstracta para errores originados en agregados. No se lanza directamente.
- `BusinessRuleViolationException` **extiende `DomainAggregateException`** (corrección a la implementación actual). Representa una violación individual y puntual de una regla de negocio sobre un agregado bien formado.
- `AggregateBusinessRuleViolationException` **extiende `DomainAggregateException`**. Se usa exclusivamente cuando una operación acumula múltiples `OutcomeDetail`. Su constructor valida la invariante antes de llamar al padre.
- `SchedulingException` y `TemporalValidationException` **se eliminan**. No añaden comportamiento ni contratos. Sus usos se reemplazan por `BusinessRuleViolationException` con el `ErrorCatalog` y `metadata` correspondientes.
- `UserNotEligibleException` **se mantiene como subclase ligera de `BusinessRuleViolationException`** únicamente porque encapsula `UserIdentityId` como campo semántico específico que el `GlobalControllerAdvice` necesita para construir la respuesta. Su existencia queda justificada por comportamiento propio, no solo por nombre.

### II. Contrato de metadatos en la raíz

`ClinicaDefinitivaException` incluye un `Map<String, Object> metadata` inmutable como campo opcional. Este mapa reemplaza la necesidad de crear subclases solo para transportar datos adicionales (campo inválido, valor recibido, aggregateId). El mapa se construye en el punto de lanzamiento y no se modifica posteriormente.

### III. Corrección del bug en `AggregateBusinessRuleViolationException`

La validación de la lista de detalles ocurre antes de la construcción del objeto. Como Java no permite lógica antes de `super()`, se extrae la validación a un método estático privado que se invoca dentro de la expresión del argumento de `super()`.

```java
public class AggregateBusinessRuleViolationException extends DomainAggregateException {

    private final List<OutcomeDetail> detalles;

    public AggregateBusinessRuleViolationException(List<OutcomeDetail> detalles) {
        super(
            primerCatalogo(detalles),   // ← validación ocurre aquí, dentro del argumento
            primerContexto(detalles),
            Map.of("totalViolaciones", detalles.size())
        );
        this.detalles = List.copyOf(detalles);
    }

    private static ErrorCatalog primerCatalogo(List<OutcomeDetail> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                "AggregateBusinessRuleViolationException requiere al menos un OutcomeDetail"
            );
        }
        return detalles.get(0).getCode();
    }

    private static DomainContext primerContexto(List<OutcomeDetail> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                "AggregateBusinessRuleViolationException requiere al menos un OutcomeDetail"
            );
        }
        return detalles.get(0).getContext();
    }

    public List<OutcomeDetail> getDetalles() {
        return detalles;
    }
}
```

### IV. Extensión de la interfaz `ErrorCatalog`

La interfaz `ErrorCatalog` se extiende para exponer los datos que la plantilla de catálogo ya documenta. Esto permite que el `GlobalControllerAdvice` derive su comportamiento del catálogo sin hardcodear mappings.

```java
public interface ErrorCatalog {
    String getCode();
    String getKey();
    String getDefaultMessage();
    HttpStatus getSuggestedHttpStatus();   // ← nuevo
    ErrorSeverity getSeverity();           // ← nuevo (enum: ERROR, WARN, INFO)
}
```

Cada enum de catálogo por agregado implementa estos dos métodos adicionales. Los valores se definen junto al error, en el lugar donde el error tiene sentido semántico, manteniendo el catálogo como fuente de verdad completa.

### V. Corrección de la contradicción ADR-22 / ADR-23

ADR-22 declara que los códigos son inmutables y nunca se reutilizan. ADR-23 documenta la reutilización de `RN-GUARDIAN-008`. Esta contradicción se resuelve de la siguiente forma:

La regla de ADR-22 queda en vigor sin excepciones. El caso de `RN-GUARDIAN-008` en ADR-23 se corrige: la entrada debe documentarse como eliminada sin reutilización. El nuevo catálogo recibe el número siguiente en secuencia (`RN-GUARDIAN-012` o el que corresponda). ADR-23 se actualiza con una nota de corrección que explica el error original y la decisión de no perpetuarlo.

### VI. Gobernanza de documentación de catálogos eliminados

**Se mantiene la práctica** de documentar catálogos eliminados.  **Se simplifica el formato.**

**Regla de cuándo crear un ADR histórico de eliminados:**  
Un ADR histórico se crea por ciclo de refactorización, no por módulo. Si en un sprint se eliminan catálogos de tres módulos, se genera un único ADR con secciones por módulo. No se genera un ADR por módulo separado a menos que la refactorización de ese módulo justifique por sí sola un documento (más de 10 eliminaciones con patrones propios).

**Formato simplificado para el ADR histórico:**

Cada entrada se limita a: código, descripción original, fecha, motivo (una de las categorías de ADR-22), reemplazo y referencia. Sin estadísticas de distribución por porcentaje, sin gráficos de barras en ASCII, sin sección de "comparación con estándares de industria" para cada eliminación. Esa información ya está en ADR-22 como principio general; repetirla en cada ADR histórico es ruido.

### VII. Gobernanza de documentación individual de errores

**Se elimina el formato de un archivo por error.** Con un catálogo de 80+ errores, este formato genera 80+ archivos sin navegación coherente. Ningún reviewer o entrevistador va a abrir 80 archivos.

**Se adopta el formato de un archivo por agregado**, con todos los errores del agregado documentados en tabla más sección de detalle.

**Estructura del archivo de catálogo por agregado:**

```markdown
# Catálogo de errores: DentistError

## Resumen

| Código | Key i18n | HTTP | Severidad | Operación |
|--------|----------|------|-----------|-----------|
| RN-DENTIST-001 | error.dentist.age | 422 | ERROR | REGISTRAR_ODONTOLOGO |
| RN-DENTIST-004 | error.dentist.deactivate.appointments | 409 | ERROR | DESACTIVAR_ODONTOLOGO |

## Detalle

### RN-DENTIST-001 — ERR_DENTIST_AGE_INSUFFICIENT
[contenido de la plantilla existente, sin sección de pruebas separada]

### RN-DENTIST-004 — ERR_DENTIST_ACTIVE_APPOINTMENTS
[ídem]

## Eliminados (referencia cruzada)
Ver ADR-23 para RN-DENTIST-003, RN-DENTIST-005, RN-DENTIST-006.
```

Este formato permite que un reviewer entienda el catálogo completo de un agregado en un único archivo, con tabla de navegación rápida y detalle disponible si quiere profundizar.

---

## Consecuencias

**Positivas:**  
La jerarquía implementada y la declarada coinciden. El bug del constructor queda resuelto. `GlobalControllerAdvice` puede derivar su comportamiento del catálogo sin lógica hardcodeada. La documentación de catálogos es navegable y escalable. La contradicción entre ADR-22 y ADR-23 queda reconocida y corregida, lo que refuerza la credibilidad de la documentación.

**Costes:**  
Requiere migrar `BusinessRuleViolationException` para extender `DomainAggregateException` en lugar de `ModelException`. Requiere eliminar `SchedulingException` y `TemporalValidationException` y reemplazar sus usos. Requiere añadir dos métodos a todos los enums de catálogo existentes. Requiere consolidar los archivos individuales de error en archivos por agregado.

**Criterios de aceptación:**
- `catch (DomainAggregateException e)` intercepta `BusinessRuleViolationException` en prueba unitaria.
- El constructor de `AggregateBusinessRuleViolationException` lanza `IllegalArgumentException` antes de llegar al `super()` cuando la lista es nula o vacía.
- `GlobalControllerAdvice` no contiene ningún `HttpStatus` hardcodeado; todos se derivan de `getCatalogo().getSuggestedHttpStatus()`.
- No existen archivos individuales de error en el repositorio; todos los errores están en el archivo de catálogo de su agregado.
- No existen referencias en el código a `SchedulingException` ni a `TemporalValidationException`.

---

## Referencias

- [ADR-(Arquitectura)-18-Simplificación general de jerarquía de excepciones en el dominio.md](ADR-%28Arquitectura%29-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md)
- [ADR-(Arquitectura)-19-Catálogo único de errores con contextos diferenciados (Entidad vs VO).md](ADR-%28Arquitectura%29-19-Cat%C3%A1logo%20%C3%BAnico%20de%20errores%20con%20contextos%20diferenciados%20%28Entidad%20vs%20VO%29.md)
- [ADR-(Arquitectura)-21-Catálogos de errores por agregado con interfaz común.md](ADR-%28Arquitectura%29-21-Cat%C3%A1logos%20de%20errores%20por%20agregado%20con%20interfaz%20com%C3%BAn.md)
- [ADR-(Arquitectura)-42-AggregateBusinessRuleViolationException para violaciones múltiples.md](ADR-%28Arquitectura%29-42-AggregateBusinessRuleViolationException%20para%20violaciones%20m%C3%BAltiples.md)