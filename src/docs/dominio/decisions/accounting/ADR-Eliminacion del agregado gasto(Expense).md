

# ADR 003 – Eliminación del agregado Gasto(Expense)
- Estado: Aprobado
- Fecha: 2025-12-03

## Contexto
En el diseño inicial se consideró modelar un agregado Gasto para representar los egresos de la organización. Sin embargo, el dominio ya cuenta con el agregado raíz AsientoContable, que encapsula la invariante de doble partida y permite registrar cualquier tipo de movimiento contable, incluyendo gastos.  
El agregado Gasto duplicaba responsabilidades y generaba redundancia semántica, ya que los egresos pueden modelarse directamente como movimientos en los asientos contables, asociados a cuentas de tipo GASTO en el plan de cuentas.

## Decisión
- Se elimina el agregado Gasto del dominio.
- Los gastos se registran exclusivamente mediante AsientoContable, con movimientos que afectan cuentas clasificadas como TipoCuenta.GASTO.
- La semántica de gasto se deriva de la clasificación de cuentas en el PlanDeCuentas, no de un agregado independiente.

## Consecuencias
- Simplificación del modelo: menos agregados, menor complejidad y mayor claridad.
- No se pierde expresividad: los gastos siguen identificables a través de los movimientos y la clasificación de cuentas.
- Trazabilidad intacta: los reportes (Libro Diario, Libro Mayor, Balance de Comprobación, Resultado de Cierre) pueden seguir mostrando gastos sin necesidad de un agregado específico.
- Exhibición profesional: demuestra criterio para evitar redundancia y mantener un modelo semánticamente limpio, donde cada agregado tiene un propósito claro.

## Relación con otros ADR
- [ADR-17-Manejo-de-plan-de-cuenta-y-asiento-contable.md](../../../arquitetura/adr/ADR-17-Manejo-de-plan-de-cuenta-y-asiento-contable.md)
- [ADR-12-Nuevos-agregados-en-modulo-contable.md](../../../arquitetura/adr/ADR-12-Nuevos-agregados-en-modulo-contable.md)
- [ADR-09-Nuevos agregados en el módulo Administration.md](../../../arquitetura/adr/ADR-09-Nuevos%20agregados%20en%20el%20m%C3%B3dulo%20Administration.md)
---

