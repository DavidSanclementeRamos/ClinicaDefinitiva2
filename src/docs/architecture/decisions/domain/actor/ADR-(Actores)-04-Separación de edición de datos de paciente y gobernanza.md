# ADR-04 (Actores): Separación de edición de datos de paciente y gobernanza por rol con validación de citas próximas

- Estado: Aprobado
- Fecha: 2025-10-07
- Autor: David Stiven Sanclemente

## Contexto
La edición de datos de paciente requiere equilibrio entre operación ágil y legitimidad clínica/auditable.  
Se distingue entre:
- Datos administrativos blandos: teléfono, dirección, email.
- Datos identitarios sensibles: nombre legal, documento, fecha de nacimiento, seguro.

Cambios sensibles cerca de citas pueden generar inconsistencias, fraude o pérdida de trazabilidad.  
Se busca una decisión mínima viable que permita cerrar el dominio principal (Actor, Schedule, Roles, Administración) con evolución planificada hacia auditoría completa.

## Decisión
- Separación de métodos:
  - editarDatosContacto: teléfono, dirección, email (sin restricciones fuertes).
  - editarDatosSensibles: nombre legal, documento, fecha de nacimiento, seguro (con permisos y validaciones).
- Gobernanza por rol:
  - Paciente: solo datos de contacto.
  - Administrativo: datos sensibles, con auditoría obligatoria.
- Validación temporal:
  - Si el paciente tiene citas en los próximos 2 días, se bloquea la edición de datos sensibles.
- Trazabilidad mínima viable:
  - Se registra lastUpdatedAt en Patient.
  - Se difiere el historial detallado para evolución futura auditable.

## Roles y permisos
| Rol            | Campos editables                           | Condiciones                        | Auditoría requerida |
|----------------|--------------------------------------------|------------------------------------|---------------------|
| Paciente       | Teléfono, dirección, email                 | Sin bloqueo por citas               | lastUpdatedAt     |
| Administrativo | Nombre legal, documento, nacimiento, seguro| Bloqueo si hay citas en ≤ 2 días   | Usuario, fecha, valor anterior/nuevo, motivo |
| Clínico        | Ninguno (solo consulta)                    | N/A                                | N/A                 |

## Reglas de validación y contratos de métodos
- Editar datos de contacto
  - Permiso: paciente y administrativo.
  - Sin bloqueo por citas.
  - Actualiza lastUpdatedAt.

- Editar datos sensibles
  - Permiso: solo administrativo autorizado.
  - Bloqueo si existen citas en ≤ 2 días.
  - Requiere justificación y registro en auditoría.
  - Actualiza lastUpdatedAt.

## Contratos sugeridos
`plaintext
editarDatosContacto(pacienteId, telefono?, direccion?, email?)
editarDatosSensibles(pacienteId, cambios, justificacion)
`

## Trazabilidad y auditoría
- MVP actual: atributo lastUpdatedAt en la entidad Patient.
- Propósito: simplicidad y frescura de datos.
- Evolución futura: entidad PatientUpdateLog con:
  - PacienteId
  - Campo
  - ValorAnterior / ValorNuevo
  - UsuarioId / Rol
  - Fecha
  - Motivo
  - CitaProximaImpactada (opcional)

## Consecuencias
- Beneficios: claridad semántica, reducción de riesgo, trazabilidad mínima sin complejidad excesiva.
- Costes: historial limitado en MVP, migración futura necesaria.
- Mitigación: ADR documenta evolución planificada, contratos facilitan implementación progresiva.

## Alternativas consideradas
- Permitir que el paciente edite datos sensibles → Riesgo alto, descartado.
- Bloquear toda edición si hay citas próximas → Demasiado restrictivo, descartado.
- Solo métrica de conteo (updateCount) → Insuficiente, descartado como única medida.

Ejemplo
```java
public void editarDatosSensibles(Map<String,Object> cambios, String justificacion) {
    if (hasAppointmentsWithinHours(48)) {
        throw new SensitiveDataUpdateBlockedException(
            "Paciente con citas próximas, edición sensible bloqueada"
        );
    }
    applyChanges(cambios);
    this.lastUpdatedAt = LocalDateTime.now();
    auditLog.record(this.id, cambios, justificacion);
}
```

## Relación con otros ADR
- [ADR-(Actores)-06-Validar responsable en paciente.md](ADR-%28Actores%29-06-Validar%20responsable%20en%20paciente.md)
- [ADR-(Actores)-05-Representación TypeGuardian Vo híbrido.md](ADR-%28Actores%29-05-Representaci%C3%B3n%20TypeGuardian%20Vo%20h%C3%ADbrido.md)
- [ADR-(Dominio)-01-Implementación de Value-Objects.md](../ADR-%28Dominio%29-01-Implementaci%C3%B3n%20de%20Value-Objects.md)
  