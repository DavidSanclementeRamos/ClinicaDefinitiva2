# ADR-04 (Cita): Sustitución de retorno booleano por excepciones semánticas en validación de agenda

- Estado: Aprobado
- Fecha: 2025-10-08
- Autor: David Stiven Sanclemente

## Contexto
El método canScheduleBetween(start, end) devolvía un boolean indicando si un intervalo podía ser agendado.  
Este enfoque obligaba a la capa orquestadora (Appointment) a interpretar el resultado, mezclando responsabilidades y reduciendo la trazabilidad semántica de los errores.

## Decisión
Se reemplaza el retorno booleano por un método validateScheduleBetween(start, end) que lanza excepciones semánticas en caso de fallo:

- InvalidScheduleException → Fechas inválidas (nulos, orden incorrecto).
- SlotNotCoveredException → Intervalo fuera de la disponibilidad declarada.
- SlotAlreadyTakenException → Intervalo solapado con otra cita.

De este modo:
- La validación es explícita y auditable.
- Appointment solo orquesta, sin lógica condicional.
- Cada error queda representado por un artefacto legítimo y exhibible.

## Justificación
- Trazabilidad: cada excepción tiene un nombre y mensaje claros, útiles para auditoría y comunicación con usuarios.
- Separación de responsabilidades: la validación vive en Schedule; Appointment se limita a orquestar.
- Extensibilidad: nuevas reglas de validación pueden añadirse como nuevas excepciones sin romper la semántica existente.

## Consecuencias
 Positivas
- Mayor claridad y trazabilidad ética.
- Código más expresivo y auditable.
- Extensible a nuevas reglas.

 Negativas
- Se introduce complejidad en el manejo de excepciones (try/catch).
- Riesgo de sobrecarga si se abusa de excepciones para flujos esperados.

## Plan de implementación
1. Refactorizar Schedule para implementar validateScheduleBetween(start, end).
2. Crear excepciones semánticas en domain.schedule.exceptions.
3. Actualizar Appointment para invocar validateScheduleBetween en lugar de interpretar booleanos.
4. Documentar regla en docs/dominio/reglas-de-negocio/schedule.md.
5. Añadir pruebas unitarias para escenarios:
    - Fechas inválidas → InvalidScheduleException.
    - Intervalo fuera de disponibilidad → SlotNotCoveredException.
    - Intervalo solapado → SlotAlreadyTakenException.

## Ejemplo de uso
```java
try {
    schedule.validateScheduleBetween(newStart, newEnd);
    appointment.reschedule(newStart, newEnd);
} catch (InvalidScheduleException | SlotNotCoveredException | SlotAlreadyTakenException e) {
    throw new BusinessRuleException(e.getMessage(), e);
}
```

