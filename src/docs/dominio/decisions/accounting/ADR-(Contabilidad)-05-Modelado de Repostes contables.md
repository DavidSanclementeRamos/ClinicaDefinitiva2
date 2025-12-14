

# ADR 001 – Modelado de Reportes Contables
- Fecha: 2025-12-03  
- Estado: Aceptado (con posibilidad de evolución futura)

---

##  Contexto
En el sistema contable se generan distintos reportes:
- Operativos: Libro Diario, Libro Mayor, Balance de Comprobación.
- Financieros oficiales: Balance General y Resultado de Cierre.

En el diseño inicial se consideró modelar cada reporte como un agregado independiente, con su propia identidad y ciclo de vida. Sin embargo, esto introducía complejidad innecesaria en una primera etapa del proyecto. Todos los reportes derivan de los Asientos Contables, que ya encapsulan la invariante de doble partida y constituyen la fuente de verdad del sistema.

---

##  Decisión
- Se crea un agregado general “ReporteContable”, capaz de representar distintos tipos de reportes.
- En esta primera etapa:
    - Libro Diario, Libro Mayor y Balance de Comprobación se modelan como proyecciones (read models) derivadas directamente del agregado raíz AsientoContable.
    - Balance General y Resultado de Cierre se modelan como instancias del agregado ReporteContable, dado que representan documentos oficiales con identidad propia, valor legal y ciclo de vida.
- Se deja explícito que en el futuro, si el dominio lo requiere, cada reporte podrá evolucionar hacia un agregado independiente, sin romper la cohesión actual.

---

##  Consecuencias
- Simplificación inicial: Se evita la complejidad de múltiples agregados en paralelo, manteniendo un solo agregado general para reportes.
- Cohesión del dominio: Los asientos contables siguen siendo la fuente de verdad; los reportes se derivan de ellos.
- Flexibilidad futura: La decisión no bloquea la evolución; si se necesita mayor granularidad, se podrán crear agregados específicos para cada reporte.
- Claridad semántica: Se distingue entre:
    - Reportes operativos efímeros (proyecciones regenerables).
    - Reportes oficiales persistentes (documentos con trazabilidad y valor legal).
- Exhibición profesional: Demuestra criterio arquitectónico: comenzar simple, evitar redundancia, y dejar espacio para evolución controlada.

---

##  Relacionados
- ADR-013 – Plan de cuentas y asientos contables
- ADR-032 – Reglas de negocio por agregado
- ADR-034 – Guardian de reglas de negocio

---

