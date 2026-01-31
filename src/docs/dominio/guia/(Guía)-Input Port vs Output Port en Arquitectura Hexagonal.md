# Guía: Input Port vs Output Port en Arquitectura Hexagonal

**Última actualización:** 2026-01-30  
**Tipo:** Guía de implementación

---

## Regla nemotécnica

```
Input Port  = Lo que OFREZCO al exterior (casos de uso)
Output Port = Lo que NECESITO del exterior (persistencia, servicios)
```

---

## Input Port (Puerto de Entrada)

### Definición
Interfaz que define lo que el sistema **ofrece** al mundo exterior.

### Características
- Define casos de uso
- Ubicado en capa de **aplicación**
- Implementado por Application Service
- Invocado por adaptadores de entrada (REST, CLI, eventos)

### Ejemplo

```java
// Input Port (interfaz)
package com.clinic.application.port.in;

public interface RegisterPatientUseCase {
    PatientDto execute(RegisterPatientCommand command);
}
```

```java
// Application Service implementa el Input Port
package com.clinic.application.service;

@Service
public class PatientApplicationService implements RegisterPatientUseCase {
    private final PatientRepository repository;  // Output Port
    
    public PatientApplicationService(PatientRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public PatientDto execute(RegisterPatientCommand command) {
        Patient patient = Patient.create(
            new Name(command.firstName(), command.lastName()),
            new Email(command.email())
        );
        
        Patient saved = repository.save(patient);
        return mapper.toDto(saved);
    }
}
```

```java
// Controlador REST invoca el Input Port
@RestController
public class PatientController {
    private final RegisterPatientUseCase registerPatient;  // Input Port
    
    @PostMapping("/patients")
    public PatientDto register(@RequestBody RegisterPatientRequest request) {
        var command = new RegisterPatientCommand(
            request.firstName(),
            request.lastName(),
            request.email()
        );
        
        return registerPatient.execute(command);
    }
}
```

---

## Output Port (Puerto de Salida)

### Definición
Interfaz que define lo que el sistema **necesita** del mundo exterior.

### Características
- Define contratos de dependencias externas
- Ubicado en capa de **dominio/aplicación**
- Implementado por adaptadores de salida (JPA, API externa, filesystem)
- Invocado por Application Service

### Ejemplo

```java
// Output Port (interfaz)
package com.clinic.domain.port.out;

public interface PatientRepository {
    Patient save(Patient patient);
    Optional<Patient> findById(PatientId id);
    Page<Patient> findAll(Pageable pageable);
    void deleteById(PatientId id);
}
```

```java
// Adaptador de infraestructura implementa el Output Port
package com.clinic.infrastructure.persistence;

@Component
public class PatientPersistenceAdapter implements PatientRepository {
    private final JpaPatientRepository jpaRepo;
    private final PatientMapper mapper;
    
    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = mapper.toEntity(patient);
        PatientEntity saved = jpaRepo.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<Patient> findById(PatientId id) {
        return jpaRepo.findById(id.value())
            .map(mapper::toDomain);
    }
}
```

---

## Comparación visual

```
┌──────────────────────────────────────────────────────────────────┐
│                    ARQUITECTURA HEXAGONAL                        │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ╔════════════════════════════════════════════════════════╗     │
│  ║              ADAPTADORES DE ENTRADA                    ║     │
│  ║  (REST Controller, CLI, Event Listener)                ║     │
│  ╚════════════════════════════════════════════════════════╝     │
│                            │                                     │
│                            │ invoca                              │
│                            ▼                                     │
│  ┌────────────────────────────────────────────────────────┐     │
│  │           INPUT PORT (interfaz)                        │     │
│  │  "Lo que OFREZCO"                                      │     │
│  │  - RegisterPatientUseCase                              │     │
│  │  - UpdatePatientUseCase                                │     │
│  └────────────────────────────────────────────────────────┘     │
│                            │                                     │
│                            │ implementado por                    │
│                            ▼                                     │
│  ┌────────────────────────────────────────────────────────┐     │
│  │      APPLICATION SERVICE                               │     │
│  │  - PatientApplicationService                           │     │
│  │  - Orquesta casos de uso                               │     │
│  │  - Usa Output Ports                                    │     │
│  └────────────────────────────────────────────────────────┘     │
│                            │                                     │
│                            │ invoca                              │
│                            ▼                                     │
│  ┌────────────────────────────────────────────────────────┐     │
│  │          OUTPUT PORT (interfaz)                        │     │
│  │  "Lo que NECESITO"                                     │     │
│  │  - PatientRepository                                   │     │
│  │  - NotificationService                                 │     │
│  └────────────────────────────────────────────────────────┘     │
│                            │                                     │
│                            │ implementado por                    │
│                            ▼                                     │
│  ╔════════════════════════════════════════════════════════╗     │
│  ║            ADAPTADORES DE SALIDA                       ║     │
│  ║  (JPA Adapter, Email Adapter, API Client)              ║     │
│  ╚════════════════════════════════════════════════════════╝     │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

---

## Tabla comparativa

| Aspecto | Input Port | Output Port |
|---------|-----------|-------------|
| **Propósito** | Define lo que el sistema ofrece | Define lo que el sistema necesita |
| **Dirección** | Entrada → Sistema | Sistema → Salida |
| **Ubicación** | Capa de aplicación | Capa de dominio/aplicación |
| **Implementado por** | Application Service | Adaptador de infraestructura |
| **Invocado por** | Adaptadores de entrada (REST, CLI) | Application Service |
| **Ejemplo** | `RegisterPatientUseCase` | `PatientRepository` |
| **Naming** | Termina en `UseCase` o `Service` | Termina en `Repository`, `Port`, `Gateway` |

---

## Flujo completo de una operación

```
1. Request HTTP
   └─→ 2. REST Controller (adaptador de entrada)
       └─→ 3. Invoca Input Port (RegisterPatientUseCase)
           └─→ 4. Application Service implementa Input Port
               └─→ 5. Invoca Output Port (PatientRepository)
                   └─→ 6. Adaptador JPA implementa Output Port
                       └─→ 7. Persiste en base de datos
```

### Código del flujo completo

```java
// 1. Request HTTP
POST /patients
{ "firstName": "Juan", "lastName": "Pérez", "email": "juan@example.com" }

// 2. REST Controller
@RestController
public class PatientController {
    private final RegisterPatientUseCase registerPatient;  // Input Port
    
    @PostMapping("/patients")
    public PatientDto register(@RequestBody RegisterPatientRequest request) {
        // 3. Invocar Input Port
        return registerPatient.execute(new RegisterPatientCommand(
            request.firstName(),
            request.lastName(),
            request.email()
        ));
    }
}

// 4. Application Service
@Service
public class PatientApplicationService implements RegisterPatientUseCase {
    private final PatientRepository repository;  // Output Port
    
    @Override
    public PatientDto execute(RegisterPatientCommand command) {
        Patient patient = Patient.create(/* ... */);
        
        // 5. Invocar Output Port
        Patient saved = repository.save(patient);
        
        return mapper.toDto(saved);
    }
}

// 6. Adaptador JPA
@Component
public class PatientPersistenceAdapter implements PatientRepository {
    private final JpaPatientRepository jpaRepo;
    
    @Override
    public Patient save(Patient patient) {
        // 7. Persistir en BD
        PatientEntity entity = mapper.toEntity(patient);
        return mapper.toDomain(jpaRepo.save(entity));
    }
}
```

---

## Inversión de dependencias

El principio clave de arquitectura hexagonal:

```
ANTES (arquitectura en capas):
Controller → Service → Repository (implementación JPA)
           ↓
   Service DEPENDE de infraestructura

AHORA (hexagonal):
Controller → Input Port ← Application Service → Output Port ← Adapter JPA
                              ↑                      ↑
                    Infraestructura DEPENDE del dominio
```

**Ventaja:** Puedo cambiar JPA por MongoDB sin modificar Application Service.

---

## Errores comunes

### ❌ Error 1: Confundir dirección del puerto

```java
// MAL: Output Port con nombre de Input Port
public interface SavePatientUseCase {  // suena a Input, pero...
    void save(Patient patient);  // ...es persistencia (Output)
}
```

```java
// BIEN: nombres claros
public interface RegisterPatientUseCase {  // Input: caso de uso
    PatientDto execute(RegisterPatientCommand command);
}

public interface PatientRepository {  // Output: persistencia
    Patient save(Patient patient);
}
```

---

### ❌ Error 2: Mezclar Input y Output en una interfaz

```java
// MAL: una interfaz hace ambas cosas
public interface PatientService {
    PatientDto register(CreatePatientDto dto);  // Input
    Patient save(Patient patient);  // Output
}
```

```java
// BIEN: separar por responsabilidad
public interface RegisterPatientUseCase {  // Input Port
    PatientDto execute(RegisterPatientCommand command);
}

public interface PatientRepository {  // Output Port
    Patient save(Patient patient);
}
```

---

### ❌ Error 3: Application Service llama directamente a JPA

```java
// MAL: Application Service depende de infraestructura
@Service
public class PatientApplicationService {
    private final JpaPatientRepository jpaRepo;  // ¡infraestructura!
    
    public PatientDto register(CreatePatientDto dto) {
        PatientEntity entity = new PatientEntity(/* ... */);
        jpaRepo.save(entity);  // acoplamiento directo
    }
}
```

```java
// BIEN: Application Service depende de Output Port
@Service
public class PatientApplicationService {
    private final PatientRepository repository;  // puerto, no implementación
    
    public PatientDto register(CreatePatientDto dto) {
        Patient patient = Patient.create(/* ... */);
        repository.save(patient);  // desacoplado
    }
}
```

---

## Checklist de implementación

Al crear un nuevo caso de uso:

**Input Port:**
- [ ] Crear interfaz en `/application/port/in/`
- [ ] Nombre termina en `UseCase` o describe la acción
- [ ] Define el contrato del caso de uso
- [ ] Application Service implementa esta interfaz
- [ ] Controlador invoca esta interfaz

**Output Port:**
- [ ] Crear interfaz en `/domain/port/out/` o `/application/port/out/`
- [ ] Nombre describe la dependencia externa (`Repository`, `Gateway`, `Port`)
- [ ] Define operaciones necesarias del exterior
- [ ] Adaptador de infraestructura implementa esta interfaz
- [ ] Application Service invoca esta interfaz

---

## Resumen en una frase

**Input Port = Qué hago yo | Output Port = Qué necesito de otros**