```
// Fecha de inicio de vacaciones
LocalDate vacationStart = LocalDate.of(2026, 2, 10);

// Hora de inicio de turno
LocalTime shiftStart = LocalTime.of(9, 0);

// Cita con fecha y hora (sin zona horaria)
LocalDateTime appointment = LocalDateTime.of(2026, 2, 10, 9, 30);

// Instante absoluto en UTC
Instant now = Instant.now();

// Fecha y hora con zona horaria
ZonedDateTime zoned = ZonedDateTime.of(appointment, ZoneId.of("America/Bogota"));

```