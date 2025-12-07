# ADR-04 (Dominio): Delegación de la lógica de desactivación de Dentist a un Domain Service

- **Estado:** Aprobado
- **Fecha:** 2025-09-30
- **Autor:** David

## Contexto
La operación de desactivación de un profesional clínico (`Dentist`) implica múltiples validaciones y efectos:
- Verificar que no existan citas activas en las próximas 24 horas.
- Evaluar si hay citas futuras.
- Cambiar el estado del usuario asociado.
- Actualizar el estado de disponibilidad.
- Cancelar o reprogramar citas.
- Emitir notificaciones o eventos.

Inicialmente, parte de esta lógica vivía dentro de la entidad `Dentist` como un método `deactivate()`. Esto hacía que la entidad asumiera responsabilidades que excedían su rol como agregador de invariantes.

## Problema
La lógica de desactivación involucra coordinación entre múltiples objetos del dominio (`UserModel`, `Appointment`, `AvailabilityStatus`), acceso a repositorios y efectos secundarios como persistencia y publicación de eventos.  
Esto rompe el principio de separación de responsabilidades, dificulta la testabilidad y acopla la entidad a detalles de infraestructura.

## Decisión
Se delega la lógica de desactivación a un **Domain Service** especializado, responsable de orquestar la operación completa.  
La entidad `Dentist` conserva únicamente las reglas locales que definen si puede o no ser desactivada (ej. `hasAppointmentsWithinHours(24)`), pero no ejecuta directamente la mutación ni los efectos externos.

## Justificación
- **Separación de responsabilidades:** La entidad mantiene sus invariantes; el servicio coordina efectos y persistencia.
- **Testabilidad:** El servicio puede probarse con mocks y escenarios controlados.
- **Transaccionalidad:** El servicio define el límite de la transacción, asegurando consistencia entre múltiples repositorios.
- **Evolución:** Permite agregar nuevas reglas o compensaciones sin modificar la entidad.
- **Observabilidad y auditoría:** El servicio puede registrar eventos y manejar errores centralizadamente.
- **Consistencia semántica:** La desactivación es una operación compuesta que afecta estado clínico, operativo y contractual; su coordinación pertenece al servicio.

## Consecuencias
- La entidad `Dentist` expone métodos como `canBeDeactivated()` o `hasAppointmentsWithinHours(int)`.
- El Domain Service encapsula la lógica completa de desactivación, incluyendo validaciones, persistencia y publicación de eventos.
- Se mejora la cohesión del modelo y se reduce el acoplamiento.
- Las pruebas unitarias se simplifican y las operaciones críticas se documentan como flujos transaccionales.

## Plan de implementación
1. Crear `DentistDeactivationService` en `com.clinica.domain.service`.
2. Definir métodos:
    - `deactivateDentist(DentistId id)`
    - Internamente: validar citas, actualizar estados, persistir cambios, publicar eventos.
3. Refactorizar entidad `Dentist` para exponer solo reglas locales (`canBeDeactivated`).
4. Integrar con catálogo de errores clínicos (ver ADR-03).
5. Añadir pruebas unitarias y de integración para escenarios de desactivación.
6. Documentar operación en `docs/dominio/reglas-de-negocio/dentist.md`.

## Ejemplo
```java
if (dentist.canBeDeactivated()) {
    dentistDeactivationService.deactivateDentist(dentistId);
} else {
    throw new ClinicalValidationException(ERR_DENTIST_NO_DESACTIVABLE);
}
```
## Relación con otros ADR 
- ADR-02 (Dominio): Implementación estratégica de Value Objects.
- 	ADR-03 (Arquitectura): Catálogo de errores clínicos por operación.
- 	ADR-04 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- 	ADR-05 (Dominio): Consolidación semántica de horarios clínicos.
## Notas
Este patrón puede extenderse a otras operaciones clínicas complejas como reprogramación masiva, activación, reasignación de turnos o cierre de agenda. Cada una puede vivir en su propio servicio de dominio, respetando los límites semánticos del modelo.