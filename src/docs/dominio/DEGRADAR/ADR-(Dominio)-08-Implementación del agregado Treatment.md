# ADR-08 (Dominio): Implementación del agregado Treatment

- **Fecha**: 2026-01-30
- **Estado**: Aprobado
- **Categoría**: Dominio (con impacto arquitectónico)

## Problema

El sistema necesita validar que un paciente no pueda desactivarse si tiene tratamientos activos.

No existía un agregado explícito para representar tratamientos odontológicos, lo que dificultaba:
- Trazabilidad del historial clínico
- Validaciones de continuidad de servicios
- Claridad en las reglas de negocio

## Decisión

Implementar agregado `Treatment` en el módulo de **Servicios clínicos odontológicos**.

### Atributos principales

```java
public class Treatment {
    private final TreatmentId id;
    private final PatientId patientId;
    private final DentistId dentistId;
    private final TreatmentType type;
    private TreatmentStatus status;
    private final LocalDate startDate;
    private LocalDate expectedEndDate;
    private LocalDate actualEndDate;
    private final List<TreatmentPhase> phases;  // VO
    private String notes;
    private final MonetaryAmount costEstimate;
    private final TarifaId tarifaId;  // referencia externa
}
```

### Separación de responsabilidades

| Concepto | Responsable | Justificación |
|----------|-------------|---------------|
| **Cobertura de servicios** | Convenio (módulo administrativo) | Define QUÉ tratamientos están cubiertos y CÓMO |
| **Cálculo de precios** | Tarifa (módulo facturación) | Lógica de precios finales según reglas comerciales |
| **Tratamiento clínico** | Treatment (módulo clínico) | Estado, fases, ejecución del servicio |
| **Historial clínico** | Conjunto de Treatments del Patient | No requiere agregado separado |

### Relación con Patient

```java
public class Patient {
    private final PatientId id;
    private final List<TreatmentId> treatmentIds;  // solo IDs, no objetos completos
    
    public boolean hasActiveTreatments() {
        // Consultar repositorio de treatments
        return treatmentRepository.existsActiveByPatientId(this.id);
    }
}
```

**Justificación:** Lista ligera evita cargar todos los tratamientos al obtener un paciente.

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Atributo `treatments: List<Treatment>` en Patient | Demasiado pesado. Un paciente puede tener 50+ tratamientos históricos |
| Cobertura como atributo en Treatment | Cobertura es responsabilidad del Convenio, no del tratamiento clínico |
| Precio final en Treatment | Los precios se calculan dinámicamente en Facturación según reglas comerciales |
| Agregado separado "ClinicalHistory" | Redundante. La colección de Treatments YA ES el historial |

## Consecuencias

**Ganamos:**
- Trazabilidad clínica explícita
- Validaciones de desactivación claras
- Separación de responsabilidades entre clínico/administrativo/facturación
- Escalable hacia auditoría y reportes

**Perdemos:**
- Queries más complejas (join Patient-Treatment)
- Carga selectiva requerida (no cargar todos los treatments de un paciente)

## Consideración de rendimiento

Para listados de pacientes, NO cargar treatments automáticamente:

```java
// ✅ Correcto: lazy loading
Page<Patient> patients = patientRepository.findAll(pageable);
// Los treatments NO se cargan

// ❌ Incorrecto: eager loading
@OneToMany(fetch = FetchType.EAGER)  // NO hacer
private List<Treatment> treatments;
```

Solo cargar treatments cuando se necesiten:

```java
// Al validar desactivación
boolean hasActive = treatmentRepository.existsActiveByPatientId(patientId);

// Al ver detalle del paciente
List<Treatment> treatments = treatmentRepository.findByPatientId(patientId);
```

## Fases del tratamiento (Value Object)

```java
public record TreatmentPhase(
    String name,                    // ej. "Diagnóstico", "Procedimiento 1"
    TreatmentPhaseStatus status,    // PENDING, IN_PROGRESS, COMPLETED
    Optional<LocalDate> scheduledDate,
    Optional<LocalDate> completedDate,
    String observations
) {
    public boolean isCompleted() {
        return status == TreatmentPhaseStatus.COMPLETED;
    }
}
```

Permite modelar tratamientos complejos con múltiples etapas (ej. ortodoncia).

## Estados del tratamiento

```java
public enum TreatmentStatus {
    ACTIVE,      // en curso
    COMPLETED,   // finalizado exitosamente
    CANCELLED,   // cancelado por paciente/clínica
    SUSPENDED    // pausado temporalmente
}
```

Regla de negocio: un paciente con al menos un `Treatment.status == ACTIVE` no puede desactivarse.

## Integración con facturación

```java
public class Treatment {
    private final TarifaId tarifaId;  // referencia, NO objeto completo
    
    // NO hacer
    // private MonetaryAmount finalPrice;  // esto lo calcula Facturación
    
    // SÍ hacer
    private final MonetaryAmount costEstimate;  // presupuesto inicial
}
```

El cálculo del precio final se hace en el contexto de Facturación:

```java
// En módulo de Facturación
MonetaryAmount finalPrice = tarifaService.calculateFinalPrice(
    treatment.getTarifaId(),
    patient.getConvenioId(),
    treatment.getType()
);
```