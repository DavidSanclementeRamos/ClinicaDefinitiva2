## Documento: Diferenciación semántica entre TimeSlot y WorkingHours

## Contexto:
En el modelo clínico, existen dos clases que representan rangos horarios: TimeSlot y WorkingHours. Aunque ambas contienen atributos similares (día de la semana, hora de inicio y fin), su propósito semántico es distinto y responde a necesidades diferentes dentro del sistema.

## Diferencias semánticas:

1. TimeSlot (Operacional):
    - Representa bloques de tiempo donde el profesional está disponible para realizar actividades clínicas, como atender citas.
    - Es dinámico, puede cambiar semana a semana según disponibilidad, vacaciones o ajustes operativos.
    - Se utiliza para validar si una cita puede agendarse, calcular duración de atención, detectar solapamientos y verificar disponibilidad futura.
    - Está ligado a la operación diaria y a la gestión de agendas.

2. WorkingHours (Contractual):
    - Representa el horario laboral formal que el profesional ha declarado como su jornada oficial.
    - Es más estable, definido por contrato o acuerdo institucional.
    - Se utiliza para validar si una acción ocurre dentro del horario permitido, verificar cumplimiento de jornada y establecer límites administrativos.
    - Está ligado a la planificación institucional, cumplimiento ético y trazabilidad laboral.

## Consecuencias:
- Mantener ambas clases permite diferenciar entre disponibilidad operativa y compromiso contractual.
- Evita confusión semántica y permite aplicar reglas éticas distintas en cada contexto.
- Facilita trazabilidad y auditoría clínica, al poder verificar si una acción fue realizada dentro del horario permitido o fuera de él.

---

## Métodos recomendados por clase:

TimeSlot (operacional):
- int duracionHoras()
- boolean seSolapaCon(TimeSlot otro)
- boolean tinecitasFuturas() // si aplica
- DayOfWeek getDayOfWeek()
- LocalTime getInicio()
- LocalTime getFin()

WorkingHours (contractual):
- boolean isWithin(LocalDateTime dateTime, DayOfWeek day)
- LocalTime Start()
- LocalTime End()
- DayOfWeek getDayOfWeek()
- Duration duracionTotal() // opcional, para trazabilidad
- boolean cubre(TimeSlot slot) // opcional, para validación cruzada

## Notas:
- Se recomienda no mezclar responsabilidades entre ambas clases.
- Si se requiere validación cruzada (ej. verificar si un TimeSlot está dentro del WorkingHours), se puede implementar un método como boolean cubre(TimeSlot slot) dentro de WorkingHours.
- Ambas clases pueden coexistir en el modelo clínico, siempre que se respete su semántica y se documente su propósito.

Fin del documento.