## Documento: Motivos para la creación de la clase Schedule

## Contexto:
En el dominio clínico existen varias responsabilidades relacionadas con la gestión temporal: disponibilidades declaradas (TimeSlot / WeeklyAvailability), horas contractuales (WorkingHours), y la agenda operativa (Appointment). Estas responsabilidades tienden a entrecruzarse cuando se realizan validaciones compuestas, decisiones de negocio o acciones transaccionales (por ejemplo: determinar si se puede agendar una cita, comprobar conflictos, calcular ventanas de cancelación, o evaluar restricciones antes de desactivar un profesional).

## Decisión:
Crear la clase Schedule como un agregador/servicio del dominio responsable de orquestar las operaciones sobre la agenda del profesional. Schedule agrupa y encapsula la colección de citas (appointments) y la disponibilidad declarada (WeeklyAvailability o lista de TimeSlot) y expone operaciones semánticas y coherentes para el dominio.

## Motivos y justificación:
1. Separación de responsabilidades
    - Evita que la entidad Dentist se convierta en un objeto con muchas responsabilidades (anemia o exceso de lógica).
    - Mantiene en Dentist solo invariantes locales y delega reglas compuestas y coordinación temporal a Schedule.

2. Encapsulamiento de reglas compuestas
    - Reglas que dependen de múltiples datos (citas + time slots + working hours) se centralizan en un único objeto con API clara.
    - Facilita implementar reglas como "no agendar si hay conflicto", "hay citas en las próximas 24h", "cumple mínimo de horas semanales", o "calcular ventanas de reprogramación".

3. Mejora de testabilidad
    - Schedule puede probarse aisladamente con escenarios de agenda sin necesidad de construir toda la entidad Dentist ni mocks complejos.
    - Permite crear pruebas unitarias de reglas temporales y de conflicto con datos controlados.

4. Coherencia transaccional
    - Al centralizar la lógica de negocio relacionada con la agenda, es más simple orquestar operaciones atómicas o semi-atómicas desde un Domain Service (ej. desactivar dentist, cancelar citas, persistir cambios).
    - Facilita la aplicación de patrones como outbox o compensación porque el punto de decisión está centralizado.

5. Reutilización y claridad semántica
    - Otros actores (Receptionist, Guardian, Patient) o servicios (scheduling service) pueden reutilizar la misma lógica de agenda sin duplicación.
    - Mejora la expresividad del modelo: Schedule.canScheduleAt(...), Schedule.hasAppointmentsWithinHours(...), Schedule.upcomingWithinWindow(...).

6. Evolución del dominio
    - Permite extender reglas de agenda (prioridades, reservas bloqueadas, reglas regulatorias de jornada) sin impactar a la entidad principal.
    - Facilita añadir métricas, políticas de cancelación, políticas de notificación y outcomes derivados de operaciones de agenda.

## Responsabilidades recomendadas de Schedule
- Mantener la colección de Appointment asociadas y exponer acceso inmutable a ellas.
- Mantener o referenciar la WeeklyAvailability (o lista de TimeSlot) que define dónde pueden ubicarse las citas.
- Validar conflictos entre una solicitud de cita y las citas existentes.
- Calcular totales temporales relevantes (horas disponibles, horas ocupadas).
- Proveer filtros y consultas semánticas (citas futuras, próximas 24 horas, citas por día).
- Exponer operaciones de negocio auxiliares usadas por Domain Services (ej. retrieveAndCancelConflictingAppointments()).

## Contrato API sugerido (ejemplos)
- List<Appointment> upcomingWithinHours(int hours)
- boolean hasAppointmentsWithinHours(int hours)
- boolean canScheduleAt(LocalDateTime dateTime)
- List<Appointment> conflictingAppointmentsFor(TimeSlot slot) // o para un intervalo candidato
- int totalAvailableHours()
- boolean cumpleMinimoHoras(int minimo)
- List<Appointment> getAppointments()
- WeeklyAvailability getWeeklyAvailability()

## Consecuencias y consideraciones
- Positivo: menor acoplamiento, mayor cohesión, reglas temporales centralizadas, más fácil de testear y evolucionar.
- Requiere: definir claramente límite del aggregate; decidir si Schedule es un VO interno de Dentist, un agregado independiente o parte de una capa de dominio (según necesidades de persistencia y transaccionalidad).
- Integración: en operaciones que mutan el estado (cancelar citas, reasignar, desactivar), la orquestación debe pasar por un Domain Service que use Schedule para la lógica y los repositorios para persistir.

## Notas de implementación práctica
- Encapsular colecciones y devolver copias inmutables.
- Mantener Schedule libre de efectos colaterales (no hacer persistencia ni enviar notificaciones; solo calcular y orquestar decisiones).
- Implementar métodos idempotentes y deterministas para facilitar pruebas y reintentos.
- Si hay operaciones que afectan varias entidades, delegar la coordinación a un Domain Service que utilice Schedule como la fuente de verdad de reglas temporales.

## Resumen
Schedule existe para centralizar y dar coherencia a la lógica de agenda, reducir la carga cognitiva de la entidad Dentist, mejorar testabilidad y mantener el dominio preparado para evolución y operaciones transaccionales complejas. Su creación es una decisión pragmática para lograr un diseño más profesional, mantenible y semánticamente claro.