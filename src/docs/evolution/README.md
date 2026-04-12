

# Material histórico y evolución del proyecto

Esta carpeta contiene documentos que representan **etapas anteriores** del desarrollo.  
No reflejan el estado actual del sistema, sino el **proceso de aprendizaje**, las **decisiones reconsideradas** y los **artefactos obsoletos**.

Se conservan por su valor pedagógico y como evidencia de la madurez arquitectónica adquirida.

---

## 📁 Estructura

```
evolution/
├── README.md                           # Este archivo
├── deprecated-adrs/                    # ADRs superados por decisiones posteriores
├── lessons-learned/                    # Retrospectivas y lecciones de implementación
├── initial-domain-discoveries/         # Primeras reglas de negocio (muchas ya no aplican)
└── deprecated-vos/                     # Documentación antigua de Value Objects (desactualizada)
```

---

## 🗂️ Subcarpetas

### `deprecated-adrs/`
ADRs que fueron **reemplazados o superados** por decisiones más recientes.  
Ejemplos:
- ADR-02, ADR-03, ADR-04  de Arquitectura → superados por ADR-18 (simplificación de excepciones)
- ADR-22, ADR-23 de Arquitectura → superados por ADR-53 (abandono del historial de catálogos)

**No utilizar como guía para el diseño actual.**  
Para decisiones vigentes, consultar [`../architecture/decisions/`](../architecture/decisions/).

### `lessons-learned/`
Documentos que **no son ADRs clásicos**, sino retrospectivas o lecciones aprendidas.  
Incluyen:
- Alcances experimentales de módulos (Actor, Schedule, Services) – ADR-20, ADR-24, ADR-29
- Evolución de validaciones y corrección de errores – ADR-16 (actores), ADR-09 (citas)
- Historia del diseño inicial de autorización (ADR-49)

Estos documentos muestran **cómo se pensó el sistema en distintas fases** y por qué se tomaron ciertos caminos. Son valiosos para entender la evolución.

### `initial-domain-discoveries/`
Archivos generados durante las primeras fases de modelado del dominio.  
Contienen reglas de negocio hipotéticas, muchas de las cuales fueron descartadas, refinadas o reubicadas (ej. validaciones que luego pasaron a Value Objects).

**No representan el modelo actual.**  
Las reglas vigentes están en el código y en los ADRs activos.

### `deprecated-vos/`
Documentación Markdown antigua que describía Value Objects individualmente (ej. `Value-object-AddressDirecion.md`).  
Esta práctica se abandonó por insostenible; el código es ahora la única fuente de verdad.

Los VOs activos se listan en [`../domain/vo/README.md`](../domain/vo/README.md).

---

## ⚠️ Advertencia

> **El contenido de esta carpeta no debe usarse como referencia para el estado actual del sistema.**  
> Para documentación vigente, consultar:
> - ADRs activos: [`../architecture/decisions/`](../architecture/decisions/)
> - Guías de implementación: [`../guides/`](../guides/)
> - Value Objects activos: [`../domain/vo/README.md`](../domain/vo/README.md)

---

## 🧠 ¿Por qué se conserva este material?

- **Evolución documentada:** muestra cómo las decisiones iniciales (a veces incorrectas o ingenuas) fueron refinadas con el tiempo.
- **Aprendizaje:** sirve como referencia para no repetir errores y para entender el contexto de las soluciones actuales.
- **Honestidad profesional:** un portafolio que oculta el proceso de aprendizaje es menos creíble que uno que lo exhibe con transparencia.

---

**Última actualización:** 2026-04-08  
**Mantenedor:** David Stiven Sanclemente
```

