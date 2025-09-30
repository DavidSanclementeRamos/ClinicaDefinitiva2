# ADR-004: Implementación sistemática de reglas de negocio por agregado

## Estado
Aceptado

## Contexto

Durante la etapa inicial del proyecto, desarrollado en Java EE, no existía una comprensión explícita de lo que constituía una "regla de negocio". Las validaciones eran técnicas, dispersas y no estaban organizadas por intención clínica ni responsabilidad semántica. El modelo carecía de trazabilidad ética y operativa.

En la migración hacia Spring Boot, se implementaron tres nuevas reglas de negocio de forma intuitiva, sin reconocer aún su verdadero potencial. Estas reglas surgieron como respuestas puntuales a necesidades clínicas, pero no fueron formalizadas ni documentadas como tales.

El punto de inflexión ocurrió al adoptar arquitectura hexagonal. Esta arquitectura pone las reglas de negocio en el centro del sistema, separándolas de la infraestructura y permitiendo que el dominio exprese su intención con claridad. A partir de este momento, se inició una implementación sistemática de reglas por agregado, con documentación exhibible, trazabilidad semántica y justificación ética.

## Decisión

Se implementarán reglas de negocio explícitas por cada agregado clínico del dominio. Estas reglas serán:

- *Descubiertas por operación*, no por intuición.
- *Documentadas como unidades semánticas*, no como validaciones técnicas.
- *Registradas como parte del modelo ético*, no como lógica incidental.

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
- Se convierte el proyecto en una declaración de principios, no solo en una demo técnica.

## Registro histórico

- En Java EE: no se reconocían las reglas de negocio como tales.
- En Spring Boot: se implementaron 3 reglas intuitivas sin formalización.
- En arquitectura hexagonal: se reconoció su centralidad y se inició su implementación sistemática.

## Relación con otros ADR

- [ADR-030: Migración a arquitectura hexagonal](ADR-030.md)
- [ADR-031: Implementación estratégica de Value Objects](ADR-031.md)
- [ADR-033: Catálogo de errores clínicos por operación](ADR-033.md)