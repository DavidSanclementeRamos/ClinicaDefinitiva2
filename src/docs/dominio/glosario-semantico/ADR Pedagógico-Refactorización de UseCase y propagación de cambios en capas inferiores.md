# ADR Pedagógico: Refactorización de UseCase y propagación de cambios en capas inferiores

**Fecha:** 26-01-2026  
**Estado:** Aprobado  
**Decisión:** Refactorizar los métodos de los casos de uso (UseCase) para soportar relaciones uno-a-muchos y paginación, y propagar los cambios a las capas de aplicación e infraestructura.

---

## Contexto
En la primera versión de la arquitectura, algunos métodos de los casos de uso devolvían un único objeto (`ReadDto`) aunque la relación en el dominio era **uno-a-muchos**.  
Ejemplo:
- `findByContractId(Long contractId)` devolvía un solo paciente, aunque un contrato puede tener varios pacientes.
- `findByGuardianId(String guardianId)` devolvía un solo paciente, aunque un guardián puede estar asociado a varios.

Esto generaba inconsistencias y limitaciones:
- Los controladores REST no podían devolver listados correctos.
- La trazabilidad clínica quedaba incompleta.
- No se aprovechaba la paginación nativa de Spring Data.

---

## Decisión
Refactorizar los métodos de los **UseCase** para que devuelvan **colecciones paginadas** (`Page<DTO>`), y ajustar las capas inferiores:

1. **UseCase (Aplicación)**
    - Antes:
      ```java
      ReadPatientDto findByContractId(Long contractId);
      ```  
    - Después:
      ```java
      Page<PatientPageDto> findByContractId(Long contractId, Pageable pageable);
      ```

2. **Adaptadores (Aplicación → Infraestructura)**
    - Mapear `Page<Entity>` a `Page<DTO>` usando el mapper.
    - Ejemplo:
      ```java
      @Override
      public Page<PatientPageDto> findByContractId(Long contractId, Pageable pageable) {
          return patientRepository.findByContractId(contractId, pageable)
                  .map(patientMapper::toPageDto);
      }
      ```

3. **Puertos de salida (Dominio)**
    - Antes:
      ```java
      Optional<PatientEntity> findByContractId(Long contractId);
      ```  
    - Después:
      ```java
      Page<PatientEntity> findByContractId(Long contractId, Pageable pageable);
      ```

4. **Repositorios (Infraestructura)**
    - Ajustar firmas para soportar paginación:
      ```java
      Page<PatientEntity> findByContractId(Long contractId, Pageable pageable);
      Page<PatientEntity> findByGuardianId(String guardianId, Pageable pageable);
      ```

---

## Motivo del cambio
- **Corrección semántica:** reflejar correctamente relaciones uno-a-muchos.
- **Escalabilidad:** soportar grandes volúmenes de datos con paginación.
- **Uniformidad:** todos los controladores devuelven `PageResponse<T>` con metadatos.
- **Trazabilidad clínica:** garantizar que los listados reflejen la realidad del dominio.

---

## Beneficios
- **Claridad pedagógica:** cualquier desarrollador entiende que `findByX` puede devolver múltiples resultados.
- **Consistencia:** todos los módulos siguen el mismo patrón.
- **Eficiencia:** se evita devolver listas completas sin paginación.
- **Mantenibilidad:** cambios futuros en la infraestructura no afectan la capa REST.

---

## Consecuencias
- Se requiere refactorizar controladores, adaptadores, puertos de salida y repositorios.
- Los mappers deben tener métodos para convertir entidades a DTO simplificado (`toPageDto`).
- La documentación debe actualizarse para reflejar que los endpoints devuelven colecciones paginadas.

---

## Ejemplo de flujo completo (findByGuardianId)

1. **Controlador REST**
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

2. **UseCase**
   ```java
   @Override
   public Page<PatientPageDto> findByGuardianId(String guardianId, Pageable pageable) {
       return patientPersistencePort.findByGuardianId(guardianId, pageable);
   }
   ```

3. **Adaptador de salida**
   ```java
   @Override
   public Page<PatientPageDto> findByGuardianId(String guardianId, Pageable pageable) {
       return patientRepository.findByGuardianId(guardianId, pageable)
               .map(patientMapper::toPageDto);
   }
    ```
   

   
4. **Repositorio(Infraestructura)**
   ```java
   Page<PatientEntity> findByGuardianId(String guardianId, Pageable pageable);
   ```
      
 5. **Puerto de salida(Dominio)**
    ```java
     Page<Patient> findByGuardianId(GuardianId guardianId, Pageable pageable);
    ```

## Decisión arquitectónica
Este ADR establece como estándar pedagógico que:

- Los métodos de UseCase que representan relaciones uno-a-muchos deben devolver Page<DTO>.

- Los controladores REST deben devolver PageResponse<T>.

- Los adaptadores y puertos de salida deben soportar Pageable.

- La infraestructura debe implementar repositorios con soporte de paginación.

De esta forma, toda la arquitectura refleja correctamente las relaciones del dominio y mantiene consistencia entre capas.