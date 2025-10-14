# ADR: Mantener y refinar upcomingWithinHours como consulta de soporte

## Estado
- Estado: Decidido
- Fecha: 2025-10-6

## Contexto
Inicialmente se consideró eliminar el método upcomingWithinHours(int hours) por no representar una regla de negocio legítima.  
Sin embargo, se identificó que este método es utilizado en la regla de negocio Dentist.deactivate(), donde se valida que un odontólogo no pueda desactivarse si tiene citas en las próximas 24 horas.

En este contexto:
- El método no es una regla de negocio en sí mismo, sino una *consulta de soporte*.
- Su rol es habilitar reglas más amplias (hasAppointmentsWithinHours, Dentist.deactivate).
- Su existencia mejora la legibilidad y exhibibilidad de las reglas.

## Decisión
Mantener el método, pero con los siguientes ajustes:

1. *Renombrar* a findAppointmentsWithinHours(int hours) para reflejar que es un query.
2. *Delegar la validación de solapamientos* a TimeIntervalRules.overlaps(...) en lugar de solo filtrar por start.
3. Mantener hasAppointmentsWithinHours(int hours) como método booleano que delega en la consulta.
4. Documentar explícitamente que este método es un *query de conveniencia*, no una regla de negocio.

## Consecuencias
- Se conserva la claridad en reglas como Dentist.deactivate().
- Se evita la duplicación de lógica de solapamiento, reutilizando TimeIntervalRules.
- Se refuerza la trazabilidad ética: cada agregado conserva sus propias validaciones, y este método se limita a exponer información.
- Se establece una convención clara: *queries devuelven colecciones, **reglas devuelven booleanos o lanzan excepciones*.

## Ejemplo de uso
```java
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
  `

---