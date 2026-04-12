## Índice de ADRs — Catálogo de Errores Surgical
Validaciones de complejidad y requisitos
ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH

Regla: RN-SURGICAL-001

Descripción: Si requiere anestesia, el nivel de complejidad debe ser al menos MEDIUM.

Fichero: ADR-XX-ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH.md

ERR_SURGICAL_INVALID_COMPLEXITY

Regla: RN-SURGICAL-003

Descripción: El nivel de complejidad debe ser: LOW, MEDIUM, HIGH o CRITICAL.

Fichero: ADR-XX-ERR_SURGICAL_INVALID_COMPLEXITY.md

ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS

Regla: RN-SURGICAL-004

Descripción: Cirugías CRITICAL deben requerir anestesia y quirófano.

Fichero: ADR-XX-ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS.md

ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH

Regla: RN-SURGICAL-007

Descripción: Cirugías que requieren quirófano deben tener complejidad al menos MEDIUM.

Fichero: ADR-XX-ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH.md

Duración, tipo y advertencias
ERR_SURGICAL_OPERATING_ROOM_DURATION_MISMATCH

Regla: RN-SURGICAL-002

Descripción: Si requiere quirófano, la duración del servicio debe ser al menos 60 minutos.

Fichero: ADR-XX-ERR_SURGICAL_OPERATING_ROOM_DURATION_MISMATCH.md

ERR_SURGICAL_TYPE_TOO_SHORT

Regla: RN-SURGICAL-006

Descripción: El tipo de cirugía debe tener al menos 3 caracteres si se especifica.

Fichero: ADR-XX-ERR_SURGICAL_TYPE_TOO_SHORT.md

WARN_SURGICAL_LOW_COMPLEXITY_OPERATING_ROOM

Regla: RN-SURGICAL-005

Descripción: Cirugías de baja complejidad no suelen requerir quirófano (advertencia).

Fichero: ADR-XX-WARN_SURGICAL_LOW_COMPLEXITY_OPERATING_ROOM.md