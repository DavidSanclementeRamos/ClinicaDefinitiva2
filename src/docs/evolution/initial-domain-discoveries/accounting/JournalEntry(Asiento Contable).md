# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: JournalEntry (Asiento Contable)

## Propósito
Representar un asiento contable que implementa el principio de partida doble, asegurando balance entre débitos y créditos. Este agregado protege la integridad contable, permite trazabilidad completa de movimientos financieros y garantiza cumplimiento normativo colombiano (PUC).

---

## CREACIÓN
- Debe tener compañía (CompanyId) válida asociada.
- Fecha del asiento es obligatoria y no puede ser futura.
- Número de documento es obligatorio (mínimo 1 carácter).
- Descripción es obligatoria (mínimo 5 caracteres).
- Debe tener al menos dos líneas (partida doble).
- La suma de débitos debe igualar la suma de créditos.
- Cada línea debe tener cuenta contable (LedgerAccountId) válida.
- Cada línea debe tener monto positivo mayor a cero.
- Se asigna estado inicial: balanced=true, posted=false.
- No puede crearse con fecha futura.

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si NO está contabilizado (posted=false).
- No puede modificarse después de contabilizar (post()).
- Puede agregarse/removerse líneas solo si no está posteado.
- Cada modificación marca el asiento como desbalanceado hasta revalidar.
- Cambios en descripción o documento requieren estado no posteado.
- Al agregar/remover líneas se debe revalidar balance.

---

## CONTABILIZACIÓN (POSTING)
- Solo puede contabilizarse si está balanceado.
- No puede contabilizarse si ya está posteado.
- No puede contabilizarse con fecha futura.
- Una vez posteado, el asiento es inmutable.
- El posteo valida automáticamente el balance antes de confirmar.

---

## REVERSA / ANULACIÓN
- Solo puede reversarse si está contabilizado (posted=true).
- La reversa requiere motivo obligatorio.
- Crea un nuevo asiento con líneas invertidas (débito↔crédito).
- El asiento original permanece pero se vincula con la reversa.
- Número de documento de reversa: originalDocument + "-REV".
- Descripción incluye: "REVERSA: [motivo] - [descripción original]".
- La reversa se crea con fecha actual del sistema.

---

## OPERACIONES DE DOMINIO
- addLine(línea) → Agrega línea y marca como desbalanceado.
- removeLine(línea) → Remueve línea si existe y no está posteado.
- updateInformation() → Actualiza descripción y documento si no está posteado.
- validateBalance() → Verifica que débitos = créditos.
- post() → Contabiliza el asiento haciéndolo inmutable.
- reverse(motivo) → Crea asiento reversa con líneas invertidas.
- getTotalDebits() → Calcula suma total de débitos.
- getTotalCredits() → Calcula suma total de créditos.
- affectsThirdParty(terceroId) → Verifica si afecta a tercero específico.
- affectsAccount(cuentaId) → Verifica si afecta a cuenta específica.

---

## INVARIANTES GLOBALES
- Un asiento válido debe cumplir: débitos = créditos (partida doble).
- Un asiento debe tener mínimo 2 líneas.
- Un asiento posteado es inmutable (no editable ni eliminable).
- Cada línea debe tener monto positivo > 0.
- La fecha del asiento no puede ser futura.
- Número de documento debe ser único por compañía y período.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada línea agregada/removida antes del posteo.
- Se registra fecha y hora exacta del posteo.
- Se registra motivo de reversa con asiento original vinculado.
- Sistema emite eventos: AsientoRegistrado, AsientoPosteado, AsientoReversado.
- Auditoría completa de intentos de modificación sobre asientos posteados.
- Integración con Libro Diario y Libro Mayor.

---

## Justificación Semántica
Estas reglas aseguran la integridad contable mediante partida doble, evitan fraude por modificación post-contabilización, y garantizan trazabilidad completa. El modelo cumple con normativa colombiana (PUC, NIIF para pymes) y está listo para exhibición internacional, auditorías externas y reportes financieros.

---

## Reglas Descubiertas (formato estandarizado)

**RN-JOURNALENTRY-001**
- Descripción: Debe cumplir partida doble (débitos = créditos).
- Condición: sum(débitos) != sum(créditos) al invocar validateBalance().
- Consecuencia: Se rechaza operación con InvalidJournalEntryException.
- Error asociado: ERR_JOURNAL_UNBALANCED

**RN-JOURNALENTRY-002**
- Descripción: Debe tener al menos dos líneas.
- Condición: JournalEntry.lines.size() < 2 al invocar validateBalance().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_JOURNAL_INSUFFICIENT_LINES

**RN-JOURNALENTRY-003**
- Descripción: Solo puede editarse si NO está contabilizado.
- Condición: JournalEntry.posted == true al invocar addLine() o removeLine().
- Consecuencia: Se rechaza operación con InvalidJournalEntryException.
- Error asociado: ERR_JOURNAL_ALREADY_POSTED

**RN-JOURNALENTRY-004**
- Descripción: No puede contabilizarse con fecha futura.
- Condición: JournalEntry.date > LocalDate.now() al invocar post().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_JOURNAL_FUTURE_DATE

**RN-JOURNALENTRY-005**
- Descripción: Solo puede reversarse si está contabilizado.
- Condición: JournalEntry.posted == false al invocar reverse().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_JOURNAL_NOT_POSTED_CANNOT_REVERSE

**RN-JOURNALENTRY-006**
- Descripción: La reversa requiere motivo obligatorio.
- Condición: reverse(reason) con reason == null || reason.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_JOURNAL_REVERSE_REQUIRES_REASON

**RN-JOURNALENTRY-007**
- Descripción: Descripción debe tener mínimo 5 caracteres.
- Condición: JournalEntry.description.trim().length() < 5.
- Consecuencia: Se rechaza operación de creación/actualización.
- Error asociado: ERR_JOURNAL_INVALID_DESCRIPTION

**RN-JOURNALENTRY-008**
- Descripción: Fecha del asiento es obligatoria.
- Condición: JournalEntry.date == null al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_JOURNAL_MISSING_DATE

**RN-JOURNALENTRY-009**
- Descripción: Número de documento es obligatorio.
- Condición: JournalEntry.documentNumber == null || documentNumber.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_JOURNAL_MISSING_DOCUMENT_NUMBER

---

## Relación con ADRs
- ADR-13 (Arquitectura): Plan de cuentas y asientos contables - implementación central.
- ADR-17 (Arquitectura): Manejo de plan de cuenta y asiento contable.
- ADR-28 (Dominio): Conocimientos contables - partida doble, PUC colombiano.
- ADR-32 (Dominio): Reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas - validación de balance y posteo.

---

## Eventos de Dominio
- JournalEntryRegistered: Al crear un nuevo asiento (draft).
- JournalEntryLineAdded: Al agregar línea al asiento.
- JournalEntryLineRemoved: Al remover línea del asiento.
- JournalEntryBalanceValidated: Al validar balance exitosamente.
- JournalEntryPosted: Al contabilizar el asiento (inmutable).
- JournalEntryReversed: Al crear asiento de reversa.
- JournalEntryModificationAttempted: Al intentar modificar asiento posteado (auditoría).

---

## Integración con Reportes Contables
- Libro Diario: Listado cronológico de todos los asientos posteados.
- Libro Mayor: Movimientos agrupados por cuenta contable.
- Balance de Comprobación: Suma de débitos y créditos por cuenta.
- Estado de Resultados: Ingresos y gastos del período.
- Balance General: Activos, pasivos y patrimonio.