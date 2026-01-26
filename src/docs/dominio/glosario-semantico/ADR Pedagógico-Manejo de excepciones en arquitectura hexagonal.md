# ADR Pedagógico: Manejo de excepciones en arquitectura hexagonal

**Fecha:** 26-01-2026  
**Estado:** Aprobado  
**Decisión:** Las excepciones de negocio (ej. `NotFoundException`) deben lanzarse en la capa de aplicación (casos de uso), no en la infraestructura. La infraestructura solo devuelve datos crudos (`Optional`, `Page`, etc.), y el controlador REST traduce las excepciones en respuestas HTTP.

---

## Contexto
En arquitecturas tradicionales (MVC), era común que los repositorios o servicios de persistencia lanzaran directamente excepciones como `EntityNotFoundException`.  
Al migrar a arquitectura hexagonal, surge la duda: ¿dónde deben lanzarse las excepciones de negocio?

---

## Decisión
- **Infraestructura (repositorios):**
    - Devuelven datos crudos (`Optional<Entity>`, `Page<Entity>`).
    - No lanzan excepciones de negocio.
    - Ejemplo:
      ```java
      Optional<PatientEntity> findById(Long id);
      ```

- **Puertos de salida (dominio):**
    - Definen contratos genéricos (`Optional`, `Page`).
    - No lanzan excepciones de negocio.

- **Aplicación (UseCase):**
    - Interpretan el resultado del repositorio.
    - Si el resultado es vacío (`Optional.empty()`), lanzan `NotFoundException`.
    - Ejemplo:
      ```java
      @Override
      public PatientDto findById(Long id) {
          return patientRepository.findById(id)
                  .map(patientMapper::toDto)
                  .orElseThrow(() -> new NotFoundException("Patient not found with id " + id));
      }
      ```

- **Infraestructura REST (controlador):**
    - No lanza excepciones, solo las captura con `@ControllerAdvice` o `@ExceptionHandler`.
    - Convierte la excepción en una respuesta HTTP adecuada (`404 Not Found`).
    - Ejemplo:
      ```java
      @ExceptionHandler(NotFoundException.class)
      public ResponseEntity<String> handleNotFound(NotFoundException ex) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
      }
      ```

---

## Motivo del cambio
- **Separación de responsabilidades:**
    - La infraestructura solo persiste datos.
    - La aplicación decide qué es un error de negocio.

- **Desacoplamiento:**
    - La infraestructura no necesita conocer reglas de negocio.
    - Los cambios en la lógica de negocio no afectan la capa de persistencia.

- **Claridad semántica:**
    - Los casos de uso expresan las reglas de negocio con nombres claros.
    - Los controladores REST traducen las excepciones a HTTP.

---

## Beneficios
- **Pedagógico:** ayuda a nuevos desarrolladores a entender dónde va cada responsabilidad.
- **Consistencia:** todas las excepciones de negocio se concentran en la capa de aplicación.
- **Flexibilidad:** se pueden agregar nuevas reglas de negocio sin tocar la infraestructura.
- **Mantenibilidad:** menos acoplamiento entre capas.

---

## Ejemplo de flujo completo

1. **Repositorio (infraestructura):**
   ```java
   Optional<PatientEntity> findById(Long id);
   ```
   
2. **Caso de uso (aplicación):**
   ```java
   @Override
   public PatientDto findById(Long id) {
    return patientRepository.findById(id)
            .map(patientMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Patient not found with id " + id));
}
    ```

3. **Controlador REST (infraestructura):**
   ```java
   @GetMapping("/patients/{id}")
   public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
       PatientDto patient = patientService.findById(id);
       return ResponseEntity.ok(patient);
   }
   ```
   
4. **Manejador de excepciones (infraestructura):**
   ```java
   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<String> handleNotFound(NotFoundException ex) {
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
   }
   ```
   
   5. **Respuesta HTTP:**
      ```java
      HTTP/1.1 404 Not Found
      Content-Type: application/json

      {
          "message": "Patient not found with id 123"
      }
      ```
      
## Decisión arquitectónica
Este ADR establece como estándar que:

Los repositorios devuelven datos crudos (Optional, Page).

Los puertos de salida no lanzan excepciones de negocio.

Los casos de uso interpretan los resultados y lanzan excepciones de negocio (NotFoundException).

Los controladores REST traducen las excepciones a respuestas HTTP.

De esta forma, la arquitectura mantiene claridad, separación de responsabilidades y facilita el aprendizaje de nuevos miembros del equipo.