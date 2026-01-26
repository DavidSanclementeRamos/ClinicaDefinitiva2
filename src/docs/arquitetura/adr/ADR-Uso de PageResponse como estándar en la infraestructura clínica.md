# ADR: Uso de `PageResponse<T>` como estándar en la infraestructura clínica

**Fecha:** 26-01-2026  
**Estado:** Aprobado  
**Decisión:** Adoptar `PageResponse<T>` como formato uniforme de respuesta para todos los endpoints REST que devuelvan colecciones paginadas.

---

## Contexto
Hasta ahora, los controladores devolvían:
- `List<T>` en endpoints de listados, sin metadatos de paginación.
- `Page<T>` directamente desde Spring Data, lo que expone detalles internos de la librería y genera respuestas poco uniformes.

Esto provocaba:
- Inconsistencia entre endpoints (algunos devolvían listas simples, otros devolvían objetos completos).
- Dificultad para auditar y exhibir resultados clínicos, ya que los metadatos de paginación no estaban estandarizados.
- Dependencia directa de Spring Data en la capa REST, lo que acopla la infraestructura.

---

## Decisión
Se define un DTO genérico `PageResponse<T>` que encapsula:
- `content`: lista de resultados mapeados a DTOs REST.
- `page`: número de página actual.
- `size`: tamaño de la página.
- `totalElements`: total de elementos en la consulta.
- `totalPages`: número total de páginas.
- `last`: indicador de si es la última página.

Todos los controladores que devuelvan colecciones deberán retornar `PageResponse<T>` en lugar de `List<T>` o `Page<T>`.

---

## Motivo del cambio
- **Uniformidad:** todos los endpoints siguen el mismo contrato de respuesta.
- **Escalabilidad:** soporta grandes volúmenes de datos con navegación clara.
- **Trazabilidad clínica:** el cliente puede auditar el tamaño y alcance de los resultados.
- **Desacoplamiento:** evita exponer directamente `Page<T>` de Spring Data en la capa REST.
- **Claridad RESTful:** el DTO refleja tanto los datos como el contexto de la consulta.

---

## Beneficios
- **Consistencia:** facilita el consumo de la API por clientes externos y frontends.
- **Mantenibilidad:** simplifica la evolución futura de la infraestructura.
- **Exhibición profesional:** el catálogo clínico puede mostrar resultados con metadatos claros.
- **Auditoría:** los metadatos permiten verificar integridad y completitud de las consultas.

---

## Consecuencias
- Se requiere refactorizar todos los controladores para devolver `PageResponse<T>`.
- Los mappers deben mantener dos métodos: `toResponse` (detalle) y `toPageResponse` (listado).
- Los casos de uso seguirán devolviendo `Page<DTO>` desde la capa de aplicación, pero serán transformados en `PageResponse<T>` en la capa REST.

---

## Ejemplo de implementación
```java
@GetMapping("/dentists")
public PageResponse<DentistPageResponse> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    Page<PageDentistDto> dentistPage = dentistUseCase.findAll(PageRequest.of(page, size));

    List<DentistPageResponse> content = dentistPage.getContent()
            .stream()
            .map(readRestMapper::toPageResponse)
            .collect(Collectors.toList());

    return new PageResponse<>(
            content,
            dentistPage.getNumber(),
            dentistPage.getSize(),
            dentistPage.getTotalElements(),
            dentistPage.getTotalPages(),
            dentistPage.isLast()
    );
}
```
## Decisión arquitectónica
Este ADR establece el uso obligatorio de PageResponse<T> como estándar en toda la infraestructura clínica para endpoints de listados y paginación.
Cualquier nuevo módulo deberá seguir esta convención para garantizar uniformidad y trazabilidad.