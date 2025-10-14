# ADR: Creación de un módulo independiente para Servicios

- **Fecha:** 2025-10-11
- **Estado:** Aprobado

## Contexto
El sistema cuenta actualmente con un módulo **Administration** que agrupa varias operaciones administrativas: facturación, pagos, contratos, gastos y también los servicios odontológicos.  
Sin embargo, se identificó que los **Servicios** representan un dominio distinto, con necesidades propias de trazabilidad clínica, categorización y relación directa con agenda y facturación.  
Mantenerlos dentro de **Administration** genera acoplamiento excesivo y dificulta la escalabilidad.

Se evaluaron tres enfoques:
1. Mantener todos los servicios como una sola entidad genérica dentro de Administration.
2. Dividir cada especialidad en entidades separadas.
3. Crear un módulo independiente para Servicios con un modelo híbrido.

## Decisión
Se creará un **módulo independiente para Servicios**, separado de **Administration**, con un **modelo híbrido**:
- Existirá una clase base `Servicio` con atributos comunes (id, nombre, categoría, código estandarizado, tarifa base, duración, requiere autorización).
- Algunas categorías críticas tendrán subentidades o extensiones con atributos específicos (ej. Ortodoncia, Cirugía Oral, Odontopediatría).
- Este módulo se integrará con:
    - **Facturación** (para generar `InvoiceItem`).
    - **Agenda (Scheduled)** (para definir duración y recursos).
    - **Pagos** (a través de facturas liquidadas).

## Servicios contemplados inicialmente
- **Prevención y diagnóstico:** consultas, limpiezas, flúor, sellantes, radiografías.
- **Odontología general y restauradora:** obturaciones, reconstrucciones, endodoncia, extracciones simples.
- **Periodoncia:** tratamientos de encías, raspados, cirugía periodontal.
- **Ortodoncia:** brackets, alineadores, controles.
- **Implantología y prótesis:** implantes, coronas, prótesis fijas/removibles.
- **Cirugía oral y maxilofacial:** extracciones quirúrgicas, frenillo, injertos.
- **Odontopediatría:** atención infantil, sellantes, restauraciones pediátricas.
- **Estética dental:** blanqueamiento, carillas, contorneado.

## Consecuencias
- **Positivas:**
    - Mayor modularidad y trazabilidad.
    - Escalabilidad: se pueden añadir nuevas categorías sin romper el modelo.
    - Integración clara con facturación y agenda.
    - Posibilidad de mapear servicios a estándares (ej. CUPS en Colombia).

- **Negativas:**
    - Incremento en la complejidad inicial del diseño.
    - Necesidad de definir reglas claras para decidir cuándo un servicio se mantiene genérico y cuándo se convierte en subentidad.

## Próximos pasos
1. Definir atributos mínimos de la clase `Servicio`.
2. Identificar qué categorías requieren subentidades desde el inicio (ej. Ortodoncia, Cirugía).
3. Diseñar relaciones con `Tarifa`, `InvoiceItem` y `Scheduled`.
4. Documentar reglas de negocio para trazabilidad y auditoría.  