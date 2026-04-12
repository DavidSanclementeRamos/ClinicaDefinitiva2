# ADR-15 (Actores): Estrategia de Desactivación de Usuario y Actores

- **Estado:** Superado
- **Fecha:** 2025-12-16
- **Autor:** David Stiven Sanclemente

Nota: *“La estrategia descrita no refleja la implementación final. La solución correcta está documentada en [ver ADR-38](../../../architecture/decisions/arch/ADR-(Arquitectura)-38-UserDeactivationPolicy%20como%20orquestador%20de%20validaciones.md) y [ver ADR-39](../../../architecture/decisions/arch/ADR-(Arquitectura)-39-Ubicación%20de%20validaciones%20de%20desactivación.md) , que introducen UserDeactivationPolicy y reglas claras de ubicación de validaciones.”*
## Contexto
El sistema requiere desactivar actores asociados a un usuario (Paciente, Odontólogo, Responsable, Secretario).  
Durante el diseño inicial se plantearon varias alternativas sobre cómo coordinar las validaciones y la acción de desactivar.  
Esto generó diferentes ADR con variaciones sobre la misma problemática.

## Decisión Inicial
La primera solución consistía en:

- Un **Domain Service** (`ActorDeactivationService`) que recorría todos los agregados del módulo Actor para validar si podían desactivarse.
- Uso de un objeto **Outcome** para acumular errores y devolver un resultado consolidado.
- Coordinación centralizada desde el servicio, que luego ejecutaba la desactivación si todas las validaciones eran exitosas.

Esta aproximación buscaba uniformidad y trazabilidad, pero introducía complejidad y validaciones innecesarias sobre agregados que no estaban siendo desactivados.

## Evolución de la Solución
Posteriormente se identificaron mejoras importantes:

### Validación por agregado específico
- Cada agregado (Paciente, Odontólogo, Responsable, Secretario) valida únicamente sus propias reglas mediante `assertCanBeDeactivated()`.
- Se evita recorrer todos los actores del módulo para desactivar uno.

### Repositorios separados
- Se descartó un **ActorRepository** monolítico.
- Cada agregado tiene su propio repositorio (`PacienteRepository`, `OdontologoRepository`, etc.), manteniendo claridad y bajo acoplamiento.
- Si se requiere polimorfismo, se puede definir una interfaz común que cada repositorio implemente.

### Usuario como coordinador
- El agregado **Usuario** es quien ejecuta la acción de desactivar, si las validaciones del actor lo permiten.
- Los actores solo exponen validación (`assertCanBeDeactivated()`) y cambio de estado (`marcarInactivo()`).

### Outcome enriquecido
- Se extendió el patrón **Outcome** para incluir severidad, categoría y códigos de error, mejorando trazabilidad y auditoría.
- Esto permite acumular múltiples razones y clasificarlas (clínico, administrativo, técnico).

### Hexagonal limpio
- Los servicios de aplicación orquestan repositorios y persistencia.
- Los **Domain Services** solo se usan para reglas transversales (ej. un Responsable con Pacientes activos).
- La lógica de negocio permanece encapsulada en los agregados.

## Consecuencias
- ✅ Mayor coherencia semántica: se desactiva solo el actor solicitado.
- ✅ Menor complejidad: se eliminan validaciones innecesarias sobre todos los actores.
- ✅ Extensibilidad: agregar nuevos actores es más simple y no rompe el contrato.
- ✅ Exhibición profesional: el ADR refleja tanto la solución inicial como la madurez alcanzada, mostrando un proceso de evolución arquitectónica.
- ✅ Trazabilidad: queda registrado cómo se pensó primero y cómo se mejoró después.


## Relación con otros ADR
- [ADR-(Actores)-14-Eliminación de Domain Services en agregados del módulo Actor.md](ADR-(Actores)-14-Eliminación de Domain Services en agregados del módulo Actor.md)
- [ADR-(Actores)-11-Separación de estado entre User y Dentist.md](ADR-(Actores)-11-Separación de estado entre User y Dentist.md)
- [ADR-(Aseso de user)-01-Desactivación de usuario.md](../userAccess/ADR-%28Aseso%20de%20user%29-01-Desactivaci%C3%B3n%20de%20usuario.md)