## ADR-03 (Dominio): Mantener y refinar upcomingWithinHours como consulta de soporte

- Estado: Aprobado
- Fecha: 2025-10-06
- Autor: David

## Contexto
Inicialmente se consideró eliminar el método upcomingWithinHours(int hours) por no representar una regla de negocio legítima.  
Sin embargo, se identificó que este método es utilizado en la regla de negocio Dentist.deactivate(), donde se valida que un odontólogo no pueda desactivarse si tiene citas en las próximas 24 horas.

## En este contexto:
- El método no es una regla de negocio en sí mismo, sino una consulta de soporte.
- Su rol es habilitar reglas más amplias (hasAppointmentsWithinHours, Dentist.deactivate).
- Su existencia mejora la legibilidad y exhibibilidad de las reglas.

## Decisión
Mantener el método, pero con los siguientes ajustes:
1. Renombrar a findAppointmentsWithinHours(int hours) para reflejar que es un query.
2. Delegar la validación de solapamientos a TimeIntervalRules.overlaps(...) en lugar de solo filtrar por start.
3. Mantener hasAppointmentsWithinHours(int hours) como método booleano que delega en la consulta.
4. Documentar explícitamente que este método es un query de conveniencia, no una regla de negocio.

## Justificación
- Claridad semántica: reglas como Dentist.deactivate() se expresan con mayor legibilidad.
- Evita duplicación: reutiliza TimeIntervalRules para solapamientos.
- Trazabilidad ética: cada agregado conserva sus propias validaciones; este método solo expone información.
- Convención clara: queries devuelven colecciones, reglas devuelven booleanos o lanzan excepciones.

## Consecuencias
- Se conserva la claridad en reglas como Dentist.deactivate().
- Se evita duplicación de lógica de solapamiento.
- Se refuerza la trazabilidad ética y la arquitectura hexagonal.
- Se establece convención clara entre queries y reglas.

## Plan de implementación
1. Renombrar upcomingWithinHours a findAppointmentsWithinHours.
2. Refactorizar lógica interna para usar TimeIntervalRules.overlaps(...).
3. Implementar hasAppointmentsWithinHours(int hours) como método booleano que delega en la consulta.
4. Actualizar todas las llamadas existentes en el código.
5. Documentar la convención en docs/dominio/reglas-de-negocio/agendamiento.md.
6. Añadir pruebas unitarias para ambos métodos.

## Ejemplo de uso
```java
public List<Appointment> findAppointmentsWithinHours(int hours) {
TimeInterval interval = TimeInterval.of(
LocalDateTime.now(),
LocalDateTime.now().plusHours(hours)
);
return appointments.stream()
.filter(a -> TimeIntervalRules.overlaps(a.getInterval(), interval))
.toList();
}

public boolean hasAppointmentsWithinHours(int hours) {
return !findAppointmentsWithinHours(hours).isEmpty();
}

// Uso en Dentist.deactivate()
if (schedule.hasAppointmentsWithinHours(24)) {
throw new PendingAppointmentsWithin24HoursException(
ContextoEntidad.DENTIST,
"Tiene citas pendientes en las próximas 24 horas"
);
}
```

## Referencias
- Dentist.deactivate()
- TimeIntervalRules.overlaps(...)
- Convención: Queries (find...) vs Reglas (can..., has..., validate...)

## Relación con otros ADR
- ADR-12 (Dominio): Eliminación del método upcomingWithinHours(int hours).
- ADR-08 (Dominio): Refactorización semántica con canScheduleAt(...).
- ADR-07 (Dominio): Delegación semántica para validar estado de usuario en agendamiento.  
  `

