# ADR-07 (Dominio): Estrategia de construcción de objetos en el dominio

- **Fecha**: 2025-11-24
- **Estado**: Aprobado
- **Categoría**: Dominio

## Problema

Diferentes objetos del dominio requieren diferentes estrategias de construcción:
- Agregados complejos con reglas de negocio (ej. Guardian)
- Entidades administrativas simples (ej. Company)
- Objetos técnicos de infraestructura

No está claro cuándo usar Builder, constructor directo o setters.

## Decisión

Aplicar estrategia según **complejidad y rol del objeto**:

### Regla numérica

| Condición | Estrategia | Justificación |
|-----------|-----------|---------------|
| ≤3 atributos simples | Constructor directo o Record | Innecesaria complejidad adicional |
| 4-6 atributos O 1+ validación compleja | Constructor + validaciones | Balance entre simplicidad y claridad |
| ≥7 atributos O construcción en múltiples pasos | Builder | Evita constructores con larga lista de parámetros |

### Regla semántica

| Tipo de objeto | Estrategia recomendada |
|---------------|----------------------|
| **Agregados con invariantes** (Guardian, Dentist) | Builder |
| **Value Objects inmutables** (Email, Name) | Record o constructor validador |
| **Entidades administrativas** (Company) | Constructor + setters opcionales |
| **Objetos técnicos** (Config, DTOs) | Record o constructor simple |

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Builder para todo | Verbosidad innecesaria en objetos simples (ej. Email) |
| Setters para todo | Rompe inmutabilidad, permite estados inconsistentes |
| Solo constructores | Constructores con 10+ parámetros son ilegibles |

## Consecuencias

**Ganamos:**
- Regla clara para decidir estrategia
- Balance entre simplicidad y expresividad
- Inmutabilidad en objetos que la requieren

**Perdemos:**
- Mix de estilos en el código (no todo es homogéneo)
- Requiere criterio por objeto (no hay "receta única")

## Implementación

### Caso 1: Agregado complejo (Builder)

```java
public class Guardian {
    private final GuardianId id;
    private final PersonalInfo personalInfo;
    private final ContactInfo contactInfo;
    private final List<PatientId> assignedPatients;
    private final GuardianStatus status;
    
    // Constructor privado
    private Guardian(Builder builder) {
        this.id = builder.id;
        this.personalInfo = builder.personalInfo;
        this.contactInfo = builder.contactInfo;
        this.assignedPatients = List.copyOf(builder.assignedPatients);
        this.status = builder.status;
        
        // Validar invariantes
        validateInvariants();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private GuardianId id;
        private PersonalInfo personalInfo;
        private ContactInfo contactInfo;
        private List<PatientId> assignedPatients = new ArrayList<>();
        private GuardianStatus status = GuardianStatus.ACTIVE;
        
        public Builder withId(GuardianId id) {
            this.id = id;
            return this;
        }
        
        public Builder withPersonalInfo(PersonalInfo info) {
            this.personalInfo = info;
            return this;
        }
        
        public Builder withContactInfo(ContactInfo info) {
            this.contactInfo = info;
            return this;
        }
        
        public Builder withAssignedPatients(List<PatientId> patients) {
            this.assignedPatients = patients;
            return this;
        }
        
        public Guardian build() {
            return new Guardian(this);
        }
    }
    
    private void validateInvariants() {
        if (id == null) throw new IllegalStateException("Guardian must have ID");
        if (personalInfo == null) throw new IllegalStateException("Personal info required");
    }
}

// Uso
Guardian guardian = Guardian.builder()
    .withId(GuardianId.generate())
    .withPersonalInfo(personalInfo)
    .withContactInfo(contactInfo)
    .withAssignedPatients(List.of(patientId))
    .build();
```

### Caso 2: Value Object simple (Record)

```java
public record Email(String value) {
    public Email {
        if (value == null || !value.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}

// Uso
Email email = new Email("test@example.com");
```

### Caso 3: Entidad administrativa (Constructor + setters)

```java
public class Company {
    private CompanyId id;
    private String name;
    private String taxId;
    private CompanyStatus status;
    
    public Company(String name, String taxId) {
        this.name = name;
        this.taxId = taxId;
        this.status = CompanyStatus.ACTIVE;
    }
    
    // Setters solo para campos que pueden cambiar
    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }
    
    public void deactivate() {
        this.status = CompanyStatus.INACTIVE;
    }
}

// Uso
Company company = new Company("Clinica OdontoSalud", "123456789");
company.updateName("Clinica Dental Integral");
```

## Decisión por tipo de construcción

**Cuándo usar cada patrón:**

```
Record → VO inmutable simple (≤3 campos, solo validación)
Constructor → Entidad simple (≤5 campos, 1-2 validaciones)
Builder → Agregado complejo (≥6 campos O construcción multi-paso)
Factory Method → Múltiples formas de crear el mismo tipo
```

## Ejemplo de Factory Method

Para casos donde el objeto se puede crear de múltiples formas:

```java
public class Dentist {
    // Constructor privado
    private Dentist(...) { }
    
    // Factory methods semánticos
    public static Dentist createNew(PersonalInfo info, LicenseNumber license) {
        // Lógica específica para dentista nuevo
    }
    
    public static Dentist createFromTransfer(
        PersonalInfo info, 
        LicenseNumber license,
        PreviousClinic previousClinic
    ) {
        // Lógica específica para dentista transferido
    }
}
```