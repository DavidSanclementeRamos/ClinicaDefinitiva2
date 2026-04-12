# ADR-07 (Cita): Consolidación de Shift como única fuente de verdad temporal

- **Estado:** Aprobado
- **Fecha:** 2026-02-07
- **Reemplaza:** [ADR-(Cita)-01-Motivos creación clase Schedule.md](../../../../evolution/deprecated-adrs/domain/ADR-%28Cita%29-01-Motivos%20creaci%C3%B3n%20clase%20Schedule.md)
- **Autor:** David Stiven Sanclemente

## Contexto
Actualmente existen tres conceptos solapados:
- **WorkingHours:** Horario contractual (recurrente)
- **Shift:** Turno operativo (fecha específica)
- **Availability:** Bloques de atención (recurrente)

Esto causa:
- Validación triple: WorkingHours → Shift → Availability
- Ambigüedad: ¿Qué pasa si Shift existe pero Availability no?
- Complejidad: 3 agregados para modelar "¿puede el dentista atender ahora?"

## Decisión
**Eliminar Availability** y consolidar toda la lógica temporal en **Shift**.

**Modelo resultante:**
```
WorkingHours (VO) → valida coherencia contractual
↓
Shift (Agregado) → única fuente de verdad operativa
↓
Appointment (Agregado) → debe estar dentro de un Shift
```
**Shift se extiende con:**
```java
public class Shift {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<ExcludedBlock> excludedBlocks; // ✅ NUEVO
    
    public void excludeBlock(LocalTime start, LocalTime end, String reason) {
        // Bloquear almuerzo, reuniones, etc.
    }
    
    public boolean canAccommodateAppointment(LocalDateTime start, LocalDateTime end) {
        return coversInterval(start, end) 
            && !fallsInExcludedBlock(start, end);
    }
}
```

## Alternativas Descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Mantener Availability | Redundante con Shift + ExcludedBlocks |
| Fusionar en WorkingHours | WorkingHours es contractual, no operativo |
| Crear AvailabilityWindow | Añade otro nivel de abstracción innecesario |

## Consecuencias

**Positivas:**
- 1 concepto menos (Availability desaparece)
- 1 validación menos (ensureAvailabilityCoverage eliminado)
- Claridad: "Un dentista tiene turnos. Las citas caen en turnos."
- Flexibilidad: ExcludedBlocks modelan restricciones específicas

**Negativas:**
- Migración de datos: Availability → Shift con ExcludedBlocks
- Requiere actualizar AppointmentSchedulingService

