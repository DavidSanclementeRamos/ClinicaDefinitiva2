# ADR-53 (Arquitectura): Abandono del historial de catálogos eliminados y de la numeración inmutable

**Estado:** Aceptado  
**Fecha:** 2026-03-04  
**Autor:** David  
**Supera:** ADR-22, ADR-23, ADR-25, ADR-30

---

## Contexto

El día anterior (2026-03-03) se formalizó en ADR-52 una política de gobernanza que incluía dos prácticas:

- Mantener un historial formal de catálogos de error eliminados (ADR-23, ADR-25, ADR-30).
- Preservar la numeración inmutable de los catálogos: los códigos eliminados nunca se reutilizan y los huecos en la secuencia son intencionales (ADR-22).

Al comenzar a implementar el historial de eliminados, la realidad del proyecto reveló una situación que los ADRs anteriores no habían dimensionado correctamente.

---

## El problema real

La deuda técnica en los catálogos de error es de naturaleza diferente a la que los ADRs anteriores anticipaban. No se trata de catálogos que fueron correctos en su momento y luego quedaron obsoletos por evolución del dominio. Se trata de catálogos que **desde su primera implementación ya estaban mal concebidos**, por las siguientes razones:

**Falta de experiencia en DDD al inicio del proyecto.** En las etapas tempranas se crearon catálogos con granularidad incorrecta: validaciones null/blank de atributos que ya eran Value Objects y por tanto ya validaban sus propias invariantes, catálogos para agregados que después fueron eliminados del modelo, catálogos que nunca llegaron a ser lanzados por ninguna excepción en el código, y catálogos duplicados de la misma invariante diferenciados solo por el verbo de la operación.

**El historial no aporta valor cuando el origen es incorrecto.** El propósito de documentar catálogos eliminados es preservar la trazabilidad de decisiones que alguna vez fueron válidas. Documentar que `ERR_DENTIST_NULL_NAME` fue eliminado porque el VO ya valida el null no enseña nada: ese catálogo nunca debió existir. La documentación de su eliminación tiene valor cero y costo alto.

**El volumen hace la práctica inviable.** La cantidad de catálogos afectados es suficientemente grande como para que el esfuerzo de rastrear y justificar cada uno consuma tiempo que el proyecto necesita para avanzar en desarrollo real. Documentar deuda que no aporta aprendizaje ni trazabilidad útil es un desperdicio.

**Nota sobre la intención original.** La idea de crear el historial no fue incorrecta en abstracto: en un proyecto maduro con catálogos bien diseñados desde el inicio, documentar eliminaciones tiene sentido. El problema es específico al momento del ciclo de vida de este proyecto. Reconocer eso y ajustar la práctica es una decisión pragmática, no una renuncia a la disciplina.

---

## Decisión

**Se abandona el historial formal de catálogos eliminados** (ADR-23, ADR-25, ADR-30 quedan superados). Los catálogos que ya no corresponden al modelo actual se eliminan del código sin registro individual. Los tres ADRs históricos se unifican en un único documento con estado Superado, conservándose solo como evidencia del proceso de aprendizaje.

**Se abandona la numeración inmutable** (ADR-22 queda superado). Dado que la deuda técnica implica eliminar una cantidad significativa de catálogos, mantener huecos en la secuencia numérica en este contexto añade confusión sin aportar trazabilidad real: no hay producción, no hay sistemas externos que referencien los códigos, y los huecos no documentarían evolución sino errores iniciales de diseño. Los nuevos catálogos usan numeración secuencial continua.

**Se conservan las lecciones aprendidas**, que sí tienen valor. Los patrones anti-patrón identificados (catálogos por operación, catálogos genéricos para múltiples VOs, responsabilidades transversales duplicadas en cada agregado) quedan documentados en la sección de contexto de este ADR y en ADR-50. Eso es suficiente.

---

## Lecciones aprendidas que se conservan

Estas lecciones emergen del proceso, aunque no se documente cada catálogo individual:

**Catálogos por operación.** Crear `ERR_PATIENT_CREATION_REQUIRES_X`, `ERR_PATIENT_UPDATE_REQUIRES_X`, `ERR_PATIENT_DELETE_REQUIRES_X` para la misma invariante es siempre un anti-patrón. Un catálogo único con contexto en el mensaje de excepción es la solución.

**Catálogos de agregado para invariantes de VO.** Si un atributo es un Value Object, la validación de sus invariantes ocurre en el VO. El agregado no necesita un catálogo para lo que el VO ya garantiza.

**Responsabilidades transversales.** El estado activo/inactivo, la validez de documentos de identidad, y otros conceptos que aplican a múltiples agregados no deben duplicarse como catálogos en cada agregado. Pertenecen a un componente transversal.

**Granularidad desde el dominio, no desde la implementación.** Un catálogo de error debe responder a una regla de negocio real, no a una línea de código de validación.

---

## Consecuencias

Los ADR-22, ADR-23, ADR-25 y ADR-30 quedan marcados como Superados. Su contenido no se elimina del repositorio: permanecen como evidencia del proceso de aprendizaje y maduración arquitectónica del proyecto, que es parte del valor de un portfolio.

El tiempo liberado se destina al desarrollo del sistema, que es donde el valor del portfolio se demuestra con mayor claridad.

---

## Referencias

- ADR-22: Estrategia de Numeración de Catálogos de Error (Superado)
- ADR-23: Histórico de Catálogos Eliminados — Módulo Actor (Superado)
- ADR-25: Histórico de Catálogos Eliminados — Módulo Schedule (Superado)
- ADR-30: Histórico de Catálogos Eliminados — Módulo dental.care.services (Superado)
- ADR-52: Jerarquía definitiva de excepciones y gobernanza de documentación
