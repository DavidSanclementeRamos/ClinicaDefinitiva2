# Mapa de responsabilidades en DDD

## 1. Métodos en el Agregado
- *Qué son*: Comportamientos que protegen las invariantes internas del agregado.
- *Cuándo usarlos*:
    - La regla depende *solo del estado del agregado*.
    - La operación modifica o valida entidades/VOs que viven dentro del agregado.
- *Ejemplo*:
    - Schedule.validateScheduleBetween(start, end)
    - Appointment.reschedule(newStart, newEnd)

➡ El agregado es el *único guardián de su consistencia*.

---

## 2. Servicios de Dominio
- *Qué son: Objetos sin estado que encapsulan reglas de negocio que **no pertenecen naturalmente a un solo agregado*.
- *Cuándo usarlos*:
    - La regla involucra *varios agregados*.
    - La lógica es de negocio puro, pero no encaja como método en una entidad/VO.
- *Ejemplo*:
    - RescheduleAppointmentService que coordina:
        - doctorSchedule.validateScheduleBetween(...)
        - roomSchedule.validateScheduleBetween(...)
        - appointment.reschedule(...)

➡ El servicio de dominio *orquesta reglas entre agregados* sin tener estado propio.

---

## 3. Servicios de Aplicación
- *Qué son: Capas externas que coordinan **casos de uso* y conectan el dominio con infraestructura (repositorios, mensajería, APIs).
- *Cuándo usarlos*:
    - Para iniciar un caso de uso desde la capa de aplicación.
    - Para coordinar transacciones, persistencia, envío de eventos, etc.
- *Ejemplo*:
    - AppointmentApplicationService.scheduleAppointment(command)
        - Carga el agregado desde el repositorio.
        - Llama a schedule(...) en el agregado.
        - Persiste los cambios.
        - Publica un evento de dominio.

➡ El servicio de aplicación *no contiene reglas de negocio*, solo orquesta el flujo entre dominio e infraestructura.

---

## 📌 Resumen visual

- *Agregado* → protege sus invariantes internas.
- *Servicio de Dominio* → encapsula reglas de negocio que cruzan agregados.
- *Servicio de Aplicación* → coordina casos de uso y conecta con infraestructura.