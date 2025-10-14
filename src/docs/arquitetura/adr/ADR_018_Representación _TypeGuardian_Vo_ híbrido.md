# ADR-07: Representación de TypeGuardian(tipoResponsable) como Value Object híbrido

## Estado
- Estado: Aceptado
- Fecha: 2025-10-7

## Contexto
Inicialmente, el atributo typeGuardian del agregado *Guardian* fue modelado como un enum fijo (MAMA, PAPA, HERMANO, etc.).  
Esto resolvía los casos de familia nuclear, pero presentaba limitaciones:

- Dificultad para extender con nuevos roles (ej. Tutor Legal, Acudiente Institucional).
- Problemas de internacionalización (mostrar “MOTHER” vs “MAMÁ”).
- Falta de trazabilidad semántica: el enum no encapsula reglas ni validaciones.

## Decisión
Se adopta un *Value Object híbrido* (TypeGuardian) con las siguientes características:

- Instancias estáticas predefinidas para familia nuclear (ej. MAMA, PAPA, HERMANO).
- Método de fábrica of(String codigo, String descripcion) para crear variantes dinámicas.
- Igualdad semántica basada en codigo.
- Preparado para internacionalización y catálogos externos.

## Alternativas consideradas
- **Mantener enum puro**: simple, pero inflexible y poco internacionalizable.
- *VO sin instancias estáticas*: flexible, pero perdería la claridad de roles familiares comunes.
- *Tabla de base de datos*: demasiado costoso para este nivel de modelado.

## Consecuencias
- Mayor coherencia semántica en el dominio.
- Posibilidad de extender sin recompilar.
- Internacionalización soportada de forma natural.
- Ligero aumento de complejidad en comparación con enum.


## Relación con otros ADR
- ADR-02: Uso sistemático de Value Objects.
- ADR-01: Migración a arquitectura hexagonal.