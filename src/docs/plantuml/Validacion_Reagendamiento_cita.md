# Diagrama de Flujo: Validación de Reagendamiento de Cita

```mermaid
flowchart TD

A[Solicitud de reagendamiento<br/> (Appointment, newStart, newEnd)] --> B[Validar estado de la cita]
B -->|No está Scheduled o no es vigente| X1[Rechazo: Cita no válida]
B -->|OK| C[Validar identidad]

C -->|Paciente distinto| X2[Rechazo: La cita no pertenece al paciente]
C -->|Odontólogo distinto| X3[Rechazo: La cita no pertenece al odontólogo]
C -->|OK| D[Validar actividad de actores]

D -->|Paciente inactivo| X4[Rechazo: Paciente inactivo]
D -->|Odontólogo inactivo| X5[Rechazo: Odontólogo inactivo]
D -->|OK| E[Validar horario laboral odontólogo]

E -->|Fuera de WorkingHours| X6[Rechazo: Fuera del horario laboral]
E -->|OK| F[Validar agenda con Schedule]

F -->|Conflicto de citas| X7[Rechazo: Horario no disponible]
F -->|OK| G[Validar políticas temporales]

G -->|Menos de X horas de anticipación| X8[Rechazo: No cumple tiempo mínimo]
G -->|Más de N meses en el futuro| X9[Rechazo: Fuera de ventana máxima]
G -->|OK| H[Reagendamiento permitido]
```
---

## 📍 Explicación de nodos
- *Estado de la cita: debe estar *Scheduled y ser vigente.
- *Identidad*: paciente y odontólogo deben coincidir con los de la cita original.
- *Actividad*: paciente y odontólogo deben estar activos.
- *Horario laboral*: el nuevo intervalo debe estar dentro de WorkingHours del odontólogo.
- *Agenda*: Schedule.canScheduleBetween debe confirmar que no hay conflictos.
- *Políticas temporales*: cumplir con anticipación mínima y ventana máxima.
- *Resultado*: si todas las validaciones pasan, el reagendamiento es permitido.

---

👉 Con este diagrama, tu ADR queda aún más exhibible: puedes mostrar tanto el *checklist textual* como el *flujo visual* de validaciones.

¿Quieres que te prepare también un *ejemplo de ADR extendido* que incluya este diagrama embebido junto con el checklist, para que quede todo en un solo documento de decisión?