1# ADR Pedagógico: Claridad entre Input Port y Output Port en Arquitectura Hexagonal

**Fecha:** 17-01-2026  
**Estado:** Aceptado como guía pedagógica

---

## Contexto
Durante la migración de un modelo MVC por capas hacia arquitectura hexagonal, surgió confusión sobre el rol de las interfaces en las capas de dominio y aplicación.  
En particular, se confundieron los **puertos de entrada (input ports)** con los **puertos de salida (output ports)**, y se usaron nombres poco claros que dificultaban la comprensión.

---

## Decisión
Separar explícitamente los conceptos de **input port** y **output port**, asignando nombres y ubicaciones consistentes:

- **Input Port:**
    - Interfaz en la capa de aplicación que define lo que el sistema ofrece (casos de uso).
    - Implementado por un *application service* o *interactor*.

- **Output Port:**
    - Interfaz en la capa de dominio/aplicación que define lo que el sistema necesita del exterior (persistencia, servicios externos).
    - Implementado por un *adapter* en infraestructura.

---

## Ejemplo

```java
// Input Port (capa aplicación)
public interface RegisterPatientUseCase {
    void execute(RegisterPatientCommand cmd);
}

// Application Service (implementa input port)
public class RegisterPatientService implements RegisterPatientUseCase {
    private final PatientRepository repository; // output port

    public RegisterPatientService(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RegisterPatientCommand cmd) {
        Patient patient = new Patient(
            new PatientId(cmd.id()),
            new Name(cmd.name()),
            new BirthDate(cmd.birthDate())
        );
        repository.save(patient);
    }
}

// Output Port (contrato)
public interface PatientRepository {
    void save(Patient patient);
    Optional<Patient> findById(PatientId id);
}

// Infra Adapter (implementa output port)
@Repository
public class JpaPatientRepository implements PatientRepository {
    // implementación con JPA
}
```

## Consecuencias
### Positivas
- Claridad conceptual: cada interfaz tiene un propósito único.

- Mejor comunicación en equipo: se evita confundir casos de uso con contratos externos.

- Testabilidad: se pueden mockear output ports en pruebas de casos de uso.

- Flexibilidad: cambiar infraestructura no afecta la capa de aplicación.

### Negativas
- Disciplina: requiere cuidado en nombrado y ubicación.

- Curva de aprendizaje: puede parecer redundante al inicio para quienes vienen de MVC.

## Aprendizaje
El error inicial no fue funcional, sino de nombres y conceptos.
Aunque las clases cumplían su rol, la falta de claridad en la terminología generaba confusión.
Este ADR pedagógico documenta la diferencia y sirve como guía para futuros desarrolladores o para mentoring.