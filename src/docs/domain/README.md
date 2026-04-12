# Dominio del sistema odontológico

Este directorio contiene la documentación del **modelo de dominio clínico** del sistema.  
El dominio es el núcleo de la arquitectura hexagonal: aquí viven los agregados, los Value Objects, las reglas de negocio y las invariantes que dan coherencia al sistema.

---

## Qué hay en este directorio

```
dominio/
├── vo/                  → Catálogo de Value Objects activos (fuente: código)
├── study-guide/         → Guías de estudio de dominios complejos (contabilidad, facturación)
└── README.md            → Este archivo
```

> **Nota:** `billing-mvp-scope.md` documenta el alcance del MVP de facturación. Está en este directorio como referencia rápida de qué está implementado y qué está fuera de alcance.

---

## Dónde está cada tipo de documentación

El modelo de dominio no vive solo en esta carpeta. Está distribuido intencionalmente:

| Qué buscas | Dónde está |
|------------|------------|
| Listado de Value Objects activos | [`vo/README.md`](./vo/README.md) |
| Reglas de negocio vigentes por módulo | [`../architecture/decisions/domain/`](../architecture/decisions/domain/) |
| Manejo de errores y catálogos | [`../support/error-catalog.md`](../support/error-catalog.md) |
| Guías de implementación (VOs, excepciones, patrones) | [`../guides/`](../guides/) |
| Descubrimientos iniciales de reglas (histórico) | [`../evolution/initial-domain-discoveries/`](../evolution/initial-domain-discoveries/) |
| Contexto de módulos complejos (contabilidad, DIAN) | [`study-guide/`](./study-guide/) |

---

## Cómo navegar la documentación del dominio

**Si quieres entender los Value Objects activos:**  
Lee [`vo/README.md`](./vo/README.md). Tiene todos los VOs agrupados por módulo con propósito y referencia al paquete Java correspondiente. El código es la fuente de verdad; el índice es una guía de orientación.

**Si quieres entender las reglas de negocio de un módulo concreto:**  
Consulta los ADRs de dominio en [`../architecture/decisions/domain/`](../architecture/decisions/domain/), en la subcarpeta del módulo correspondiente (`actor/`, `schedule/`, `billing/`, `accounting/`, etc.).

**Si quieres entender cómo funciona el manejo de errores:**  
Lee [`../support/error-catalog.md`](../support/error-catalog.md) y los ADRs relacionados: ADR-18, ADR-19, ADR-21, ADR-40 y ADR-52 son los más relevantes.

**Si quieres entender un dominio complejo antes de leer el código:**  
Revisa [`study-guide/`](./study-guide/). Contiene guías de estudio sobre contabilidad colombiana (PUC, DIAN, partida doble) y facturación clínica — contexto necesario para entender las decisiones de modelado de esos módulos.

**Si quieres ver cómo evolucionó el modelo:**  
Consulta [`../evolution/initial-domain-discoveries/`](../evolution/initial-domain-discoveries/) para los descubrimientos iniciales de reglas, y [`../evolution/lessons-learned/`](../evolution/lessons-learned/) para las retrospectivas de cada módulo.

---

## Decisión sobre la documentación de reglas de negocio

Las reglas de negocio vigentes **no se mantienen en archivos Markdown separados**. Están en dos lugares:

1. **El código** — los agregados y Value Objects son la fuente de verdad. Un VO que valida que una fecha no sea futura documenta esa regla mejor que cualquier archivo de texto.
2. **Los ADRs de dominio** — documentan el *por qué* de cada decisión de modelado, no el *qué* (eso lo hace el código).

Los archivos de `initial-domain-discoveries/` existieron como práctica de exploración temprana. Esa práctica evolucionó y se abandonó; la explicación está en [ADR-29 (lección aprendida)](../evolution/lessons-learned/ADR-%28Arquitectura%29-29-alcance%20experimental%20del%20m%C3%B3dulo%20dental.care.services.md).

---

**Última actualización:** 2026-04-11  
**Mantenedor:** David Stiven Sanclemente