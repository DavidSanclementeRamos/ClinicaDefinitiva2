

# ADR-17 (Actores): Modelado del agregado Recepcionista frente al Dentista

- **Fecha**: 2026-02-09
- **Estado**:  Aprobado
- **Categoría**: Dominio — Lecciones aprendidas
- **Autor:** David Stiven Sanclemente

---

## Contexto

En las primeras fases de migración del módulo **Actor**, se modelaron tanto el **Dentista** como el **Recepcionista**.  
El Dentista recibió un modelado rico:
- Estado de disponibilidad (Disponible, Vacaciones, Incapacidad).
- Jornada laboral para validar turnos operativos.
- Validaciones de cobertura de citas y ausencias.

El Recepcionista, en cambio, se dejó más **pobre**: sin estados de disponibilidad ni jornada laboral. La decisión inicial se tomó porque se consideraba que la disponibilidad del recepcionista no afectaba directamente la atención clínica ni la cobertura de citas.

---

## Problema

Con el tiempo surgió la duda:
- ¿Debe el Recepcionista tener el mismo modelado de disponibilidad y jornada laboral que el Dentista?
- ¿Se pierde consistencia arquitectónica al dejarlo más liviano?

---

## Decisión

Se decidió **mantener al Recepcionista más simple**, documentando que la diferencia es **intencional** y responde al rol de cada actor en el dominio:

- **Dentista**: su disponibilidad y jornada laboral son **críticas** para el agendamiento de citas y turnos operativos.
- **Recepcionista**: su rol es **administrativo** y de soporte; no impacta directamente la atención clínica ni la cobertura de citas.

Por lo tanto:
- El Recepcionista no tendrá estados de disponibilidad ni jornada laboral en el agregado.
- Su modelado se centrará en: asignación de sector/sucursal, permisos de operación y trazabilidad administrativa.
- Si en el futuro se requiere validar presencia de recepcionistas (ej. métricas, auditoría), se podrá modelar como **atributo opcional** o en un módulo de infraestructura, sin afectar el núcleo de dominio.

---

## Alternativas descartadas

| Alternativa | Razón de descarte |
|-------------|------------------|
| Modelar disponibilidad y jornada laboral igual que el Dentista | No aporta valor al dominio clínico; el recepcionista no afecta cobertura de citas. |
| Ignorar completamente al Recepcionista | Se pierde trazabilidad administrativa y control de permisos. |

---

## Consecuencias

- **Ganancia**: el modelo se mantiene claro y enfocado en lo que realmente impacta la atención clínica.
- **Costo**: el agregado Recepcionista parece más “pobre” comparado con el Dentista, pero esto es correcto semánticamente.
- **Flexibilidad futura**: si se requiere modelar disponibilidad administrativa, se puede añadir sin romper el diseño actual.

---

## Reflexión

Esta decisión nace de reconocer que **no todos los agregados deben tener la misma riqueza**. El Dentista necesita estados y jornada laboral porque impacta la atención clínica; el Recepcionista no. Documentar esta diferencia evita confusión futura y muestra que la simplicidad también es una decisión consciente en DDD.

