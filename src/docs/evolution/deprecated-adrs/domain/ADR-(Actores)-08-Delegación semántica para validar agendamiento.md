

# ADR-08 (Actores): Delegación semántica para validar estado de usuario en agendamiento

- Estado: Superado por [ADR-(Cita)-09](../../../architecture/decisions/domain/schedule/ADR-%28Cita%29-09-Ubicaci%C3%B3n%20de%20validaciones%20de%20estado%20y%20disponibilidad%20en%20el%20m%C3%B3dulo%20de%20citas.md)
- Fecha: 2025-10-15
- Autor: David Stiven Sanclemente

## Contexto
En el proceso de agendamiento de citas clínicas, es necesario validar que el odontólogo esté activo.  
El estado del odontólogo depende del estado de su usuario, que puede estar activo, suspendido, eliminado o inactivo.

Para evitar acoplamiento entre Appointment y el modelo de usuario, se decidió encapsular esta lógica mediante delegación semántica.

## Decisión
Se implementó un Value Object llamado UserStatus que define la semántica del estado del usuario.  
El método isActive() encapsula la lógica de validación.

La validación se delega siguiendo esta cadena:

`
Appointment → Dentist → UserModel → UserStatus
`

De esta forma, Appointment consulta únicamente a Dentist, sin depender directamente del modelo de usuario.

## Justificación
- Encapsulación semántica: cada clase conoce solo lo que necesita.
- Reducción de acoplamiento: Appointment no depende del modelo de usuario.
- Reusabilidad: el VO UserStatus puede usarse en otros contextos del sistema.
- Trazabilidad: permite auditar por qué una cita fue rechazada.
- Exhibición profesional: el diseño comunica claramente la intención del modelo.

## Consecuencias
- Se mejora la expresividad del código.
- Se facilita la migración del modelo al inglés.
- Se habilita la documentación viva del modelo clínico.

## Plan de implementación
1. Crear VO UserStatus en com.clinica.domain.vo.
2. Definir estados (ACTIVE, SUSPENDED, DELETED, INACTIVE) y método isActive().
3. Refactorizar UserModel para delegar validación a UserStatus.
4. Refactorizar Dentist para delegar validación a UserModel.
5. Refactorizar Appointment para consultar únicamente a Dentist.
6. Añadir pruebas unitarias para cada estado de usuario.
7. Documentar reglas en docs/dominio/reglas-de-negocio/agendamiento.md.

## Ejemplo
```java
if (!dentist.isActive()) {
    throw new ClinicalValidationException(ERRAPPOINTMENTDENTIST_INACTIVE);
}
```

## Relación con otros ADR

- [ADR-(Cita)-09-Ubicación de validaciones de estado y disponibilidad en el módulo de citas.md](../../../architecture/decisions/domain/schedule/ADR-%28Cita%29-09-Ubicaci%C3%B3n%20de%20validaciones%20de%20estado%20y%20disponibilidad%20en%20el%20m%C3%B3dulo%20de%20citas.md)  

