

#  ADR 05 (Servicio): Delegación de validaciones a Value Objects y cambio de nombre de Money a Price

- Estado: Aprobado  
- Fecha: 01 de enero de 2026
- Autor: David Stiven Sanclemente

---

## Contexto
En el diseño inicial del sistema, atributos como name y description se manejaban como simples cadenas de texto (String). Esto generaba problemas de consistencia y repetición de validaciones en diferentes capas (ej. verificar longitud, evitar valores vacíos, controlar caracteres).

Además, existía un Value Object llamado Money que representaba valores monetarios. Sin embargo, el nombre Money resultaba ambiguo y menos preciso en el contexto del dominio odontológico y administrativo. El término Price refleja mejor la semántica: no se trata de dinero genérico, sino de un precio asociado a servicios, convenios y facturación.

Este ADR se redacta retrospectivamente para formalizar decisiones ya aplicadas en el código, pero que no estaban documentadas.

---

## Problema
- Validaciones dispersas y repetitivas en atributos básicos (name, description).
- Riesgo de inconsistencias semánticas (ej. un servicio con nombre vacío o descripción inválida).
- Ambigüedad en el VO Money, que no reflejaba con claridad su rol en el dominio.

---

## Soluciones evaluadas

Opción A: Mantener validaciones en entidades y usar Money
- Validaciones de name y description en constructores o servicios de aplicación.
- Mantener el VO Money como representación monetaria.

Ventajas:
- Simplicidad inicial.
- No requiere refactorización.

Desventajas:
- Validaciones repetidas y dispersas.
- Riesgo de inconsistencias.
- Money es demasiado genérico y poco exhibible.

---

Opción B: Delegar validaciones a VO y renombrar Money → Price
- Crear VO Name y Description que encapsulen reglas de validación.
- Renombrar Money a Price, reflejando semántica más precisa.
- Centralizar validaciones y semántica en el dominio.

Ventajas:
- Encapsulación: validaciones consistentes y reutilizables.
- Claridad semántica: Price refleja mejor el rol económico en servicios y facturación.
- Exhibible: muestra aplicación rigurosa de DDD y lenguaje ubicuo.
- Extensible: fácil de internacionalizar o enriquecer con nuevas reglas.

Desventajas:
- Requiere refactorización de código existente.
- Mayor número de clases VO en el dominio.

---

Opción C: Usar un módulo de utilidades para validaciones
- Validaciones centralizadas en helpers o servicios de aplicación.
- Mantener Money como estaba.

Ventajas:
- Centralización parcial de validaciones.
- Menor refactorización.

Desventajas:
- No es semánticamente rico.
- Exhibición débil: parece un enfoque técnico, no de dominio.
- Money sigue siendo ambiguo.

---

Decisión
Se adopta la Opción B:
- Delegar validaciones de name y description a VO especializados (Name, Description).
- Renombrar el VO Money a Price para reflejar semántica más clara y explícita.

---

Consecuencias
- Positivas:
    - Validaciones encapsuladas y consistentes.
    - Claridad semántica en el dominio.
    - Exhibición profesional: muestra aplicación rigurosa de DDD.
    - Extensibilidad futura (ej. internacionalización, catálogos dinámicos).
- Negativas:
    - Refactorización retrospectiva de código y documentación.
    - Mayor número de clases VO en el dominio.

---
