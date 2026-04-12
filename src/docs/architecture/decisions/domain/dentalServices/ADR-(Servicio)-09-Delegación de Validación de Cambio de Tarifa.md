
# ADR 09 (Servicio): Delegación de Validación de Cambio de Tarifa

- Estado: Aprobado
- Fecha: 2026-01-05
- Autor: David Stiven Sanclemente
- 
## Contexto
En el diseño del agregado `Service`, surgió la necesidad de controlar la actualización de tarifas (`updateRate`) con reglas de negocio específicas:
- **RN-SERVICE-003**: El servicio debe ser editable.
- **RN-SERVICE-008**: La justificación es obligatoria si existen citas.
- **RN-SERVICE-009**: El cambio de tarifa debe estar dentro de un rango razonable respecto al valor anterior.

Durante la discusión arquitectónica se consideró la posibilidad de **delegar la validación del rango de cambio de tarifa al VO `Price`**, dado que este encapsula la semántica de valores monetarios y operaciones aritméticas.

## Decisión
Tras el análisis, se concluyó que aunque el VO `Price` podría implementar la validación de rango, esta regla corresponde más al **contexto del agregado `Service`** que a la semántica pura del VO. Por ello, se decidió crear un **Domain Service** (`ServiceRatePolicy`) que encapsula la política de negocio para validar cambios de tarifa.

El agregado `Service` delega en `ServiceRatePolicy` la validación del rango de cambio de tarifa, manteniendo el VO `Price` enfocado únicamente en operaciones aritméticas y comparaciones.

## Consecuencias
- **Positivas:**
    - Separación clara de responsabilidades: el VO `Price` conserva su pureza semántica.
    - El agregado `Service` mantiene el control de invariantes y flujo de negocio.
    - La política de tarifas puede evolucionar fácilmente (ej. diferentes rangos por tipo de servicio).
    - Mayor trazabilidad y claridad en el catálogo de errores (`ERR_SERVICE_RATE_CHANGE_OUT_OF_RANGE`, `ERR_SERVICE_RATE_CHANGE_REQUIRES_JUSTIFICATION`).

- **Negativas:**
    - Introduce un nuevo Domain Service, aumentando el número de componentes.
    - Requiere coordinación adicional entre VO, Agregado y Domain Service.

## Alternativas consideradas
1. **Delegar validación en VO `Price`:**
    - Ventaja: simplicidad y encapsulación directa.
    - Desventaja: mezcla reglas de negocio contextuales con semántica pura del VO.

2. **Validación directamente en el Agregado `Service`:**
    - Ventaja: control centralizado.
    - Desventaja: el agregado se sobrecarga con lógica que puede ser reutilizable.

## Decisión final
Se adopta la opción de **Domain Service `ServiceRatePolicy`** para validar cambios de tarifa, garantizando separación de responsabilidades y flexibilidad futura, dejando al VO `Price` únicamente con responsabilidades aritméticas y semánticas.

