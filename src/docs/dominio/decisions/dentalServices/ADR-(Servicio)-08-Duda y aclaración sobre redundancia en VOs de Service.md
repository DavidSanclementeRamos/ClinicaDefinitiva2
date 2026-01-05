

# ADR 08 (Service): Duda y aclaración sobre redundancia en VOs de Service
- Estado: Aprobado
- Fecha: 2026-01-05
- Autor: David
## Contexto
Durante el diseño del agregado `Service` surgió una duda sobre posible redundancia entre distintos Value Objects:

- **`ServiceName`**: incluye un enum interno (`DentalServiceName`) que permite distinguir servicios predefinidos y personalizados.
- **`ServiceId`**: define identificadores estáticos de servicios conocidos, garantizando identidad técnica y trazabilidad.
- **`ServiceCatalog`**: agrega un catálogo maestro con `id`, `name` y `category`, incluyendo un enum interno de valores por defecto.

La inquietud fue que tanto `ServiceName` con su enum como `ServiceId` con sus identificadores estáticos ya brindan la información necesaria para distinguir cada servicio. En ese contexto, el enum interno de categorías en `ServiceCatalog` parecía redundante.

## Aclaración
Tras el análisis se concluye que los tres VOs cumplen **roles complementarios** y no redundantes:

- **ServiceId** asegura identidad técnica y trazabilidad.
- **ServiceName** aporta expresividad semántica, permitiendo nombres predefinidos o personalizados.
- **ServiceCatalog** funciona como catálogo maestro, necesario para validar coherencia entre categoría y detalles (`ServiceDetails`) según la regla **RN-SERVICE-004**.

La validación RN-SERVICE-004 garantiza que el catálogo y los detalles estén alineados, evitando inconsistencias. Esto justifica la existencia de `ServiceCatalog` como pieza adicional y aclara que no es redundante.

## Consecuencias
- **Positivas:**
    - Separación clara de responsabilidades entre identidad, nombre y categoría.
    - Flexibilidad para manejar servicios personalizados sin perder coherencia con categorías oficiales.
    - Mayor trazabilidad y claridad en validaciones de negocio.

- **Negativas:**
    - Aumenta el número de VOs, lo que puede percibirse como complejidad adicional.

## Alternativas consideradas
1. **Usar solo ServiceId + ServiceName:**
    - Ventaja: simplicidad.
    - Desventaja: se pierde la validación de coherencia entre categoría y detalles.

2. **Mantener ServiceCatalog como catálogo maestro:**
    - Ventaja: permite RN-SERVICE-004 y asegura consistencia categórica.
    - Desventaja: introduce un VO adicional.

## Decisión final
Se mantiene la existencia de los tres VOs (`ServiceId`, `ServiceName`, `ServiceCatalog`) porque se complementan y no son redundantes. La duda inicial se aclara: aunque `ServiceName` y `ServiceId` permiten distinguir servicios, `ServiceCatalog` es necesario para validar coherencia categórica con `ServiceDetails`. Esto asegura separación de responsabilidades alineada con DDD.

-