## ADR-010: Delegación de la lógica de desactivación de Dentist a un Domain Service

## Contexto:
La operación de desactivación de un profesional clínico (Dentist) implica múltiples validaciones y efectos: verificar que no existan citas activas en las próximas 24 horas, evaluar si hay citas futuras, cambiar el estado del usuario asociado, actualizar el estado de disponibilidad, cancelar o reprogramar citas, y emitir notificaciones o eventos. Inicialmente, parte de esta lógica vivía dentro de la entidad Dentist como un método llamado deactivate().

## Problema:
La entidad Dentist comenzaba a asumir responsabilidades que exceden su rol como agregador de invariantes. La lógica de desactivación involucra coordinación entre múltiples objetos del dominio (UserModel, Appointment, AvailabilityStatus), acceso a repositorios, y efectos secundarios como persistencia y publicación de eventos. Esto rompe el principio de separación de responsabilidades, dificulta la testabilidad, y acopla la entidad a detalles de infraestructura.

## Decisión:
Se delega la lógica de desactivación a un Domain Service especializado, responsable de orquestar la operación completa. Este servicio valida las reglas clínicas, coordina los cambios de estado, interactúa con los repositorios, y publica los eventos necesarios. La entidad Dentist conserva únicamente las reglas locales que definen si puede o no ser desactivada (por ejemplo: hasAppointmentsWithinHours(24)), pero no ejecuta directamente la mutación ni los efectos externos.

## Justificación:
- *Separación de responsabilidades*: La entidad mantiene sus invariantes; el servicio coordina efectos y persistencia.
- *Testabilidad*: El servicio puede probarse con mocks y escenarios controlados sin necesidad de construir la entidad completa.
- *Transaccionalidad*: El servicio define el límite de la transacción, asegurando consistencia entre múltiples repositorios.
- *Evolución*: Permite agregar nuevas reglas, efectos o compensaciones sin modificar la entidad.
- *Observabilidad y auditoría*: El servicio puede registrar eventos, emitir outcomes y manejar errores de forma centralizada.
- *Consistencia semántica*: La desactivación es una operación compuesta que afecta el estado clínico, operativo y contractual del profesional; su coordinación pertenece al servicio, no a la entidad.

## Consecuencias:
- La entidad Dentist expone métodos como canBeDeactivated() o hasAppointmentsWithinHours(int) para ser consultada por el servicio.
- El Domain Service encapsula la lógica completa de desactivación, incluyendo validaciones, persistencia y publicación de eventos.
- Se mejora la cohesión del modelo, se reduce el acoplamiento, y se habilita una evolución legítima del sistema clínico.
- Las pruebas unitarias se simplifican, y las operaciones críticas se documentan como flujos transaccionales.

## Notas:
Este patrón puede extenderse a otras operaciones clínicas complejas como reprogramación masiva, activación, reasignación de turnos, o cierre de agenda. Cada una puede vivir en su propio servicio de dominio, respetando los límites semánticos del modelo.

Fin del documento.