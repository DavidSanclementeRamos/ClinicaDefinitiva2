# Plantilla ADR

Usar esta plantilla para todos los ADRs futuros. Eliminar las secciones en cursiva antes de publicar.

---

# ADR-XXX: Título conciso que describe la decisión

- **Fecha**: YYYY-MM-DD
- **Estado**: [Propuesto | Aprobado | Rechazado | Deprecado | Reemplazado por ADR-YYY]
- **Categoría**: [Arquitectura | Dominio]

---

## Problema

*Descripción del problema que motivó esta decisión.*
*Máximo 2-3 párrafos. Ser específico.*

**Preguntas a responder:**
- ¿Qué problema resuelve este ADR?
- ¿Por qué es importante resolverlo ahora?
- ¿Qué pasa si no se resuelve?

**Ejemplo:**
```
El agregado Schedule coordina Appointment y Availability. Expone queries 
semánticas como hasAppointmentsWithinHours() que encapsulan lógica de negocio.

No está claro si debemos consultar directamente un AppointmentRepository 
o usar ScheduleRepository para obtener toda la agenda.
```

---

## Decisión

*Describir la decisión tomada de manera clara y concisa.*
*Incluir una regla o criterio de decisión si aplica.*

**Formato recomendado:**
```
[Acción concreta]

Regla: [Criterio de decisión claro]

[Código o diagrama si es necesario]
```

**Ejemplo:**
```
Usar ScheduleRepository como punto de acceso único a la agenda de un dentista.

Regla: Siempre que la lógica de negocio involucre tanto citas como 
disponibilidad, obtener el Schedule completo desde infraestructura.
```

---

## Alternativas descartadas

*Tabla comparativa de las opciones que NO se eligieron y por qué.*

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| [Opción A] | [Razón concreta] |
| [Opción B] | [Razón concreta] |
| [Opción C] | [Razón concreta] |

**Ejemplo:**
```
| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Consultar AppointmentRepository directamente | Rompe cohesión del agregado |
| Tener ambos repositorios | Duplicación innecesaria |
| Exponer Appointment como agregado raíz | No tiene sentido sin Schedule |
```

---

## Consecuencias

*Lo que GANAMOS y lo que PERDEMOS con esta decisión.*
*Ser honesto sobre los trade-offs.*

### Ganamos
- [Beneficio 1]
- [Beneficio 2]
- [Beneficio 3]

### Perdemos
- [Costo 1]
- [Costo 2]
- [Costo 3]

**Ejemplo:**
```
Ganamos:
- Cohesión del modelo: Schedule es el agregado raíz
- Queries semánticas trazables
- Validaciones centralizadas

Perdemos:
- Queries más pesadas: reconstruir Schedule completo es costoso
- No optimizado para reportes analíticos puros
```

---

## Implementación

*Solo incluir esta sección si la implementación es compleja o no obvia.*
*Código mínimo necesario para entender la decisión.*

```java
// Ejemplo de implementación
public interface ScheduleRepository {
    Optional<Schedule> findByDentistId(DentistId dentistId);
}

public void deactivateDentist(DentistId dentistId) {
    Schedule schedule = scheduleRepository
        .findByDentistId(dentistId)
        .orElseThrow();
    
    if (schedule.hasAppointmentsWithinHours(24)) {
        throw new BusinessRuleViolationException(/* ... */);
    }
}
```

---

## Notas adicionales

*Sección opcional para información adicional:*
- Contexto histórico relevante
- Referencias a otros ADRs relacionados
- Casos especiales o excepciones
- Planes de evolución futura

**Ejemplo:**
```
Excepción para reportes:
Para queries analíticas puras donde NO se requiere lógica de negocio,
sí puede existir un read model separado que consulte Appointment directamente.
```

---

## Checklist antes de aprobar

- [ ] El título es conciso y describe claramente la decisión
- [ ] El problema está bien definido (≤3 párrafos)
- [ ] La decisión es clara y tiene una regla o criterio
- [ ] Se documentan las alternativas descartadas
- [ ] Se listan tanto beneficios como costos
- [ ] No hay texto genérico ("separación de responsabilidades", "mantenibilidad")
- [ ] No hay referencias innecesarias a "equipo" o "nuevos desarrolladores"
- [ ] La implementación (si existe) es mínima y clara
- [ ] El estado está actualizado (Propuesto/Aprobado/Rechazado)

---

## Guía de categorías

### Arquitectura
Decisiones sobre estructura técnica del sistema:
- Separación de capas
- Manejo de excepciones
- Patrones de persistencia
- Comunicación entre módulos
- Estrategias de testing

### Dominio
Decisiones sobre modelado del negocio:
- Diseño de agregados
- Reglas de negocio
- Value Objects
- Domain Services
- Relaciones entre entidades

---

## Anti-patrones a evitar

### ❌ Título vago
```
❌ ADR: Refactorización de UseCase y propagación de cambios
✅ ADR: Paginación obligatoria en relaciones 1:N
```

### ❌ Problema genérico
```
❌ "Necesitamos mejorar la arquitectura"
✅ "Los métodos de UseCase devuelven un solo objeto aunque 
    la relación es 1:N, causando pérdida de datos"
```

### ❌ Decisión sin criterio
```
❌ "Usar mappers según convenga"
✅ "Regla: Mapper estático si ≤3 atributos, inyectado si >3 o composición"
```

### ❌ Solo beneficios (sin trade-offs)
```
❌ "Ganamos: todo | Perdemos: nada"
✅ Listar beneficios Y costos reales
```

### ❌ Texto genérico
```
❌ "Esta decisión mejora la mantenibilidad y separación de responsabilidades"
✅ Explicar CÓMO mejora algo específico del sistema
```

### ❌ Código excesivo
```
❌ 200 líneas de código en la implementación
✅ Snippets clave (10-30 líneas) que ilustran la decisión
```