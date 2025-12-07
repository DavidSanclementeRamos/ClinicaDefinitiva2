# ✅ Checklist de Validaciones de Reagendamiento

## 1. Estado de la cita
- [x] **Debe estar programada (Scheduled)**
    - Validación: `original.isScheduled()`
    - Fuente: `Appointment` (estado interno).

- [x] **Debe ser vigente (no pasada ni en curso)**
    - Validación: `original.validateIsFuture()` o equivalente.
    - Fuente: `Appointment` (cálculo de vigencia).

---

## 2. Identidad
- [x] **Mismo paciente**
    - Validación: `original.getPatient().equals(patient)`
    - Fuente: `Appointment`.

- [x] **Mismo odontólogo**
    - Validación: `original.getDentist().equals(dentist)`
    - Fuente: `Appointment`.

---

## 3. Actividad de actores
- [x] **Paciente activo**
    - Validación: `patient.validateReschedule()`
    - Fuente: `Patient`.

- [x] **Odontólogo activo y en horario laboral**
    - Validación: `dentist.validateReschedule(newStart, newEnd)`
    - Fuente: `Dentist.canWorkBetween(...)` → `WorkingHours.isWithinRange(...)`.

---

## 4. Agenda y disponibilidad
- [x] **Sin conflictos de horario**
    - Validación: `schedule.canScheduleBetween(newStart, newEnd)`
    - Fuente: `Schedule.canScheduleBetween(...)`.

- [x] **Disponibilidad neta en el día** *(opcional, más granular)*
    - Validación: `schedule.getTotalAvailableTime(newStart.getDayOfWeek())`
    - Fuente: `Schedule.getTotalAvailableTime(...)`.

---

## 5. Políticas temporales
- [x] **Tiempo mínimo de anticipación**
    - Validación:
      ```java
      schedule.findAppointmentsWithinHours(MIN_HOURS_BEFORE_RESCHEDULE).contains(original)
      ```  
    - Fuente: `Schedule.findAppointmentsWithinHours(...)`.

- [x] **Ventana máxima de reagendamiento**
    - Validación:
      ```java
      schedule.findAppointmentsWithin(MAX_MONTHS_AHEAD * 30).contains(original)
      ```  
    - Fuente: `Schedule.findAppointmentsWithin(...)`.

---

## 6. Otras reglas opcionales
- [ ] **Capacidad diaria** (ej. máximo N citas por día)
    - Validación: `schedule.findAppointmentsOn(newStart.toLocalDate())`
    - Fuente: `Schedule.findAppointmentsOn(...)`.

- [ ] **Carga de trabajo total** (ej. no superar X horas de atención en un día)
    - Validación: `schedule.getTotalOccupiedTime()`
    - Fuente: `Schedule.getTotalOccupiedTime(...)`.

---

## 🎯 Beneficios
- **Trazabilidad**: cada regla de negocio está ligada a un método concreto.
- **Exhibible**: puede documentarse como contrato de diseño.
- **Evolutivo**: si cambian las políticas, solo ajustas la query o constante, no la lógica central.