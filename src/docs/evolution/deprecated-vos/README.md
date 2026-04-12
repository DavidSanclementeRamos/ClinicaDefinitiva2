# Value Objects obsoletos – documentación histórica

Esta carpeta contiene archivos Markdown que documentaban **individualmente** cada Value Object del dominio durante las primeras etapas del proyecto.

**Estos documentos no reflejan el estado actual del sistema.**  
Se conservan únicamente como **evidencia del proceso de aprendizaje** y de la evolución del diseño.

## 🚨 Advertencia

- **No utilizar estos archivos como referencia para el código actual.**
- **Muchos de los VOs descritos han cambiado, sido renombrados o eliminados.**
- **Las validaciones y reglas de negocio que aparecen aquí pueden estar desactualizadas o ser incorrectas.**

## 📌 ¿Dónde está la documentación vigente?

La documentación activa de los Value Objects se encuentra en:

- **Índice general:** [README.md](../../dominio/vo/README.md)
- **Código fuente:** Paquete `com.example.ClinicaDefinitiva.domain.*.vo` (y `*.model` para detalles de composición)

**El código es la única fuente de verdad.**  
Cada VO implementa sus validaciones en el constructor o métodos de fábrica (`of`, `from`, `valueOf`).

## 🔄 Diferencias entre documentación antigua y estado actual

| Aspecto | Documentación antigua (esta carpeta) | Estado actual del sistema |
|---------|--------------------------------------|---------------------------|
| **Cobertura** | Intentaba documentar cada VO por separado | Solo se mantiene un índice general |
| **Exhaustividad** | Muchos VOs quedaron sin documentar | El código es la referencia completa |
| **Precisión** | Desactualizada, con errores y omisiones | El código siempre está sincronizado |
| **VOs obsoletos** | Incluye VOs que ya no existen (ej. `WeeklyAvailability`, `Schedule`, `AvailabilityStatus`) | Esos VOs fueron eliminados del código |
| **VOs vigentes** | Pueden tener nombres o validaciones distintas | Coinciden exactamente con el código |

## 📁 ¿Qué VOs obsoletos están documentados aquí?

Los archivos que se encuentran en esta carpeta corresponden a VOs que **ya no forman parte del modelo activo** o cuya implementación ha cambiado drásticamente. Algunos ejemplos:

- `WeeklyAvailability` – Eliminado, su lógica se integró en `Shift` y `ScheduleQueryService`
- `Schedule` – Transformado en `ScheduleQueryService`
- `AvailabilityStatus` – Eliminado al consolidar `Shift` como única fuente de verdad temporal
- Otros VOs que pueden haber sido renombrados o cuyas validaciones ya no coinciden

**Si un VO documentado aquí aún existe en el código, su documentación individual está desactualizada y no debe ser consultada.**

## 🧠 Propósito de conservar estos archivos

- **Mostrar la evolución del diseño** – desde intentos de documentación exhaustiva hasta un enfoque pragmático centrado en el código.
- **Evidencia de aprendizaje** – documentar decisiones tempranas y cómo se refinaron con el tiempo.
- **Contexto histórico** – para entender por qué ciertos VOs desaparecieron o cambiaron.

No se actualizarán ni corregirán. Son una cápsula del tiempo del proyecto en sus fases iniciales.

---

**Última actualización:** 2026-04-08  
**Mantenedor:** David Stiven Sanclemente