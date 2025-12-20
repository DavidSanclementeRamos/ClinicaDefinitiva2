# ADR-14 (Dominio): Estrategia de Desactivación de Usuario y Actores

- **Estado:** Aprobado
- **Fecha:** 2025-12-16
- **Autor:** David

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
- [ADR-(Actores)-14-Eliminación de Domain Services en agregados del módulo Actor.md](ADR-%28Actores%29-14-Eliminaci%C3%B3n%20de%20Domain%20Services%20en%20agregados%20del%20m%C3%B3dulo%20Actor.md)
- [ADR-(Actores)-11-Separación de estado entre User y Dentist.md](ADR-%28Actores%29-11-Separaci%C3%B3n%20de%20estado%20entre%20User%20y%20Dentist.md)
- [ADR-(Aseso de user)-01-Desactivación de usuario.md](../userAccess/ADR-%28Aseso%20de%20user%29-01-Desactivaci%C3%B3n%20de%20usuario.md)