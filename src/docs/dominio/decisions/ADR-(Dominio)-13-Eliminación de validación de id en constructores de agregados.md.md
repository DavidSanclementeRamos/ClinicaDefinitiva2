
# ADR-13 (Dominio): Eliminación de validación de `id` en constructores de agregados

- **Fecha**: 2026-02-25
- **Estado**: Aprobado
- **Categoría**: Dominio

---

## Contexto

En el sistema, los agregados son reconstruidos desde la base de datos por los repositorios de infraestructura. Esto implica que el ciclo de vida de un agregado tiene dos fases claramente diferenciadas:

1. **Creación en dominio**: el agregado nace mediante un método de fábrica o un `Builder`, sin identidad asignada. En este punto solo existen las reglas de negocio que definen su estado inicial.
2. **Materialización desde infraestructura**: el repositorio persiste el agregado, obtiene el `id` generado por la base de datos, y reconstruye el objeto completo inyectando dicho identificador.

Esta distinción es intencional y está alineada con el principio DDD de que **la identidad de un agregado es responsabilidad de la capa de infraestructura**, no del dominio.

---

## Problema

Durante la evolución del modelo se identificó que algunos agregados (ej. `Rate`) incluyen la siguiente validación en su constructor:

```java
this.id = Objects.requireNonNull(builder.id, "Rate ID no puede ser nulo");
```

Mientras que otros agregados no la tienen. Esto generó tres síntomas concretos que alertaron sobre una inconsistencia de diseño:

### 1. Los métodos de fábrica no pueden probarse en aislamiento

Los métodos de fábrica (`Rate.create(...)`) representan la intención de negocio de crear una nueva tarifa. Sin embargo, al invocarlos en tests unitarios, el constructor lanza una excepción porque no existe un `id` en ese momento. En producción, ese `id` lo asigna la base de datos después de la persistencia; en el test, no hay base de datos.

Esto hace que los tests unitarios estén acoplados a un detalle de infraestructura, cuando su responsabilidad debería ser exclusivamente validar reglas de negocio.

### 2. Duplicación de validación ya cubierta por el Value Object

Los identificadores del dominio están representados como Value Objects (`RateId`, `InvoiceId`, etc.), los cuales ya validan la ausencia de valor nulo en su propia construcción. Al repetir la validación de `null` en el constructor del agregado, se introduce una redundancia que contradice el principio de **"confiar en las validaciones de los VOs"**, declarado en ADR-12.

### 3. Inconsistencia entre agregados

Algunos agregados validan el `id` en el constructor, otros no. Esta asimetría no responde a ninguna decisión arquitectónica documentada, sino que refleja una práctica accidental que varió entre implementaciones. El resultado es un modelo donde el contrato de construcción de los agregados no es predecible ni uniforme.

---

## Decisión

**Se elimina la validación explícita de `id` en los constructores de los agregados.**

La asignación del identificador en el constructor sigue siendo posible, pero sin `requireNonNull` sobre él:

```java
// ❌ Antes
this.id = Objects.requireNonNull(builder.id, "Rate ID no puede ser nulo");

// ✅ Después
this.id = builder.id;
```

Esta decisión se sustenta en tres pilares:

### Pilar 1: Separación de responsabilidades entre dominio e infraestructura

El dominio no debe conocer ni preocuparse por cómo se generan los identificadores. Esa responsabilidad pertenece a la infraestructura: la base de datos, un generador de UUIDs, o cualquier mecanismo externo. Validar el `id` en el constructor del agregado introduce una dependencia implícita hacia ese mecanismo externo dentro del dominio.

### Pilar 2: Confianza en los Value Objects (ADR-12)

Los Value Objects de identidad son los guardianes naturales de sus propias invariantes. Si `RateId` garantiza que no puede existir con valor nulo, entonces cualquier `id` que llegue al constructor del agregado ya es un objeto válido por construcción. Repetir la validación es ruido semántico, no una protección real.

### Pilar 3: Los constructores de agregados validan reglas de negocio, no infraestructura

La responsabilidad de un constructor de agregado es garantizar que el objeto nace en un estado de negocio consistente: fechas válidas, estados permitidos, relaciones requeridas. El `id` no forma parte de las reglas de negocio; es un artefacto de persistencia. Por tanto, no pertenece a las validaciones del constructor.

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|---|---|
| Mantener la validación en el constructor | Duplica lo que ya hacen los VOs, rompe la consistencia entre agregados, y acopla los tests de dominio a la infraestructura |
| Exigir que todos los métodos de fábrica reciban el `id` como parámetro | Invierte el flujo correcto: obliga al dominio a conocer el `id` antes de persistir, lo que no es posible en bases de datos con identidad autogenerada |
| Forzar un patrón de construcción uniforme (todos con Builder o todos con fábrica) | Contradice ADR-07, que establece que la estrategia de construcción depende de la complejidad del agregado. Imponer uniformidad cosmética por encima de criterio arquitectónico es una decisión incorrecta |
| Mantener la inconsistencia actual sin documentar | No es una decisión, es una deuda técnica que crece silenciosamente |

---

## Consecuencias

### Lo que ganamos

- **Consistencia en todo el modelo**: todos los agregados siguen el mismo contrato respecto al `id`. La regla es única y predecible.
- **Tests de dominio que prueban dominio**: los métodos de fábrica pueden validarse en aislamiento sin simular infraestructura. El test verifica reglas de negocio, no la presencia de un identificador técnico.
- **Alineación con ADR-12**: se elimina la duplicación de validaciones. Los VOs son la única fuente de verdad sobre sus propias invariantes.
- **Claridad semántica**: quien lea el constructor de un agregado entiende inmediatamente que el `id` es un dato de infraestructura, no una invariante de negocio.

### Lo que asumimos como responsabilidad

- **Los repositorios son la última línea de defensa del `id`**: es responsabilidad de la capa de infraestructura garantizar que ningún agregado sea devuelto sin identificador. Esta es una responsabilidad que ya les corresponde por su rol, y no representa una carga nueva.
- **El modelo confía en sus capas**: esta decisión refuerza el principio de que cada capa es responsable de sus propias invariantes. Si infraestructura falla en asignar el `id`, el error se manifiesta en esa capa, no en el dominio.

---

## Implementación

### Ejemplo aplicado en `Rate`

```java
private Rate(Builder builder) {
    this.id = builder.id; // El id lo asigna infraestructura; el VO garantiza su validez si está presente
    this.serviceId = Objects.requireNonNull(builder.serviceId, "Service no puede ser nulo");
    this.amount = Objects.requireNonNull(builder.amount, "Amount no puede ser nulo");
    this.payerType = Objects.requireNonNull(builder.payerType, "PayerType no puede ser nulo");
    this.validFrom = Objects.requireNonNull(builder.validFrom, "ValidFrom no puede ser nulo");
    this.validTo = builder.validTo;
    this.contractId = builder.contractId;
    this.active = builder.active;

    validateBusinessRules();
}
```

### Cómo queda el test del método de fábrica

```java
@Test
void shouldCreateRateWithCorrectBusinessRules() {
    Rate rate = Rate.create(
            ServiceId.of(20L),
            Price.of(300, Currency.getInstance("COP")),
            Rate.PayerType.EPS,
            ContractId.of(200L)
    );

    // Validamos reglas de negocio, no el id
    assertTrue(rate.isActive());
    assertTrue(rate.isIndefinite());
    assertEquals(Rate.PayerType.EPS, rate.getPayerType());
    assertEquals(300, rate.getAmount().getAmount().intValue());
}
```

---

## Relación con otros ADR

| ADR | Relación |
|---|---|
| ADR-07 (Estrategia de construcción de objetos) | Esta decisión no impone uniformidad de patrón. Cada agregado mantiene la estrategia que le corresponde según su complejidad |
| ADR-12 (Validaciones en VOs) | Esta decisión es una consecuencia directa: si los VOs validan sus invariantes, los agregados no deben repetirlas |
| ADR sobre eliminación del Builder en Receptionist | Ejemplo de que la estrategia de construcción es una decisión per-agregado, no una regla global |