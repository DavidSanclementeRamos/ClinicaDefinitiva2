
# ADR-04 (Dominio): Inquietud sobre el rol de los Servicios de Dominio frente a métodos en Agregados

- Estado: Aprobado
- Fecha: 2025-10-08
- Autor: David Stiven Sanclemente

## Contexto
Durante el diseño del método reagendar en la entidad Appointment, surgió la duda sobre la utilidad real de los Servicios de Dominio.

Situación:
- Tanto el Paciente como el Odontólogo agrupan sus reglas de reagendamiento en métodos propios de sus agregados.
- El agregado Schedule expone reglas para validar disponibilidad y solapamientos.
- Appointment.reagendar() actúa como orquestador de reglas, invocando a los agregados responsables antes de mutar su estado.

Esto genera la inquietud:
> Si cada agregado valida lo suyo y Appointment coordina, ¿cuál es entonces la verdadera función de los Servicios de Dominio?

## Decisión
- Es correcto que cada agregado valide sus propias invariantes mediante métodos internos.
- Es legítimo que Appointment.reagendar() actúe como orquestador de esas validaciones, siempre que no absorba lógica que pertenece a otros agregados.
- Los Servicios de Dominio no son necesarios en este caso, porque la operación de reagendar se resuelve dentro de un conjunto de agregados relacionados (Paciente, Odontólogo, Schedule).
- Los Servicios de Dominio se reservarán para operaciones que:
  - Involucren varios agregados raíz sin relación directa.
  - Expresen reglas de negocio que no tienen un “hogar natural” en una entidad o agregado.
  - Requieran coordinación transversal (ej. agenda del odontólogo + sala de procedimientos + facturación).

## Consecuencias
Positivas
- Se mantiene la claridad semántica: cada regla vive en su agregado legítimo.
- Appointment conserva un rol de coordinador natural de su propio ciclo de vida.
- Se evita crear servicios de dominio innecesarios, reduciendo complejidad artificial.

Negativas
- Puede persistir la confusión conceptual sobre cuándo usar un servicio de dominio.
- Riesgo de que Appointment absorba demasiada orquestación si no se vigila la frontera entre coordinación y lógica transversal.

## Plan de implementación
1. Documentar criterios de uso de servicios de dominio en docs/dominio/servicios.md.
2. Refactorizar Appointment.reagendar() para mantener solo coordinación legítima.
3. Revisar periódicamente que las reglas transversales se ubiquen en servicios de dominio cuando corresponda.
4. Añadir pruebas de integración para validar que Appointment no absorba lógica de otros agregados.

Ejemplo
```java
public void reagendar(LocalDateTime newStart, LocalDateTime newEnd) {
this.validateScheduledAndActive();
this.patient.validateActive();
this.dentist.validateActive();
this.dentist.canWorkBetween(newStart, newEnd);
this.schedule.validateScheduleBetween(newStart, newEnd);

    this.start = newStart;
    this.end = newEnd;
}
```


