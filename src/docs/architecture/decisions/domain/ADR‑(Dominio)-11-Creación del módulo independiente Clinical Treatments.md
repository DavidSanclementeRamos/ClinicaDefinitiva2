

# ADR‑11 (Dominio): Creación del módulo independiente Clinical Treatments

- **Estado:** Aprobado
- **Fecha:** 2026‑02‑18
- **Autor:** David Stiven Sanclemente
- **Categoría:** Dominio Clínico

---

## Contexto

El agregado `Treatment` estaba ubicado en el módulo *Dental Services*, junto al catálogo de servicios odontológicos. Sin embargo, esta ubicación generaba problemas semánticos y de trazabilidad:

- Los **servicios odontológicos** representan procedimientos ofertados (consultas, limpiezas, ortodoncia, etc.).
- Los **tratamientos clínicos** representan procesos longitudinales (diagnóstico, evolución, estado, asignaciones).
- Mezclar ambos en el mismo módulo diluye la separación entre catálogo de servicios y procesos clínicos.
- Además, el uso de `VOContext.DENTAL_SERVICES` para excepciones de `TreatmentId` resultaba poco semántico: un tratamiento no es un servicio, es parte del dominio clínico.

---

## Problema

- Ubicación incorrecta de `Treatment` dentro de *Dental Services*.
- Confusión semántica entre catálogo de servicios y procesos clínicos.
- Trazabilidad deficiente en excepciones y VOContext.

---

## Decisión

Se crea un módulo independiente **Clinical Treatments**, separado de *Dental Services* y *Administration*, para modelar procesos clínicos longitudinales.

- `Treatment` y sus VOs se trasladan a *Clinical Treatments*.
- Se actualiza `VOContext` → `VOContext.CLINICAL_TREATMENTS`.
- *Dental Services* conserva únicamente el catálogo de servicios odontológicos.
- *Clinical Treatments* se convierte en el lugar para diagnósticos, asignaciones y evolución clínica.

---

## Alternativas descartadas

| Alternativa | Razón de descarte |
|-------------|-------------------|
| Mantener `Treatment` en *Dental Services* | Mezcla catálogo de servicios con procesos clínicos, rompe semántica. |
| Ubicar `Treatment` en *Administration* | Administración gestiona convenios y roles, no procesos clínicos. |
| Crear submódulo dentro de *Dental Services* | Sigue acoplando servicios y tratamientos, no resuelve el problema. |

---

## Consecuencias

### Ganamos
- Separación clara entre catálogo de servicios y procesos clínicos.
- Trazabilidad semántica correcta: `VOContext.CLINICAL_TREATMENTS`.
- Escalabilidad: el módulo puede crecer hacia historias clínicas, diagnósticos y evolución.
- Claridad arquitectónica: lo clínico separado de lo administrativo y financiero.

### Perdemos
- Incremento en el número de módulos a mantener.
- Necesidad de definir interfaces claras entre *Clinical Treatments* y *Dental Services* (ej. un tratamiento referencia servicios).


---

## Relación con otros ADR

- [ADR-(Arquitectura)-05-Creación de un módulo independiente para Servicios.md](../arch/ADR-%28Arquitectura%29-05-Creaci%C3%B3n%20de%20un%20m%C3%B3dulo%20independiente%20para%20Servicios.md)