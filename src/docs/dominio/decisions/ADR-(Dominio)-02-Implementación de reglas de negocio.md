# ADR-04: Implementación sistemática de reglas de negocio por agregado

- **Estado:** Aceptado
- **Fecha:** 2025-09-20
- **Autor:** David

## Contexto
Durante la etapa inicial del proyecto, desarrollado en Java EE, no existía una comprensión explícita de lo que constituía una "regla de negocio".  
Las validaciones eran técnicas, dispersas y no estaban organizadas por intención clínica ni responsabilidad semántica. El modelo carecía de trazabilidad ética y operativa.

En la migración hacia Spring Boot se implementaron tres nuevas reglas de negocio de forma intuitiva, como respuestas puntuales a necesidades clínicas, pero sin formalización ni documentación.

El punto de inflexión ocurrió al adoptar arquitectura hexagonal. Esta arquitectura coloca las reglas de negocio en el centro del sistema, separándolas de la infraestructura y permitiendo que el dominio exprese su intención con claridad. A partir de este momento se inició una implementación sistemática de reglas por agregado, con documentación exhibible, trazabilidad semántica y justificación ética.

## Decisión
Se implementarán reglas de negocio explícitas por cada agregado clínico del dominio. Estas reglas serán:

- **Descubiertas por operación**, no por intuición.
- **Documentadas como unidades semánticas**, no como validaciones técnicas.
- **Registradas como parte del modelo ético**, no como lógica incidental.

Cada regla será:
- Asociada a su agregado correspondiente.
- Justificada en términos clínicos, operativos y éticos.
- Registrada en catálogos de errores clínicos y plantillas de reglas por operación.
- Exhibida como parte del modelo para entrevistas, auditoría y evolución legítima.

## Consecuencias
- El sistema gana trazabilidad ética y semántica.
- Se evita la degeneración de reglas dispersas o duplicadas.
- Se facilita la internacionalización del modelo.
- Se habilita la validación por operación, no por tipo de dato.
- El proyecto se convierte en una declaración de principios, no solo en una demo técnica.

## Plan de implementación
1. Crear plantilla estándar para documentar reglas de negocio (operación, agregado, justificación clínica, referencia a ADR).
2. Asociar cada regla a su agregado en `docs/dominio/reglas-de-negocio/`.
3. Vincular reglas a entradas del catálogo de errores clínicos [ver ADR-03](../arquitetura/adr/ADR-03-Catalogo-Errores-ClÍnicos.md).
4. Documentar reglas históricas en `docs/dominio/exploraciones/` para mantener trazabilidad.
5. Validar reglas mediante Value Objects [ver ADR-02](ADR-02-Value-Objects-(VO).md) y excepciones clínicas.
6. Revisar y aprobar nuevas reglas en comité técnico + clínico.

## Registro histórico
- **Java EE:** no se reconocían las reglas de negocio como tales.
- **Spring Boot:** se implementaron 3 reglas intuitivas sin formalización.
- **Arquitectura hexagonal:** se reconoció su centralidad y se inició su implementación sistemática.

## Relación con otros ADR

- [ADR-01: Migración a arquitectura hexagonal](ADR-01-Migración-a-Hexagonal.md)
- [ADR-02: Implementación estratégica de Value Objects](ADR-02-Value-Objects-(VO).md)
- [ADR-03: Catálogo de errores clínicos por operación](ADR-03-Catalogo-Errores-ClÍnicos.md)