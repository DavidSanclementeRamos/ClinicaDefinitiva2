# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: AdministrativeReport (Reporte Administrativo)

## Propósito
Representar reportes administrativos y financieros que consolidan información contable, indicadores de gestión y documentos adjuntos. Este agregado gestiona el ciclo de vida del reporte (borrador → revisión → publicado → archivado) y protege la integridad de la información gerencial.

---

## CREACIÓN
- Debe tener título válido (Name).
- Debe especificarse período de reporte (Period).
- Debe registrar usuario creador (UserId).
- Se asigna estado inicial DRAFT por defecto.
- Fecha de creación se registra automáticamente.
- Listas de referencias, indicadores y adjuntos se inicializan vacías.
- Última actualización se registra automáticamente.

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si está en estado DRAFT.
- No puede editarse si está en revisión, publicado o archivado.
- Puede agregarse/removerse referencias a asientos contables.
- Puede agregarse/removerse indicadores de gestión.
- Puede agregarse/removerse documentos adjuntos.
- Cada modificación actualiza automáticamente lastUpdate.
- Cambios en título y notas requieren estado editable.

---

## FLUJO DE ESTADOS

**DRAFT (Borrador)**
- Estado inicial al crear el reporte.
- Permite todas las modificaciones.
- Puede enviarse a revisión si está completo.

**UNDER_REVIEW (En Revisión)**
- Solo accesible desde DRAFT.
- No permite modificaciones de contenido.
- Solo pueden agregarse/removerse adjuntos.
- Puede aprobarse o rechazarse.

**PUBLISHED (Publicado)**
- Solo accesible desde UNDER_REVIEW.
- Inmutable (no permite modificaciones).
- Solo pueden agregarse adjuntos adicionales.
- Registra usuario aprobador y fecha.
- Puede archivarse.

**ARCHIVED (Archivado)**
- Estado final del reporte.
- Totalmente inmutable.
- Puede desarchivarse solo a DRAFT.

---

## OPERACIONES DE DOMINIO
- addJournalEntryReference() → Agrega referencia a asiento contable.
- removeJournalEntryReference() → Remueve referencia a asiento.
- addIndicator() → Agrega indicador de gestión.
- removeIndicator() → Remueve indicador.
- addAttachment() → Agrega documento adjunto.
- removeAttachment() → Remueve documento adjunto.
- updateInformation() → Actualiza título y notas.
- submitForReview() → Envía reporte a revisión.
- approve(aprobador) → Aprueba y publica el reporte.
- reject(motivo) → Rechaza y devuelve a borrador.
- archive() → Archiva el reporte.
- unarchive() → Restaura reporte archivado a borrador.
- isComplete() → Verifica si tiene contenido mínimo.
- isEditable() → Verifica si permite modificaciones.

---

## INVARIANTES GLOBALES
- Un reporte debe tener al menos un asiento contable O un indicador para enviarse a revisión.
- Un reporte publicado es inmutable excepto adjuntos.
- Un reporte archivado es totalmente inmutable.
- No puede aprobarse si no está en revisión.
- No puede rechazarse sin motivo obligatorio.
- Solo puede archivarse si está publicado.
- Debe tener título, período y creador válidos.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de estado con fecha y usuario.
- Se registra usuario aprobador y fecha de aprobación.
- Se registra motivo de rechazo en notas del reporte.
- Sistema emite alertas al intentar modificar reporte no editable.
- Auditoría completa de referencias, indicadores y adjuntos.
- Integración con asientos contables y métricas de gestión.

---

## Justificación Semántica
Estas reglas aseguran que los reportes administrativos sean coherentes, trazables y auditables. Protegen la integridad de información gerencial crítica, evitan modificaciones no autorizadas post-publicación y permiten flujo de aprobación formal. El modelo está listo para exhibición internacional y cumplimiento normativo.

---

## Reglas Descubiertas (formato estandarizado)

**RN-ADMINREPORT-001**
- Descripción: Solo puede editarse si está en estado DRAFT.
- Condición: AdministrativeReport.status != DRAFT al invocar operaciones de edición.
- Consecuencia: Se rechaza operación con InvalidReportStatusException.
- Error asociado: ERR_REPORT_NOT_EDITABLE

**RN-ADMINREPORT-002**
- Descripción: Debe tener contenido mínimo para enviar a revisión.
- Condición: journalEntryReferences.isEmpty() && indicators.isEmpty() al invocar submitForReview().
- Consecuencia: Se rechaza operación con InvalidAdministrativeReportException.
- Error asociado: ERR_REPORT_INCOMPLETE

**RN-ADMINREPORT-003**
- Descripción: Solo puede enviarse a revisión desde DRAFT.
- Condición: AdministrativeReport.status != DRAFT al invocar submitForReview().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_REPORT_CANNOT_SUBMIT

**RN-ADMINREPORT-004**
- Descripción: Solo puede aprobarse si está en revisión.
- Condición: AdministrativeReport.status != UNDER_REVIEW al invocar approve().
- Consecuencia: Se rechaza operación con InvalidReportStatusException.
- Error asociado: ERR_REPORT_CANNOT_APPROVE

**RN-ADMINREPORT-005**
- Descripción: Solo puede rechazarse si está en revisión.
- Condición: AdministrativeReport.status != UNDER_REVIEW al invocar reject().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_REPORT_CANNOT_REJECT

**RN-ADMINREPORT-006**
- Descripción: El rechazo requiere motivo obligatorio.
- Condición: reject(reason) con reason == null || reason.isBlank().
- Consecuencia: Se rechaza operación con InvalidAdministrativeReportException.
- Error asociado: ERR_REPORT_REJECTION_REQUIRES_REASON

**RN-ADMINREPORT-007**
- Descripción: Solo puede archivarse si está publicado.
- Condición: AdministrativeReport.status != PUBLISHED al invocar archive().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_REPORT_CANNOT_ARCHIVE

**RN-ADMINREPORT-008**
- Descripción: Solo puede desarchivarse si está archivado.
- Condición: AdministrativeReport.status != ARCHIVED al invocar unarchive().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_REPORT_CANNOT_UNARCHIVE

**RN-ADMINREPORT-009**
- Descripción: No puede agregarse referencia duplicada a asiento contable.
- Condición: journalEntryReferences.contains(journalEntryId) == true al invocar addJournalEntryReference().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_REPORT_DUPLICATE_JOURNAL_ENTRY

**RN-ADMINREPORT-010**
- Descripción: No puede removerse referencia inexistente.
- Condición: !journalEntryReferences.contains(journalEntryId) al invocar removeJournalEntryReference().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND

**RN-ADMINREPORT-011**
- Descripción: Aprobación requiere usuario aprobador válido.
- Condición: approve(approver) con approver == null.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_REPORT_MISSING_APPROVER

---

## Relación con ADRs
- ADR-13 (Arquitectura): Plan de cuentas y asientos contables - integración con reportes.
- ADR-28 (Dominio): Conocimientos administrativos y contables.
- ADR-30 (Dominio): Catálogo CRUD por rol - permisos de aprobación.
- ADR-32 (Dominio): Reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas - validación de flujo de estados.

---

## Eventos de Dominio
- AdministrativeReportCreated: Al crear nuevo reporte (DRAFT).
- ReportJournalEntryAdded: Al agregar referencia a asiento.
- ReportIndicatorAdded: Al agregar indicador.
- ReportAttachmentAdded: Al agregar documento adjunto.
- ReportSubmittedForReview: Al enviar a revisión.
- ReportApproved: Al aprobar y publicar.
- ReportRejected: Al rechazar y devolver a borrador.
- ReportArchived: Al archivar reporte publicado.
- ReportUnarchived: Al restaurar reporte archivado.

---

## Tipos de Reportes Comunes

**Reportes Financieros**
- Balance General
- Estado de Resultados
- Flujo de Caja
- Balance de Comprobación

**Reportes Operativos**
- Indicadores de Gestión Clínica
- Productividad por Profesional
- Ocupación de Agenda

**Reportes Administrativos**
- Gastos por Categoría
- Pagos a Proveedores
- Cuentas por Cobrar/Pagar

---

## Indicadores Comunes (Indicator)
- name: Nombre del indicador
- value: Valor numérico o porcentual
- unit: Unidad de medida (%, COP, unidades)
- period: Período de medición
- trend: Tendencia (↑ mejora, ↓ deterioro, → estable)
## Relacionados
- [ADR-Resolución de inconsistencia entre catálogo de errores y excepciones en AdministrativeReport.md](../../decisions/accounting/ADR-Resoluci%C3%B3n%20de%20inconsistencia%20entre%20cat%C3%A1logo%20de%20errores%20y%20excepciones%20en%20AdministrativeReport.md)
- [Índice de ADRs — Catálogo de Errores AdministrativeReport (EDM10).md](../../catalogo-de-error/accounting/administrativeReport/%C3%8Dndice%20de%20ADRs%20%E2%80%94%20Cat%C3%A1logo%20de%20Errores%20AdministrativeReport%20%28EDM10%29.md)