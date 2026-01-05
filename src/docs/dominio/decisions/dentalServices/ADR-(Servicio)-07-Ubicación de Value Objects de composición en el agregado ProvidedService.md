
---

# ADR- 07 (Servicio): Ubicación de Value Objects de composición en el agregado `ProvidedService`

## Estado
**En uso (documentado a posteriori)**

## Contexto
El agregado raíz `ProvidedService` representa un **servicio general** dentro del dominio.  
Para crear servicios más específicos, se utilizan **Value Objects de composición** que encapsulan atributos y comportamientos particulares (ej. configuraciones, estados, restricciones clínicas).

En la arquitectura actual:
- Existe una separación semántica entre **entidades** y **Value Objects (VO)**, reflejada en catálogos de error, excepciones y paquetes.
- Sin embargo, los VO de composición de `ProvidedService` se han ubicado dentro del paquete `entity`, lo cual no es semánticamente correcto, pero facilita la búsqueda y navegación del código al mantenerlos junto al agregado raíz.
- Esta práctica ya se venía aplicando en el proyecto; lo que faltaba era su documentación formal.

## Decisión
Se acuerda que:
- Los **VO de composición directamente ligados al agregado raíz** `ProvidedService` se ubicarán en el mismo paquete que la entidad raíz (`entity.providedservice`), para mantener **ergonomía visual y cohesión práctica**.
- Se documenta explícitamente que esta decisión **no implica que VO sean entidades**, sino que responde a criterios de **agrupación por proximidad funcional**.
- Las **excepciones y catálogos de error** seguirán diferenciando entre entidad y VO, manteniendo la separación semántica en la capa de dominio.
- En ADRs y documentación se deja constancia de esta decisión para evitar confusión futura.

## Justificación
- **Ergonomía**: facilita la búsqueda y comprensión del agregado, ya que los VO de composición son parte esencial de la definición de servicios específicos.
- **Cohesión**: los VO de composición no son reutilizables fuera del agregado `ProvidedService`, por lo que su ubicación junto a la raíz refuerza la idea de pertenencia.
- **Trazabilidad**: se mantiene la separación semántica en catálogos de error y excepciones, garantizando claridad en validaciones y reglas de negocio.
- **Consistencia documental**: se registra esta decisión como práctica ya existente, para que futuros colaboradores comprendan el motivo.

## Consecuencias
- **Positivas**:
    - Mayor productividad y facilidad de navegación en el código.
    - Cohesión visual entre el agregado raíz y sus VO de composición.
- **Negativas**:
    - Se rompe parcialmente la separación semántica estricta entre VO y entidad en la estructura de paquetes.
    - Puede generar confusión si no se consulta este ADR.



---
