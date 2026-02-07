package com.example.ClinicaDefinitiva.domain.errors.catalog.schedule;


public enum TimeSlotError {

/**
 * ⚠️ CATÁLOGO COMPLETO ELIMINADO (2025-12-28)
 *
 * Decisión Arquitectónica: TimeSlot NO debe ser un agregado persistido.
 * TimeSlot es un concepto derivado que se genera dinámicamente desde Availability.
 *
 * Consecuencia: Todos los catálogos RN-TIMESLOT-* fueron eliminados porque:
 * - Validaciones estructurales → ocurren en Availability
 * - Validaciones de asignación → ocurren en Appointment
 * - Validaciones de cobertura → ocurren en Domain Service
 *
 * Ver: ADR-25 "TimeSlot como Derivado Ligero" para justificación completa
 */

// RN-TIMESLOT-001: ELIMINADA (2025-12-28)
// Motivo: Validación ocurre en Availability
// Original: "La duración debe ser positiva y dentro de límites permitidos"
// Reemplazo: Availability valida su duración completa
// Ver: ADR-25

// RN-TIMESLOT-002: ELIMINADA (2025-12-28)
// Motivo: Delegada a Dentist.ensureEditable()
// Original: "No puede crearse si el profesional está inactivo"
// Ver: ADR-25

// RN-TIMESLOT-003: ELIMINADA (2025-12-28)
// Motivo: Validación ocurre en Availability
// Original: "No puede solaparse con otro TimeSlot ya asignado"
// Reemplazo: Availability.overlapsWith()
// Ver: ADR-25

// RN-TIMESLOT-004: ELIMINADA (2025-12-28)
// Motivo: Validación ocurre en Appointment/Availability
// Original: "No puede editarse si tiene cita asignada o está dentro de 24h previas"
// Reemplazo: Validación en Domain Service
// Ver: ADR-25

// RN-TIMESLOT-005: ELIMINADA (2025-12-28)
// Motivo: Validación ocurre en Domain Service con pessimistic lock
// Original: "No puede tener más de una cita asignada"
// Reemplazo: AppointmentSchedulingService.ensureNoConflicts() con lock
// Ver: ADR-25

// RN-TIMESLOT-006: ELIMINADA (2025-12-28)
// Motivo: Validación ocurre en Domain Service
// Original: "Debe estar contenido dentro de una disponibilidad válida"
// Reemplazo: AppointmentSchedulingService.ensureAvailabilityCoverage()
// Ver: ADR-25

// RN-TIMESLOT-007: ELIMINADA (2025-12-28)
// Motivo: Validación ocurre en Appointment
// Original: "Cancelación requiere motivo obligatorio"
// Reemplazo: Appointment.cancel(reason) valida motivo
// Ver: ADR-25

// RN-TIMESLOT-008: ELIMINADA (2025-12-28)
// Motivo: Validación ocurre en Domain Service
// Original: "No puede cancelarse si tiene cita activa"
// Reemplazo: Consulta a AppointmentRepository en Domain Service
// Ver: ADR-25

// RN-TIMESLOT-009: ELIMINADA (2025-12-28)
// Motivo: Validación ocurre en Availability
// Original: "No puede extenderse fuera de la disponibilidad original"
// Reemplazo: Availability.extend() valida límites
// Ver: ADR-25

    /*
     * NOTA HISTÓRICA:
     *
     * Este archivo se mantiene como registro histórico de la decisión arquitectónica
     * de NO modelar TimeSlot como agregado persistido.
     *
     * Los 9 catálogos eliminados representan un aprendizaje clave:
     * "No todo concepto del dominio necesita ser un agregado. TimeSlot es una
     * proyección derivada, no una entidad con identidad propia."
     *
     * Este archivo puede eliminarse físicamente en el futuro, pero se mantiene
     * temporalmente para documentar la evolución del diseño arquitectónico.
     *
     */
}