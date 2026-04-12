# ADR-40 (Arquitectura): Estrategia Híbrida de Manejo de Errores - Outcome para Módulos Técnicos, Exceptions para Reglas de Negocio

- **Fecha:** 28/01/2026  
- **Estado**: Aprobado
- **Contexto:** Módulo de Acceso (UserIdentity) y su integración con módulos de negocio
- **Autor:** David Stiven Sanclemente

---

## Contexto

### Situación Inicial
Al inicio del proyecto, implementé un sistema robusto de manejo de excepciones para todas las reglas de negocio. Este sistema incluye:
- Jerarquía de excepciones de dominio (`BusinessRuleViolationException`, `DomainAggregateException`)
- Catálogo centralizado de errores (`ErrorCatalog`)
- Context tracking para trazabilidad (`EntityContext`)
- Exception handlers globales en la capa de presentación

Este enfoque funcionaba bien para módulos clínicos y administrativos (Patient, Appointment, MedicalRecord, etc.), donde las violaciones de reglas son **eventos excepcionales** que interrumpen el flujo normal del negocio.

```java
// Ejemplo típico en módulos de negocio
public static Patient registerPatient(Person data, GuardianId guardian) {
    if (!data.getAge().isEligibleForRegistration()) {
        throw new DomainAggregateException(
            ErrorCatalog.ERR_PATIENT_INVALID_AGE,
            EntityContext.PATIENT
        );
    }
    // Esta ES una situación excepcional - el registro debe detenerse
}
```

### El Cambio de Paradigma

Al comenzar a trabajar en el **módulo de acceso** (UserIdentity), se me presentó la idea de usar **objetos Outcome** en lugar de excepciones para ciertos escenarios. Las razones eran convincentes:

1. **Login fallido NO es excepcional** - es un flujo de control esperado
2. **Múltiples intentos son normales** - necesito acumular información sin stack traces costosos
3. **Validaciones en cadena** - composición más natural que try-catch anidados
4. **Performance** - evitar overhead de excepciones en operaciones frecuentes

```java
// Antes (problemático)
public void recordFailedLogin() {
    this.failedAttempts++;
    if (this.failedAttempts >= MAX_ATTEMPTS) {
        throw new AccountLockedException(); // ← Stack trace innecesario
    }
    throw new InvalidCredentialsException(); // ← Más overhead
}

// Después (mejor)
public Outcome recordFailedLogin(Instant now, int maxAttempts, Duration lockDuration) {
    if (isLocked(now)) {
        return Outcome.fail(new OutcomeDetail(
            UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
            Severity.ERROR,
            Category.TECNICO
        ));
    }
    // Control de flujo sin excepciones
}
```

### La Duda Crítica

Implementé `Outcome` exitosamente en el módulo de acceso. Funcionaba tan bien que surgió la pregunta natural:

**¿Debería migrar TODOS los módulos a Outcome?**

La tentación era fuerte:
- ✅ Consistencia aparente en toda la codebase
- ✅ Mejor performance teórica
- ✅ Composición funcional más elegante

Pero algo no encajaba. Los módulos clínicos usando excepciones se sentían **correctos**. Un paciente menor sin guardian **SÍ es excepcional**. Una cita en horario no laborable **SÍ debe detener el flujo**.

---

## Decisión

**Adopto una estrategia híbrida de manejo de errores:**

### 1. Módulos Técnicos → Outcome
**Aplica a:** UserIdentity, Roles, Permissions, Authentication, Authorization

**Razón:** Estos módulos manejan **flujos de control esperados** más que excepciones reales.

**Características:**
- Validaciones frecuentes (cada request)
- Múltiples posibles resultados válidos
- Composición de validaciones
- Performance crítica

**Ejemplos:**
```java
// UserIdentity - Outcome es natural
public Outcome canPerformSensitiveAction(Instant now) {
    if (!verified) return Outcome.fail(...);        // Común en usuarios nuevos
    if (isLocked(now)) return Outcome.fail(...);    // Recuperable después del timeout
    if (status != ACTIVE) return Outcome.fail(...); // Estado válido, solo no autorizado
    return Outcome.ok();
}

// Login - múltiples razones esperadas de fallo
public Outcome authenticate(Email email, String password) {
    // Usuario no existe - no excepcional, puede ser typo
    // Password incorrecto - esperado, intentar de nuevo
    // Cuenta bloqueada - temporal, esperar
    // No verificado - flujo normal, enviar email
}
```

### 2. Módulos de Negocio → Exceptions
**Aplica a:** Patient, Doctor, Appointment, MedicalRecord, Prescription, etc.

**Razón:** Estos módulos modelan **reglas de negocio que NO deberían violarse**.

**Características:**
- Invariantes de dominio
- Estados inconsistentes
- Reglas médicas/clínicas
- Situaciones que requieren intervención

**Ejemplos:**
```java
// Patient - Exception es correcto
public static Patient registerPatient(Person data, GuardianId guardian) {
    if (!data.getAge().isEligibleForRegistration()) {
        throw new DomainAggregateException(...);
        // Esto NO es un flujo alternativo - es un ERROR en los datos
    }
    
    if (!data.getAge().isAdult() && guardian == null) {
        throw new BusinessRuleViolationException(...);
        // Esto viola una REGLA LEGAL - debe corregirse
    }
}

// Appointment - violación de invariante
public void reschedule(LocalDateTime newDateTime) {
    if (this.status == AppointmentStatus.COMPLETED) {
        throw new BusinessRuleViolationException(...);
        // Una cita completada NO puede reprogramarse - estado inconsistente
    }
}
```

### 3. Anti-Corruption Layer - Domain Services
**Problema:** Los módulos de negocio necesitan validar usuarios, pero UserIdentity usa Outcome.

**Solución:** Domain Services traducen entre paradigmas.

```java
@Service
public class UserAccessValidator {
    
    // Consume Outcome (módulo técnico)
    public void validateUserCanPerformSensitiveAction(UserId userIdentityId, ...) {
        UserIdentity user = userRepo.findById(userIdentityId)...;
        
        Outcome eligibility = user.canPerformSensitiveAction(now);
        
        // Traduce a Exception (módulo negocio)
        if (!eligibility.isSuccess()) {
            throw new UserNotEligibleException(...);
        }
    }
}
```

---

## Razonamiento

### ¿Por qué NO migrar todo a Outcome?

#### 1. Semántica del Dominio
```java
// Con Outcome - pierde claridad semántica
public Outcome registerPatient(Person data, GuardianId guardian) {
    Outcome ageValidation = validateAge(data);
    if (!ageValidation.isSuccess()) return ageValidation;
    
    Outcome guardianValidation = validateGuardian(data, guardian);
    if (!guardianValidation.isSuccess()) return guardianValidation;
    
    // El código sugiere "alternativas válidas" cuando en realidad son ERRORES
}

// Con Exception - clara intención
public static Patient registerPatient(Person data, GuardianId guardian) {
    if (!data.getAge().isEligibleForRegistration()) {
        throw new DomainAggregateException(...);
    }
    // El código grita: "ESTO NO DEBERÍA PASAR"
}
```

#### 2. Stack Traces son Valiosos para Bugs
En módulos de negocio, cuando ocurre una excepción **quiero** el stack trace completo:
- ¿Qué controlador llamó esto?
- ¿Qué servicio orquestó?
- ¿Qué datos causaron el problema?

Con Outcome, pierdo esta información de debugging crucial.

#### 3. Integración con Spring Boot
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<?> handleBusinessRule(BusinessRuleViolationException ex) {
        // Spring maneja esto automáticamente
        // Logging, métricas, alertas - todo integrado
    }
}
```

Con Outcome, necesitaría chequeos manuales en cada endpoint.

#### 4. Transacciones y Rollback
```java
@Transactional
public void scheduleSurgery(SurgeryRequest request) {
    // Si lanzo exception, Spring hace rollback automático
    throw new BusinessRuleViolationException(...);
}

// Con Outcome, necesito lógica manual de rollback
```

### ¿Por qué SÍ usar Outcome en módulos técnicos?

#### 1. Performance en Hot Paths
```java
// Autenticación - ejecutado en CADA request
@Filter
public void authenticate(HttpServletRequest request) {
    Outcome auth = authService.validateToken(request.getHeader("Authorization"));
    // Sin overhead de excepciones en happy path
}
```

#### 2. Composición Elegante
```java
public Outcome canPerformSensitiveAction(Instant now) {
    return checkVerification()
        .flatMap(v -> checkLockStatus(now))
        .flatMap(l -> checkActiveStatus());
    // Composición funcional natural
}
```

#### 3. Información Rica sin Overhead
```java
public Outcome recordFailedLogin(...) {
    // Puedo retornar múltiples warnings/infos
    // Sin el costo de crear excepciones
    return Outcome.fail(List.of(
        new OutcomeDetail(..., Severity.WARNING, ...),
        new OutcomeDetail(..., Severity.INFO, ...)
    ));
}
```

---

## Consecuencias

### Positivas

✅ **Claridad semántica:** Outcome para flujos esperados, Exceptions para errores reales

✅ **Performance optimizada:** Sin overhead de excepciones en authentication/authorization

✅ **Mejor debugging:** Stack traces donde realmente importan (reglas de negocio)

✅ **Composición funcional:** Outcome permite encadenar validaciones técnicas elegantemente

✅ **Integración Spring:** Excepciones aprovechan @ExceptionHandler, @Transactional, etc.

✅ **Evolución gradual:** No requiere big bang migration, cada módulo usa lo apropiado

### Negativas

⚠️ **Inconsistencia aparente:** Debo tener claro "cuándo usar qué"

⚠️ **Anti-Corruption Layer:** Necesito Domain Services para traducir entre paradigmas

⚠️ **Dos sistemas de error:** La documentación debe explicar ambos enfoques

### Mitigaciones

1. **Documentación clara:**
```markdown
## Guía de Manejo de Errores

- **¿Es autenticación/autorización/acceso?** → Outcome
- **¿Es regla de negocio clínica/administrativa?** → Exception
- **¿Duda?** → Preguntarme: "¿Esto debería pasar en operación normal?"
  - Sí → Outcome
  - No → Exception
```

2. **Domain Services como puentes:**
```java
// UserAccessValidator traduce automáticamente
// Desarrollo de módulos de negocio solo ve exceptions
```

3. **Code review checklist:**
- [ ] ¿Usé Outcome en módulo técnico?
- [ ] ¿Usé Exception en módulo de negocio?
- [ ] ¿Necesito Domain Service para integración?

---

## Experiencia de Implementación

### Lo que Aprendí

**Semana 1:** Implementé Outcome en UserIdentity. Funcionó mejor de lo esperado para login/authentication.

**Semana 2:** Intenté usarlo en Patient. Se sintió forzado. Las reglas clínicas gritaban por exceptions.

**Semana 3:** Decisión híbrida. Creé `UserAccessValidator` como puente.

**Resultado:** Mejor de ambos mundos. Cada paradigma en su contexto apropiado.

### Reflexiones Personales

Al principio me resistí a tener dos sistemas. Buscaba consistencia total. Pero luego vi que cada uno brilla en su dominio:

- Login con Outcome es más limpio y eficiente
- Las reglas médicas con Exceptions son más claras y seguras

El `UserAccessValidator` fue la pieza clave. Los servicios de aplicación no necesitan saber que UserIdentity usa Outcome internamente.

---

## Alternativas Consideradas

### 1. Solo Exceptions (status quo)
**Rechazada:** Performance impact en authentication hot path. Semántica incorrecta para flujos esperados.

### 2. Solo Outcome (migración completa)
**Rechazada:** Pérdida de stack traces valiosos. Integración pobre con Spring. Semántica confusa para reglas de negocio.

### 3. Result<T, E> (Rust-style)
**Considerada:** Similar a Outcome pero más tipado.  
**Rechazada:** Complejidad adicional sin beneficios claros sobre Outcome. Java no tiene pattern matching como Rust.

### 4. Either<L, R> (Scala-style)
**Considerada:** Abstracto y reusable.  
**Rechazada:** Demasiado genérico. Outcome con OutcomeDetail es más expresivo para mi dominio.

---

## Criterios de Decisión para Nuevos Módulos

### Usa Outcome si:
- ✅ El módulo maneja autenticación/autorización/acceso
- ✅ Las "fallas" son flujos de control esperados
- ✅ Se ejecuta en hot path (cada request)
- ✅ Necesito componer múltiples validaciones
- ✅ Quiero retornar información rica sin excepciones

### Usa Exceptions si:
- ✅ El módulo modela reglas de negocio clínicas/administrativas
- ✅ Las fallas representan estados inconsistentes
- ✅ Necesito stack traces para debugging
- ✅ Aprovecho @Transactional rollback
- ✅ Las violaciones requieren intervención humana

### ¿Todavía con duda?
Documentar la duda y razonar con calma. Mejor invertir tiempo en la decisión correcta que tener deuda técnica después.

---

## Referencias

- ADR-0010: Cambios en el módulo de acceso y diseño del agregado UserIdentity
- ADR-0012: Migración de validación de usuarios a Domain Service
- Outcome implementation: `domain/util/Outcome.java`
- Exception hierarchy: `domain/exceptionsDomain/`

---

## Notas de Revisión

**Próxima revisión:** 28/07/2026 (6 meses)

**Métricas a evaluar:**
- Performance de authentication (p95, p99)
- Tasa de exceptions en módulos de negocio
- Tiempo de debugging promedio
- Consistencia en uso de paradigmas

**Posibles ajustes:**
- Si performance de exceptions mejora significativamente en Java futuro, reconsiderar
- Si surgen más módulos técnicos, documentar patrones comunes
- Si la traducción Outcome→Exception se vuelve repetitiva, evaluar abstracción

---

## Aprendizajes Clave

### Sobre Arquitectura

**No buscar consistencia por consistencia:**
La uniformidad total (todo Outcome o todo Exception) parece atractiva superficialmente, pero cada paradigma tiene su lugar natural. Forzar uno donde no encaja crea más problemas que los que resuelve.

**Las abstracciones deben seguir al dominio:**
El módulo de acceso trata con flujos de control frecuentes (autenticación). Los módulos clínicos tratan con invariantes críticos (seguridad del paciente). Usar la misma herramienta para ambos es como usar un martillo para todo.

### Sobre Decisiones

**La incomodidad es una señal:**
Cuando Outcome en Patient "se sentía forzado", esa era mi intuición arquitectónica avisando. Aprendí a escuchar esas señales en lugar de ignorarlas por buscar consistencia.

**Los puentes son valiosos:**
El Anti-Corruption Layer (UserAccessValidator) no es "código extra innecesario". Es la inversión que permite que cada módulo use el paradigma correcto sin contaminar a otros.

### Sobre Pragmatismo

**Perfección teórica vs práctica:**
Teóricamente, Outcome es "más funcional" y "más elegante". Pero en práctica, Spring Boot está diseñado para Exceptions. Luchar contra el framework crea fricción innecesaria.

**Performance donde importa:**
No optimizo todo. Solo optimizo los hot paths (authentication). En el resto del código, la claridad y el debugging valen más que nanosegundos.

### Sobre Evolución

**Las decisiones arquitectónicas no son permanentes:**
Esta decisión es correcta hoy. En 6 meses, si Java mejora las excepciones o Spring añade mejor soporte para Outcome, puedo reconsiderar. La arquitectura debe evolucionar.

**Documentar el razonamiento:**
Este ADR no solo dice "qué" decidí, sino "por qué". Cuando revise en el futuro, o cuando trabaje con otros desarrolladores, este contexto será invaluable.

---

## Conclusión Personal

Esta decisión me enseñó que la arquitectura de software no es sobre aplicar dogmas (usar X siempre, nunca usar Y). Es sobre entender el contexto, escuchar las señales del código, y elegir la herramienta correcta para cada trabajo.

Outcome y Exceptions no son enemigos. Son herramientas complementarias. Outcome brilla en módulos técnicos con flujos de control esperados. Exceptions brillan en módulos de negocio con invariantes críticos.

Mi objetivo no es escribir el código más "puro" o "funcional" o "consistente". Mi objetivo es escribir código que sea:
- **Correcto:** Hace lo que debe hacer
- **Claro:** Otros (incluyéndome en el futuro) pueden entenderlo
- **Mantenible:** Puede evolucionar sin reescrituras masivas
- **Apropiado:** Usa las herramientas correctas para cada contexto

Esta decisión híbrida cumple todos esos objetivos.

## Relación con otros ADRs

- [ADR-(Arquitectura)-37-Arquitectura hexagonal para módulo de acceso.md](ADR-%28Arquitectura%29-37-Arquitectura%20hexagonal%20para%20m%C3%B3dulo%20de%20acceso.md)
- [ADR‑(Arquitectura)-46-Integración JWT y Spring Security en arquitectura hexagonal.md](ADR%E2%80%91%28Arquitectura%29-46-Integraci%C3%B3n%20JWT%20y%20Spring%20Security%20en%20arquitectura%20hexagonal.md)