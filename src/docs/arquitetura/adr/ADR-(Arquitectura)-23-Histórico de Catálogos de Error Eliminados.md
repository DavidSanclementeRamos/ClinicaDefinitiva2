# ADR-23 (Arquitectura): Histórico de Catálogos de Error Eliminados

**Estado:** ⛔ Superado — ver ADR-53 (2026-03-04)  
**Fecha de creación:** 2025-12-24  
**Fecha de cierre:** 2026-03-04  
**Autor:** David  
**Unifica:** ADR-23 (Módulo Actor), ADR-25 (Módulo Schedule), ADR-30 (Módulo dental.care.services)

---

## Nota de cierre

Este documento fue la implementación de una práctica que resultó inviable en el contexto actual del proyecto. La práctica de documentar catálogos eliminados tiene sentido cuando los catálogos fueron correctos en algún momento y quedaron obsoletos por evolución del dominio. En este caso, la mayoría de los catálogos eliminados nunca estuvieron bien diseñados desde su origen, debido a la falta de experiencia en DDD en las etapas iniciales del proyecto.

Documentar cada eliminación individual requería un esfuerzo desproporcionado respecto al valor producido. La decisión de abandonar esta práctica y el razonamiento completo están en **ADR-51**.

Este archivo se conserva en el repositorio como evidencia del proceso de aprendizaje.

---

## Catálogos eliminados — registro simplificado

Lo que sí se preserva: los patrones que originaron los errores, no el detalle de cada catálogo.

### Módulo Actor (2025-12-24)

**Dentist:** RN-DENTIST-006, RN-DENTIST-008, RN-DENTIST-009  
**Guardian:** RN-GUARDIAN-001, RN-GUARDIAN-002, RN-GUARDIAN-006, RN-GUARDIAN-007, RN-GUARDIAN-008  
**Patient:** RN-PATIENT-001, RN-PATIENT-004, RN-PATIENT-005, RN-PATIENT-007, RN-PATIENT-011, RN-PATIENT-012  
**Receptionist:** RN-RECEPTIONIST-001, RN-RECEPTIONIST-005, RN-RECEPTIONIST-009

Motivos recurrentes: validaciones de VO colocadas en el agregado, responsabilidades de estado activo/inactivo duplicadas en cada agregado en lugar de delegarse a UserIdentity, catálogos genéricos que agrupaban múltiples invariantes de VOs distintos, catálogos por operación para la misma invariante.

### Módulo Schedule (fecha de eliminación documentada en ADR-25 original)

Catálogos eliminados por motivos equivalentes a los del Módulo Actor. El patrón dominante fue la misma granularidad incorrecta aplicada al contexto de agendamiento.

### Módulo dental.care.services (2026-01-06)

**ProvidedService:** RN-SERVICE-001, RN-SERVICE-002, RN-SERVICE-007, RN-SERVICE-009, RN-SERVICE-010, RN-SERVICE-013, RN-SERVICE-014

Motivos recurrentes: validaciones de formato y rangos colocadas en el agregado en lugar del VO correspondiente, y una regla de facturabilidad ubicada en el módulo incorrecto (debía estar en Billing).

---

## Patrones anti-patrón identificados

Estas lecciones tienen valor independientemente de si se documenta cada catálogo individual.

**Catálogos por operación.** Tres catálogos para la misma invariante diferenciados por el verbo (crear / editar / eliminar) es siempre un error. La solución es un catálogo único con contexto en el mensaje de excepción.

**Catálogos de agregado para invariantes de VO.** Si el atributo es un Value Object, el VO valida su propia invariante. El agregado no necesita un catálogo envoltorio para eso.

**Responsabilidades transversales duplicadas.** El estado activo/inactivo y conceptos similares que aplican a múltiples agregados pertenecen a un componente transversal, no se duplican en cada agregado.

**Catálogos genéricos que agrupan múltiples VOs.** Un catálogo que dice "debe tener nombre y documento válidos" agrupa dos validaciones de VOs distintos en una sola entrada. Cada VO es responsable de su propia invariante.

---

## Referencias

- ADR-51: Abandono del historial de catálogos eliminados (decisión de cierre)
- ADR-50: Jerarquía definitiva de excepciones y gobernanza de documentación
- ADR-22: Estrategia de Numeración (Superado por ADR-51)

- [ADR-(Arquitectura)-18-Simplificación general de jerarquía de excepciones en el dominio.md](ADR-%28Arquitectura%29-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md)
- [ADR-(Arquitectura)-19-Catálogo único de errores con contextos diferenciados (Entidad vs VO).md](ADR-%28Arquitectura%29-19-Cat%C3%A1logo%20%C3%BAnico%20de%20errores%20con%20contextos%20diferenciados%20%28Entidad%20vs%20VO%29.md)
- [ADR-(Arquitectura)-20-Alcance Experimental del Módulo Actor.md](ADR-%28Arquitectura%29-20-Alcance%20Experimental%20del%20M%C3%B3dulo%20Actor.md)
- [ADR-(Arquitectura)-21-Catálogos de errores por agregado con interfaz común.md](ADR-%28Arquitectura%29-21-Cat%C3%A1logos%20de%20errores%20por%20agregado%20con%20interfaz%20com%C3%BAn.md)

