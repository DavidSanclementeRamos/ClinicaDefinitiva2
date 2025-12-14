
# ADR-Modelado del Plan de Cuentas

- Fecha: 2025-12-03
- Estado: Aprobado

## Contexto
El plan de cuentas es una estructura jerárquica que define las cuentas contables (activo, pasivo, ingreso, gasto). En este sistema, se carga desde un archivo JSON y se utiliza como catálogo de referencia.

## Decisión
- El Plan de Cuentas se modela como una estructura de referencia inmutable, cargada desde JSON.
- Se implementa como clases de consulta (read-only service), no como agregado.
- Se proveen métodos de búsqueda y filtrado (buscarPorCodigo, obtenerPorTipo) para facilitar validación semántica e internacionalización.

## Consecuencias
- El plan de cuentas no tiene ciclo de vida dentro del dominio, evitando complejidad innecesaria.
- Se asegura consistencia semántica al validar asientos contra un catálogo centralizado.
- La separación clara entre catálogo de referencia y entidades dinámicas refuerza la trazabilidad y la ética del diseño.
- La carga desde JSON permite flexibilidad internacional y adaptación a distintos marcos contables.

