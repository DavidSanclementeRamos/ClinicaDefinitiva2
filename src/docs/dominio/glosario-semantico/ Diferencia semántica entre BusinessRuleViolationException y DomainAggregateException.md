
---
# ADR- Diferencia semántica entre BusinessRuleViolationException y DomainAggregateException

**Fecha:** 2025-12-07  
**Estado:** Aceptado (con posibilidad de evolución futura)

---

## Contexto
Tras la simplificación de la jerarquía de excepciones en el dominio (ver [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](../../arquitetura/adr/ADR-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md)), surgió la necesidad de clarificar la **diferencia semántica** entre dos tipos de excepciones que permanecen en el modelo:

- **BusinessRuleViolationException**
- **DomainAggregateException**

Ambas forman parte de la nueva jerarquía simplificada, pero cumplen roles distintos en la semántica del dominio. La falta de diferenciación clara podría generar confusión en el equipo de desarrollo y en la interpretación de los errores.

---

## Decisión
Se establece la siguiente diferenciación semántica:

- **BusinessRuleViolationException**
    - Representa la **violación de una regla de negocio** dentro de un agregado o contexto.
    - Se utiliza cuando una operación o acción incumple una restricción explícita del dominio.
    - Ejemplo:
      ```java
      throw new BusinessRuleViolationException(
          "Dentist",
          "MinimumAgeRule",
          "El odontólogo no cumple la edad mínima para ser registrado."
      );
      ```

- **DomainAggregateException**
    - Representa una **condición inválida o inconsistente en el estado global de un agregado**.
    - Se utiliza cuando el agregado en su conjunto no cumple con las invariantes necesarias para existir o operar correctamente.
    - Ejemplo:
      ```java
      throw new DomainAggregateException(
          "Patient",
          "ResponsibleAssignment",
          "Paciente requiere un responsable asignado."
      );
      ```

---

## Diferencia Semántica

| Aspecto                        | BusinessRuleViolationException                          | DomainAggregateException                          |
|--------------------------------|---------------------------------------------------------|--------------------------------------------------|
| **Nivel de aplicación**         | Regla puntual dentro de un agregado                     | Estado global del agregado                        |
| **Naturaleza del error**        | Violación de una restricción de negocio                 | Inconsistencia estructural o falta de invariante  |
| **Ejemplo típico**              | Edad mínima, formato de datos, disponibilidad           | Paciente sin responsable, cita sin fechas válidas |
| **Granularidad**                | Fina (regla específica)                                | Gruesa (agregado completo)                        |
| **Uso recomendado**             | Validar acciones y operaciones                          | Validar consistencia del agregado                 |

---

## Consecuencias
- **Claridad semántica**: El equipo distingue cuándo usar cada excepción según el tipo de error.
- **Consistencia**: Se mantiene una jerarquía coherente que refleja reglas vs. invariantes.
- **Escalabilidad**: Nuevas reglas o invariantes pueden expresarse sin necesidad de crear clases adicionales.
- **Mantenibilidad**: Se evita confusión y se facilita la lectura del código y los mensajes de error.

---

## Relacionados
- [ADR-18-Simplificación general de jerarquía de excepciones en el dominio.md](ADR-18-Simplificación%20general%20de%20jerarquía%20de%20excepciones%20en%20el%20dominio.md)
- [ADR-03-Jerarquía global de excepciones y excepciones para Value Objects de Persona.md](ADR-03-Jerarquía%20global%20de%20excepciones%20y%20excepciones%20para%20Value%20Objects%20de%20Persona.md)
- [ADR-04-Jerarquía de excepciones para valores faltantes en Value Objects.md](ADR-04-Jerarquía%20de%20excepciones%20para%20valores%20faltantes%20en%20Value%20Objects.md)

---

