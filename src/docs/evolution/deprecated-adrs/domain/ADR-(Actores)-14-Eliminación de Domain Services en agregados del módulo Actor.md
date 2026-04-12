# ADR-14 (Actores): Eliminación de Domain Services en agregados del módulo Actor

- **Estado:** Superado
- **Fecha:** 2025-12-11
- **Autor:** David Stiven Sanclemente

Nota: *“La eliminación total de Domain Services fue excesiva. Se mantienen Domain Services para operaciones de coordinación entre agregados [ver ADR-38](../../../architecture/decisions/arch/ADR-(Arquitectura)-38-UserDeactivationPolicy%20como%20orquestador%20de%20validaciones.md) y [ver ADR-39](../../../architecture/decisions/arch/ADR-(Arquitectura)-39-Ubicación%20de%20validaciones%20de%20desactivación.md).”*


## Contexto
En ADR-04 se decidió delegar la lógica de desactivación de `Dentist` a un **Domain Service**, dado que esa operación implicaba coordinación entre múltiples objetos del dominio, acceso a repositorios y efectos externos como persistencia y publicación de eventos.

Sin embargo, en el módulo **Actor** (que incluye los agregados `Dentist`, `Patient` y `Receptionist`), se identificó que gran parte de la lógica de negocio que se estaba delegando a Domain Services correspondía en realidad a **validaciones locales de invariantes**. Ejemplos:
- Validar que el usuario asociado esté activo.
- Verificar disponibilidad de horarios.
- Evitar citas duplicadas.
- Asegurar que un paciente menor tenga responsable asignado.

Estas reglas no requieren coordinación externa ni efectos secundarios, sino que son **invariantes propias de cada agregado**.

## Problema
El uso de Domain Services para encapsular reglas locales generaba:
- **Duplicación de lógica**: cada servicio repetía validaciones de `user.isActive()`.
- **Acoplamiento innecesario**: los agregados dependían de servicios para operaciones que podían resolver internamente.
- **Complejidad artificial**: se introducían clases adicionales sin aportar valor semántico.
- **Inconsistencia arquitectónica**: mientras ADR-04 justificaba Domain Services para operaciones compuestas, aquí se usaban para reglas simples.

## Decisión
Se elimina el uso de Domain Services en los agregados del módulo Actor (`Dentist`, `Patient`, `Receptionist`) para reglas locales.
- La validación de estado de usuario se encapsula en un **Value Object/Policy `UserStatus`**, reutilizable en todos los agregados.
- Cada agregado conserva sus métodos de negocio (`updateSensitiveData`, `scheduleAppointment`, `validateReschedule`, etc.) y protege sus invariantes directamente.
- Solo se mantiene el **AppointmentDomainService** como servicio transversal, encargado de orquestar reglas que involucran múltiples agregados (ej. agendar cita entre paciente, odontólogo y recepcionista).

## Justificación
- **Claridad semántica:** las reglas locales pertenecen al agregado, no a un servicio externo.
- **Protección de invariantes:** los agregados nacen y mutan siempre en estados válidos.
- **Simplicidad:** se reduce la verbosidad y el número de clases innecesarias.
- **Consistencia con ADR-04:** se mantiene la distinción:
    - Domain Services para operaciones compuestas con efectos externos (ej. desactivación de Dentist).
    - Agregados para reglas locales y validaciones internas.
- **Reutilización:** `UserStatus` centraliza la validación de estado activo, evitando duplicación.

## Consecuencias
- Los agregados del módulo Actor ya no dependen de Domain Services para validar estado de usuario o reglas simples.
- Se mejora la cohesión y se reduce el acoplamiento.
- El modelo es más exhibible y profesional, mostrando agregados ricos con lógica propia.
- Se mantiene la necesidad de Domain Services solo en operaciones transversales (ej. `AppointmentDomainService`).

## Plan de implementación
1. Eliminar clases de Domain Services redundantes en `Dentist`, `Patient` y `Receptionist`.
2. Introducir el VO/Policy `UserStatus` para encapsular validación de estado activo.
3. Refactorizar métodos de negocio en cada agregado para usar `UserStatus`.
4. Mantener y documentar `AppointmentDomainService` como servicio transversal.
5. Actualizar pruebas unitarias para validar invariantes directamente en los agregados.
6. Documentar esta decisión en `docs/dominio/actor/ADR-33-eliminacion-domain-service.md`.

## Ejemplo
```java
// Antes: validación en Domain Service
dentistService.updateSensitiveData(dentist, data, user);

// Ahora: validación directa en el agregado con VO
dentist.updateSensitiveData(data, UserStatus.from(user));
```


