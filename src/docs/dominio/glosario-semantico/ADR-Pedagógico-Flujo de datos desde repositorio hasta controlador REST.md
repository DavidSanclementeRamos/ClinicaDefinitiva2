# ADR Pedagógico: Flujo de datos desde repositorio hasta controlador REST

**Fecha:** 26-01-2026  
**Estado:** Aprobado  
**Decisión:** Documentar de manera pedagógica cómo fluye la información en la arquitectura clínica, desde la capa de infraestructura (repositorios) hasta la capa de exposición (controladores REST), incluyendo puertos de salida, adaptadores y casos de uso.

---

## Contexto
Tras la refactorización de los casos de uso (`UseCase`) para soportar relaciones uno-a-muchos y paginación, surgieron dudas sobre cómo se propagan los cambios hacia las capas inferiores.  
Este ADR busca explicar de manera **didáctica** el flujo completo de datos, para que cualquier miembro del equipo entienda cómo se conectan las piezas.

---

## Flujo de datos paso a paso

1. **Repositorio (Infraestructura)**
    - Implementado con Spring Data JPA.
    - Devuelve `Page<Entity>` directamente desde la base de datos.
    - Ejemplo:
      ```java
      Page<PatientEntity> findByGuardianId(String guardianId, Pageable pageable);
      ```

2. **Puerto de salida (Dominio)**
    - Define la interfaz que abstrae el acceso a datos.
    - Firma:
      ```java
      Page<PatientEntity> findByGuardianId(String guardianId, Pageable pageable);
      ```

3. **Adaptador (Aplicación → Infraestructura)**
    - Implementa el puerto de salida.
    - Convierte `Entity` en `DTO` usando el mapper.
    - Ejemplo:
      ```java
      @Override
      public Page<PatientPageDto> findByGuardianId(String guardianId, Pageable pageable) {
          return patientRepository.findByGuardianId(guardianId, pageable)
                  .map(patientMapper::toPageDto);
      }
      ```

4. **Caso de uso (Aplicación)**
    - Expone el método al controlador.
    - Firma:
      ```java
      Page<PatientPageDto> findByGuardianId(String guardianId, Pageable pageable);
      ```

5. **Controlador REST (Infraestructura REST)**
    - Recibe la petición HTTP.
    - Invoca el caso de uso.
    - Mapea `PageDto` a `PageResponse`.
    - Ejemplo:
      ```java
      @GetMapping("/guardian/{guardianId}")
      public PageResponse<PatientPageResponse> findByGuardianId(
              @PathVariable String guardianId,
              @RequestParam(defaultValue = "0") int page,
              @RequestParam(defaultValue = "10") int size) {
 
          Page<PatientPageDto> patientPage = patientUserCase.findByGuardianId(guardianId, PageRequest.of(page, size));
 
          List<PatientPageResponse> content = patientPage.getContent()
                  .stream()
                  .map(readMapperRest::toPageResponse)
                  .collect(Collectors.toList());
 
          return new PageResponse<>(
                  content,
                  patientPage.getNumber(),
                  patientPage.getSize(),
                  patientPage.getTotalElements(),
                  patientPage.getTotalPages(),
                  patientPage.isLast()
          );
      }
      ```

6. **Respuesta JSON (Cliente)**
    - El cliente recibe un objeto uniforme:
      ```json
      {
        "content": [
          {
            "patientId": 1,
            "firstName": "Juan",
            "lastName": "Pérez",
            "guardianId": "G123"
          }
        ],
        "page": 0,
        "size": 10,
        "totalElements": 25,
        "totalPages": 3,
        "last": false
      }
      ```

---

## Motivo del cambio
- **Corrección semántica:** reflejar relaciones uno-a-muchos.
- **Escalabilidad:** soportar grandes volúmenes de datos con paginación.
- **Uniformidad:** todos los controladores devuelven `PageResponse<T>`.
- **Trazabilidad clínica:** garantizar que los listados reflejen la realidad del dominio.

---

## Beneficios
- **Claridad pedagógica:** cualquier desarrollador entiende el flujo de datos.
- **Consistencia:** todos los módulos siguen el mismo patrón.
- **Eficiencia:** se aprovecha la paginación nativa de Spring Data.
- **Mantenibilidad:** cambios futuros en infraestructura no afectan la capa REST.

---

## Decisión arquitectónica
Este ADR establece como estándar que:
- Los métodos de UseCase que representan relaciones uno-a-muchos deben devolver `Page<DTO>`.
- Los adaptadores deben mapear `Page<Entity>` a `Page<DTO>`.
- Los puertos de salida deben soportar `Pageable`.
- Los controladores REST deben devolver `PageResponse<T>` con metadatos.

De esta forma, toda la arquitectura refleja correctamente las relaciones del dominio y mantiene consistencia entre capas.
