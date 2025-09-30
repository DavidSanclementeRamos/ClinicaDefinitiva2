

## ADR-009: Delegación semántica para validar estado de usuario en agendamiento

## Contexto  
En el proceso de agendamiento de citas clínicas, es necesario validar que el odontólogo esté activo. El estado del odontólogo depende del estado de su usuario, que puede estar activo, suspendido, eliminado o inactivo. Para evitar acoplamiento entre Appointment y el modelo de usuario, se decidió encapsular esta lógica mediante delegación semántica.

## Decisión  
Se implementó un Value Object llamado UserStatus que define la semántica del estado del usuario. El método isActive() encapsula la lógica de validación. Luego, se delega esta validación desde UserModel hacia UserStatus, y desde Dentist hacia UserModel, permitiendo que Appointment consulte únicamente a Dentist.

## Cadena de delegación:

`
Appointment → Dentist → UserModel → UserStatus
`

## Justificación

- Encapsulación semántica: Cada clase conoce solo lo que necesita.
- Reducción de acoplamiento: Appointment no depende del modelo de usuario.
- Reusabilidad: El VO UserStatus puede usarse en otros contextos del sistema.
- Trazabilidad: Permite auditar por qué una cita fue rechazada.
- Exhibición profesional: El diseño comunica claramente la intención del modelo.

## Consecuencias

- Se mejora la expresividad del código.
- Se facilita la migración del modelo al inglés.
- Se habilita la documentación viva del modelo clínico.