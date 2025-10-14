# ADR: Separación de edición de datos de paciente y gobernanza por rol con validación de citas próximas

## Contexto
La edición de datos de paciente requiere equilibrio entre operación ágil y legitimidad clínica/auditable.  
Se distingue entre datos **administrativos blandos** (teléfono, dirección, email) y datos **identitarios sensibles** (nombre legal, documento, fecha de nacimiento, seguro).  
Cambios sensibles cerca de citas pueden generar inconsistencias, fraude o pérdida de trazabilidad.  
Se busca una decisión mínima viable que permita cerrar el dominio principal (Actor, Schedule, Roles, Administración) con evolución planificada hacia auditoría completa.

## Decisión
- **Separación de métodos**:
    - `editarDatosContacto`: teléfono, dirección, email (sin restricciones fuertes).
    - `editarDatosSensibles`: nombre legal, documento, fecha de nacimiento, seguro (con permisos y validaciones).
- **Gobernanza por rol**:
    - Paciente: solo datos de contacto.
    - Administrativo: datos sensibles, con auditoría obligatoria.
- **Validación temporal**:
    - Si el paciente tiene citas en los próximos 2 días, se bloquea la edición de datos sensibles.
- **Trazabilidad mínima viable**:
    - Se registra `lastUpdatedAt` en Paciente.
    - Se difiere el historial detallado para una evolución futura auditable.

## Roles y permisos

| Rol              | Campos editables                         | Condiciones                        | Auditoría requerida |
|------------------|------------------------------------------|------------------------------------|---------------------|
| Paciente         | Teléfono, dirección, email               | Sin bloqueo por citas               | `lastUpdatedAt`     |
| Administrativo   | Nombre legal, documento, nacimiento, seguro | Bloqueo si hay citas en ≤ 2 días   | Usuario, fecha, valor anterior/nuevo, motivo |
| Clínico          | Ninguno (solo consulta)                  | N/A                                | N/A                 |

## Reglas de validación y contratos de métodos

- **Editar datos de contacto**
    - Permiso: paciente y administrativo.
    - Sin bloqueo por citas.
    - Actualiza `lastUpdatedAt`.

- **Editar datos sensibles**
    - Permiso: solo administrativo autorizado.
    - Bloqueo si existen citas en ≤ 2 días.
    - Requiere justificación y registro en auditoría.
    - Actualiza `lastUpdatedAt`.

### Contratos sugeridos
```plaintext
editarDatosContacto(pacienteId, telefono?, direccion?, email?)
editarDatosSensibles(pacienteId, cambios, justificacion)
```
## Trazabilidad y auditoría
• 	MVP actual
• 	Atributo lastUpdateAt en la entidad Paciente.
• 	Propósito: simplicidad y frescura de datos.
• 	Evolución futura
• 	Entidad PatientUpdateLog con:
• 	PacienteId
• 	Campo
• 	ValorAnterior / ValorNuevo
• 	UsuarioId / Rol
• 	Fecha
• 	Motivo
• 	CitaProximaImpactada (opcional)
## Consecuencias
• 	Beneficios
• 	Claridad semántica y reducción de riesgo.
• 	Trazabilidad mínima sin complejidad excesiva.
• 	Costes
• 	Historial limitado en MVP.
• 	Migración futura necesaria.
• 	Mitigación
• 	ADR documenta la evolución planificada.
• 	Contratos definidos facilitan implementación progresiva.
## Alternativas consideradas
• 	Permitir que el paciente edite datos sensibles → Riesgo alto, descartado.
• 	Bloquear toda edición si hay citas próximas → Demasiado restrictivo, descartado.
• 	Solo métrica de conteo (updateCount) → Insuficiente, descartado como única medida.
## Estado
 -	Estado: Aceptada (provisional, MVP con lastUpdateLog).
- Fecha: 2025-10-7
 -	Revisión: Al preparar exhibición clínica, migrar a PatientUpdateLog.