

# ADR-07 (Dominio): Delegación semántica para validar estado de usuario en agendamiento

- Estado: Aprobado
- Fecha: 2025-10-15
- Autor: David

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
- ADR-02 (Dominio): Implementación estratégica de Value Objects.
- ADR-04 (Dominio): Delegación de la lógica de desactivación de Dentist a un Domain Service.
- ADR-05 (Dominio): Mantener mutación local en Dentist y delegar coordinación externa.
- ADR-06 (Dominio): Ubicación del patrón Builder en la entidad Dentist.  
  

