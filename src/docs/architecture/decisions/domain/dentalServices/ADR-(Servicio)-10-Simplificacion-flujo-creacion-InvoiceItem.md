
```markdown
# ADR-Servicio-10: Simplificación del flujo de creación de InvoiceItem – eliminación de ServiceRendered

- **Fecha**: 2026-04-08
- **Estado**: Aprobado
- **Categoría**: Dominio / Servicios Odontológicos
- **Supera**: ADR-01 (Servicio), ADR-04 (Servicio)
- **Autor**: David Stiven Sanclemente

---

## Problema

El ADR-01 propuso la creación de un Value Object `ServiceRendered` para representar la prestación concreta de un servicio odontológico, con el objetivo de desacoplar el catálogo (`ProvidedService`) del proceso de facturación. Además, el ADR-04 discutía la ubicación de ese snapshot (dominio vs aplicación).

Durante la implementación real del módulo de facturación y servicios, se optó por **no crear `ServiceRendered`**. El flujo actual funciona de la siguiente manera:

1. El Application Service (`InvoiceApplicationService`) recibe un `AddInvoiceItemDto` con los identificadores del servicio (`serviceId`) y la tarifa (`rateId`).
2. El servicio de aplicación obtiene directamente los agregados `ProvidedService` y `Rate` desde sus respectivos repositorios.
3. Invoca `InvoiceItemFactoryService.createFromRateSnapshot(service, rate, quantity, performedAt)`, que construye el `InvoiceItem` copiando el precio (`unitPrice`) y los datos del servicio en el momento de la creación.

Este flujo es más simple, tiene menos clases y cumple con todos los requisitos de trazabilidad y auditoría (snapshot de precio, validación de existencia de servicio y tarifa). No se ha detectado ninguna necesidad real de un objeto `ServiceRendered` intermedio.

---

## Decisión

**No implementar `ServiceRendered`.** Se mantiene el flujo actual:

- `InvoiceItemFactoryService` recibe directamente `ProvidedService` y `Rate`.
- El Application Service es responsable de obtener esos agregados desde los repositorios.
- La creación de `InvoiceItem` incluye un snapshot de precio (`unitPrice`) y datos de servicio (`serviceCode`, `serviceDescription`) para garantizar inmutabilidad histórica.

**Regla:** No se introducirá un objeto `ServiceRendered` a menos que surja un caso de uso claro que lo requiera (por ejemplo, necesidad de modelar eventos de dominio de “servicio prestado” independientes de la facturación).

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Implementar `ServiceRendered` como VO de dominio (ADR-01) | Añade complejidad innecesaria sin aportar valor real. Los datos que contendría ya están disponibles en `ProvidedService` y `Rate`. El snapshot de precio ya se hace en `InvoiceItem`. |
| Ubicar `ServiceRendered` en aplicación o dominio (ADR-04) | El debate sobre su ubicación es irrelevante porque el objeto no existe. El ADR-04 queda obsoleto. |
| Crear `ServiceRendered` como evento de dominio para otros contextos | No hay evidencia de que se necesite. Si en el futuro surge, se evaluará como una decisión separada. |

---

## Consecuencias

### Lo que ganamos

- **Simplicidad:** Menos clases, menos mapeos, flujo más directo.
- **Mantenibilidad:** Se evita mantener un objeto `ServiceRendered` y su lógica de transformación.
- **Trazabilidad preservada:** El snapshot de precio y datos del servicio sigue existiendo en `InvoiceItem`.
- **Cumplimiento de objetivos originales:** Separación de responsabilidades, auditoría, validación de tarifas vigentes – todo sigue funcionando.

### Lo que perdemos

- **Posible reutilización:** Si otro contexto necesitara la noción de “servicio prestado”, hoy no hay un objeto reutilizable. Se podría crear en ese momento.
- **Documentación alineada:** Los ADR-01 y ADR-04 ya no reflejan la realidad. Quedan superados.

### Impacto en ADRs existentes

- **ADR-01 (Servicio):** Superado por este ADR. Se moverá a `evolution/deprecated-adrs/`.
- **ADR-04 (Servicio):** Superado (basado en una premisa no implementada). Se moverá a `evolution/deprecated-adrs/`.

---

## Implementación actual (código relevante)

```java
// InvoiceApplicationService.addItem()
ProvidedService service = providedServiceRepository.findById(serviceId)
        .orElseThrow(() -> new ProvidedServiceNotFoundException(...));
Rate rate = rateRepository.findById(rateId)
        .orElseThrow(() -> new RateNotFoundException(...));

InvoiceItem item = invoiceItemFactoryService.createFromRateSnapshot(
        service, rate, quantity, performedAt
);
invoice.addItem(item);
```

```java
// InvoiceItemFactoryService
public InvoiceItem createFromRateSnapshot(ProvidedService service, Rate rate, Quantity quantity, LocalDateTime performedAt) {
    return InvoiceItem.builder()
            .serviceId(service.getId())
            .serviceCode(service.getCode().getValue())
            .serviceDescription(service.getName().getValue())
            .unitPrice(rate.getAmount())      // snapshot de precio
            .quantity(quantity)
            .rateId(rate.getId())
            .performedAt(performedAt)
            .build();
}
```

---

## Relación con otros ADRs

- **Supera:** 
- [ADR-(Servicio)-01-ServiceRendered.md](../../../../evolution/deprecated-adrs/domain/ADR-%28Servicio%29-01-ServiceRendered.md)
- [ADR-(Servicio)-04-Ubicación del Snapshot ServiceRendered.md](../../../../evolution/deprecated-adrs/domain/ADR-%28Servicio%29-04-Ubicaci%C3%B3n%20del%20Snapshot%20ServiceRendered.md)
- **Complementa:** 
- [ADR-(Servicio)-02-Ubicación del VO Price.md](ADR-%28Servicio%29-02-Ubicaci%C3%B3n%20del%20VO%20Price.md)
---

**Nota:** Este ADR documenta la decisión de no implementar `ServiceRendered`. Cualquier contribución futura que intente reintroducirlo deberá justificarse con un nuevo caso de uso que no pueda resolverse con el flujo actual.
```

